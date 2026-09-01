#!/bin/sh

# Exit on any error
set -e

# Identify staged Kotlin files
STAGED_KOTLIN_FILES=$(git diff --name-only --cached | grep '\.kt[s]\?$' || true)

if [ -z "$STAGED_KOTLIN_FILES" ]; then
    exit 0
fi

echo "Running ktlintCheck..."

# 1. Stash unstaged changes to ensure ktlintCheck only sees what's being committed.
# --keep-index keeps the staged changes in the worktree.
STASH_COUNT_BEFORE=$(git stash list | wc -l | tr -d ' ')
STASH_NAME="pre-commit-ktlint-$(date +%s)"
git stash push -q --keep-index --include-untracked -m "$STASH_NAME"
STASH_COUNT_AFTER=$(git stash list | wc -l | tr -d ' ')

# 2. Run ktlintCheck.
# Capture the exit status so we can restore the stash even if linting fails.
set +e
./gradlew ktlintCheck
RESULT=$?
set -e

# 3. Restore unstaged changes if a stash was created.
if [ "$STASH_COUNT_AFTER" -gt "$STASH_COUNT_BEFORE" ]; then
    git stash pop -q
fi

# 4. Final outcome.
if [ $RESULT -ne 0 ]; then
    echo ""
    echo "ERROR: ktlintCheck failed."
    echo "Please run './gradlew ktlintFormat' to fix formatting issues, then stage the changes yourself."
    echo "Partial staging has been preserved."
    exit $RESULT
fi

echo "ktlintCheck passed."
