package sleaky.lavaRises;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class helpCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
            return true;
        }
        sender.sendMessage("Here's a list of available commands:");
        sender.sendMessage("");
        sender.sendMessage("/help - Shows available commands and their usage");
        sender.sendMessage("------------------------------------------------");
        sender.sendMessage("/startlavarises <MapSize> <seed> <TimeBetweenEachLavaRise> <TimeUntilPvP> - Allows you to start the game");
        sender.sendMessage("------------------------------------------------");
        sender.sendMessage("MapSize is the world border size, seed is the world seed, TimeBetweenEachLavaRise is the amount of time it takes for the lava to rise one block (sec), TimeUntilPvP is the amount of time until players can pvp (sec)");
        sender.sendMessage("------------------------------------------------");
        sender.sendMessage("/stopgame - stops a currently going on game");
        return true;
    }
}
