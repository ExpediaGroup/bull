<head>
    <title>Third Party Library comparison</title>
</head>

# Third-Party Libraries comparison

This page compares the Transformer functionalities with the following Third-Party libraries:

* [Apache BeanUtils](http://commons.apache.org/proper/commons-beanutils/)
* [Jackson Data Bind](https://github.com/FasterXML/jackson-databind)
* [Dozer](http://dozer.sourceforge.net/)

## Functions

|                                                                                                                         | **BULL** | **Apache Bean Utils** | **Jackson** | **Dozer** |
|:------------------------------------------------------------------------------------------------------------------------|:--------:|:---------------------:|:-----------:|:---------:|
| Mutable bean copy                                                                                                       |    X     |           X           |      X      |    X+     |
| Mutable bean with nested objects                                                                                        |    X     |           -           |      X      |    X+     |
| Mutable bean extending classes                                                                                          |    X     |           -           |      X      |    X+     |
| Immutable bean copy                                                                                                     |    X     |           -           |      -      |    X*     |
| Mixed bean copy                                                                                                         |    X     |           -           |      -      |    X+     |
| Copy of beans without getter and setter methods defined                                                                 |    X     |           -           |      -      |     -     |
| Mutable Bean with different field's name                                                                                |    X     |           -           |      -      |    X+     |
| Mixed with different field's type                                                                                       |    X     |           -           |      -      |    X+     |
| Immutable with different field's type                                                                                   |    X     |           -           |      -      |    X+     |
| Mutable Bean containing collection type fields containing complex objects                                               |    X     |           -           |      X      |     X     |
| Mixed Bean containing collection type fields containing complex objects                                                 |    X     |           -           |      -      |    X+     |
| Immutable Bean containing collection type fields containing complex objects                                             |    X     |           -           |      -      |    X+     |
| Mutable Bean containing containing Map type fields with nested Maps inside.  e.g. `Map<String, Map<String, Integer>>`   |    X     |           -           |      X      |     X     |
| Mixed Bean containing containing Map type fields with nested Maps inside.  e.g. `Map<String, Map<String, Integer>>`     |    X     |           -           |      -      |    X+     |
| Immutable Bean containing containing Map type fields with nested Maps inside.  e.g. `Map<String, Map<String, Integer>>` |    X     |           -           |      -      |    X+     |
| Annotation field validation                                                                                             |    X     |           -           |      X      |     -     |

_[*] Immutable types are not supported by Dozer. When a type doesn't have a no-arg constructor and all fields are final, Dozer can't perform the mapping.
The workaround is introducing the Builder Pattern. An example can be found [here](http://codeslut.blogspot.com/2010/05/mapping-immutable-value-objects-with.html)_
_[+] Requires a custom configuration_
