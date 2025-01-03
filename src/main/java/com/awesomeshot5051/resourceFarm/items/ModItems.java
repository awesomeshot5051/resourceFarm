package com.awesomeshot5051.resourceFarm.items;

import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.datacomponents.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class ModItems {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Main.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MODID);
    public static final DeferredItem<Item> XP_UPGRADE = ITEMS.register("xp_upgrade_card",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FORTUNE_UPGRADE = ITEMS.register("fortune_upgrade",
            () -> new Item(new Item.Properties()));
    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, Main.MODID);
    //    public static final DeferredHolder<Item, Item> INCUBATOR = ITEM_REGISTER.register("incubator", () -> ModBlocks.INCUBATOR.get().toItem());
    public static final DeferredHolder<Item, Item> INVENTORY_VIEWER = ITEM_REGISTER.register("inventory_viewer", () -> ModBlocks.INVENTORY_VIEWER.get().toItem());
    public static final DeferredHolder<Item, Item> ANDESITE_FARM = ITEM_REGISTER.register("andesite_farm", () -> ModBlocks.ANDESITE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> COPPER_FARM = ITEM_REGISTER.register("copper_farm", () -> ModBlocks.COPPER_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> COAL_FARM = ITEM_REGISTER.register("coal_farm", () -> ModBlocks.COAL_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DCOPPER_FARM = ITEM_REGISTER.register("dcopper_farm", () -> ModBlocks.DCOPPER_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DCOAL_FARM = ITEM_REGISTER.register("dcoal_farm", () -> ModBlocks.DCOAL_FARM.get().toItem());
    // Regular ores
    public static final DeferredHolder<Item, Item> IRON_FARM = ITEM_REGISTER.register("iron_farm", () -> ModBlocks.IRON_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> GOLD_FARM = ITEM_REGISTER.register("gold_farm", () -> ModBlocks.GOLD_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DIAMOND_FARM = ITEM_REGISTER.register("diamond_farm", () -> ModBlocks.DIAMOND_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> EMERALD_FARM = ITEM_REGISTER.register("emerald_farm", () -> ModBlocks.EMERALD_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> LAPIS_FARM = ITEM_REGISTER.register("lapis_farm", () -> ModBlocks.LAPIS_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> REDSTONE_FARM = ITEM_REGISTER.register("redstone_farm", () -> ModBlocks.REDSTONE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> GRANITE_FARM = ITEM_REGISTER.register("granite_farm", () -> ModBlocks.GRANITE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> SAND_FARM = ITEM_REGISTER.register("sand_farm", () -> ModBlocks.SAND_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> CONCRETE_POWDER_FARM = ITEM_REGISTER.register("cpowder_farm", () -> ModBlocks.CONCRETE_POWDER_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> CONCRETE_FARM = ITEM_REGISTER.register("concrete_farm", () -> ModBlocks.CONCRETE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> COBBLESTONE_FARM = ITEM_REGISTER.register("cobblestone_farm", () -> ModBlocks.COBBLESTONE_FARM.get().toItem());
    // Deepslate variants
    public static final DeferredHolder<Item, Item> DIRON_FARM = ITEM_REGISTER.register("diron_farm", () -> ModBlocks.DIRON_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DGOLD_FARM = ITEM_REGISTER.register("dgold_farm", () -> ModBlocks.DGOLD_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DDIAMOND_FARM = ITEM_REGISTER.register("ddiamond_farm", () -> ModBlocks.DDIAMOND_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DEMERALD_FARM = ITEM_REGISTER.register("demerald_farm", () -> ModBlocks.DEMERALD_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DLAPIS_FARM = ITEM_REGISTER.register("dlapis_farm", () -> ModBlocks.DLAPIS_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DREDSTONE_FARM = ITEM_REGISTER.register("dredstone_farm", () -> ModBlocks.DREDSTONE_FARM.get().toItem());
    // Nether variants
    public static final DeferredHolder<Item, Item> NETHER_QUARTZ_FARM = ITEM_REGISTER.register("nether_quartz_farm", () -> ModBlocks.NETHER_QUARTZ_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> NETHER_GOLD_FARM = ITEM_REGISTER.register("nether_gold_farm", () -> ModBlocks.NETHER_GOLD_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> NETHERITE_FARM = ITEM_REGISTER.register("netherite_farm", () -> ModBlocks.NETHERITE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> ICON_ITEM = ITEM_REGISTER.register("icon_item",
            () -> new IconItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BASALT_FARM = ITEM_REGISTER.register("basalt_farm", () -> ModBlocks.BASALT_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> BLACKSTONE_FARM = ITEM_REGISTER.register("blackstone_farm", () -> ModBlocks.BLACKSTONE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> CALCITE_FARM = ITEM_REGISTER.register("calcite_farm", () -> ModBlocks.CALCITE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DIRT_FARM = ITEM_REGISTER.register("dirt_farm", () -> ModBlocks.DIRT_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> DEEPSLATE_FARM = ITEM_REGISTER.register("deepslate_farm", () -> ModBlocks.DEEPSLATE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> ESTONE_FARM = ITEM_REGISTER.register("estone_farm", () -> ModBlocks.ESTONE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> GLOWSTONE_FARM = ITEM_REGISTER.register("glowstone_farm", () -> ModBlocks.GLOWSTONE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> GRASS_FARM = ITEM_REGISTER.register("grass_farm", () -> ModBlocks.GRASS_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> GRAVEL_FARM = ITEM_REGISTER.register("gravel_farm", () -> ModBlocks.GRAVEL_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> NETHERRACK_FARM = ITEM_REGISTER.register("netherrack_farm", () -> ModBlocks.NETHERRACK_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> OBSIDIAN_FARM = ITEM_REGISTER.register("obsidian_farm", () -> ModBlocks.OBSIDIAN_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> PURPUR_FARM = ITEM_REGISTER.register("purpur_farm", () -> ModBlocks.PURPUR_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> RSAND_FARM = ITEM_REGISTER.register("rsand_farm", () -> ModBlocks.RSAND_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> SNOW_FARM = ITEM_REGISTER.register("snow_farm", () -> ModBlocks.SNOW_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> STONE_FARM = ITEM_REGISTER.register("stone_farm", () -> ModBlocks.STONE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> SSTONE_FARM = ITEM_REGISTER.register("sstone_farm", () -> ModBlocks.SSTONE_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> SSOIL_FARM = ITEM_REGISTER.register("ssoil_farm", () -> ModBlocks.SSOIL_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> SSAND_FARM = ITEM_REGISTER.register("ssand_farm", () -> ModBlocks.SSAND_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> TERRACOTTA_FARM = ITEM_REGISTER.register("terracotta_farm", () -> ModBlocks.TERRACOTTA_FARM.get().toItem());
    public static final DeferredHolder<Item, Item> TUFF_FARM = ITEM_REGISTER.register("tuff_farm", () -> ModBlocks.TUFF_FARM.get().toItem());
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Main.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<VillagerData>> VILLAGER_DATA_COMPONENT = DATA_COMPONENT_TYPE_REGISTER.register("villager", () -> DataComponentType.<VillagerData>builder().persistent(VillagerData.CODEC).networkSynchronized(VillagerData.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PickTypeData>> PICK_TYPE = DATA_COMPONENT_TYPE_REGISTER.register("pick_type", () -> DataComponentType.<PickTypeData>builder().persistent(PickTypeData.CODEC).networkSynchronized(PickTypeData.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<VillagerBlockEntityData>> BLOCK_ENTITY_DATA_COMPONENT = DATA_COMPONENT_TYPE_REGISTER.register("block_entity", () -> DataComponentType.<VillagerBlockEntityData>builder().networkSynchronized(VillagerBlockEntityData.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FarmBlockEntityData>> FARM_ENTITY_DATA_COMPONENT = DATA_COMPONENT_TYPE_REGISTER.register("farm_entity", () -> DataComponentType.<FarmBlockEntityData>builder().networkSynchronized(FarmBlockEntityData.STREAM_CODEC).build());

    public static void init(IEventBus eventBus) {
        ITEM_REGISTER.register(eventBus);
        DATA_COMPONENT_TYPE_REGISTER.register(eventBus);
        REGISTRY.register(eventBus);
        ITEMS.register(eventBus);
    }

}
