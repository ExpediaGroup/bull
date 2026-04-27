#!/usr/bin/env bash
# Builds the JMH benchmark fat jar and runs it immediately.
# Any arguments are forwarded to JMH, e.g.:
#   ./benchmark.sh                          # full run (~10-15 min)
#   ./benchmark.sh -f 1 -wi 2 -i 3         # quick smoke run (~2 min)
#   ./benchmark.sh mutableSimpleBean        # single benchmark
#   ./benchmark.sh -rf json -rff out.json   # write results to JSON

set -euo pipefail

MODULES="bull-common,bull-converter,bull-bean-transformer,bull-benchmark"
JAR="bull-benchmark/target/benchmarks.jar"
MVN="./mvnw"

if [[ "$(uname -s)" == MINGW* || "$(uname -s)" == CYGWIN* ]]; then
    MVN="./mvnw.cmd"
fi

echo "==> Building benchmark jar..."
"$MVN" package -pl "$MODULES" -DskipTests -Djacoco.skip=true -q

echo "==> Running benchmarks..."
java -jar "$JAR" "$@"
