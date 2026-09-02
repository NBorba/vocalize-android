#!/usr/bin/env bash

set -e

if [ -z "$1" ]; then
    echo "Error: Feature module name required."
    echo "Usage: ./scripts/create-feature-module.sh <feature-name>"
    echo "Example: ./scripts/create-feature-module.sh home  # Creates :feature:home-api and :feature:home-impl"
    exit 1
fi

RAW_NAME="$1"
# Convert to lowercase and sanitize hyphens/spaces
BASE_NAME=$(echo "$RAW_NAME" | tr '[:upper:]' '[:lower:]' | tr ' ' '-' | sed 's/-api$//' | sed 's/-impl$//')

if [[ -z "$BASE_NAME" || ! "$BASE_NAME" =~ ^[a-z0-9_-]+$ ]]; then
    echo "Error: Invalid feature name '$RAW_NAME'."
    echo "Feature name must contain only alphanumeric characters, hyphens, or underscores."
    exit 1
fi

PKG_BASE=$(echo "$BASE_NAME" | tr '-' '_')

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SETTINGS_FILE="$PROJECT_ROOT/settings.gradle.kts"

# 1. Create -api module
API_MODULE_NAME="${BASE_NAME}-api"
API_DIR="$PROJECT_ROOT/feature/$API_MODULE_NAME"
API_PKG_DIR="$API_DIR/src/main/kotlin/com/nborba/vocalize/feature/$PKG_BASE/api"

if [ -d "$API_DIR" ]; then
    echo "Warning: Directory 'feature/$API_MODULE_NAME' already exists. Skipping API module creation."
else
    echo "Creating feature API module ':feature:$API_MODULE_NAME'..."
    mkdir -p "$API_PKG_DIR"

    cat <<EOF > "$API_DIR/build.gradle.kts"
plugins {
    id("vocalize.android.library")
}

android {
    namespace = "com.nborba.vocalize.feature.$PKG_BASE.api"
}

dependencies {
}
EOF

    touch "$API_PKG_DIR/.gitkeep"

    API_INCLUDE="include(\":feature:$API_MODULE_NAME\")"
    if ! grep -Fq "$API_INCLUDE" "$SETTINGS_FILE"; then
        echo "$API_INCLUDE" >> "$SETTINGS_FILE"
        echo "Added '$API_INCLUDE' to settings.gradle.kts"
    fi
fi

# 2. Create -impl module
IMPL_MODULE_NAME="${BASE_NAME}-impl"
IMPL_DIR="$PROJECT_ROOT/feature/$IMPL_MODULE_NAME"
IMPL_PKG_DIR="$IMPL_DIR/src/main/kotlin/com/nborba/vocalize/feature/$PKG_BASE/impl"

if [ -d "$IMPL_DIR" ]; then
    echo "Warning: Directory 'feature/$IMPL_MODULE_NAME' already exists. Skipping IMPL module creation."
else
    echo "Creating feature IMPL module ':feature:$IMPL_MODULE_NAME'..."
    mkdir -p "$IMPL_PKG_DIR"

    cat <<EOF > "$IMPL_DIR/build.gradle.kts"
plugins {
    id("vocalize.android.feature")
}

android {
    namespace = "com.nborba.vocalize.feature.$PKG_BASE.impl"
}

dependencies {
    implementation(project(":feature:$API_MODULE_NAME"))
}
EOF

    touch "$IMPL_PKG_DIR/.gitkeep"

    IMPL_INCLUDE="include(\":feature:$IMPL_MODULE_NAME\")"
    if ! grep -Fq "$IMPL_INCLUDE" "$SETTINGS_FILE"; then
        echo "$IMPL_INCLUDE" >> "$SETTINGS_FILE"
        echo "Added '$IMPL_INCLUDE' to settings.gradle.kts"
    fi
fi

echo "✅ Feature API & IMPL modules created successfully!"
echo "   API:  feature/$API_MODULE_NAME  (Package: com.nborba.vocalize.feature.$PKG_BASE.api)"
echo "   IMPL: feature/$IMPL_MODULE_NAME (Package: com.nborba.vocalize.feature.$PKG_BASE.impl)"
