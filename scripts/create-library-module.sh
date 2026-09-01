#!/usr/bin/env bash

set -e

if [ -z "$1" ]; then
    echo "Error: Library/Core module name required."
    echo "Usage: ./scripts/create-library-module.sh <module-name> [prefix]"
    echo "Example: ./scripts/create-library-module.sh data        # creates :core:data"
    echo "Example: ./scripts/create-library-module.sh ui core     # creates :core:ui"
    exit 1
fi

RAW_NAME="$1"
PREFIX="${2:-core}" # Defaults to 'core' as per standard Android conventions (:core:data, :core:model, etc.)

MODULE_NAME=$(echo "$RAW_NAME" | tr '[:upper:]' '[:lower:]' | tr ' ' '-')
PACKAGE_NAME=$(echo "$MODULE_NAME" | tr '-' '_')

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
MODULE_DIR="$PROJECT_ROOT/$PREFIX/$MODULE_NAME"
PACKAGE_DIR="$MODULE_DIR/src/main/kotlin/com/nborba/vocalize/$PREFIX/$PACKAGE_NAME"

if [ -d "$MODULE_DIR" ]; then
    echo "Error: Directory '$PREFIX/$MODULE_NAME' already exists."
    exit 1
fi

echo "Creating library module ':$PREFIX:$MODULE_NAME'..."

mkdir -p "$PACKAGE_DIR"

# Create build.gradle.kts
cat <<EOF > "$MODULE_DIR/build.gradle.kts"
plugins {
    id("vocalize.android.library")
}

android {
    namespace = "com.nborba.vocalize.$PREFIX.$PACKAGE_NAME"
}

dependencies {
}
EOF

# Create .gitkeep in package directory
touch "$PACKAGE_DIR/.gitkeep"

# Update settings.gradle.kts if not already included
SETTINGS_FILE="$PROJECT_ROOT/settings.gradle.kts"
MODULE_INCLUDE="include(\":$PREFIX:$MODULE_NAME\")"

if ! grep -Fq "$MODULE_INCLUDE" "$SETTINGS_FILE"; then
    echo "$MODULE_INCLUDE" >> "$SETTINGS_FILE"
    echo "Added '$MODULE_INCLUDE' to settings.gradle.kts"
fi

echo "✅ Library module ':$PREFIX:$MODULE_NAME' created successfully!"
echo "   Path: $PREFIX/$MODULE_NAME"
echo "   Package: com.nborba.vocalize.$PREFIX.$PACKAGE_NAME"
