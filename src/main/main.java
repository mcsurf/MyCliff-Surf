package main;

import main.arena.ArenaManager;
import main.commands.AdminCommand;
import main.commands.KitCreationCommand;
import main.commands.MapCreationCommand;
import main.commands.StatsCommand;
import main.data.MySQL;
import main.listeners.PlayerListener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

	ArenaManager am = new ArenaManager(this);
	private MySQL sql;
	public static String Prefix = "§7[§aSurf§7] §f";

	public void onEnable() {
		am.load();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);
		this.getCommand("stats").setExecutor(new StatsCommand());
		this.getCommand("createmap").setExecutor(new MapCreationCommand());
		this.getCommand("createkit").setExecutor(new KitCreationCommand());
		this.getCommand("admin").setExecutor(new AdminCommand());
		this.getServer().getScheduler()
				.scheduleSyncRepeatingTask(this, new mainRunnable(), 0L, 20L);
		try {
			this.sql = new MySQL();
			this.sql.openConnection();
			this.sql.queryUpdate("CREATE TABLE IF NOT EXISTS surf_stats (id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(16), kills INT(100), tode INT(100), level INT(10))");
		} catch (Exception e) {
			System.err.println("[Surf] MYSQL SERVICE ERROR: " + e.getMessage());
		}
		am.changeArena();
		am.changeKit();
	}

	public void onDisable() {
		for (Player p : this.getServer().getOnlinePlayers()) {
			p.kickPlayer(Prefix + "§cDER SERVER WIRD RELOADED!");
		}
		am.save();
	}

	public MySQL getMySQL() {
		return this.sql;
	}
}
