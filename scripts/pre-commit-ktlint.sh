#!/bin/sh

set -e

echo "Running ktlintFormat..."

./gradlew ktlintFormat

# Add files that might have been changed by ktlintFormat
STAGED_KOTLIN_FILES=$(git diff --name-only --cached | grep '\.kt[s]\?$' || true)
if [ -n "$STAGED_KOTLIN_FILES" ]; then
    echo "$STAGED_KOTLIN_FILES" | xargs git add
fi

echo "ktlintFormat finished."
