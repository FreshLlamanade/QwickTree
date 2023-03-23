package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.MaterialTransformer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Set;

public class AllowedTools extends ConfigurationValue<Set<Material>> {
    
    public AllowedTools() {
        super("items", getDefaultTools(), new MaterialTransformer().collect(() -> EnumSet.noneOf(Material.class)));
    }
    
    private static Set<Material> getDefaultTools() {
        return EnumSet.of(
                Material.STONE_AXE,
                Material.IRON_AXE,
                Material.GOLDEN_AXE,
                Material.DIAMOND_AXE,
                Material.NETHERITE_AXE
        );
    }
    
    public boolean contains(Material material) {
        return get().contains(material);
    }
    
    public boolean contains(ItemStack item) {
        return item != null && get().contains(item.getType());
    }
    
}
