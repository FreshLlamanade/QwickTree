package me.monst.qwicktree.config.values.tree;

import me.monst.pluginutil.configuration.ConfigurationBranch;

public class Trees extends ConfigurationBranch {

    public final TreeConfiguration oak;
    public final TreeConfiguration spruce;
    public final TreeConfiguration birch;
    public final TreeConfiguration jungle;
    public final TreeConfiguration acacia;
    public final TreeConfiguration darkOak;
    public final TreeConfiguration mangrove;
    
    public Trees() {
        super("trees");
        this.oak = addChild(new OakConfiguration());
        this.birch = addChild(new BirchConfiguration());
        this.spruce = addChild(new SpruceConfiguration());
        this.jungle = addChild(new JungleConfiguration());
        this.acacia = addChild(new AcaciaConfiguration());
        this.darkOak = addChild(new DarkOakConfiguration());
        this.mangrove = addChild(new MangroveConfiguration());
    }

}
