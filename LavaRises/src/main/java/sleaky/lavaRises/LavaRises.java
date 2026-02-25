package sleaky.lavaRises;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LavaRises extends JavaPlugin {

    @Override
    public void onEnable() {
        GameLogic logic = new GameLogic(this);
        getCommand("startlavarises").setExecutor(new startLavaRises(logic));
        getServer().getPluginManager().registerEvents(logic, this);
        getCommand("stopgame").setExecutor(new stopLavaRises());
        getCommand("help").setExecutor(new helpCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
