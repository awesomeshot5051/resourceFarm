package com.awesomeshot5051.resourceFarm.data.providers.recipe.recipe;

import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.data.*;
import com.awesomeshot5051.resourceFarm.items.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class UpgradeRecipe extends ShapedRecipe {
    public static final DataComponentType<ItemContainerContents> pickTypeComponent = ModDataComponents.PICK_TYPE.get();

    public final ShapedRecipePattern pattern;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;
    final ItemStack result;
    List<Item> shovelFarms = new ArrayList<>(List.of(
            ModItems.CONCRETE_POWDER_FARM.get(),
            ModItems.DIRT_FARM.get(),
            ModItems.GRASS_FARM.get(),
            ModItems.GRAVEL_FARM.get(),
            ModItems.SAND_FARM.get(),
            ModItems.RSAND_FARM.get(),  // Red Sand Farm
            ModItems.SSAND_FARM.get(), // Soul Sand Farm
            ModItems.SSOIL_FARM.get(), // Soul Soil Farm
            ModItems.SNOW_FARM.get()
    ));

    private ItemContainerContents pickContents;
    private ItemStack result2;

    public UpgradeRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.result = result;
        this.result2 = result;
        this.showNotification = showNotification;
    }

    public UpgradeRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) {
        this(group, category, pattern, result, true);
    }

    public static List<Item> getPlanks() {

        // Retrieve all items in the "planks" tag
        return BuiltInRegistries.ITEM.getTag(ItemTags.PLANKS)
                .stream()
                .flatMap(holderSet -> holderSet.stream().map(Holder::value))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeSerializer<UpgradeRecipe> getSerializer() {
        return ModRecipes.UPGRADE_SERIALIZER.get();
    }

    public boolean matches(CraftingInput input, Level level) {
        return this.pattern.matches(input);
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Main.MODID, result.getDescriptionId());
    }

    private boolean areAllModifiersEqual(List<ItemStack> modifier) {
        return modifier.size() == 4
                && modifier.get(0).toString().equals(modifier.get(1).toString())
                && modifier.get(0).toString().equals(modifier.get(2).toString())
                && modifier.get(0).toString().equals(modifier.get(3).toString());
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider registries) {
        Map<Ingredient, Item> materialToPickaxeMap = Map.of(
                Ingredient.of(Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS, Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS, Items.MANGROVE_PLANKS, Items.BAMBOO_PLANKS, Items.CHERRY_PLANKS), Items.WOODEN_PICKAXE,
                Ingredient.of(Items.COBBLESTONE, Items.COBBLED_DEEPSLATE), Items.STONE_PICKAXE,
                Ingredient.of(Items.IRON_INGOT), Items.IRON_PICKAXE,
                Ingredient.of(Items.GOLD_INGOT), Items.GOLDEN_PICKAXE,
                Ingredient.of(Items.DIAMOND), Items.DIAMOND_PICKAXE,
                Ingredient.of(Items.NETHERITE_INGOT), Items.NETHERITE_PICKAXE
        );
        Map<Ingredient, Item> materialToShovelMap = Map.of(
                Ingredient.of(Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS, Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS, Items.MANGROVE_PLANKS, Items.BAMBOO_PLANKS, Items.CHERRY_PLANKS), Items.WOODEN_SHOVEL,
                Ingredient.of(Items.COBBLESTONE, Items.COBBLED_DEEPSLATE), Items.STONE_SHOVEL,
                Ingredient.of(Items.IRON_INGOT), Items.IRON_SHOVEL,
                Ingredient.of(Items.GOLD_INGOT), Items.GOLDEN_SHOVEL,
                Ingredient.of(Items.DIAMOND), Items.DIAMOND_SHOVEL,
                Ingredient.of(Items.NETHERITE_INGOT), Items.NETHERITE_SHOVEL
        );


        List<ItemStack> modifer = new ArrayList<>(List.of(craftingInput.getItem(1), craftingInput.getItem(3), craftingInput.getItem(5), craftingInput.getItem(7)));
        if (areAllModifiersEqual(modifer)) {
            List<ItemStack> itemStacks = new ArrayList<>();
            ItemStack pickStack;

            if (craftingInput.getItem(4).get(ModDataComponents.PICK_TYPE) != null) {
                pickStack = craftingInput.getItem(4).get(ModDataComponents.PICK_TYPE).copyOne();
            } else if (shovelFarms.contains(craftingInput.getItem(4).getItem())) {
                pickStack = new ItemStack(Items.STONE_SHOVEL);
            } else {
                pickStack = new ItemStack(Items.STONE_PICKAXE);
            }
            if (shovelFarms.contains(craftingInput.getItem(4).getItem())) {
                if (isHigherShovelType(pickStack, modifer.getFirst())) {
                    itemStacks.add(getResultItem(registries));
                    for (Map.Entry<Ingredient, Item> entry : materialToShovelMap.entrySet()) {
                        if (entry.getKey().test(modifer.getFirst())) {
                            // Convert the modifier into its corresponding pickaxe
                            pickStack = new ItemStack(entry.getValue());
                            break; // Break since we found the corresponding pickaxe
                        }
                    }
                    // Set the pick type in the result item's data
                    pickContents = ItemContainerContents.fromItems(Collections.singletonList(pickStack));
                    result2 = getResultItem(registries).copy(); // Copy the result item to avoid modifying the original
                }
            } else {
                if (isHigherPickType(pickStack, modifer.getFirst())) {
                    itemStacks.add(getResultItem(registries));
                    for (Map.Entry<Ingredient, Item> entry : materialToPickaxeMap.entrySet()) {
                        if (entry.getKey().test(modifer.getFirst())) {
                            // Convert the modifier into its corresponding pickaxe
                            pickStack = new ItemStack(entry.getValue());
                            break; // Break since we found the corresponding pickaxe
                        }
                    }
                    // Set the pick type in the result item's data
                    pickContents = ItemContainerContents.fromItems(Collections.singletonList(pickStack));
                    result2 = getResultItem(registries).copy(); // Copy the result item to avoid modifying the original
                }
            }
        }
        result2.set(pickTypeComponent, pickContents);
        super.assemble(craftingInput, registries);
        return result2;
    }

    private boolean isHigherShovelType(ItemStack baseShovelType, ItemStack modifierShovelType) {
        // Define ShovelType levels in ascending order of strength
        List<Item> shovelTypeHierarchy = new ArrayList<>(List.of(
                Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL,
                Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL
        ));

        // Map each shovel type to its corresponding material
        Map<Item, Ingredient> shovelToMaterialMap = Map.of(
                Items.WOODEN_SHOVEL, Ingredient.of(Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS,
                        Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS,
                        Items.MANGROVE_PLANKS, Items.BAMBOO_PLANKS, Items.CHERRY_PLANKS),
                Items.STONE_SHOVEL, Ingredient.of(Items.COBBLESTONE, Items.COBBLED_DEEPSLATE),
                Items.IRON_SHOVEL, Ingredient.of(Items.IRON_INGOT),
                Items.GOLDEN_SHOVEL, Ingredient.of(Items.GOLD_INGOT),
                Items.DIAMOND_SHOVEL, Ingredient.of(Items.DIAMOND),
                Items.NETHERITE_SHOVEL, Ingredient.of(Items.NETHERITE_INGOT)
        );

        // Convert baseShovelType and modifierShovelType to their corresponding materials
        Item baseShovelItem = baseShovelType.getItem();
        Item modifierShovelItem = modifierShovelType.getItem();

        Item baseMaterialType = null;
        Item modifierMaterialType = null;

        // Find the materials corresponding to the shovel items
        for (Map.Entry<Item, Ingredient> entry : shovelToMaterialMap.entrySet()) {
            if (entry.getKey().equals(baseShovelType.getItem())) {
                baseMaterialType = entry.getKey();
            }
            if (entry.getValue().test(modifierShovelType)) {
                modifierMaterialType = entry.getKey();
            }
        }

        // Ensure both types were mapped to a valid shovel
        if (baseMaterialType == null || modifierMaterialType == null) {
            return false; // Invalid types, cannot compare
        }

        // Compare indices in the hierarchy
        int baseIndex = shovelTypeHierarchy.indexOf(baseMaterialType);
        int modifierIndex = shovelTypeHierarchy.indexOf(modifierMaterialType);

        // Return true if the modifier type is higher in the hierarchy
        return modifierIndex > baseIndex;
    }

    private boolean isHigherPickType(ItemStack basePickType, ItemStack modifierPickType) {
        // Define PickType levels in ascending order of strength
        List<Item> pickTypeHierarchy = new ArrayList<>(List.of(
                Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE,
                Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE
        ));

        // Map each pickaxe type to its corresponding material
        Map<Item, Ingredient> pickaxeToMaterialMap = Map.of(
                Items.WOODEN_PICKAXE, Ingredient.of(Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS,
                        Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS,
                        Items.MANGROVE_PLANKS, Items.BAMBOO_PLANKS, Items.CHERRY_PLANKS),
                Items.STONE_PICKAXE, Ingredient.of(Items.COBBLESTONE, Items.COBBLED_DEEPSLATE),
                Items.IRON_PICKAXE, Ingredient.of(Items.IRON_INGOT),
                Items.GOLDEN_PICKAXE, Ingredient.of(Items.GOLD_INGOT),
                Items.DIAMOND_PICKAXE, Ingredient.of(Items.DIAMOND),
                Items.NETHERITE_PICKAXE, Ingredient.of(Items.NETHERITE_INGOT)
        );

        // Convert basePickType and modifierPickType to their corresponding materials
        Item basePickItem = basePickType.getItem();
        Item modifierPickItem = modifierPickType.getItem();

        Item baseMaterialType = null;
        Item modifierMaterialType = null;

        // Find the materials corresponding to the pickaxe items
        for (Map.Entry<Item, Ingredient> entry : pickaxeToMaterialMap.entrySet()) {
            if (entry.getKey().equals(basePickType.getItem())) {
                baseMaterialType = entry.getKey();
            }
            if (entry.getValue().test(modifierPickType)) {
                modifierMaterialType = entry.getKey();
            }
        }

        // Ensure both types were mapped to a valid pickaxe
        if (baseMaterialType == null || modifierMaterialType == null) {
            return false; // Invalid types, cannot compare
        }

        // Compare indices in the hierarchy
        int baseIndex = pickTypeHierarchy.indexOf(baseMaterialType);
        int modifierIndex = pickTypeHierarchy.indexOf(modifierMaterialType);

        // Return true if the modifier type is higher in the hierarchy
        return modifierIndex > baseIndex;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.pattern.width() && height >= this.pattern.height();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.result;
    }


    public ItemStack getResult() {
        return result;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return super.getType();
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

//    @Override
//    public boolean isSpecial() {
//        return true;
//    }


    public static class Serializer implements RecipeSerializer<UpgradeRecipe> {
        public static final MapCodec<UpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec((p_340778_) -> p_340778_.group(Codec.STRING.optionalFieldOf("group", "").forGetter((p_311729_) -> p_311729_.group), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((p_311732_) -> p_311732_.category), ShapedRecipePattern.MAP_CODEC.forGetter((p_311733_) -> p_311733_.pattern), ItemStack.STRICT_CODEC.fieldOf("result").forGetter((p_311730_) -> p_311730_.result), Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((p_311731_) -> p_311731_.showNotification)).apply(p_340778_, UpgradeRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeRecipe> STREAM_CODEC = StreamCodec.of(UpgradeRecipe.Serializer::toNetwork, UpgradeRecipe.Serializer::fromNetwork);

        public Serializer() {
        }

        private static UpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern UpgradeRecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            boolean flag = buffer.readBoolean();
            return new UpgradeRecipe(s, craftingbookcategory, UpgradeRecipepattern, itemstack, flag);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, UpgradeRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.showNotification);
        }

        public MapCodec<UpgradeRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, UpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }


}