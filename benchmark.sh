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
# Scores are in µs/op; we convert to ms for readability.
# Result lines look like either of:
#   TransformerBenchmark.mutableSimpleBean  avgt   2   0.536          us/op
#   TransformerBenchmark.mutableSimpleBean  avgt  20   0.536 ± 0.012  us/op
# -----------------------------------------------------------------------
echo ""
echo "==> Transformation times summary (ms/op):"
echo ""

awk '
function us_to_ms(v,    r) {
    r = v / 1000
    # print with enough decimals to show non-zero values even for fast paths
    return sprintf("%.6f", r)
}
/^TransformerBenchmark\./ {
    split($1, parts, ".")
    name  = parts[2]
    score = $4
    if ($5 == "±") { error = "± " us_to_ms($6); unit = $7 }
    else            { error = "";                unit = $5 }
    scores[name]  = us_to_ms(score)
    errors[name]  = error
    # classify into bean transformations vs type conversions
    if (name ~ /typeConversion/) {
        conv[length(conv)+1] = name
    } else {
        beans[length(beans)+1] = name
    }
}
END {
    fmt = "  %-44s  %12s  %-16s\n"
    sep = "  " sprintf("%-44s", "--------------------------------------------") \
          "  " sprintf("%-12s", "------------") \
          "  " sprintf("%-16s", "----------------")

    print "  Bean transformations:"
    printf fmt, "  Benchmark", "Time (ms/op)", "Error (ms/op)"
    print sep
    for (i = 1; i <= length(beans); i++) {
        n = beans[i]
        printf fmt, "  " n, scores[n], errors[n]
    }

    print ""
    print "  Type conversions:"
    printf fmt, "  Benchmark", "Time (ms/op)", "Error (ms/op)"
    print sep
    for (i = 1; i <= length(conv); i++) {
        n = conv[i]
        printf fmt, "  " n, scores[n], errors[n]
    }
}
' "$TMPOUT"
