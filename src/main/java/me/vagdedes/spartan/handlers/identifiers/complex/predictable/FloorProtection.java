package me.vagdedes.spartan.handlers.identifiers.complex.predictable;

import me.vagdedes.spartan.functionality.important.MultiVersion;
import me.vagdedes.spartan.handlers.stability.Moderation;
import me.vagdedes.spartan.objects.data.Handlers;
import me.vagdedes.spartan.objects.replicates.SpartanPlayer;
import me.vagdedes.spartan.utils.gameplay.GroundUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.event.entity.EntityDamageEvent;

public class FloorProtection {

    private static final int blocksOffGround = 2;

    public static void runDamage(SpartanPlayer p, EntityDamageEvent.DamageCause dmg) {
        if (!p.getHandlers().isDisabled(Handlers.HandlerType.Floor)) {
            boolean ground = p.isOnGroundCustom() || p.getBlocksOffGround(blocksOffGround + 1, true, true) <= blocksOffGround,
                    fall = dmg == EntityDamageEvent.DamageCause.FALL;
            int time = 10;

            if ((dmg == EntityDamageEvent.DamageCause.FIRE_TICK || fall) && ground && !Moderation.wasDetected(p)
                    || dmg == EntityDamageEvent.DamageCause.STARVATION
                    || dmg == EntityDamageEvent.DamageCause.LIGHTNING
                    || dmg == EntityDamageEvent.DamageCause.THORNS
                    || dmg == EntityDamageEvent.DamageCause.VOID
                    || dmg == EntityDamageEvent.DamageCause.POISON
                    || dmg == EntityDamageEvent.DamageCause.WITHER
                    || dmg == EntityDamageEvent.DamageCause.SUFFOCATION && !p.isOutsideOfTheBorder()) {
                Handlers handlers = p.getHandlers();

                if (fall) {
                    handlers.add(Handlers.HandlerType.Floor, "fall", time);
                }
                handlers.add(Handlers.HandlerType.Floor, time);
            } else if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_10) && dmg == EntityDamageEvent.DamageCause.HOT_FLOOR
                    || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_11) && dmg == EntityDamageEvent.DamageCause.MAGIC
                    || dmg == EntityDamageEvent.DamageCause.CONTACT) {
                p.getHandlers().add(Handlers.HandlerType.Floor, time * 3);
            }

            if (ground) {
                GroundUtils.setOnGround(p, 1);
            }
        }
    }

    public static void runReceiveDamage(SpartanPlayer p, Entity damager, EntityDamageEvent.DamageCause damageCause) {
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && damager instanceof Firework) {
            p.getHandlers().add(Handlers.HandlerType.Floor, 10);
        }
    }

    public static boolean hasCooldown(SpartanPlayer p, boolean fall) {
        Handlers handlers = p.getHandlers();
        return (handlers.has(Handlers.HandlerType.Floor)
                && (!fall || !handlers.has(Handlers.HandlerType.Floor, "fall")))
                && (fall || p.getBlocksOffGround(blocksOffGround + 1, true, true) <= blocksOffGround);
    }

    public static boolean hasCooldown(SpartanPlayer p) {
        return hasCooldown(p, false);
    }
}
