package me.monst.qwicktree.config.values.tree;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.MapTransformer;
import me.monst.qwicktree.config.transform.*;
import me.monst.qwicktree.tree.DamageType;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public abstract class TreeConfiguration extends ConfigurationBranch {
    
    public final ConfigurationValue<Boolean> enabled;
    public final ConfigurationValue<Boolean> replant;
    public final ConfigurationValue<Boolean> autoCollect;
    public final ConfigurationValue<Boolean> stump;
    public final ConfigurationValue<Boolean> anyBlock;
    public final ConfigurationValue<Integer> replantDelay;
    public final LeafSettings leaf;
    public final LogSettings log;
    public final DamageSettings damage;
    public final ConfigurationValue<Map<Material, Double>> drops;
    
    public TreeConfiguration(String treeName) {
        super(treeName);
        this.enabled = addChild(new ConfigurationValue<>("enabled", true, new BooleanTransformer()));
        this.autoCollect = addChild(new ConfigurationValue<>("auto-collect", false, new BooleanTransformer()));
        this.stump = addChild(new ConfigurationValue<>("stump", true, new BooleanTransformer()));
        this.anyBlock = addChild(new ConfigurationValue<>("any-block", false, new BooleanTransformer()));
        this.replant = addChild(new ConfigurationValue<>("replant", true, new BooleanTransformer()));
        this.replantDelay = addChild(new ConfigurationValue<>("replant-delay", 0, new IntegerTransformer()));
        this.leaf = addChild(new LeafSettings(getDefaultLeafReach(), getDefaultLeafMin()));
        this.log = addChild(new LogSettings(getDefaultLogMin(), getDefaultLogMax()));
        this.damage = addChild(new DamageSettings());
        this.drops = addChild(new ConfigurationValue<>("drops", getDefaultDrops(),
                new MapTransformer<>(
                        () -> new EnumMap<>(Material.class),
                        new MaterialTransformer(),
                        new DoubleTransformer())));
    }
    
    public static class LeafSettings extends ConfigurationBranch {
        public final ConfigurationValue<Integer> reach;
        public final ConfigurationValue<Integer> groundOffset;
        public final ConfigurationValue<Integer> min;
        
        private LeafSettings(int reach, int min) {
            super("leaf");
            this.reach = addChild(new ConfigurationValue<>("reach", reach, new IntegerTransformer()));
            this.groundOffset = addChild(new ConfigurationValue<>("groundOffset", 0, new IntegerTransformer()));
            this.min = addChild(new ConfigurationValue<>("min", min, new IntegerTransformer()));
        }
    }
    
    public static class LogSettings extends ConfigurationBranch {
        public final ConfigurationValue<Integer> min;
        public final ConfigurationValue<Integer> max;
        
        private LogSettings(int min, int max) {
            super("log");
            this.min = addChild(new ConfigurationValue<>("min", min, new IntegerTransformer()));
            this.max = addChild(new ConfigurationValue<>("max", max, new IntegerTransformer()));
        }
    }
    
    public static class DamageSettings extends ConfigurationBranch {
        public final ConfigurationValue<DamageType> type;
        public final ConfigurationValue<Integer> amount;
        
        private DamageSettings() {
            super("damage");
            this.type = addChild(new ConfigurationValue<>("type", DamageType.NORM, new DamageTypeTransformer()));
            this.amount = addChild(new ConfigurationValue<>("amount", 1, new IntegerTransformer()));
        }
    }
    
    abstract int getDefaultLeafReach();
    
    abstract int getDefaultLeafMin();
    
    abstract int getDefaultLogMin();
    
    abstract int getDefaultLogMax();
    
    abstract Map<Material, Double> getDefaultDrops();
    
}
