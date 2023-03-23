package me.monst.qwicktree.config.transform;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.transform.Transformer;
import me.monst.qwicktree.util.Message;
import org.bukkit.Material;

public class MaterialTransformer implements Transformer<Material> {
    
    @Override
    public Material parse(String input) throws ArgumentParseException {
        Material material = Material.matchMaterial(input);
        if (material == null)
            throw new ArgumentParseException(Message.MATERIAL_CONVERT_ERROR.prepare(input));
        return material;
    }
    
}
