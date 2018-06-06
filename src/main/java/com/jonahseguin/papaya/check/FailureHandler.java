package com.jonahseguin.papaya.check;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.check.violation.Violation;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.player.record.CheckRecord;
import lombok.Getter;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jonah on 6/4/2018.
 * Project: papaya
 *
 * @ 4:23 PM
 */
@Getter
public class FailureHandler {

    private long lastTick = System.currentTimeMillis();
    private long currentTick = 0; // Current tick

    private final long LAG_SPIKE_MILLIS;
    private final int LAG_SPIKE_PARDON_TICKS;
    private final int FAILURES_PROCESS_TICK;
    private final Papaya papaya;
    private final ConcurrentLinkedQueue<CheckFailure> failureQueue = new ConcurrentLinkedQueue<>();

    public FailureHandler(Papaya papaya) {
        this.papaya = papaya;
        this.LAG_SPIKE_MILLIS = papaya.getPapayaConfig().getCheckLagSpikeMsIgnore();
        this.LAG_SPIKE_PARDON_TICKS = papaya.getPapayaConfig().getCheckLagSpikePardonPeriodTicks();
        this.FAILURES_PROCESS_TICK = papaya.getPapayaConfig().getCheckFailProcessPerTick();

        papaya.getServer().getScheduler().runTaskTimer(papaya, () -> {
            // Process failures and handle lag spikes
            final long lagSpikeDuration = System.currentTimeMillis() - lastTick;
            if (lagSpikeDuration > LAG_SPIKE_MILLIS) {
                this.handleLagSpike(lagSpikeDuration);
            }
            else {
                this.processFailures();
            }
            currentTick++;
            lastTick = System.currentTimeMillis();
        }, 0L, 1L);

        papaya.getServer().getScheduler().runTaskTimerAsynchronously(papaya, () -> {
            // Remove expired violations
            for (PapayaProfile profile : papaya.getProfileCache().getCache().getCachedProfiles()) {
                if (profile != null) {
                    for (CheckRecord record : profile.getRecord().getCheckRecords().values()) {
                        Iterator<Violation> vit = record.getViolations().iterator();
                        while (vit.hasNext()) {
                            Violation violation = vit.next();
                            if (violation != null) {
                                if (violation.isExpired()) {
                                    record.getViolations().remove(violation);
                                }
                            }
                        }
                    }
                }
            }
        }, 20L, 20L);
    }

    public void addFailure(CheckFailure failure) {
        failureQueue.add(failure);
    }

    private void processFailures() {
        if (failureQueue.isEmpty()) return;
        for (int i = FAILURES_PROCESS_TICK; i > 0; i--) {
            CheckFailure failure = failureQueue.poll();
            if (failure != null) {
                failure.process();
            }
            else {
                break;
            }
        }
    }

    private void handleLagSpike(long lagSpike) {
        int pardoned = 0;
        Iterator<CheckFailure> it = failureQueue.iterator();
        while (it.hasNext()) {
            CheckFailure failure = it.next();
            if (this.inPardonPeriod(failure)) {
                failureQueue.remove(failure);
                pardoned++;
            }
        }
        for (PapayaProfile profile : papaya.getProfileCache().getCache().getCachedProfiles()) {
            if (profile != null) {
                for (CheckRecord record : profile.getRecord().getCheckRecords().values()) {
                    Iterator<Violation> vit = record.getViolations().iterator();
                    while (vit.hasNext()) {
                        Violation violation = vit.next();
                        if (violation != null) {
                            if (inPardonPeriod(violation)) {
                                record.getViolations().remove(violation);
                            }
                        }
                    }
                }
            }
        }
        papaya.getAlertManager().sendAlert(PapayaLang.LAG_SPIKE_PARDONED.format(lagSpike, pardoned));
    }

    private boolean inPardonPeriod(CheckFailure failure) {
        final long lagSpikeDuration = System.currentTimeMillis() - lastTick;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(lagSpikeDuration);
        long pardonTicks = seconds * 20 * 2;
        return failure.getCurrentTick() >= (currentTick - pardonTicks);
    }

    private boolean inPardonPeriod(Violation vio) {
        final long lagSpikeDuration = System.currentTimeMillis() - lastTick;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(lagSpikeDuration);
        long pardonTicks = seconds * 20 * 2;
        return vio.getCurrentTick() >= (currentTick - pardonTicks);
    }

}
