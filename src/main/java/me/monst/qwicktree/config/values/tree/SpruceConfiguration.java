package me.monst.qwicktree.config.values.tree;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class SpruceConfiguration extends TreeConfiguration {

    public SpruceConfiguration() {
        super("spruce");
    }
    
    @Override
    int getDefaultLeafReach() {
        return 3;
    }
    
    @Override
    int getDefaultLeafMin() {
        return 8;
    }
    
    @Override
    int getDefaultLogMin() {
        return 4;
    }
    
    @Override
    int getDefaultLogMax() {
        return 200;
    }
    
    @Override
    Map<Material, Double> getDefaultDrops() {
        Map<Material, Double> map = new EnumMap<>(Material.class);
        map.put(Material.SPRUCE_LEAVES, 0.01);
        map.put(Material.SPRUCE_SAPLING, 0.025);
        return map;
    }
    
}
