package me.monst.qwicktree.config.values.tree;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class JungleConfiguration extends TreeConfiguration {

    public JungleConfiguration() {
        super("jungle");
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
        return 200;
    }
    
    @Override
    Map<Material, Double> getDefaultDrops() {
        Map<Material, Double> map = new EnumMap<>(Material.class);
        map.put(Material.JUNGLE_LEAVES, 0.01);
        map.put(Material.JUNGLE_SAPLING, 0.025);
        return map;
    }
    
}
