package me.screescree.SuperiorSteed.superiorhorse.persistenttype;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class PersistentDataType_ENUM<E extends Enum<E>> implements PersistentDataType<String, E> {
    private final Class<E> enumClass;

    public PersistentDataType_ENUM(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }
    
    @Override
    public Class<E> getComplexType() {
        return enumClass;
    }

    @Override
    public E fromPrimitive(String primitive, PersistentDataAdapterContext context) {
        return Enum.valueOf(enumClass, primitive);
    }

    @Override
    public String toPrimitive(E complex, PersistentDataAdapterContext context) {
        return complex.name();
    }
}
