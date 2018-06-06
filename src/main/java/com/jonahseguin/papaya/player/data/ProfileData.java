package com.jonahseguin.papaya.player.data;

import com.jonahseguin.papaya.player.PapayaProfile;
import lombok.Getter;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:06 PM
 */
@Getter
public abstract class ProfileData {

    protected final PapayaProfile profile;

    public ProfileData(PapayaProfile profile) {
        this.profile = profile;
    }
}
