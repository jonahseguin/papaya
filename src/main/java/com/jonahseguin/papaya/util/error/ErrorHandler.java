package com.jonahseguin.papaya.util.error;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.util.msg.FancyMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by Jonah on 10/15/2017.
 * Project: purifiedCore
 *
 * @ 5:08 PM
 */
public class ErrorHandler {

    private final Map<String, ErrorRecord> records = new HashMap<>();

    public Set<ErrorRecord> getUnreadRecords() {
        return records.values().stream()
                .filter(ErrorRecord::isUnread).collect(Collectors.toSet());
    }

    public ErrorRecord addRecord(Exception ex) {
        ErrorRecord er = new ErrorRecord(ex, ex.getMessage(), System.currentTimeMillis());
        records.put(er.getId().toLowerCase(), er);

        // Also log the record if in debug
        Papaya.debug(new FancyMessage(Papaya.format("&7[Debug] &c&lException: &r&c" + er.getMessage())).command("/er read " + er.getId()).tooltip("&7[" + er.getId() + "]"));
        if (Papaya.isDebug()) {
            ex.printStackTrace();
        }
        return er;
    }

    public ErrorRecord addRecord(Exception ex, String message) {
        ErrorRecord er = new ErrorRecord(ex, message + ": " + ex.getMessage(), System.currentTimeMillis());
        records.put(er.getId().toLowerCase(), er);

        // Also log the record if in debug
        Papaya.debug(new FancyMessage(Papaya.format("&7[Debug] &c&lException: &r&c" + message)).command("/er read " + er.getId()).tooltip("&7[" + er.getId() + "]"));
        if (Papaya.isDebug()) {
            ex.printStackTrace();
        }

        return er;
    }

    public ErrorRecord readRecord(String id) {
        id = id.toLowerCase();
        ErrorRecord er = getRecord(id);
        if (er != null) {
            return readRecord(er);
        }
        return null;
    }

    public ErrorRecord readRecord(ErrorRecord record) {
        record.markAsRead();
        return record;
    }

    public ErrorRecord getRecord(String id) {
        return records.get(id.toLowerCase());
    }

}
