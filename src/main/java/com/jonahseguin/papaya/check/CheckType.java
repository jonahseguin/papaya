package com.jonahseguin.papaya.check;

import lombok.Getter;
import org.apache.commons.lang.WordUtils;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 5:18 PM
 */
@Getter
public enum CheckType {

    FAST_BOW,
    FLY,
    SPEED,
    KEEP_ALIVE,
    NO_FALL,
    TIMER, // MorePackets
    AUTO_CLICK,
    CRITICALS,
    V_CLIP,
    REGEN,
    HEADLESS,
    REACH,
    PHASE,
    VELOCITY,
    BED_FLY,
    FAST_EAT,
    JESUS,
    SNEAK,
    DIRECTION
    ;

    private final String name;

    CheckType() {
        this.name = WordUtils.capitalizeFully(super.toString().replaceAll("_", " ")).replaceAll(" ", "");
    }

    CheckType(String name) {
        this.name = name;
    }

    public static CheckType fromString(String s) {
        for (CheckType checkType : values()) {
            if (checkType.getName().equalsIgnoreCase(s) || checkType.toString().equalsIgnoreCase(s)) {
                return checkType;
            }
        }
        return null;
    }

}
