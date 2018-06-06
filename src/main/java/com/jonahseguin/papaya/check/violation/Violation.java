package com.jonahseguin.papaya.check.violation;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.CheckFailure;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.player.PapayaProfile;
import lombok.*;
import org.mongodb.morphia.annotations.Embedded;

import java.util.UUID;

/**
 * Created by Jonah on 6/3/2018.
 * Project: papaya
 *
 * @ 7:27 PM
 */
@Embedded
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Violation {

    private String failureID;
    private String id = UUID.randomUUID().toString();
    private PapayaProfile profile;
    private long time;
    private long currentTick;
    private CheckType checkType;
    private String detail;
    private long expiryTime = 0;

    public Violation(String failureID, PapayaProfile profile, long time, long currentTick, CheckType checkType, String detail) {
        this.failureID = failureID;
        this.profile = profile;
        this.time = time;
        this.currentTick = currentTick;
        this.checkType = checkType;
        this.detail = detail;
    }

    public boolean isExpired() {
        return expiryTime < System.currentTimeMillis();
    }

    public static Violation fromFailure(CheckFailure failure) {
        Violation violation = new Violation(failure.getId(), failure.getProfile(), failure.getTime(), failure.getCurrentTick(), failure.getCheckType(), failure.getDetail());
        violation.setExpiryTime(System.currentTimeMillis() + (Papaya.getInstance().getPapayaConfig().getCheckFailViolationExpiryMinutes() * 60 * 1000));
        return violation;
    }
}
