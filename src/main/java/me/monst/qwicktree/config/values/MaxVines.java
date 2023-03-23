package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.IntegerTransformer;

public class MaxVines extends ConfigurationValue<Integer> {
    
    public MaxVines() {
        super("max-vines", 10, new IntegerTransformer());
    }
    
}
