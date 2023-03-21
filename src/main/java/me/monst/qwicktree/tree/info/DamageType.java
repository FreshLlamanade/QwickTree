package me.monst.qwicktree.tree.info;

public enum DamageType {
	NONE,
	NORM,
	FIXED,
	MULT,
	;
	
	public static DamageType getFromName(String name) {
		for (DamageType type: values())
			if (type.name().equalsIgnoreCase(name))
				return type;
		
		return null;
	}
}
