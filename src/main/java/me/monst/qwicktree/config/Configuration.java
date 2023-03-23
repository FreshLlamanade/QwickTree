package me.monst.qwicktree.config;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.YamlFile;
import me.monst.qwicktree.QwickTree;
import me.monst.qwicktree.config.values.*;
import me.monst.qwicktree.config.values.tree.Trees;

public class Configuration extends ConfigurationBranch {
    
    private final YamlFile file;
    
    public final AllowedTools allowedTools = new AllowedTools();
    
    public final HouseMaterials houseMaterials = new HouseMaterials();
    
    public final TreeProtection treeProtection = new TreeProtection();
    
    public final EnablePermissions usePerms = new EnablePermissions();
    
    public final AllowSelfToggle allowSelfToggle = new AllowSelfToggle();
    
    public final GroupDrops doGroupDrops = new GroupDrops();
    
    public final CreativeModeSettings creativeModeSettings = new CreativeModeSettings();
    
    public final MaxVines maxVines = new MaxVines();
    
    public final Trees trees = new Trees();
    
    public Configuration(QwickTree plugin) {
        super("config.yml");
        
        this.file = new YamlFile(plugin, getKey());
    }
    
    public void reload() {
        load();
        save();
    }
    
    public void load() {
        feed(file.load());
    }
    
    public void save() {
        file.save(getAsYaml());
    }
    
}
