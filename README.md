# vocalize

Offline-first audio journal that automatically structures raw voice notes into actionable insights.

## Development

### Code Style
This project uses [ktlint](https://pinterest.github.io/ktlint/) to maintain consistent code formatting.

### Git Hooks
To ensure high code quality, a Git `pre-commit` hook is automatically installed when you first build the project (via the `:app:preBuild` task). This hook runs `ktlint` checks on your staged changes before every commit.

If you need to run lint checks manually, use:
```bash
./gradlew ktlintCheck
```

To automatically fix formatting issues:
```bash
./gradlew ktlintFormat
```
