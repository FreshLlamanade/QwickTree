package me.monst.qwicktree.chop;

import java.util.List;

import me.monst.qwicktree.tree.TreeInfo;
import org.bukkit.Location;

public class TreeReplanter implements Runnable {
	protected TreeInfo tree;
	protected List<Location> baseLocations;
	protected int toReplant;
	
	public TreeReplanter(TreeInfo tree, List<Location> baseLocations, int toReplant) {
		this.tree = tree;
		this.baseLocations = baseLocations;
		this.toReplant = toReplant;
	}
	
	public void run() {
		//Don't bother if there's nothing left..
		if (toReplant <= 0)
			return;
		
		for (Location location: baseLocations) {
			tree.replantSapling(location);
			
			toReplant--;
			if (toReplant <= 0)
				break;
		}
	}
	
}
