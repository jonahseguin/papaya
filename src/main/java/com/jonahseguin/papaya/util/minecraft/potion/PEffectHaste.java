package com.jonahseguin.papaya.util.minecraft.potion;

import lombok.Data;

/**
 * Created by Jonah on 6/3/2018.
 * Project: papaya
 *
 * @ 1:56 PM
 */
@Data
public class PEffectHaste {

    public static int maxLevelArmSwingMining = 3;
    public static double miningSpeedMultiplier = 1.20; // Per level
    public static double attackSpeedMultiplier = 1.10; // Per level

    // At levels 3 or higher, the player's arm will not move while mining.
    // See also: Mining fatigue

    public static boolean willArmSwingMining(int level) {
        return level < maxLevelArmSwingMining;
    }

}
