package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.BooleanTransformer;

public class CreativeModeSettings extends ConfigurationBranch {
    
    public final ConfigurationValue<Boolean> doAutoCollect;
    public final ConfigurationValue<Boolean> replant;
    public final ConfigurationValue<Boolean> doDamage;
    
    public CreativeModeSettings() {
        super("creative");
        this.doAutoCollect = addChild(new ConfigurationValue<>("autoCollect", true, new BooleanTransformer()));
        this.replant = addChild(new ConfigurationValue<>("replant", true, new BooleanTransformer()));
        this.doDamage = addChild(new ConfigurationValue<>("damage", false, new BooleanTransformer()));
    }
    
}
