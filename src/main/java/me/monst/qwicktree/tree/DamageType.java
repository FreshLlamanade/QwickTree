package me.monst.qwicktree.tree;

public enum DamageType {
	NONE,
	NORM,
	FIXED,
	MULT,
	;
	
	public static DamageType getFromName(String name) {
		return valueOf(name.toUpperCase());
	}
	
}
