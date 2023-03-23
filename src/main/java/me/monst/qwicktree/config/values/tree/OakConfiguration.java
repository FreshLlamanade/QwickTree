package me.monst.qwicktree.config.values.tree;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class OakConfiguration extends TreeConfiguration {

    public OakConfiguration() {
        super("oak");
    }
    
    @Override
    int getDefaultLeafReach() {
        return 2;
    }
    
    @Override
    int getDefaultLeafMin() {
        return 10;
    }
    
    @Override
    int getDefaultLogMin() {
        return 4;
    }
    
    @Override
    int getDefaultLogMax() {
        return 50;
    }
    
    @Override
    Map<Material, Double> getDefaultDrops() {
        Map<Material, Double> map = new EnumMap<>(Material.class);
        map.put(Material.OAK_LEAVES, 0.01);
        map.put(Material.OAK_SAPLING, 0.025);
        map.put(Material.APPLE, 0.005);
        return map;
    }
    
}
