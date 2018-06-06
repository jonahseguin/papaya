package com.jonahseguin.papaya.check;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.backend.config.annotations.ConfigData;
import com.jonahseguin.papaya.player.PapayaProfile;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 5:14 PM
 */
@Getter
@Setter
public abstract class Check extends CheckConfig {

    protected final Papaya papaya;
    protected final CheckType checkType;

    @ConfigData("enabled")
    private boolean enabled = true;

    @ConfigData("cancel")
    private boolean cancel = true;

    @ConfigData("auto-ban")
    private boolean autoban = true;

    @ConfigData("infraction-violations")
    private int infractionVL = 5; // Amount of violations before receiving an infraction

    public Check(Papaya papaya, CheckType checkType) {
        super(checkType);
        this.papaya = papaya;
        this.checkType = checkType;
    }

    public abstract String description();

    public abstract void startup();

    public abstract void shutdown();

    public boolean fail(PapayaProfile profile, Object... data) {

        String detail = "";
        if (data.length > 0) {
            detail = data[0].toString();
        }

        CheckFailure failure = CheckFailure.create(profile, getCheckType(), detail);

        papaya.getFailureHandler().addFailure(failure);

        return cancel;
    }

}
