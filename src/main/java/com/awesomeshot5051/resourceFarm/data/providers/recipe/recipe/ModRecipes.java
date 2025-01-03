package com.awesomeshot5051.resourceFarm.data.providers.recipe.recipe;

import com.awesomeshot5051.resourceFarm.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;


public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, "resource_farms");
    public static final Supplier<RecipeSerializer<CustomBlockRecipe>> SHAPED_SERIALIZER =
            RECIPE_SERIALIZERS.register("shaped_serializer", CustomBlockRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<CustomShapelessBlockRecipe>> SHAPELESS_SERIALIZER =
            RECIPE_SERIALIZERS.register("shapeless_serializer", CustomShapelessBlockRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<UpgradeRecipe>> UPGRADE_SERIALIZER =
            RECIPE_SERIALIZERS.register("upgrade_serializer", UpgradeRecipe.Serializer::new);
    // Register the custom recipe serializer
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Main.MODID);
    public static final Supplier<RecipeType<CustomBlockRecipe>> FARM_RECIPE =
            RECIPE_TYPES.register(
                    "farm_recipe",
                    // We need the qualifying generic here due to generics being generics.
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Main.MODID, "farm_recipe"))
            );
    public static final Supplier<RecipeType<CustomShapelessBlockRecipe>> SHAPELESS_FARM_RECIPE =
            RECIPE_TYPES.register(
                    "shapeless_farm_recipe",
                    // We need the qualifying generic here due to generics being generics.
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Main.MODID, "shapeless_farm_recipe"))
            );
    public static final Supplier<RecipeType<UpgradeRecipe>> UPGRADE_RECIPE =
            RECIPE_TYPES.register(
                    "upgrade_recipe",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Main.MODID, "upgrade_recipe")));

    public static void registerRecipes(IEventBus eventBus) {
        // Example of registering your custom recipe using a custom recipe serializer
        RECIPE_SERIALIZERS.register(eventBus);
    }

    public static void registerTypes(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
