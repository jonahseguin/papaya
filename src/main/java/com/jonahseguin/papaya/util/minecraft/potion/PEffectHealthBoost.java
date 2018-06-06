package com.jonahseguin.papaya.util.minecraft.potion;

/**
 * Created by Jonah on 6/3/2018.
 * Project: papaya
 *
 * @ 2:00 PM
 */
public class PEffectHealthBoost {

    public static final int maxHealthAddedPerLevel = 4;

    // Actual health may be above the base value after the effect ends and will not be capped, unless the entity is healed. Extra hearts of a mounted mob can be seen while riding it.

    public static double getMaxHealth(double baseMaxHealth, int level) {
        return baseMaxHealth + (maxHealthAddedPerLevel * level);
    }

}
