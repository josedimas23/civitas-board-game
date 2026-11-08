#!/usr/bin/env bash
# Compile the game and the model tests, then run them.
set -euo pipefail

cd "$(dirname "$0")"

if [[ -n "${JAVA_HOME:-}" ]]; then
  JAVA_BIN="$JAVA_HOME/bin"
elif command -v brew >/dev/null && [[ -d "$(brew --prefix openjdk@21 2>/dev/null)/bin" ]]; then
  JAVA_BIN="$(brew --prefix openjdk@21)/bin"
else
  JAVA_BIN="$(dirname "$(command -v javac || echo /usr/bin/javac)")"
fi

rm -rf build/test-classes
mkdir -p build/test-classes
find src test -name '*.java' -print0 | xargs -0 "$JAVA_BIN/javac" -encoding UTF-8 -nowarn -d build/test-classes

exec "$JAVA_BIN/java" -cp build/test-classes civitas.ModeloTest
