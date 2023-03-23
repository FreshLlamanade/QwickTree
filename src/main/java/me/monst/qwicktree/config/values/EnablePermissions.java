package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.BooleanTransformer;
import me.monst.qwicktree.util.Permission;

public class EnablePermissions extends ConfigurationValue<Boolean> {
    
    public EnablePermissions() {
        super("enable-permissions", true, new BooleanTransformer());
        afterSet();
    }
    
    @Override
    protected void afterSet() {
        Permission.USE.setDefault(get());
    }
    
}
