package me.monst.qwicktree.config.transform;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.transform.Transformer;
import me.monst.qwicktree.tree.DamageType;

public class DamageTypeTransformer implements Transformer<DamageType> {
    
    @Override
    public DamageType parse(String input) throws ArgumentParseException {
        try {
            return DamageType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException("'" + input + "' is not a valid damage type.");
        }
    }
    
}
