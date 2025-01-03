package com.awesomeshot5051.resourceFarm.blocks.tileentity.overworld.ores.common.regular;

import com.awesomeshot5051.resourceFarm.Main;
import com.awesomeshot5051.resourceFarm.OutputItemHandler;
import com.awesomeshot5051.resourceFarm.blocks.ModBlocks;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.ModTileEntities;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.VillagerTileentity;
import de.maxhenkel.corelib.blockentity.ITickableBlockEntity;
import de.maxhenkel.corelib.inventory.ItemListInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoalOreFarmTileentity extends VillagerTileentity implements ITickableBlockEntity {
    public ItemStack pickType;
    protected NonNullList<ItemStack> inventory;
    protected long timer;
    protected ItemStackHandler itemHandler;
    protected OutputItemHandler outputItemHandler;

    public CoalOreFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.COAL_FARM.get(), ModBlocks.COAL_FARM.get().defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new ItemStackHandler(inventory);
        outputItemHandler = new OutputItemHandler(inventory);
        pickType = new ItemStack(Items.STONE_PICKAXE);
    }

    public static double getCoalGenerateTime(CoalOreFarmTileentity tileEntity) {
        return (double) Main.SERVER_CONFIG.coalGenerateTime.get() /
                (tileEntity.getPickType().getItem().equals(Items.WOODEN_PICKAXE) ? 1 :
                        tileEntity.getPickType().getItem().equals(Items.STONE_PICKAXE) ? 10 :
                                tileEntity.getPickType().getItem().equals(Items.IRON_PICKAXE) ? 15 :
                                        tileEntity.getPickType().getItem().equals(Items.GOLDEN_PICKAXE) ? 20 :
                                                tileEntity.getPickType().getItem().equals(Items.DIAMOND_PICKAXE) ? 25 :
                                                        tileEntity.getPickType().getItem().equals(Items.NETHERITE_PICKAXE) ? 30 :
                                                                1); // Default to Wooden PICKAXE divisor if none matches

    }

    public static double getCoalBreakTime(CoalOreFarmTileentity tileEntity) {

        return getCoalGenerateTime(tileEntity) + (tileEntity.getPickType().getItem().equals(Items.WOODEN_PICKAXE) ? (20 * 10) :
                tileEntity.getPickType().getItem().equals(Items.STONE_PICKAXE) ? (20 * 8) :
                        tileEntity.getPickType().getItem().equals(Items.IRON_PICKAXE) ? (20 * 4) :
                                tileEntity.getPickType().getItem().equals(Items.DIAMOND_PICKAXE) ? (20 * 2) :
                                        tileEntity.getPickType().getItem().equals(Items.NETHERITE_PICKAXE) ? (20 * 2) :
                                                tileEntity.getPickType().getItem().equals(Items.GOLDEN_PICKAXE) ? (20 * 5) :
                                                        (20 * 10)); // Default to Wooden PICKAXE break time if none matches

    }

    //private static final ResourceKey<LootTable> COAL_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("entities/coal"));
    public ItemStack getPickType() {
        return pickType;
    }

    public long getTimer() {
        return timer;
    }

    @Override
    public void tick() {
        // Increment the main timer
        timer++;

        // Handle reset and item drops
        if (timer >= getCoalBreakTime(this)) {
            for (ItemStack drop : getDrops()) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    drop = itemHandler.insertItem(i, drop, false);
                    if (drop.isEmpty()) {
                        break;
                    }
                }
            }

            timer = 0L; // Reset the timer
            sync(); // Sync to the client
        }

        setChanged(); // Mark the tile entity as dirty

    }

    private List<ItemStack> getDrops() {
        if (!(level instanceof ServerLevel serverWorld)) {
            return Collections.emptyList();
        }
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Items.COAL));


        return drops;
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);

        ContainerHelper.saveAllItems(compound, inventory, false, provider);
        compound.putLong("Timer", timer);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        ContainerHelper.loadAllItems(compound, inventory, provider);
        timer = compound.getLong("Timer");
        super.loadAdditional(compound, provider);
    }

    public IItemHandler getItemHandler() {
        return outputItemHandler;
    }

}
