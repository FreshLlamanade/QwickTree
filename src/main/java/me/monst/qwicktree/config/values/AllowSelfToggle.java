package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.BooleanTransformer;
import me.monst.qwicktree.util.Permission;

public class AllowSelfToggle extends ConfigurationValue<Boolean> {
    
    public AllowSelfToggle() {
        super("allowSelfToggle", false, new BooleanTransformer());
        afterSet();
    }
    
    @Override
    protected void afterSet() {
        if (get())
            Permission.TOGGLE_SELF.setDefault(true);
        else
            Permission.TOGGLE_SELF.setOp(true);
    }
    
}
