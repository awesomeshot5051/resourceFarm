package com.awesomeshot5051.resourceFarm.blocks.tileentity.overworld.soil;

import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.*;
import de.maxhenkel.corelib.blockentity.*;
import de.maxhenkel.corelib.inventory.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.items.*;

import java.util.*;

public class SnowFarmTileentity extends VillagerTileentity implements ITickableBlockEntity {

    public ItemStack pickType;
    protected NonNullList<ItemStack> inventory;
    protected long timer;
    protected ItemStackHandler itemHandler;
    protected long breakStage;
    protected OutputItemHandler outputItemHandler;

    public SnowFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.SNOW_FARM.get(), ModBlocks.SNOW_FARM.get().defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new ItemStackHandler(inventory);
        outputItemHandler = new OutputItemHandler(inventory);
        pickType = new ItemStack(Items.STONE_SHOVEL);
    }

    public static double getSnowGenerateTime(SnowFarmTileentity tileEntity) {
        return (double) Main.SERVER_CONFIG.coalGenerateTime.get() /
                (tileEntity.getPickType().getItem().equals(Items.IRON_SHOVEL) ? 15 :
                        tileEntity.getPickType().getItem().equals(Items.GOLDEN_SHOVEL) ? 20 :
                                tileEntity.getPickType().getItem().equals(Items.DIAMOND_SHOVEL) ? 25 :
                                        tileEntity.getPickType().getItem().equals(Items.NETHERITE_SHOVEL) ? 30 :
                                                1); // Default to Wooden SHOVEL divisor if none matches
    }

    public static double getSnowBreakTime(SnowFarmTileentity tileEntity) {
        return getSnowGenerateTime(tileEntity) + (tileEntity.getPickType().getItem().equals(Items.STONE_SHOVEL) ? (20 * 8) :
                tileEntity.getPickType().getItem().equals(Items.IRON_SHOVEL) ? (20 * 4) :
                        tileEntity.getPickType().getItem().equals(Items.DIAMOND_SHOVEL) ? (20 * 2) :
                                tileEntity.getPickType().getItem().equals(Items.NETHERITE_SHOVEL) ? (20 * 2) :
                                        tileEntity.getPickType().getItem().equals(Items.GOLDEN_SHOVEL) ? (20 * 2) :
                                                (20 * 10)); // Default to Wooden SHOVEL break time if none matches
    }

    public long getTimer() {
        return timer;
    }

    public long getBreakStage() {
        return breakStage;
    }

    public ItemStack getPickType() {
        return pickType;
    }

    @Override
    public void tick() {
        timer++;

        if (timer >= getSnowBreakTime(this)) {
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
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Items.SNOW)); // Change this as needed for custom loot
        return drops;
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {

        ContainerHelper.saveAllItems(compound, inventory, false, provider);
        // Save the pickType as an NBT tag
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
            // If no pickType is saved, set a default one (e.g., Stone Shovel)
            pickType = new ItemStack(Items.STONE_SHOVEL);
        }

        timer = compound.getLong("Timer");
        super.loadAdditional(compound, provider);
    }

    public IItemHandler getItemHandler() {
        return outputItemHandler;
    }
}

