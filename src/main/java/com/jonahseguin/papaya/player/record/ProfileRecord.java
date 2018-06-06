package com.jonahseguin.papaya.player.record;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.CheckFailure;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.violation.Violation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonah on 6/3/2018.
 * Project: papaya
 *
 * @ 7:33 PM
 */
@Embedded
@NoArgsConstructor
@Getter
public class ProfileRecord {

    private Map<CheckType, CheckRecord> checkRecords = new HashMap<>();

    public CheckRecord getCheckRecord(CheckType checkType) {
        if (checkRecords.containsKey(checkType)) {
            return checkRecords.get(checkType);
        }
        CheckRecord checkRecord = new CheckRecord(checkType, new HashSet<>(), 0);
        checkRecords.put(checkType, checkRecord);
        return checkRecord;
    }

    public void addFailure(CheckFailure failure) {
        CheckRecord record = getCheckRecord(failure.getCheckType());
        Violation violation = Violation.fromFailure(failure);
        record.getViolations().add(violation);
        if (!failure.getProfile().onAlertCooldown(failure.getCheckType())) {
            failure.getProfile().alertCooldown(failure.getCheckType(), 3);
            Papaya.getInstance().getAlertManager().checkAlert(violation, record.getViolationLevel());
        }
    }

}
