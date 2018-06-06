package com.jonahseguin.papaya.player.data;

import com.jonahseguin.papaya.player.PapayaProfile;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:05 PM
 */
@Getter
@Setter
public class ProfileClientData extends ProfileData {

    private boolean onGround = true;
    private int movementPacketsLastSecond = 0;
    private int onGroundPacketsLastSecond = 0;
    private boolean sneaking = false;
    private boolean sprinting = false;
    private int sneakPackets = 0;
    private int unsneakPackets = 0;

    public ProfileClientData(PapayaProfile profile) {
        super(profile);
    }
}
