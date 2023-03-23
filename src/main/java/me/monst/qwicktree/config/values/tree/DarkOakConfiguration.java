package me.monst.qwicktree.config.values.tree;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class DarkOakConfiguration extends TreeConfiguration {

    public DarkOakConfiguration() {
        super("dark-oak");
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
        return 200;
    }
    
    @Override
    Map<Material, Double> getDefaultDrops() {
        Map<Material, Double> map = new EnumMap<>(Material.class);
        map.put(Material.DARK_OAK_LEAVES, 0.01);
        map.put(Material.DARK_OAK_SAPLING, 0.025);
        map.put(Material.APPLE, 0.005);
        return map;
    }
    
}
