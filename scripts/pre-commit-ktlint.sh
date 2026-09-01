#!/bin/sh

set -e

echo "Running ktlintFormat..."

./gradlew ktlintFormat

# Add files that might have been changed by ktlintFormat
git add $(git diff --name-only --cached | grep '\.kt[s]\?$')

echo "ktlintFormat finished."
