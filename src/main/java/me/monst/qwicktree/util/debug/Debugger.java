package me.monst.qwicktree.util.debug;

import java.util.HashMap;

import me.monst.qwicktree.util.Message;
import org.bukkit.entity.Player;

public class Debugger {
	private static final HashMap<Player, Debugger> instances = new HashMap<>();
	
	public static Debugger get(Player player) {
		if (instances.get(player) == null)
			instances.put(player, new Debugger(player));
		
		return instances.get(player);
	}
	
	public static void remove(Player player) {
		instances.remove(player);
	}
	
	
	
	private final Player player;
	private final HashMap<String, Integer> stages;
	private boolean enabled;
	
	private Debugger(Player player) {
		this.player = player;
		
		stages = new HashMap<>();
		enabled = false;
	}
	
	public boolean toggleEnabled() {
		enabled = !enabled;
		
		return enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void addStage(String key) {
		int stage = 1;
		
		if (stages.containsKey(key))
			stage += stages.get(key);
		
		setStage(key, stage);
	}
	
	public void setStage(String key, int stage) {
		stages.put(key, stage);
	}
	
	public void outputDebugger() {
		if (enabled) {
			Message.DEBUG_TITLE.send(player);
			
			for (String key: stages.keySet())
				Message.DEBUG_ITEM.send(player, key, stages.get(key).toString());
		}
		
		stages.clear();
	}
}
