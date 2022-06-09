<head>
    <title>Transformer Performaces</title>
</head>

# Performance

This page shows the transformer performance for the following objects:

* Mutable objects
* Mutable objects extending another mutable object
* Immutable objects
* Immutable objects extending another immutable object
* Mixed objects

|                                                                                               |                                      **Mutable**                                       |                                      **Immutable**                                       |                                      **Mixed**                                       |
|:----------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------:|
| Simple objects (without nested objects)                                                       |                                        ~0.011ms                                        |                                         ~0.018ms                                         |                                          NA                                          |
| Complex objects (containing several nested object and several items in Map and Array objects) |                                        ~0.37ms                                         |                                         ~0.21ms                                          |                                       ~0.22ms                                        | 
| CPU/Heap usage                                                                                | [~0.2%/35 MB](docs/site/resources/images/stats/performance/mutableObject/jvmStats.jpg) | [~0.2%/30 MB](docs/site/resources/images/stats/performance/immutableObject/jvmStats.jpg) | [~0.2%/25 MB](docs/site/resources/images/stats/performance/mixedObject/jvmStats.jpg) |

Transformation time [screenshot](../images/stats/performance/transformationTime.png)

## Real case testing

The Bean Utils library has been tested on a real case scenario integrating it into a real edge service (called BPE).
The purpose was to compare the latency introduced by the library plus the memory/CPU usage.
The dashboard's screenshot shows the latency of the invoked downstream service (called BPAS) and the one where the library has been installed (BPE). 
Following the obtained results:

|                                             |                                 **Classic transformer**                                 |                               **BeanUtils library**                               |
|:--------------------------------------------|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------:|
| Throughput per second                       |                                           60                                            |                                        60                                         |
| Average CPU usage                           |                                          0.3%                                           |                                       0.3%                                        |
| Min/Max Heap Memory Usage (MB)              |                                         90/320                                          |                                      90/320                                       |
| Average Latency than the downstream service |                                          +2ms                                           |                                       +2ms                                        |
| JVM stats screenshot                        | [screenshot](../images/stats/performance/realTestCase/classicTransformer/jvmStats.jpg)  | [screenshot](../images/stats/performance/realTestCase/beanUtilsLib/jvmStats.jpg)  |
| Dashboard screenshot                        | [screenshot](../images/stats/performance/realTestCase/classicTransformer/dashboard.jpg) | [screenshot](../images/stats/performance/realTestCase/beanUtilsLib/dashboard.jpg) |
