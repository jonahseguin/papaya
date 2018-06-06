package com.jonahseguin.papaya.util;

import com.jonahseguin.papaya.Papaya;
import lombok.Getter;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 11:16 PM
 */
@Getter
public class TickManager {

    private long totalTick = 0;
    private int relativeTick = 0;

    public TickManager(Papaya papaya) {
        papaya.getServer().getScheduler().runTaskTimer(papaya, () -> {
            totalTick++;
            relativeTick++;
            if (relativeTick > 20) {
                relativeTick = 1;
            }
        }, 1L, 1L);
    }
}
