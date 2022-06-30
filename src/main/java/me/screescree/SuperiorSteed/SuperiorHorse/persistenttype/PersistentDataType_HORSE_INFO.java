package me.screescree.SuperiorSteed.superiorhorse.persistenttype;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.info.Seed;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class PersistentDataType_HORSE_INFO implements PersistentDataType<PersistentDataContainer, SuperiorHorseInfo> {
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }
    
    @Override
    public Class<SuperiorHorseInfo> getComplexType() {
        return SuperiorHorseInfo.class;
    }

    @Override
    public SuperiorHorseInfo fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext context) {
        SuperiorHorseInfo info = new SuperiorHorseInfo();
        SuperiorSteed plugin = SuperiorSteed.getInstance();

        info.setHunger(container.get(new NamespacedKey(plugin, "hunger"), PersistentDataType.DOUBLE));
        info.setHydration(container.get(new NamespacedKey(plugin, "hydration"), PersistentDataType.DOUBLE));
        info.setTrust(container.get(new NamespacedKey(plugin, "trust"), PersistentDataType.DOUBLE));
        info.setFriendliness(container.get(new NamespacedKey(plugin, "friendliness"), PersistentDataType.DOUBLE));
        info.setComfortability(container.get(new NamespacedKey(plugin, "comfortability"), PersistentDataType.DOUBLE));
        info.setWaterBravery(container.get(new NamespacedKey(plugin, "waterBravery"), PersistentDataType.DOUBLE));

        info.setAge(container.get(new NamespacedKey(plugin, "age"), PersistentDataType.LONG));

        info.setGroomedBy(container.get(new NamespacedKey(plugin, "groomedBy"), new PersistentDataType_INTEGER_SET()));
        info.setGroomExpireTimer(container.get(new NamespacedKey(plugin, "groomExpireTimer"), PersistentDataType.INTEGER));

        info.setMale(container.get(new NamespacedKey(plugin, "isMale"), new PersistentDataType_BOOLEAN()));
        info.setStallion(container.get(new NamespacedKey(plugin, "isStallion"), new PersistentDataType_BOOLEAN()));

        info.setColor(container.get(new NamespacedKey(plugin, "color"), new PersistentDataType_ENUM<Color>(Color.class)));
        info.setStyle(container.get(new NamespacedKey(plugin, "style"), new PersistentDataType_ENUM<Style>(Style.class)));

        if (container.has(new NamespacedKey(plugin, "ownerUuid"), new PersistentDataType_UUID())) {
            info.setOwnerUuid(container.get(new NamespacedKey(plugin, "ownerUuid"), new PersistentDataType_UUID()));
        }

        info.setSpeed(container.get(new NamespacedKey(plugin, "speed"), PersistentDataType.DOUBLE));
        info.setJumpStrength(container.get(new NamespacedKey(plugin, "jumpStrength"), PersistentDataType.DOUBLE));
        info.setMaxHealth(container.get(new NamespacedKey(plugin, "maxHealth"), PersistentDataType.DOUBLE));

        info.setTraits(container.get(new NamespacedKey(plugin, "traits"), new PersistentDataType_ENUM_SET<Trait>(Trait.class)));
        info.setFavoriteSeed(container.get(new NamespacedKey(plugin, "favoriteSeed"), new PersistentDataType_ENUM<Seed>(Seed.class)));

        info.setPregnancyTimer(container.get(new NamespacedKey(plugin, "pregnancyTimer"), PersistentDataType.INTEGER));
        info.setPregnancyComplication(container.get(new NamespacedKey(plugin, "pregnancyComplication"), PersistentDataType.DOUBLE));
        
        if (container.has(new NamespacedKey(plugin, "pregnantWith"), this)) {
            info.setPregnantWith(container.get(new NamespacedKey(plugin, "pregnantWith"), this));
        }
        
        return info;
    }

    @Override
    public PersistentDataContainer toPrimitive(SuperiorHorseInfo info, PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer(); // ???
        
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "hunger"), PersistentDataType.DOUBLE, info.getHunger());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "hydration"), PersistentDataType.DOUBLE, info.getHydration());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "trust"), PersistentDataType.DOUBLE, info.getTrust());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "friendliness"), PersistentDataType.DOUBLE, info.getFriendliness());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "comfortability"), PersistentDataType.DOUBLE, info.getComfortability());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "waterBravery"), PersistentDataType.DOUBLE, info.getWaterBravery());

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "age"), PersistentDataType.LONG, info.getAge());

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "groomedBy"), new PersistentDataType_INTEGER_SET(), info.getGroomedBy());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "groomExpireTimer"), PersistentDataType.INTEGER, info.getGroomExpireTimer());

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "isMale"), new PersistentDataType_BOOLEAN(), info.isMale());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "isStallion"), new PersistentDataType_BOOLEAN(), info.isStallion());

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "color"), new PersistentDataType_ENUM<Color>(Color.class), info.getColor());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "style"), new PersistentDataType_ENUM<Style>(Style.class), info.getStyle());

        if (info.getOwnerUuid() != null) {
            container.set(new NamespacedKey(SuperiorSteed.getInstance(), "ownerUuid"), new PersistentDataType_UUID(), info.getOwnerUuid());
        }

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "speed"), PersistentDataType.DOUBLE, info.getSpeed());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "jumpStrength"), PersistentDataType.DOUBLE, info.getJumpStrength());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "maxHealth"), PersistentDataType.DOUBLE, info.getMaxHealth());

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "traits"), new PersistentDataType_ENUM_SET<Trait>(Trait.class), info.getTraits());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "favoriteSeed"), new PersistentDataType_ENUM<Seed>(Seed.class), info.getFavoriteSeed());

        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "pregnancyTimer"), PersistentDataType.INTEGER, info.getPregnancyTimer());
        container.set(new NamespacedKey(SuperiorSteed.getInstance(), "pregnancyComplication"), PersistentDataType.DOUBLE, info.getPregnancyComplication());

        if (info.getPregnantWith() != null) {
            container.set(new NamespacedKey(SuperiorSteed.getInstance(), "pregnantWith"), this, info.getPregnantWith());
        }

        return container;
    }
}