package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.MaterialTransformer;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.EnumSet;
import java.util.Set;

public class HouseMaterials extends ConfigurationValue<Set<Material>> {
    
    public HouseMaterials() {
        super("house", getDefaultHouseMaterials(),
                new MaterialTransformer().collect(() -> EnumSet.noneOf(Material.class)));
    }
    
    private static Set<Material> getDefaultHouseMaterials() {
        return EnumSet.of(
                Material.GLASS,
                Material.GLASS_PANE,
                Material.IRON_BARS,
                Material.IRON_TRAPDOOR,
                Material.IRON_DOOR,
                Material.BLACK_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS,
                Material.BROWN_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS,
                Material.LIGHT_GRAY_STAINED_GLASS,
                Material.LIME_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS,
                Material.ORANGE_STAINED_GLASS,
                Material.PINK_STAINED_GLASS,
                Material.PURPLE_STAINED_GLASS,
                Material.RED_STAINED_GLASS,
                Material.WHITE_STAINED_GLASS,
                Material.YELLOW_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.BROWN_STAINED_GLASS_PANE,
                Material.CYAN_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS_PANE,
                Material.GREEN_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS_PANE,
                Material.MAGENTA_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE,
                Material.WHITE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.BLACK_CARPET,
                Material.BLUE_CARPET,
                Material.BROWN_CARPET,
                Material.CYAN_CARPET,
                Material.GRAY_CARPET,
                Material.GREEN_CARPET,
                Material.LIGHT_BLUE_CARPET,
                Material.LIGHT_GRAY_CARPET,
                Material.LIME_CARPET,
                Material.MAGENTA_CARPET,
                Material.ORANGE_CARPET,
                Material.PINK_CARPET,
                Material.PURPLE_CARPET,
                Material.RED_CARPET,
                Material.WHITE_CARPET,
                Material.YELLOW_CARPET,
                Material.OAK_FENCE,
                Material.SPRUCE_FENCE,
                Material.BIRCH_FENCE,
                Material.JUNGLE_FENCE,
                Material.ACACIA_FENCE,
                Material.DARK_OAK_FENCE,
                Material.OAK_FENCE_GATE,
                Material.SPRUCE_FENCE_GATE,
                Material.BIRCH_FENCE_GATE,
                Material.JUNGLE_FENCE_GATE,
                Material.ACACIA_FENCE_GATE,
                Material.DARK_OAK_FENCE_GATE,
                Material.OAK_DOOR,
                Material.SPRUCE_DOOR,
                Material.BIRCH_DOOR,
                Material.JUNGLE_DOOR,
                Material.ACACIA_DOOR,
                Material.DARK_OAK_DOOR,
                Material.OAK_TRAPDOOR,
                Material.SPRUCE_TRAPDOOR,
                Material.BIRCH_TRAPDOOR,
                Material.JUNGLE_TRAPDOOR,
                Material.ACACIA_TRAPDOOR,
                Material.DARK_OAK_TRAPDOOR
        );
    }
    
    public boolean contains(Material material) {
        return get().contains(material);
    }
    
    public boolean contains(Block block) {
        return block != null && get().contains(block.getType());
    }
    
}
