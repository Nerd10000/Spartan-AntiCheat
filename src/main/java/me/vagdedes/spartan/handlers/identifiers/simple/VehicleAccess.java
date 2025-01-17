package me.vagdedes.spartan.handlers.identifiers.simple;

import me.vagdedes.spartan.objects.data.Handlers;
import me.vagdedes.spartan.objects.replicates.SpartanPlayer;
import me.vagdedes.spartan.system.Enums;
import me.vagdedes.spartan.system.SpartanBukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class VehicleAccess {

    private static final int ticks = 7;

    public static void run() {
        List<SpartanPlayer> players = SpartanBukkit.getPlayers();

        if (players.size() > 0) {
            for (SpartanPlayer p : players) {
                Entity vehicle = p.getVehicle();

                if (vehicle != null && !(vehicle instanceof LeashHitch)) {
                    runEnter(p, vehicle, true);
                }
            }
        }
    }

    public static void runExit(SpartanPlayer p) {
        Entity vehicle = p.getVehicle();

        if (vehicle == null || vehicle instanceof LivingEntity) {
            Handlers handlers = p.getHandlers();
            handlers.add(Handlers.HandlerType.Vehicle, ticks);
            handlers.add(Handlers.HandlerType.Vehicle, "exit", ticks);

            if (handlers.has(Handlers.HandlerType.Vehicle, "non-boat")) {
                handlers.add(Handlers.HandlerType.Vehicle, "non-boat", ticks);
            }
        }
    }

    public static void runEnter(SpartanPlayer p, Entity entity, boolean enter) {
        // Damage, BouncingBlocks, WaterElevator cover vehicles too
        // Velocity is far too important to be manipulated
        // Simple ones do not need to be manipulated here
        // Liquid is based on the past, so it's not counted here
        Handlers handlers = p.getHandlers();
        handlers.remove(Handlers.HandlerType.ElytraUse);
        handlers.remove(Handlers.HandlerType.Trident);
        handlers.remove(Handlers.HandlerType.Piston);
        handlers.remove(Handlers.HandlerType.FishingHook);
        handlers.remove(Handlers.HandlerType.ExtremeCollision);
        handlers.remove(Handlers.HandlerType.Floor);

        // Separator

        handlers.add(Handlers.HandlerType.Vehicle);

        if (enter) {
            handlers.add(Handlers.HandlerType.Vehicle, "enter", ticks);
        }
        if (!(entity instanceof Boat)) {
            handlers.add(Handlers.HandlerType.Vehicle, "non-boat");
        }
    }

    public static boolean hasExitCooldown(SpartanPlayer p, Enums.HackType hackType) {
        return hackType != Enums.HackType.EntityMove
                && p.getHandlers().has(Handlers.HandlerType.Vehicle, "exit")
                && !p.getProfile().isSuspectedOrHacker(hackType);
    }

    public static boolean hasEnterCooldown(SpartanPlayer p, Enums.HackType hackType) {
        return hackType != Enums.HackType.EntityMove
                && p.getHandlers().has(Handlers.HandlerType.Vehicle, "enter");
    }
}
