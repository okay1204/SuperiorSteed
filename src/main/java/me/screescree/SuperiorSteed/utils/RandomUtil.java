package me.screescree.SuperiorSteed.utils;

import java.util.Random;

import io.netty.util.internal.ThreadLocalRandom;

public class RandomUtil {
    public static <E extends Enum<E>> E getRandomEnum(Class<E> enumClass) {
        Random random = ThreadLocalRandom.current();
        return enumClass.getEnumConstants()[random.nextInt(enumClass.getEnumConstants().length)];
    }
}
