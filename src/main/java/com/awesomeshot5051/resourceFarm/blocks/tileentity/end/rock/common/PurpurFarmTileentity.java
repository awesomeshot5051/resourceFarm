package com.awesomeshot5051.resourceFarm.blocks.tileentity.end.rock.common;

import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.*;
import com.awesomeshot5051.resourceFarm.datacomponents.*;
import com.awesomeshot5051.resourceFarm.enums.*;
import de.maxhenkel.corelib.blockentity.*;
import de.maxhenkel.corelib.inventory.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.items.*;

import java.util.*;

import static com.awesomeshot5051.resourceFarm.datacomponents.PickaxeEnchantments.*;

@SuppressWarnings("ALL")
public class PurpurFarmTileentity extends VillagerTileentity implements ITickableBlockEntity {

    public ItemStack pickType;
    public Map<ResourceKey<Enchantment>, Boolean> pickaxeEnchantments = initializePickaxeEnchantments();
    public ItemStack pickaxeType;
    protected NonNullList<ItemStack> inventory;
    protected long timer;
    protected ItemStackHandler itemHandler;
    protected OutputItemHandler outputItemHandler;

    public PurpurFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.PURPUR_FARM.get(), ModBlocks.PURPUR_FARM.get().defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new ItemStackHandler(inventory);
        outputItemHandler = new OutputItemHandler(inventory);
        pickType = new ItemStack(Items.STONE_PICKAXE);
    }

    public static double getPurpurGenerateTime(PurpurFarmTileentity tileEntity) {
        return (double) Main.SERVER_CONFIG.coalGenerateTime.get() /
                (tileEntity.getPickType().getItem().equals(Items.IRON_PICKAXE) ? 15 :
                        tileEntity.getPickType().getItem().equals(Items.GOLDEN_PICKAXE) ? 20 :
                                tileEntity.getPickType().getItem().equals(Items.DIAMOND_PICKAXE) ? 25 :
                                        tileEntity.getPickType().getItem().equals(Items.NETHERITE_PICKAXE) ? 30 :
                                                1); // Default to Wooden PICKAXE divisor if none matches

    }

    public static double getPurpurBreakTime(PurpurFarmTileentity farm) {

        PickaxeType pickAxe = PickaxeType.fromItem(farm.getPickType().getItem());
        if (farm.getPickType().isEnchanted()) {
            farm.setPickaxeEnchantmentStatus(farm);
        }
        int baseValue = 20;
        if (PickaxeEnchantments.getPickaxeEnchantmentStatus(farm.pickaxeEnchantments, Enchantments.EFFICIENCY)) {
            baseValue = 10;
        }
        return getPurpurGenerateTime(farm) + (pickAxe.equals(PickaxeType.NETHERITE) ? (baseValue * 3.2) :
                pickAxe.equals(PickaxeType.DIAMOND) ? (baseValue * 5.6) :
                        pickAxe.equals(PickaxeType.IRON) ? (baseValue * 4.8) :
                                pickAxe.equals(PickaxeType.STONE) ? (baseValue * 6.4) :
                                        pickAxe.equals(PickaxeType.WOODEN) ? (baseValue * 6.4) :
                                                6.4);

    }

    public long getTimer() {
        return timer;
    }


    public ItemStack getPickType() {
        return pickType;
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= getPurpurBreakTime(this)) {
            for (ItemStack drop : getDrops()) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    drop = itemHandler.insertItem(i, drop, false);
                    if (drop.isEmpty()) {
                        break;
                    }
                }
            }
            timer = 0L;
            sync();
        }
        setChanged();
    }

    private List<ItemStack> getDrops() {
        if (!(level instanceof ServerLevel serverWorld)) {
            return Collections.emptyList();
        }
        int dropCount = serverWorld.random.nextIntBetweenInclusive(1, 3);
        if (getPickaxeEnchantmentStatus(pickaxeEnchantments, Enchantments.FORTUNE)) {
            dropCount = serverWorld.random.nextIntBetweenInclusive(1, 5);
        }
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Items.PURPUR_BLOCK, dropCount)); // Change this as needed for custom loot
        return drops;
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {

        ContainerHelper.saveAllItems(compound, inventory, false, provider);
// Save the shovelType as an NBT tag
        if (pickType != null) {
            CompoundTag pickTypeTag = new CompoundTag();
            pickTypeTag.putString("id", pickType.getItem().builtInRegistryHolder().key().location().toString()); // Save the item ID
            pickTypeTag.putInt("count", pickType.getCount()); // Save the count
            compound.put("PickType", pickTypeTag); // Add the tag to the main compound
        }
        compound.putLong("Timer", timer);
        super.saveAdditional(compound, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        ContainerHelper.loadAllItems(compound, inventory, provider);
        if (compound.contains("PickType")) {
            SyncableTileentity.loadPickType(compound, provider).ifPresent(stack -> this.pickType = stack);

        }
        if (pickType == null) {
// If no shovelType is saved, set a default one (e.g., Stone Pickaxe)
            pickType = new ItemStack(Items.STONE_PICKAXE);
        }

        timer = compound.getLong("Timer");
        super.loadAdditional(compound, provider);
    }

    public IItemHandler getItemHandler() {
        return outputItemHandler;
    }

    protected Map<ResourceKey<Enchantment>, Boolean> getPickaxeEnchantments() {
        return pickaxeEnchantments;
    }
}
