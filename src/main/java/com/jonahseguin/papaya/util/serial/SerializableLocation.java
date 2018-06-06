package com.jonahseguin.papaya.util.serial;

import com.jonahseguin.papaya.exception.WorldNotFoundException;
import lombok.*;
import org.bson.Document;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Jonah on 10/15/2017.
 * Project: purifiedCore
 *
 * @ 6:12 PM
 */
@Embedded
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class SerializableLocation {

    @Transient
    private transient Location location = null;
    @NonNull
    private String world;
    @NonNull
    private double x;
    @NonNull
    private double y;
    @NonNull
    private double z;
    @NonNull
    private double pitch;
    @NonNull
    private double yaw;

    public static SerializableLocation fromLocation(Location location) {
        return new SerializableLocation(location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    public static SerializableLocation fromJson(String json) {
        Document document = Document.parse(json);
        String world = document.getString("world");
        double x = document.getDouble("x");
        double y = document.getDouble("y");
        double z = document.getDouble("z");
        double pitch = document.getDouble("pitch");
        double yaw = document.getDouble("yaw");
        return new SerializableLocation(world, x, y, z, pitch, yaw);
    }

    public static String toPrettyString(Location location) {
        return "World: " + location.getWorld().getName() + ", X: " + location.getBlockX() + ", Y: " + location.getBlockY() + " , Z: " + location.getBlockZ();
    }

    public Location toLocation() throws WorldNotFoundException {
        return new Location(worldNameToWorld(), x, y, z, (float) yaw, (float) pitch);
    }

    public void setLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public World worldNameToWorld() throws WorldNotFoundException {
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld != null) {
            return bukkitWorld;
        } else {
            throw new WorldNotFoundException("SerializedLocation: World no longer exists or never existed: " + this.world);
        }
    }

    public String toJson() {
        Document document = new Document()
                .append("world", world)
                .append("x", x)
                .append("y", y)
                .append("z", z)
                .append("pitch", pitch)
                .append("yaw", yaw);
        return document.toJson();
    }

}
