package com.jonahseguin.papaya.util.minecraft;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 10:15 PM
 */
public enum PBlockAttribute {

    CAN_ENTER, // player can be within the block
    TRANSPARENT, // player can freely pass through the block
    SOLID, // block is solid; player cannot pass through
    LIQUID, // block is liquid (water/lava): player can enter
    NOT_FULL_SIZED, // block is not a full sized (1x1) block ex. slabs, snow
    PREVENT_H_PASS, // specifically prevent horizontal passing (phase)
    PREVENT_V_CLIP, // specifically prevent vertical passing (vclip)
    HAS_GRAVITY, // block drops when placed (gravel, sand)
    STAIRS, // Is a stair block
    ENTERABLE, // doors, gates, etc./
    DOUBLE_SIZED, // Double-sized block (door, etc.)
    CLIMBABLE, // ladders, vines
    SPEEDS_UP, // speeds the player up when they come in contact with it (i.e ice)
    SLOWS_DOWN, // slows the player down when they come in contact with it (i.e soul sand)
    FENCE_WALL // Is a fence/wall blocks; which are 1 block tall, but their collision box for entities is 1.5 blocks tall

}
