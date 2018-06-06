package com.jonahseguin.papaya.util.serial;

/**
 * Created by Jonah on 4/4/2018.
 * Project: purifiedCore
 *
 * @ 7:37 PM
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Embedded
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SerializablePotionEffect {

    private int amplifier = 1;
    private int duration = 0;
    private String typeName = "HEAL";
    private boolean ambient = false;

    public static SerializablePotionEffect fromPotionEffect(PotionEffect potionEffect) {
        return new SerializablePotionEffect(potionEffect.getAmplifier(), potionEffect.getDuration(), potionEffect.getType().getName(), potionEffect.isAmbient());
    }

    public PotionEffect toPotionEffect() {
        return new PotionEffect(PotionEffectType.getByName(typeName), duration, amplifier, ambient);
    }

}
