package com.jonahseguin.papaya.player.record;

import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.violation.Violation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonah on 6/3/2018.
 * Project: papaya
 *
 * @ 7:34 PM
 */
@Embedded
@Getter
@Setter
@NoArgsConstructor
public class CheckRecord {

    private CheckType checkType;
    private Set<Violation> violations = new HashSet<>();
    private int autobans = 0;

    public CheckRecord(CheckType checkType) {
        this.checkType = checkType;
    }

    public CheckRecord(CheckType checkType, Set<Violation> violations, int autobans) {
        this.checkType = checkType;
        this.violations = violations;
        this.autobans = autobans;
    }

    public int getViolationLevel() {
        int x = 0;
        for (Violation violation : violations) {
            if (!violation.isExpired()) {
                x += 1;
            }
        }
        return x;
    }

}
