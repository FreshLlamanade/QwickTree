package me.monst.qwicktree.config.values;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.qwicktree.config.transform.BooleanTransformer;

public class TreeProtectionMessage extends ConfigurationBranch {
    
    public final ConfigurationValue<Boolean> chat;
    public final ConfigurationValue<Boolean> console;
    
    public TreeProtectionMessage() {
        super("message");
        this.chat = addChild(new ConfigurationValue<>("chat", false, new BooleanTransformer()));
        this.console = addChild(new ConfigurationValue<>("console", true, new BooleanTransformer()));
    }
    
}
