package com.awesomeshot5051.resourceFarm.blocks.tileentity.end.rock.common;

import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.*;
import com.awesomeshot5051.resourceFarm.datacomponents.*;
import com.awesomeshot5051.resourceFarm.gui.*;
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

public class EndStoneFarmTileentity extends VillagerTileentity implements ITickableBlockEntity {

    private final MultiItemStackHandler itemHandler;
    public ItemStack pickType;
    protected NonNullList<ItemStack> outputInventory;
    protected NonNullList<ItemStack> upgradeInventory;
    protected long timer;
    protected ItemStackHandler outputHandler;
    protected long breakStage;

    public EndStoneFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.ESTONE_FARM.get(), ModBlocks.ESTONE_FARM.get().defaultBlockState(), pos, state);
        outputInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        upgradeInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        outputHandler = new ItemStackHandler(outputInventory);
        itemHandler = new MultiItemStackHandler(upgradeInventory, outputInventory, UpgradeSlot::isValid);
        pickType = new ItemStack(Items.STONE_PICKAXE);
    }

    public static double getEndStoneGenerateTime(EndStoneFarmTileentity tileEntity) {
        return (double) Main.SERVER_CONFIG.coalGenerateTime.get() /
                (tileEntity.getPickType().getItem().equals(Items.IRON_PICKAXE) ? 15 :
                        tileEntity.getPickType().getItem().equals(Items.GOLDEN_PICKAXE) ? 20 :
                                tileEntity.getPickType().getItem().equals(Items.DIAMOND_PICKAXE) ? 25 :
                                        tileEntity.getPickType().getItem().equals(Items.NETHERITE_PICKAXE) ? 30 :
                                                1); // Default to Wooden PICKAXE divisor if none matches

    }

    public static double getEndStoneBreakTime(EndStoneFarmTileentity tileEntity) {

        return getEndStoneGenerateTime(tileEntity) + (tileEntity.getPickType().getItem().equals(Items.STONE_PICKAXE) ? (20 * 8) :
                tileEntity.getPickType().getItem().equals(Items.IRON_PICKAXE) ? (20 * 4) :
                        tileEntity.getPickType().getItem().equals(Items.DIAMOND_PICKAXE) ? (20 * 2) :
                                tileEntity.getPickType().getItem().equals(Items.NETHERITE_PICKAXE) ? (20 * 2) :
                                        tileEntity.getPickType().getItem().equals(Items.GOLDEN_PICKAXE) ? (20 * 2) :
                                                (20 * 10)); // Default to Wooden PICKAXE break time if none matches

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
        if (timer >= getEndStoneBreakTime(this)) {
            for (ItemStack drop : getDrops()) {
                for (int i = 0; i < outputHandler.getSlots(); i++) {
                    drop = outputHandler.insertItem(i, drop, false);
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
        List<ItemStack> drops = new ArrayList<>();
        if (!(level instanceof ServerLevel serverWorld)) {
            return Collections.emptyList();
        }
        if (this.hasUpgrades()) {
            if (this.getUpgrade().equalsIgnoreCase("xp_upgrade")) {
                drops.add(Items.EXPERIENCE_BOTTLE.getDefaultInstance());
            }
        }

        drops.add(new ItemStack(Items.END_STONE)); // Change this as needed for custom loot
        return drops;
    }

    public Container getOutputInventory() {
        return new ItemListInventory(outputInventory, this::setChanged);
    }

    public Container getUpgradeInventory() {
        return new ItemListInventory(upgradeInventory, this::setChanged);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
// Save the pickType as an NBT tag
//        if (pickType != null) {
//            CompoundTag pickTypeTag = new CompoundTag();
//            pickTypeTag.putString("id", pickType.getItem().builtInRegistryHolder().key().location().toString()); // Save the item ID
//            pickTypeTag.putInt("count", pickType.getCount()); // Save the count
//            compound.put("PickType", pickTypeTag); // Add the tag to the main compound
//        }
        compound.put("UpgradeInventory", ContainerHelper.saveAllItems(compound, upgradeInventory, true, provider));
        compound.put("OutputInventory", ContainerHelper.saveAllItems(compound, outputInventory, true, provider));
        compound.putLong("Timer", timer);

    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {

//        if (compound.contains("PickType")) {
//            SyncableTileentity.loadPickType(compound, provider).ifPresent(stack -> this.pickType = stack);
//        }
//        if (pickType == null) {
//// If no pickType is saved, set a default one (e.g., Stone Pickaxe)
//            pickType = new ItemStack(Items.STONE_PICKAXE);
//        }
        VillagerData.convertInventory(compound.getCompound("UpgradeInventory"), upgradeInventory, provider);
        VillagerData.convertInventory(compound.getCompound("OutputInventory"), outputInventory, provider);
        timer = compound.getLong("Timer");
        super.loadAdditional(compound, provider);
    }

    public boolean hasUpgrades() {
        return !getUpgradeInventory().isEmpty();
    }

    public String getUpgrade() {
        return getUpgradeInventory().getItem(0).toString();
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}
