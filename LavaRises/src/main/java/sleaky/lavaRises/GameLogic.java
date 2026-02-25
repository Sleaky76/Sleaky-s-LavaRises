package sleaky.lavaRises;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class GameLogic implements Listener {

    public static int currentY = -64;
    public static long PVPtimer;
    private final LavaRises plugin;
    private final Queue<Location> blockQueue = new LinkedList<>();
    private final scoreBoard gameBoard = new scoreBoard();

    public GameLogic(LavaRises plugin) {
        this.plugin = plugin;
        startQueueProcessor();
    }

    public void setWorldBorder(long time, double size) {
        World world = Bukkit.getWorld("lavaRises");
        if (world != null) {
            world.getWorldBorder().setCenter(0, 0);
            world.getWorldBorder().changeSize(size, time);
        }
    }

    public void setGameRule() {
        World world = Bukkit.getWorld("lavaRises");
        if (world != null) {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        }
    }

    private void startQueueProcessor() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5000; i++) {
                    Location loc = blockQueue.poll();
                    if (loc == null) break;
                    loc.getBlock().setType(Material.LAVA, false);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void LavaRiseByOneBlock(int ticksBetweenLayers, Location corner1, Location corner2) {
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
        World world = corner1.getWorld();
        if (startLavaRises.GS == GameStates.STOPPED) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (currentY >= 250) {
                    this.cancel();
                    return;
                }
                if (startLavaRises.GS == GameStates.STOPPED) {
                    this.cancel();
                    return;
                }

                // Instead of setting blocks, we just add them to the "To-Do" list
                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        blockQueue.add(new Location(world, x, currentY, z));
                    }
                }
                currentY++;
            }
        }.runTaskTimer(plugin, 0L, (long) ticksBetweenLayers);
    }

    public void PVPTimer(long timer) {
        PVPtimer = timer;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (PVPtimer > 0) {
                    PVPtimer--;
                }
                if (PVPtimer == 0) {
                    startLavaRises.PS = PlayerStates.PVP_ON;
                }
                if (CheckForSurvivors() == 1) {
                    announceWinnerAndStopGame(getLastSurvivor());
                    this.cancel();
                    return;
                }
                if (startLavaRises.GS == GameStates.STOPPED) {
                    this.cancel();
                    return;
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    gameBoard.updateScoreboard(p, currentY);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public static int CheckForSurvivors() {
        if (Bukkit.getWorld("lavaRises") == null) {
            System.out.println("Couldn't find world !");
            return 0;
        }
        List<Player> Survivors = new ArrayList<>(Bukkit.getWorld("lavaRises")
                .getPlayers()).stream().filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .collect(Collectors.toList());

        return Survivors.size();
    }

    public static Player getLastSurvivor() {
        List<Player> Survivors = new ArrayList<>(Bukkit.getWorld("lavaRises")
                .getPlayers()).stream().filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .collect(Collectors.toList());

        if (Survivors.size() > 1) {
            return null;
        }
        return Survivors.getFirst();
    }

    public static void announceWinnerAndStopGame(Player p) {
        Location loc = Bukkit.getWorld("world").getSpawnLocation();
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendTitle(ChatColor.GOLD + p.getName() + " has won !", "");
            currentY = -64;
            players.teleport(loc);
            players.getInventory().clear();
            players.setExp(0);
            players.setHealth(20);
            players.setFoodLevel(20);
            players.setSaturation(5);
            players.setGameMode(GameMode.SURVIVAL);
        }
        startLavaRises.GS = GameStates.STOPPED;
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onPVPWhilePVPOff(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && startLavaRises.PS == PlayerStates.PVP_OFF) {
            event.setCancelled(true);
        }
    }
}