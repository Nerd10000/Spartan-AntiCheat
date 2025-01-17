package me.vagdedes.spartan.handlers.identifiers.complex.unpredictable;

import me.vagdedes.spartan.objects.data.Handlers;
import me.vagdedes.spartan.objects.replicates.SpartanLocation;
import me.vagdedes.spartan.objects.replicates.SpartanPlayer;
import me.vagdedes.spartan.system.SpartanBukkit;
import me.vagdedes.spartan.utils.gameplay.MoveUtils;
import me.vagdedes.spartan.utils.java.math.AlgebraUtils;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

public class Piston {

    private static final double
            horizontalDistance = 3.0,
            verticalDistance = 2.0;

    public static void run(Block block, List<Block> blocks) {
        List<SpartanPlayer> players = SpartanBukkit.getPlayers();

        if (players.size() > 0) {
            boolean runBlocks = blocks.size() > 0;
            World world = block.getWorld();

            for (SpartanPlayer p : players) {
                if (p.getWorld().equals(world)) {
                    SpartanLocation location = p.getLocation();
                    double preX = AlgebraUtils.getPreDistance(location.getX(), block.getX()),
                            diffY = location.getY() - block.getY(),
                            preZ = AlgebraUtils.getPreDistance(location.getZ(), block.getZ());

                    if (!run(p, preX, diffY, preZ) // Check if the player is nearby to the piston
                            && runBlocks
                            && Math.sqrt(preX + (diffY * diffY) + preZ) <= MoveUtils.chunk) { // Check if the player is nearby to the piston affected blocks
                        for (Block affected : blocks) {
                            preX = AlgebraUtils.getPreDistance(location.getX(), affected.getX());
                            diffY = location.getY() - block.getY();
                            preZ = AlgebraUtils.getPreDistance(location.getZ(), affected.getZ());

                            if (run(p, preX, diffY, preZ)) { // Check if the player is nearby to the piston affected block
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean run(SpartanPlayer player, double preX, double diffY, double preZ) {
        if (Math.sqrt(preX + preZ) <= horizontalDistance
                && Math.abs(diffY) <= verticalDistance) {
            player.getHandlers().add(Handlers.HandlerType.Piston, 30);
            return true;
        }
        return false;
    }
}
