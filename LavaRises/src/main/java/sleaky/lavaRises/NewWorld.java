package sleaky.lavaRises;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

public class NewWorld extends ChunkGenerator {
    WorldCreator wc = new WorldCreator("lavaRises");

    public void createNewRandomWorld() {
        wc.seed((long) (Math.random()*100000000));
        wc.createWorld();
    }

    public void createSetSeedWorld(long seed) {
        wc.seed(seed);
        wc.createWorld();
    }

    public void teleportPlayersToWorld(WorldBorder border) {
        if (Bukkit.getWorld("lavaRises") == null) {
            System.out.println("Could not teleport players to world");
        }
        Location loc = Bukkit.getWorld("lavaRises").getSpawnLocation();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setHealth(20);
            p.setFoodLevel(20);
            p.getInventory().clear();
            p.setSaturation(5);
            p.teleport(loc);
        }
    }

}
