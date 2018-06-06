package com.jonahseguin.papaya.util;

import com.jonahseguin.papaya.util.minecraft.PBlockAttribute;
import com.jonahseguin.papaya.util.minecraft.PapayaBlock;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.server.v1_7_R4.EntityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 9:02 PM
 */
public class BlockUtil {

    public static double[] BLOCK_HEIGHTS = {1.0, 1.5, 0.9375, 0.875, 0.8125, 0.75, 0.625, 0.5625, 0.5, 0.375, 0.3125, 0.25, 0.1875, 0.125, 0.09375, 0.0625};

    public static Block getBlockFaceUnderPlayer(Player player) {
        return player.getLocation().getBlock().getRelative(BlockFace.DOWN);
    }

    public static List<Block> getBlocksAround(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (double x = -radius; x <= radius; x++) {
            for (double y = -radius; y <= radius; y++)
                for (double z = -radius; z <= radius; z++) {
                    blocks.add(location.clone().add(x, 0, z).getBlock());
                }
        }
        blocks.add(location.getBlock());

        return blocks;
    }

    public static boolean isOnGround(Player player) {
        List<Block> blocks = getBlocksBelow(player);
        for(Block block : blocks) {
            Material material = block.getType();
            PapayaBlock papayaBlock = PapayaBlock.fromMaterial(material);
            if (papayaBlock != null) {
                if (papayaBlock.canStandOn()) {
                    return true;
                }
                else if (papayaBlock.hasAttribute(PBlockAttribute.LIQUID)) {
                    Block above = block.getLocation().clone().add(0, 1, 0).getBlock();
                    PapayaBlock abovePb = PapayaBlock.fromMaterial(above.getType());
                    if (abovePb.hasAttribute(PBlockAttribute.LIQUID)) {
                        return true;
                    }
                }
            }
        }
        // Catches things like fences, which since they are 'technically' 1.5 blocks high, are not caught by the getBlocksBelow
        Block fullyUnder = player.getLocation().getBlock().getLocation().clone().subtract(0, 1, 0).getBlock();
        PapayaBlock underPb = PapayaBlock.fromMaterial(fullyUnder.getType());
        return underPb.canStandOn();
    }

    public static ArrayList<Block> getBlocksBelow(Player player) {
        ArrayList<Block> blocksBelow = new ArrayList<Block>();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        AxisAlignedBB boundingBox = entityPlayer.boundingBox;
        World world = player.getWorld();
        double yBelow = player.getLocation().getY() - 0.0001;
        Block northEast = new Location(world, boundingBox.d, yBelow, boundingBox.c).getBlock();
        Block northWest = new Location(world, boundingBox.a, yBelow, boundingBox.c).getBlock();
        Block southEast = new Location(world, boundingBox.d, yBelow, boundingBox.f).getBlock();
        Block southWest = new Location(world, boundingBox.a, yBelow, boundingBox.f).getBlock();
        Block[] blocks = {northEast, northWest, southEast, southWest};
        for (Block block : blocks) {
            if (!blocksBelow.isEmpty()) {
                boolean duplicateExists = false;
                for (int i = 0; i < blocksBelow.size(); i++) {
                    if (blocksBelow.get(i).equals(block)) {
                        duplicateExists = true;
                    }
                }
                if (!duplicateExists) {
                    blocksBelow.add(block);
                }
            } else {
                blocksBelow.add(block);
            }
        }
        return blocksBelow;
    }

    public static Location roundLocation(Location location) {
        double offsetX = location.getX() % 1.0D;
        double offsetZ = location.getZ() % 1.0D;
        if (offsetX >= 0.5) {
            location.setX(location.getX() + offsetX);
        }
        else {
            location.setX(location.getX() - offsetX);
        }
        if (offsetZ >= 0.5) {
            location.setZ(location.getZ() + offsetZ);
        }
        else {
            location.setZ( location.getZ() - offsetZ);
        }
        return location;
    }

}
