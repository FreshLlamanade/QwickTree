package me.monst.qwicktree.config.values.tree;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class AcaciaConfiguration extends TreeConfiguration {

    public AcaciaConfiguration() {
        super("acacia");
    }
    
    @Override
    int getDefaultLeafReach() {
        return 3;
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
        return 20;
    }
    
    @Override
    Map<Material, Double> getDefaultDrops() {
        Map<Material, Double> map = new EnumMap<>(Material.class);
        map.put(Material.ACACIA_LEAVES, 0.01);
        map.put(Material.ACACIA_SAPLING, 0.025);
        return map;
    }
    
}
