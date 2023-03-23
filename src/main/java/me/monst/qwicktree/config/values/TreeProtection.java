package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.BooleanTransformer;

public class TreeProtection extends ConfigurationBranch {
    
    public final ConfigurationValue<Boolean> enabled;
    public final TreeProtectionMessage message;
    
    public TreeProtection() {
        super("tree-protection");
        this.enabled = addChild(new ConfigurationValue<>("enabled", true, new BooleanTransformer()));
        this.message = addChild(new TreeProtectionMessage());
    }
    
}
