package uk.co.gorbb.qwicktree.tree.info;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

import static org.bukkit.Material.*;

public enum TreeType {
	OAK("Oak", OAK_LOG, OAK_LEAVES, OAK_SAPLING),
	PINE("Pine", SPRUCE_LOG, SPRUCE_LEAVES, SPRUCE_SAPLING),
	BIRCH("Birch", BIRCH_LOG, BIRCH_LEAVES, BIRCH_SAPLING),
	JUNGLE("Jungle", JUNGLE_LOG, JUNGLE_LEAVES, JUNGLE_SAPLING),
	DARK_OAK("Dark Oak", DARK_OAK_LOG, DARK_OAK_LEAVES, DARK_OAK_SAPLING),
	ACACIA("Acacia", ACACIA_LOG, ACACIA_LEAVES, ACACIA_SAPLING),
	CUSTOM("Custom", null, null, null),
	;
	
	private final String name;
	private final Material log;
	private final Material leaf;
	private final Material sapling;
	
	TreeType(String name, Material log, Material leaf, Material sapling) {
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
	
	public static TreeType getFromSpecies(TreeSpecies species) {
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
}
