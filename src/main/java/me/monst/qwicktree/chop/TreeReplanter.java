package me.monst.qwicktree.chop;

import java.util.List;

import me.monst.qwicktree.tree.Tree;
import org.bukkit.Location;

public class TreeReplanter implements Runnable {
	protected Tree tree;
	protected List<Location> baseLocations;
	protected int toReplant;
	
	public TreeReplanter(Tree tree, List<Location> baseLocations, int toReplant) {
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
