package sleaky.lavaRises;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.eclipse.aether.util.listener.ChainedTransferListener;

public class scoreBoard {
    public void updateScoreboard(Player player, int lavaHeight) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective obj = board.registerNewObjective("LavaRise", Criteria.DUMMY, ChatColor.GOLD + "" + ChatColor.BOLD + "LAVA RISE");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score blankLine = obj.getScore(" "); // Spacer
        blankLine.setScore(4);

        Score height = obj.getScore(ChatColor.WHITE + "Lava Height: " + ChatColor.RED + lavaHeight);
        height.setScore(3);

        Score Survivors = obj.getScore(ChatColor.WHITE + "Survivors: " + ChatColor.GOLD + GameLogic.CheckForSurvivors());
        Survivors.setScore(2);
        if (GameLogic.PVPtimer > 0) {
            Score PVPtimer = obj.getScore(ChatColor.WHITE + "Time until pvp on: " + ChatColor.GOLD + GameLogic.PVPtimer / 60 + ":" + GameLogic.PVPtimer % 60);
            PVPtimer.setScore(1);
        } else {
            Score PVPtimer = obj.getScore(ChatColor.RED + "PVP IS ON !");
            PVPtimer.setScore(1);
        }

        player.setScoreboard(board);
    }
}
