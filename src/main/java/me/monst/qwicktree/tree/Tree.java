package me.monst.qwicktree.tree;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.monst.qwicktree.util.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;
import static org.bukkit.Material.ACACIA_SAPLING;

public class Tree {
	
	private final Type treeType;			//Used for internal purposes.
	
	private final boolean 				enabled,			// Whether this tree is enabled.
										replant,			// Whether to replace saplings when the tree is chopped down.
										autoCollect,		// Whether to automatically place the items dropped into the player's inventory.
										stump,				// Can the tree be chopped from above the base block?
										anyBlock;			// Can any block be broken to chop the entire tree?
	
	private final int					leafReach,			// How far from the logs to search for leaves.
										leafGroundOffset,	// What level to ignore leaves at depending on how close they are to the ground.
										leafMin;			// Minimum number of leaves required for the tree to be valid.
	
	private final int					logMin,				// Minimum number of logs required for the tree to be valid.
										logMax;				// Maximum number of logs allowed in a valid tree. If any more logs are found, the tree is not valid.
	
	private final Map<Double, Material> drops;				// Which items to drop when the tree is chopped.
	
	private final DamageType 			damageType;			// Which type of damage to deal to a damagable item when the tree is chopped.
	private final int					damageAmount,		// The multiplier or amount of damage to deal, depending on the damage type.
										replantTimer;		// Time (in ticks) to replant the tree after chopping it
	
	public Tree(Type treeType, boolean enabled, boolean replant, boolean autoCollect, boolean stump, boolean anyBlock, int leafReach, int leafGroundOffset, int leafMin,
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
	
	private Map<Double, Material> processDrops(List<String> drops) {
		Map<Double, Material> newDrops = new TreeMap<>();
		double chance = 0;
		double scale = 1;
		
		//First, scan through the list and figure out if it needs scaling
		for (String row: drops) {
			try {
				chance += Double.parseDouble(row.split(",")[1]);
			} catch (NumberFormatException ignored) {} //Silently fail
		}
		
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
	
	public Type getType() {
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
	
	public Map<Double, Material> getDrops() {
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
	
	public enum Type {
		OAK("Oak", OAK_LOG, OAK_LEAVES, OAK_SAPLING),
		PINE("Pine", SPRUCE_LOG, SPRUCE_LEAVES, SPRUCE_SAPLING),
		BIRCH("Birch", BIRCH_LOG, BIRCH_LEAVES, BIRCH_SAPLING),
		JUNGLE("Jungle", JUNGLE_LOG, JUNGLE_LEAVES, JUNGLE_SAPLING),
		DARK_OAK("Dark Oak", DARK_OAK_LOG, DARK_OAK_LEAVES, DARK_OAK_SAPLING),
		ACACIA("Acacia", ACACIA_LOG, ACACIA_LEAVES, ACACIA_SAPLING),
		GIANT_RED_MUSHROOM("Red Mushroom", RED_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, RED_MUSHROOM),
		GIANT_BROWN_MUSHROOM("Brown Mushroom", BROWN_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK, BROWN_MUSHROOM),
		CUSTOM("Custom", null, null, null),
		;
		
		private final String name;
		private final Material log;
		private final Material leaf;
		private final Material sapling;
		
		Type(String name, Material log, Material leaf, Material sapling) {
			this.name = name;
			this.log = log;
			this.leaf = leaf;
			this.sapling = sapling;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public Material getLogType() {
			return log;
		}
		
		public Material getLeafType() {
			return leaf;
		}
		
		public Material getSaplingType() {
			return sapling;
		}
		
		public static Type getFromSpecies(TreeSpecies species) {
			switch (species) {
				case GENERIC:	return OAK;
				case REDWOOD:	return PINE;
				case BIRCH:		return BIRCH;
				case JUNGLE:	return JUNGLE;
				case DARK_OAK:	return DARK_OAK;
				case ACACIA:	return ACACIA;
				default:		return CUSTOM;
			}
		}
		
		public static Type getFromType(TreeType type) {
			switch (type) {
				case TREE:
				case BIG_TREE:		return OAK;
				case REDWOOD:
				case TALL_REDWOOD:	return PINE;
				case BIRCH:			return BIRCH;
				case JUNGLE:
				case SMALL_JUNGLE:
				case COCOA_TREE:
				case JUNGLE_BUSH:	return JUNGLE;
				case RED_MUSHROOM:	return CUSTOM;
				case BROWN_MUSHROOM:return CUSTOM;
				case SWAMP:			return CUSTOM;
				case ACACIA:		return ACACIA;
				case DARK_OAK:		return DARK_OAK;
				default:			return CUSTOM;
			}
		}
	}
}
