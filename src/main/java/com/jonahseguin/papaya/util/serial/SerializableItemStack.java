package com.jonahseguin.papaya.util.serial;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Created by Jonah on 10/15/2017.
 * Project: purifiedCore
 *
 * @ 6:59 PM
 */
@Embedded
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SerializableItemStack {

    private String type;
    private int amount;
    private short durability;
    private int materialDataType;
    private byte materialData;

    public static SerializableItemStack fromItemStack(ItemStack itemStack) {
        return new SerializableItemStack(itemStack.getType().toString(), itemStack.getAmount(), itemStack.getDurability(),
                itemStack.getData().getItemTypeId(), itemStack.getData().getData());
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(Material.valueOf(type.toUpperCase()), amount, durability);
        itemStack.setData(new MaterialData(materialDataType, materialData));
        return itemStack;
    }


}
