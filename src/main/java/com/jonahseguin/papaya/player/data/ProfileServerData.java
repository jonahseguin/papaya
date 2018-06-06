package com.jonahseguin.papaya.player.data;

import com.jonahseguin.papaya.player.PapayaProfile;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:05 PM
 */
@Getter
@Setter
public class ProfileServerData extends ProfileData {

    private final Map<Integer, Long> sentKeepAliveIDs = new HashMap<>();
    private Location lastGroundLocation = null;
    private long lastGroundTime = 0;
    private boolean onGround = true;
    private boolean noFallWaitForDamage = false;
    private boolean tookFallDamage = false;
    private boolean eating = false;

    public ProfileServerData(PapayaProfile profile) {
        super(profile);
    }

    public boolean isKeepAliveValid(int id) {
        return sentKeepAliveIDs.containsKey(id);
    }

    public void removeKeepAliveID(int id) {
        sentKeepAliveIDs.remove(id);
    }

    public void addKeepAliveID(int id) {
        sentKeepAliveIDs.put(id, System.currentTimeMillis());
    }

    public long getKeepAliveSentTime(int id) {
        return sentKeepAliveIDs.get(id);
    }

    public double getFallDistance(Location to) {
        if (lastGroundLocation != null) {
            return lastGroundLocation.getY() - to.getY();
        }
        return 0;
    }

}
