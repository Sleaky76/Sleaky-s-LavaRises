package sleaky.lavaRises;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class stopLavaRises implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be a player to use this command.");
            return true;
        }
        if (startLavaRises.GS != GameStates.IN_GAME) {
            sender.sendMessage(ChatColor.RED + "There are no games currently going on.");
            return true;
        } else {
            startLavaRises.GS = GameStates.STOPPED;
            Location loc = Bukkit.getWorld("world").getSpawnLocation();
            GameLogic.currentY = -64;
            for (Player p : Bukkit.getWorld("lavaRises").getPlayers()) {
                p.teleport(loc);
                p.getInventory().clear();
                p.setExp(0);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setSaturation(5);
                p.setGameMode(GameMode.SURVIVAL);
            }
            Bukkit.unloadWorld("lavaRises", false);
            sender.sendMessage(ChatColor.GREEN + "Game successfully stopped.");
        }
        return true;
    }
}
