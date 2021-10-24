<head>
    <title>Builder</title>
</head>

# Builder supported patterns

The library support the transformation of Java Bean using the following Builder patterns:

### Standard pattern:

```java
public class ItemType {
    private final Class<?> objectClass;
    private final Class<?> genericClass;

    ItemType(final Class<?> objectClass, final Class<?> genericClass) {
        this.objectClass = objectClass;
        this.genericClass = genericClass;
    }

    public static ItemTypeBuilder builder() {
        return new ItemType.ItemTypeBuilder();
    }

    // getter methods

    public static class ItemTypeBuilder {
        private Class<?> objectClass;
        private Class<?> genericClass;

        ItemTypeBuilder() {
        }

        public ItemTypeBuilder objectClass(final Class<?> objectClass) {
            this.objectClass = objectClass;
            return this;
        }

        public ItemTypeBuilder genericClass(final Class<?> genericClass) {
            this.genericClass = genericClass;
            return this;
        }

        public com.expediagroup.transformer.model.ItemType build() {
            return new ItemType(this.objectClass, this.genericClass);
        }
    }
}
```

### Custom Builder pattern:

To enable the transformation of Java Beans using the following Builder pattern:

```java
public class ItemType {
    private final Class<?> objectClass;
    private final Class<?> genericClass;

    ItemType(final ItemTypeBuilder builder) {
        this.objectClass = builder.objectClass;
        this.genericClass = builder.genericClass;
    }

    public static ItemTypeBuilder builder() {
        return new ItemType.ItemTypeBuilder();
    }

    // getter methods

    public static class ItemTypeBuilder {
        private Class<?> objectClass;
        private Class<?> genericClass;

        ItemTypeBuilder() {
        }

        public ItemTypeBuilder objectClass(final Class<?> objectClass) {
            this.objectClass = objectClass;
            return this;
        }

        public ItemTypeBuilder genericClass(final Class<?> genericClass) {
            this.genericClass = genericClass;
            return this;
        }

        public com.expediagroup.transformer.model.ItemType build() {
            return new ItemType(this);
        }
    }
}
```

It's needed to enable the custom Builder Transformation as following:

```java
ToBean toBean = new BeanTransformer()
                         .setCustomBuilderTransformationEnabled(true)
                         .transform(sourceObject, ToBean.class);
```