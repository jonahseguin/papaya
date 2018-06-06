package com.jonahseguin.papaya.sniffing.logger;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.player.PapayaProfile;

import java.io.File;

/**
 * Created by Jonah on 6/2/2018.
 * Project: papaya
 *
 * @ 11:11 PM
 */
public class PlayerSniffLog {

    private final PapayaProfile profile;
    private final String logName;
    private String fileName = null;

    public PlayerSniffLog(PapayaProfile profile, String logName) {
        this.profile = profile;
        this.logName = logName;
    }

    private void loadFileName() {
        int id = 1;
        String fileName = String.format("%s_%03d.txt", logName, id);
        File f = new File(Papaya.getInstance().getDataFolder().getPath() + File.separator + "logs" + fileName);
        f.mkdirs();
        while (f.exists()) {
            id++;
            fileName = String.format("%s_%03d.txt", logName, id);
            f = new File(Papaya.getInstance().getDataFolder().getPath() + File.separator + "logs" + fileName);
        }
        this.fileName = fileName;
    }

}
