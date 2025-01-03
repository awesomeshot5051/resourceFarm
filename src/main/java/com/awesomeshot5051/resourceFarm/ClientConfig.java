package com.awesomeshot5051.resourceFarm;

import de.maxhenkel.corelib.config.*;
import net.neoforged.neoforge.common.*;

public class ClientConfig extends ConfigBase {

    //    public final ModConfigSpec.BooleanValue enableRightClickPickup;
    public final ModConfigSpec.DoubleValue villagerVolume;
    public final ModConfigSpec.EnumValue<CycleTradesButtonLocation> cycleTradesButtonLocation;
    public final ModConfigSpec.BooleanValue renderBlockContents;
    public final ModConfigSpec.IntValue blockRenderDistance;
    public final ModConfigSpec.BooleanValue pickaxeSoundRendered;

    public ClientConfig(ModConfigSpec.Builder builder) {
        super(builder);
        pickaxeSoundRendered = builder
                .comment("Whether or not the pickaxe mining sounds will be played")
                .define("pickaxeSound", true);
//        enableRightClickPickup = builder
//                .comment("If villagers should be able to be picked up by sneaking and right-clicking")
//                .define("villager.sneak_pick_up", true);
        villagerVolume = builder
                .comment("The volume of every villager related sound in this mod")
                .defineInRange("villager.volume", 1D, 0D, 1D);
        cycleTradesButtonLocation = builder
                .comment("The location of the cycle trades button")
                .defineEnum("villager.cycle_trades_button_location", CycleTradesButtonLocation.TOP_LEFT);
        renderBlockContents = builder
                .comment("Disables rendering any contents of the villager blocks when set to false", "Set to false if you have performance issues with the mod")
                .worldRestart()
                .define("villager.render_block_contents", true);
        blockRenderDistance = builder
                .comment("The distance in blocks at which the block contents are rendered", "This has no effect if 'render_block_contents' is disabled")
                .defineInRange("villager.block_render_distance", 32, 1, 256);
    }

    public enum CycleTradesButtonLocation {
        TOP_LEFT, TOP_RIGHT, NONE
    }

}
