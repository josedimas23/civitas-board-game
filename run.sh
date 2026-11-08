#!/usr/bin/env bash
# Compile and launch Civitas (Swing GUI) in one step.
set -euo pipefail

cd "$(dirname "$0")"

# Use JAVA_HOME if set, else Homebrew's openjdk@21, else whatever is on PATH.
if [[ -n "${JAVA_HOME:-}" ]]; then
  JAVA_BIN="$JAVA_HOME/bin"
elif command -v brew >/dev/null && [[ -d "$(brew --prefix openjdk@21 2>/dev/null)/bin" ]]; then
  JAVA_BIN="$(brew --prefix openjdk@21)/bin"
else
  JAVA_BIN="$(dirname "$(command -v javac || echo /usr/bin/javac)")"
fi

if ! "$JAVA_BIN/javac" -version >/dev/null 2>&1; then
  echo "Error: no JDK found. Install one (Java 18+), e.g. 'brew install openjdk@21'." >&2
  exit 1
fi

rm -rf build/classes
mkdir -p build/classes
find src -name '*.java' -print0 | xargs -0 "$JAVA_BIN/javac" -encoding UTF-8 -nowarn -d build/classes

exec "$JAVA_BIN/java" -cp build/classes GUI.Main
