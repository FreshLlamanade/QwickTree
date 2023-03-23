package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.BooleanTransformer;
import me.monst.qwicktree.util.Permission;

public class GroupDrops extends ConfigurationValue<Boolean> {
    
    public GroupDrops() {
        super("groupDrops", true, new BooleanTransformer());
    }
    
}
