package sleaky.lavaRises;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class startLavaRises implements CommandExecutor {

    private final NewWorld world = new NewWorld();
    private final GameLogic logic;
    public static GameStates GS = GameStates.STOPPED;
    public static PlayerStates PS = PlayerStates.PVP_ON;

    public startLavaRises(GameLogic logic) {
        this.logic = logic;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be a player to use this command.");
            return true;
        }
        if (GS == GameStates.IN_GAME) {
            sender.sendMessage(ChatColor.RED + "A game is already going on.");
            return true;
        }
        GS = GameStates.IN_GAME;
        PS = PlayerStates.PVP_OFF;
        int worldSize = 250;
        int timeBetweenLayers = 60;
        long TimeUntilPvP = 900;
        if (args.length == 4 && isInt(args[0]) && isLong(args[1]) && isInt(args[2]) && isLong(args[3])) {
            worldSize = Integer.parseInt(args[0]);
            timeBetweenLayers = Integer.parseInt(args[2]) * 20;
            TimeUntilPvP = Long.parseLong(args[3]);
            sender.sendMessage(ChatColor.GREEN + "Creating world with seed " + args[1] + " and size " + worldSize + ".");
            world.createSetSeedWorld(Long.parseLong(args[1]));
        } else if (args.length == 3 && isInt(args[0]) && isLong(args[1]) && isInt(args[2])) {
            timeBetweenLayers = Integer.parseInt(args[2]) * 20;
            worldSize = Integer.parseInt(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Creating world with seed " + args[1] + " and size " + worldSize + ".");
            world.createSetSeedWorld(Long.parseLong(args[1]));
        } else if (args.length == 2 && isInt(args[0]) && isLong(args[1])) {
            worldSize = Integer.parseInt(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Creating world with seed " + args[1] + " and size " + worldSize + ".");
            world.createSetSeedWorld(Long.parseLong(args[1]));
        } else if (args.length == 1 && isInt(args[0])) {
            worldSize = Integer.parseInt(args[0]);
            sender.sendMessage(ChatColor.GREEN + "Creating new random world of size " + worldSize);
            world.createNewRandomWorld();
        } else {
            sender.sendMessage(ChatColor.GREEN + "Creating new random world with default 250 size");
            world.createNewRandomWorld();
        }

        World lavaRisesWorld = Bukkit.getWorld("lavaRises");
        Location playerSpawn = lavaRisesWorld.getSpawnLocation();
        double radius = worldSize / 2d;
        if (lavaRisesWorld != null) {
            Location corner1 = new Location(lavaRisesWorld, playerSpawn.getX() - radius, -64, playerSpawn.getZ() - radius);
            Location corner2 = new Location(lavaRisesWorld, playerSpawn.getX() + radius, -64, playerSpawn.getZ() + radius);

            sender.sendMessage(corner1.getX()+ "," + corner1.getZ() + "," + corner2.getX() + "," + corner2.getZ());

            logic.setWorldBorder(1, worldSize);
            logic.LavaRiseByOneBlock(timeBetweenLayers, corner1, corner2);
            world.teleportPlayersToWorld(lavaRisesWorld.getWorldBorder());
            logic.setGameRule();
            logic.PVPTimer(TimeUntilPvP);
        } else {
            sender.sendMessage(ChatColor.RED + "Error: World 'lavaRises' could not be found!");
        }

        return true;
    }

    private static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}