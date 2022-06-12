package me.screescree.SuperiorSteed.SuperiorHorse;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class BooleanTagType implements PersistentDataType<Byte, Boolean> {
    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }
    
    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public Boolean fromPrimitive(Byte primitive, PersistentDataAdapterContext arg1) {
        return primitive == 1;
    }

    @Override
    public Byte toPrimitive(Boolean complex, PersistentDataAdapterContext arg1) {
        return complex ? (byte) 1 : (byte) 0;
    }
}
