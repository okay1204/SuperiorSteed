package me.screescree.SuperiorSteed.superiorhorse.persistenttype;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.reflect.TypeToken;

public class PersistentDataType_INTEGER_SET implements PersistentDataType<int[], Set<Integer>> {
    @Override
    public Class<int[]> getPrimitiveType() {
        return int[].class;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Class<Set<Integer>> getComplexType() {
        return (Class<Set<Integer>>) new TypeToken<Set<Integer>>(){}.getRawType();
    }

    @Override
    public Set<Integer> fromPrimitive(int[] primitive, PersistentDataAdapterContext context) {
        Set<Integer> set = new HashSet<>();
        for (int i : primitive) {
            set.add(i);
        }
        return set;
    }

    @Override
    public int[] toPrimitive(Set<Integer> complex, PersistentDataAdapterContext context) {
        int[] array = new int[complex.size()];
        int i = 0;
        for (Integer integer : complex) {
            array[i] = integer;
            i++;
        }
        return array;
    }
}