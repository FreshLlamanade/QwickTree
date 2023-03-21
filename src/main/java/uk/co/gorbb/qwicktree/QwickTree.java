package uk.co.gorbb.qwicktree;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import uk.co.gorbb.qwicktree.config.Config;
import uk.co.gorbb.qwicktree.tree.info.TreeType;
import uk.co.gorbb.qwicktree.util.DisabledList;
import uk.co.gorbb.qwicktree.util.HouseIgnore;
import uk.co.gorbb.qwicktree.util.Logging;

public class QwickTree extends JavaPlugin {
	private static QwickTree instance;
	
	public static QwickTree get() {
		return instance;
	}
	
	private final HashMap<TreeType, Integer> chopCount;
	
	public QwickTree() {
		instance = this;
		
		chopCount = new HashMap<>();
	}
	
	@Override
	public void onEnable() {
		Config.get().update();
		Config.get().load();
		
		getServer().getPluginManager().registerEvents(new QTListener(), this);
		getCommand("qt").setExecutor(new QTCommand());
		
		//Load data
		HouseIgnore.get().load();
		DisabledList.get().load();
		
		//Check for loaded logging plugins
		Logging.checkPlugins();
	}
	
	@Override
	public void onDisable() {
		//Save data
		HouseIgnore.get().save();
		DisabledList.get().save();
		
		getServer().getScheduler().cancelTasks(this);
	}
	
	public void addTreeChop(TreeType type) {
		chopCount.compute(type, (t, count) -> count == null ? 1 : count + 1);
	}
	
	public HashMap<TreeType, Integer> getChopCount() {
		return new HashMap<>(chopCount);
	}
	
}
