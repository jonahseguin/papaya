package com.jonahseguin.papaya.sniffing;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 10:08 PM
 */
public enum PacketSniffType {

    USE_ENTITY, // done
    KEEP_ALIVE, // done // server sends a keep-alive with a random ID, the client must respond with the same packet (cannot send more!)
    PLAYER_ON_GROUND, // done  // on ground packet --> for fall damage
    PLAYER_POSITION, // done // player can only send so many movement packets
    PLAYER_LOOK, // done
    PLAYER_POSITION_AND_LOOK, // done
    VEHICLE_MOVE, // todo
    ENTITY_ACTION, // done // start/stop sneaking, start/stop sprinting, etc.
    HELD_ITEM_CHANGE, // todo
    ARM_ANIMATION // done // when a player's arm swings

}
