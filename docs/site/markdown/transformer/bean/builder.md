<head>
    <title>Builder</title>
</head>

# Builder supported patterns

The library support the transformation of Java Bean using the following Builder patterns:

### Standard pattern:

~~~Java
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

        public com.hotels.transformer.model.ItemType build() {
            return new ItemType(this.objectClass, this.genericClass);
        }
    }
}
~~~

### Custom Builder pattern:

~~~Java
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

        public com.hotels.transformer.model.ItemType build() {
            return new ItemType(this);
        }
    }
}
~~~