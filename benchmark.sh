#!/usr/bin/env bash
# Builds the JMH benchmark fat jar and runs it, then prints a formatted
# transformation-time summary.
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
BUILD_LOG=$(mktemp)
trap 'rm -f "$TMPOUT" "$BUILD_LOG"' EXIT
if ! "$MVN" package -pl "$MODULES" -DskipTests -Djacoco.skip=true -q >"$BUILD_LOG" 2>&1; then
    cat "$BUILD_LOG"
    exit 1
fi

TMPOUT=$(mktemp)

echo "==> Running benchmarks..."
java -jar "$JAR" "$@" 2>&1 \
    | grep -v \
        -e "^NOTE:" \
        -e "^WARNING:" \
        -e "^Processing [0-9]" \
        -e "^Writing out" \
    | tee "$TMPOUT"

# -----------------------------------------------------------------------
# Pretty-print a transformation-time summary from the JMH result lines.
# Result lines look like either of:
#   TransformerBenchmark.mutableSimpleBean  avgt   2   0.536          us/op
#   TransformerBenchmark.mutableSimpleBean  avgt  20   0.536 ± 0.012  us/op
# -----------------------------------------------------------------------
echo ""
echo "==> Transformation times summary:"
echo ""

awk '
BEGIN {
    SEP = "  "
    printf "  %-44s  %10s  %-13s  %s\n", "Benchmark", "Score", "Error", "Unit"
    printf "  %-44s  %10s  %-13s  %s\n", \
        "--------------------------------------------", \
        "----------", "-------------", "-----"
}
/^TransformerBenchmark\./ {
    # $1 = full name, $2 = mode, $3 = cnt, $4 = score
    # $5 = "±" (multi-iter) or unit (single-iter)
    split($1, parts, ".")
    name = parts[2]
    score = $4
    if ($5 == "±") {
        error = "± " $6
        unit  = $7
    } else {
        error = ""
        unit  = $5
    }
    printf "  %-44s  %10s  %-13s  %s\n", name, score, error, unit
}
' "$TMPOUT"
