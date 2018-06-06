package com.jonahseguin.papaya;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jonahseguin.papaya.backend.config.LanguageConfig;
import com.jonahseguin.papaya.backend.config.PapayaConfig;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.backend.database.mongo.MongoManager;
import com.jonahseguin.papaya.backend.database.redis.RedisManager;
import com.jonahseguin.papaya.check.CheckManager;
import com.jonahseguin.papaya.check.FailureHandler;
import com.jonahseguin.papaya.check.alert.AlertManager;
import com.jonahseguin.papaya.command.PapayaCommands;
import com.jonahseguin.papaya.fix.AsyncHitDetection;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.player.PapayaProfileCache;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;
import com.jonahseguin.papaya.util.TickManager;
import com.jonahseguin.papaya.util.error.ErrorHandler;
import com.jonahseguin.papaya.util.lag.Lag;
import com.jonahseguin.papaya.util.msg.FancyMessage;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 7:31 PM
 */
@Getter
public class Papaya extends JavaPlugin {

    private static Papaya instance = null;

    private ProtocolManager protocolManager = null;

    private MongoManager mongoManager = null;
    private RedisManager redisManager = null;
    private PapayaConfig papayaConfig = null;
    private LanguageConfig languageConfig = null;
    private ErrorHandler errorHandler = null;
    private PapayaProfileCache profileCache = null;
    private TickManager tickManager = null;
    private PapayaCommands papayaCommands = null;
    private PacketSniffManager packetSniffManager = null;
    private CheckManager checkManager = null;
    private FailureHandler failureHandler = null;
    private AlertManager alertManager = null;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        Papaya.instance = this;
        this.getServer().getScheduler().runTaskTimer(instance, new Lag(), 1L, 1L);
        this.errorHandler = new ErrorHandler();
        this.mongoManager = new MongoManager(this);
        this.redisManager = new RedisManager();
        this.papayaConfig = new PapayaConfig(this);
        this.languageConfig = new LanguageConfig(this);
        this.profileCache = new PapayaProfileCache(this);
        this.tickManager = new TickManager(this);
        this.papayaCommands = new PapayaCommands(this);
        this.papayaCommands.register();
        this.packetSniffManager = new PacketSniffManager(this);
        this.checkManager = new CheckManager(this);
        this.checkManager.startup();
        this.failureHandler = new FailureHandler(this);
        this.alertManager = new AlertManager(this);
        new AsyncHitDetection(this);
    }

    @Override
    public void onDisable() {
        this.papayaConfig.save();
        this.profileCache.getCache().shutdown();
        this.checkManager.shutdown();
        Papaya.instance = null;
    }

    public static Papaya getInstance() {
        return instance;
    }

    /**
     * Formats a string, converting '&<color character>' format to actual ChatColors.
     * Also formats '{0}' '{1}' etc. to replace with the provided arguments, in the respective order.
     * @param s The initial string to format
     * @param args The arguments to parse into the format
     * @return The formatted string
     */
    public static String format(String s, Object... args) {
        if (args != null) {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (s.contains("{" + i + "}")) {
                        s = s.replace("{" + i + "}", args[i].toString());
                    }
                }
            }
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String formatWithPrefix(String s, Object... args) {
        return PapayaLang.PAPAYA_PREFIX.format() + format(s, args);
    }

    /**
     * Whether or not debug is enabled for the PurifiedCore
     * @return boolean
     */
    public static boolean isDebug() {
        if (getInstance().getPapayaConfig() == null) {
            return false;
        }
        return getInstance().getPapayaConfig().isDebug();
    }

    /**
     * Logs a message (generic; info) with the given prefix & message
     * Effectively calls {@link #log(String)}
     * @param prefix The prefix
     * @param message the message
     */
    public static void log(String prefix, String message) {
        log("[" + prefix + "] " + message);
    }

    /**
     * Logs a message with the info level
     * (does not log from the core plugin; but from the generic server)
     * @param message the message to log
     */
    public static void log(String message) {
        Bukkit.getLogger().info(message);
    }

    /**
     * Sends a debug message to console if {@link #isDebug()} == true
     * Also sends the debug message to any online papayaProfile with debug enabled {@link PapayaProfile#isDebug()} == true
     * @param msg the debug message to send
     */
    public static void debug(String msg) {
        if (isDebug()) {
            log("[Debug] " + msg);
        }
        for (Player pl : getInstance().getServer().getOnlinePlayers()) {
            PapayaProfile papayaProfile = Papaya.getInstance().getProfileCache().get(pl);
            if (papayaProfile.isDebug()) {
                pl.sendMessage(PapayaLang.DEBUG_FORMAT.format(msg));
            }
        }
    }

    /**
     * Sends a debug message to console if {@link #isDebug()} == true
     * Also sends the debug message to any online papayaProfile with debug enabled {@link PapayaProfile#isDebug()} == true
     * @param msg FancyMessage
     */
    public static void debug(FancyMessage msg) {
        if (isDebug()) {
            msg.send(Bukkit.getConsoleSender());
        }
        for (Player pl : getInstance().getServer().getOnlinePlayers()) {
            PapayaProfile papayaProfile = Papaya.getInstance().getProfileCache().get(pl);
            if (papayaProfile.isDebug()) {
                msg.send(pl);
            }
        }
    }

    /**
     * Runs a method on the main thread, sync.
     * @param runnable to run
     */
    public static void runSync(Runnable runnable) {
        getInstance().getServer().getScheduler().runTask(getInstance(), runnable);
    }

    /**
     * Runs a method on a separate thread, async.
     * @param runnable to run
     */
    public static void runASync(Runnable runnable) {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }

    public ClassLoader getPapayaClassLoader() {
        return getClassLoader();
    }

}
