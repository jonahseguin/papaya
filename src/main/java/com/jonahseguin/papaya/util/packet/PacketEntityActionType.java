package com.jonahseguin.papaya.util.packet;

/**
 * Created by Jonah on 6/1/2018.
 * Project: papaya
 *
 * @ 4:53 PM
 */
public enum PacketEntityActionType {

    // for 1.7.10 - 1.8
    // credit
    // https://github.com/dmulloy2/PacketWrapper/blob/f427cbe2b5e28a0524e85ca1130bb788ffe32073/PacketWrapper/src/main/java/com/comphenix/packetwrapper/WrapperPlayClientEntityAction.java

    CROUCH(1),
    UNCROUCH(2),
    LEAVE_BED(3),
    START_SPRINTING(4),
    STOP_SPRINTING(5);

    private final int id;

    PacketEntityActionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PacketEntityActionType fromId(int id) {
        for( PacketEntityActionType actionType : values()) {
            if (actionType.getId() == id) {
                return actionType;
            }
        }
        return null;
    }

}
