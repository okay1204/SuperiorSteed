package me.screescree.SuperiorSteed.superiorhorse.persistenttype;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.reflect.TypeToken;

public class PersistentDataType_ENUM_SET<E extends Enum<E>> implements PersistentDataType<String, Set<E>> {
    private final Class<E> enumClass;

    public PersistentDataType_ENUM_SET(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Class<Set<E>> getComplexType() {
        return (Class<Set<E>>) new TypeToken<Set<E>>(){}.getRawType();
    }

    @Override
    public Set<E> fromPrimitive(String primitive, PersistentDataAdapterContext context) {
        String[] enumStrings = primitive.split(",");

        Set<E> enums = new HashSet<E>();
        if (enumStrings[0].length() > 0) {
            for (String enumString : enumStrings) {
                enums.add(Enum.valueOf(enumClass, enumString));
            }
        }
        return enums;
    }

    @Override
    public String toPrimitive(Set<E> complex, PersistentDataAdapterContext context) {
        String enumString = "";
        for (E enumValue : complex) {
            enumString += enumValue.name() + ",";
        }

        return enumString;
    }
}
