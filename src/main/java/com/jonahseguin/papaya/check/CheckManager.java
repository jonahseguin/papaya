package com.jonahseguin.papaya.check;

import com.comphenix.protocol.AsynchronousManager;
import com.comphenix.protocol.ProtocolManager;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.packet.*;
import com.jonahseguin.papaya.check.type.BukkitCheck;
import com.jonahseguin.papaya.checks.fasteat.CheckFastEat;
import com.jonahseguin.papaya.checks.fly.CheckBedFly;
import com.jonahseguin.papaya.checks.fly.CheckFly;
import com.jonahseguin.papaya.checks.headless.CheckHeadless;
import com.jonahseguin.papaya.checks.nofall.CheckNoFall;
import com.jonahseguin.papaya.checks.keepalive.CheckKeepAlive;
import com.jonahseguin.papaya.checks.sneak.CheckSneak;
import com.jonahseguin.papaya.checks.timer.CheckTimer;
import com.jonahseguin.papaya.exception.PapayaCheckException;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 5:38 PM
 */
@Getter
public class CheckManager {

    private final Papaya papaya;
    private final ConcurrentMap<CheckType, Check> checks = new ConcurrentHashMap<>();

    public CheckManager(Papaya papaya) {
        this.papaya = papaya;
        // Register checks
        this.registerCheck(new CheckKeepAlive(papaya));
        this.registerCheck(new CheckNoFall(papaya));
        this.registerCheck(new CheckTimer(papaya));
        this.registerCheck(new CheckBedFly(papaya));
        this.registerCheck(new CheckSneak(papaya));
        this.registerCheck(new CheckHeadless(papaya));
        this.registerCheck(new CheckFastEat(papaya));
        this.registerCheck(new CheckFly(papaya));

        // Register listeners + enable checks
        this.registerChecks();
        this.registerPacketListeners();
    }

    public final void startup() {
        this.checks.values().forEach(Check::startup);
    }

    public final void shutdown() {
        this.checks.values().forEach(check -> {
            if (check instanceof BukkitCheck) {
                ((BukkitCheck) check).unregisterListener();
            }
            check.shutdown();
        });
    }

    private void registerPacketListeners() {
        final ProtocolManager protocolManager = papaya.getProtocolManager();
        final AsynchronousManager asynchronousManager = protocolManager.getAsynchronousManager();

        protocolManager.addPacketListener(new PacketListenerKeepAlive(papaya));
        protocolManager.addPacketListener(new PacketListenerEntityAction(papaya));

        asynchronousManager.registerAsyncHandler(new PacketListenerFlying(papaya)).start();
        asynchronousManager.registerAsyncHandler(new PacketListenerLook(papaya)).start();
        asynchronousManager.registerAsyncHandler(new PacketListenerPosition(papaya)).start();
        asynchronousManager.registerAsyncHandler(new PacketListenerPositionLook(papaya)).start();
    }

    private void registerChecks() {
        this.checks.values().forEach(check -> {
            if (check instanceof BukkitCheck) {
                BukkitCheck bukkitCheck = (BukkitCheck) check;
                bukkitCheck.registerListener();
            }
        });
    }

    private void registerCheck(Check check) {
        this.checks.put(check.getCheckType(), check);
    }

    public final Check getCheck(CheckType checkType) {
        if (!this.checks.containsKey(checkType)) {
            throw new PapayaCheckException("CheckType for " + checkType.getName() + " is not registered!");
        }
        return this.checks.get(checkType);
    }

}
