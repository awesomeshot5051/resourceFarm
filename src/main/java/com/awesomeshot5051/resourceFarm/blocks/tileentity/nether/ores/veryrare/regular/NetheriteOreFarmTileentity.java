package com.awesomeshot5051.resourceFarm.blocks.tileentity.nether.ores.veryrare.regular;

import com.awesomeshot5051.corelib.blockentity.*;
import com.awesomeshot5051.corelib.inventory.*;
import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.*;
import com.awesomeshot5051.resourceFarm.datacomponents.*;
import com.awesomeshot5051.resourceFarm.enums.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.items.*;

import java.util.*;

import static com.awesomeshot5051.resourceFarm.datacomponents.PickaxeEnchantments.*;

@SuppressWarnings("ALL")
public class NetheriteOreFarmTileentity extends FarmTileentity implements ITickableBlockEntity {

    public ItemStack pickType;
    public Map<ResourceKey<Enchantment>, Boolean> pickaxeEnchantments = initializePickaxeEnchantments();
    public ItemStack pickaxeType;
    public boolean upgradeEnabled;
    public CustomData customData = CustomData.EMPTY;
    protected NonNullList<ItemStack> inventory;
    protected long timer;
    protected ItemStackHandler itemHandler;
    protected OutputItemHandler outputItemHandler;
    private boolean soundOn = true;

    public NetheriteOreFarmTileentity(BlockPos pos, BlockState state) {
        super(ModTileEntities.NETHERITE_FARM.get(), ModBlocks.NETHERITE_FARM.get().defaultBlockState(), pos, state);
        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        itemHandler = new ItemStackHandler(inventory);
        outputItemHandler = new OutputItemHandler(inventory);
        pickType = new ItemStack(Items.DIAMOND_PICKAXE);
    }

    public static double getNetheriteGenerateTime(NetheriteOreFarmTileentity tileEntity) {
        return (double) Main.SERVER_CONFIG.coalGenerateTime.get() / (
                tileEntity.getPickType().getItem().equals(Items.DIAMOND_PICKAXE) ? 5 :
                        tileEntity.getPickType().getItem().equals(Items.NETHERITE_PICKAXE) ? 10 :
                                1);

    }

    public static double getNetheriteBreakTime(NetheriteOreFarmTileentity tileEntity) {


        PickaxeType pickAxe = PickaxeType.fromItem(tileEntity.getPickType().getItem());
        if (tileEntity.getPickType().isEnchanted()) {
            tileEntity.setPickaxeEnchantmentStatus(tileEntity);
        }
        int baseValue = 20;
        if (PickaxeEnchantments.getPickaxeEnchantmentStatus(tileEntity.pickaxeEnchantments, Enchantments.EFFICIENCY)) {
            baseValue = 10;
        }
        return getNetheriteGenerateTime(tileEntity) + (pickAxe.equals(PickaxeType.NETHERITE) ? (baseValue * 3.2) :
                pickAxe.equals(PickaxeType.DIAMOND) ? (baseValue * 4.8) :
                        6.4);

    }

    public long getTimer() {
        return timer;
    }

    @Override
    public boolean toggleSound() {
        return !soundOn;
    }

    @Override
    public boolean getSound() {
        return soundOn;
    }


    public ItemStack getPickType() {
        return pickType;
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= getNetheriteBreakTime(this)) {
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
        int dropCount = serverWorld.random.nextIntBetweenInclusive(0, 1);
        if (getPickaxeEnchantmentStatus(pickaxeEnchantments, Enchantments.FORTUNE)) {
            dropCount = serverWorld.random.nextIntBetweenInclusive(1, 5);
        }
        drops.add(new ItemStack(Items.ANCIENT_DEBRIS, dropCount));
        if (upgradeEnabled) {
            drops.clear();
            drops.add(new ItemStack(Items.NETHERITE_SCRAP, dropCount));
        }
        return drops;
    }

    public Container getOutputInventory() {
        return new ItemListInventory(inventory, this::setChanged);
    }

    @Override
    public CustomData getCustomData() {
        return customData;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {

        ContainerHelper.saveAllItems(compound, inventory, false, provider);

        if (pickType != null) {
            CompoundTag pickTypeTag = new CompoundTag();
            pickTypeTag.putString("id", BuiltInRegistries.ITEM.getKey(pickType.getItem()).toString());
            pickTypeTag.putInt("count", pickType.getCount());
            compound.put("PickType", pickTypeTag);
        }
        if (!pickaxeEnchantments.isEmpty()) {
            ListTag enchantmentsList = new ListTag();
            for (Map.Entry<ResourceKey<Enchantment>, Boolean> entry : pickaxeEnchantments.entrySet()) {
                if (entry.getValue()) {
                    CompoundTag enchantmentTag = new CompoundTag();
                    enchantmentTag.putString("id", entry.getKey().location().toString());
                    enchantmentsList.add(enchantmentTag);
                }
            }
            compound.put("PickaxeEnchantments", enchantmentsList);
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
        if (compound.contains("PickaxeEnchantments")) {
            pickaxeEnchantments = SyncableTileentity.loadPickaxeEnchantments(compound, provider, this);
        }
        if (compound.contains("upgrade")) {
            upgradeEnabled = true;
        }
        if (pickType == null) {

            pickType = new ItemStack(Items.WOODEN_PICKAXE);
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
