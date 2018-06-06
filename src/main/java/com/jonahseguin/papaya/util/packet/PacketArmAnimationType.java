package com.jonahseguin.papaya.util.packet;

/**
 * Created by Jonah on 6/1/2018.
 * Project: papaya
 *
 * @ 4:53 PM
 */
public enum PacketArmAnimationType {

    // for 1.7.10 - 1.8
    // credit
    // https://github.com/dmulloy2/PacketWrapper/blob/f427cbe2b5e28a0524e85ca1130bb788ffe32073/PacketWrapper/src/main/java/com/comphenix/packetwrapper/WrapperPlayClientArmAnimation.java

    NO_ANIMATION(0),
    SWING_ARM(1),
    DAMAGE_ANIMATION(2),
    LEAVE_BED(3),
    EAT_FOOD(5),
    UNKNOWN_(102),
    CROUCH(104),
    UNCROUCH(105);

    private final int id;

    PacketArmAnimationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PacketArmAnimationType fromId(int id) {
        for( PacketArmAnimationType actionType : values()) {
            if (actionType.getId() == id) {
                return actionType;
            }
        }
        return null;
    }

}
