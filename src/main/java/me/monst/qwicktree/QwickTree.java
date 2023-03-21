package me.monst.qwicktree;

import java.util.HashMap;

import me.monst.qwicktree.tree.Tree;
import org.bukkit.plugin.java.JavaPlugin;

import me.monst.qwicktree.config.Config;
import me.monst.qwicktree.util.DisabledList;
import me.monst.qwicktree.util.HouseIgnore;
import me.monst.qwicktree.util.Logging;

public class QwickTree extends JavaPlugin {
	private static QwickTree instance;
	
	public static QwickTree get() {
		return instance;
	}
	
	private final HashMap<Tree.Type, Integer> chopCount;
	
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
	
	public void addTreeChop(Tree.Type type) {
		chopCount.compute(type, (t, count) -> count == null ? 1 : count + 1);
	}
	
	public HashMap<Tree.Type, Integer> getChopCount() {
		return new HashMap<>(chopCount);
	}
	
}
