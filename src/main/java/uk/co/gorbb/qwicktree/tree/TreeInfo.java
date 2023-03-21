package uk.co.gorbb.qwicktree.tree;

import java.util.List;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import uk.co.gorbb.qwicktree.tree.info.DamageType;
import uk.co.gorbb.qwicktree.tree.info.TreeType;
import uk.co.gorbb.qwicktree.util.Message;

public class TreeInfo {
	private TreeType					treeType;			//Used for internal purposes.
	
	private boolean 					enabled,			//Whether or not this tree is enabled.
										replant,			//Whether or not to replace saplings when the tree is chopped down.
										autoCollect,		//Whether to automatically place the items dropped into the player's inventory.
										stump,				//Can the tree be chopped from above the base block?
										anyBlock;			//Can any block be broken to chop the entire tree?
	
	private int							leafReach,			//How far from the logs to search for leaves.
										leafGroundOffset,	//What level to ignore leaves at depending on how close they are to the ground.
										leafMin;			//Minimum number of leaves required for the tree to be valid.
	
	private int							logMin,				//Minimum number of logs required for the tree to be valid.
										logMax;				//Maximum number of logs allowed in a valid tree. If any more logs are found, the tree is not valid.
	
	private TreeMap<Double, Material>	drops;				//Which items to drop when the tree is chopped.
	
	private DamageType					damageType;			//Which type of damage to deal to a damagable item when the tree is chopped.
	private int							damageAmount,		//The multiplier or amount of damage to deal, depending on the damage type.
										replantTimer;		//Time (in ticks) to replant the tree after chopping it
	
	public TreeInfo(TreeType treeType, boolean enabled, boolean replant, boolean autoCollect, boolean stump, boolean anyBlock, int leafReach, int leafGroundOffset, int leafMin,
					int logMin, int logMax, List<String> drops, DamageType damageType, int damageAmount, int replantTimer) {
		this.treeType = treeType;
		
		this.enabled = enabled;
		this.replant = replant;
		this.autoCollect = autoCollect;
		this.stump = stump;
		this.anyBlock = anyBlock;
		
		this.leafReach = leafReach;
		this.leafGroundOffset = leafGroundOffset;
		this.leafMin = leafMin;
		
		this.logMin = logMin;
		this.logMax = logMax;
		
		this.drops = processDrops(drops);
		
		this.damageType = damageType;
		this.damageAmount = damageAmount;
		this.replantTimer = replantTimer;
	}
	
	private TreeMap<Double, Material> processDrops(List<String> drops) {
		TreeMap<Double, Material> newDrops = new TreeMap<Double, Material>();
		double chance = 0;
		double scale = 1;
		
		//First, scan through the list and figure out if it needs scaling
		for (String row: drops)
			try { chance += Double.parseDouble(row.split(",")[1]); } catch (NumberFormatException e) {}//Silently fail
		
		//If the total chance count is higher than 1, then it all needs scaling down so it only adds up to 1
		if (chance > 1) {
			scale /= chance;
			Message.CHANCE_SCALING.warn(treeType.toString(), String.valueOf(scale));
		}
		chance = 0;
		
		for (String row: drops) {
			String[] data = row.split(",");
			
			Material material = Material.getMaterial(data[0]);
			
			if (material == null) {
				Message.MATERIAL_CONVERT_ERROR.warn(data[0]);
				continue; //Skip if material not found
			}
			
			try {
				chance += (Double.parseDouble(data[1]) * scale);
			}
			catch (NumberFormatException e) {
				Message.CHANCE_CONVERT_ERROR.warn(data[1], material.toString());
				continue; //Skip if chance not valid
			}
			
			newDrops.put(chance, material);
		}
		if (chance < 1.0)
			newDrops.put(1.0, null);
		
		
		
		return newDrops;
	}
	
	public TreeType getType() {
		return treeType;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean doReplant() {
		return replant;
	}
	
	public boolean doAutoCollect() {
		return autoCollect;
	}
	
	public boolean getAnyBlock() {
		return anyBlock;
	}
	
	public boolean getAllowStump() {
		return stump;
	}
	
	public int getLeafReach() {
		return leafReach;
	}
	
	public int getLeafGroundOffset() {
		return leafGroundOffset;
	}
	
	public int getLeafMin() {
		return leafMin;
	}
	
	public int getLogMin() {
		return logMin;
	}
	
	public int getLogMax() {
		return logMax;
	}
	
	public TreeMap<Double, Material> getDrops() {
		return drops;
	}
	
	public DamageType getDamageType() {
		return damageType;
	}
	
	public int getDamageAmount() {
		return damageAmount;
	}
	
	public int getReplantTimer() {
		return replantTimer;
	}
	
	public boolean isValidLog(Block block) {
		return block != null && treeType.getLogType() == block.getType();
	}
	
	public boolean isValidLeaf(Block block) {
		return block != null && treeType.getLeafType() == block.getType();
	}
	
	public boolean isValidSapling(Block block) {
		return block != null && treeType.getSaplingType() == block.getType();
	}
	
	public boolean isValidSapling(ItemStack item) {
		return item != null && isValidSapling(item.getType());
	}
	
	public boolean isValidSapling(Material material) {
		return treeType.getSaplingType() == material;
	}
	
	public boolean isValidStandingBlock(Block block) {
		switch (block.getType()) {
			case DIRT:
			case COARSE_DIRT:
			case PODZOL:
			case GRASS:
			case FARMLAND:
				return true;
			default:
				return false;
		}
	}
	
	public void replantSapling(Location location) {
		Block block = location.getBlock();
		
		block.setType(treeType.getSaplingType());
	}
	
	public ItemStack getLogItem(int qty) {
		return new ItemStack(treeType.getLogType(), qty);
	}
}
