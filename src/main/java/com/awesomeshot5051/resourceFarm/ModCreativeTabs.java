package com.awesomeshot5051.resourceFarm;


import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.items.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_RESOURCE_FARMS = TAB_REGISTER.register("assets/resource_farms", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.COPPER_FARM.get())) // Use your custom item here
            .displayItems((features, output) -> {


/// Common Regular
                output.accept(new ItemStack(ModBlocks.BASALT_FARM.get()));
                output.accept(new ItemStack(ModBlocks.BLACKSTONE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.CALCITE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.COAL_FARM.get()));
                output.accept(new ItemStack(ModBlocks.COBBLESTONE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.COPPER_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DIRT_FARM.get()));
                output.accept(new ItemStack(ModBlocks.GRASS_FARM.get()));
                output.accept(new ItemStack(ModBlocks.GRAVEL_FARM.get()));
                output.accept(new ItemStack(ModBlocks.SAND_FARM.get()));
                output.accept(new ItemStack(ModBlocks.STONE_FARM.get()));

/// Uncommon Regular
                output.accept(new ItemStack(ModBlocks.CONCRETE_POWDER_FARM.get()));
                output.accept(new ItemStack(ModBlocks.CONCRETE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.REDSTONE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.SSTONE_FARM.get()));

/// Rare Regular
                output.accept(new ItemStack(ModBlocks.LAPIS_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DIAMOND_FARM.get()));
                output.accept(new ItemStack(ModBlocks.TERRACOTTA_FARM.get()));

/// Very Rare Regular
                output.accept(new ItemStack(ModBlocks.EMERALD_FARM.get()));
                output.accept(new ItemStack(ModBlocks.NETHERITE_FARM.get()));

/// Common Deepslate
                output.accept(new ItemStack(ModBlocks.DCOAL_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DCOPPER_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DGOLD_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DIRON_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DEEPSLATE_FARM.get()));

/// Uncommon Deepslate
                output.accept(new ItemStack(ModBlocks.DREDSTONE_FARM.get()));

/// Rare Deepslate
                output.accept(new ItemStack(ModBlocks.DDIAMOND_FARM.get()));
                output.accept(new ItemStack(ModBlocks.DLAPIS_FARM.get()));

/// Very Rare Deepslate
                output.accept(new ItemStack(ModBlocks.DEMERALD_FARM.get()));

/// Nether Farms
                output.accept(new ItemStack(ModBlocks.GLOWSTONE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.NETHER_GOLD_FARM.get()));
                output.accept(new ItemStack(ModBlocks.NETHER_QUARTZ_FARM.get()));
                output.accept(new ItemStack(ModBlocks.NETHERRACK_FARM.get()));
                output.accept(new ItemStack(ModBlocks.SSAND_FARM.get()));
                output.accept(new ItemStack(ModBlocks.SSOIL_FARM.get()));

/// End Farms
                output.accept(new ItemStack(ModBlocks.ESTONE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.PURPUR_FARM.get()));

/// Miscellaneous Farms
                output.accept(new ItemStack(ModBlocks.OBSIDIAN_FARM.get()));
                output.accept(new ItemStack(ModBlocks.ANDESITE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.GRANITE_FARM.get()));
                output.accept(new ItemStack(ModBlocks.RSAND_FARM.get()));
                output.accept(new ItemStack(ModBlocks.SNOW_FARM.get()));
                output.accept(new ItemStack(ModBlocks.TUFF_FARM.get()));

                output.accept(new ItemStack(ModItems.XP_UPGRADE.get()));
            })
            .title(Component.translatable("itemGroup.resource_farms"))
            .build());

    public static void init(IEventBus eventBus) {
        TAB_REGISTER.register(eventBus);
    }

}
