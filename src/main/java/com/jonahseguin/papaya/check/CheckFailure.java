package com.jonahseguin.papaya.check;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.util.lag.Lag;
import lombok.Data;

import java.util.UUID;

/**
 * Created by Jonah on 6/4/2018.
 * Project: papaya
 *
 * @ 4:23 PM
 */
@Data
public class CheckFailure {

    private final String id = UUID.randomUUID().toString();
    private final PapayaProfile profile;
    private final CheckType checkType;
    private final long time;
    private final long currentTick;
    private final int ping;
    private final double tps;
    private final String detail;

    public static CheckFailure create(PapayaProfile profile, CheckType checkType, String detail) {
        return new CheckFailure(profile, checkType, System.currentTimeMillis(), Papaya.getInstance().getFailureHandler().getCurrentTick(), profile.getPing(), Lag.getTPS(), detail);
    }

    public void process() {
        profile.getRecord().addFailure(this);
    }

}
