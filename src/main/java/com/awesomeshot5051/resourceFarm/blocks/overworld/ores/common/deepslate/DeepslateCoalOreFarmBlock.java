package com.awesomeshot5051.resourceFarm.blocks.overworld.ores.common.deepslate;


import com.awesomeshot5051.corelib.block.*;
import com.awesomeshot5051.corelib.blockentity.*;
import com.awesomeshot5051.corelib.client.*;
import com.awesomeshot5051.resourceFarm.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.overworld.ores.common.deepslate.*;
import com.awesomeshot5051.resourceFarm.datacomponents.*;
import com.awesomeshot5051.resourceFarm.enums.*;
import com.awesomeshot5051.resourceFarm.gui.*;
import com.awesomeshot5051.resourceFarm.items.render.overworld.ores.common.deepslate.*;
import com.awesomeshot5051.resourceFarm.sounds.*;
import net.minecraft.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.*;

import static net.minecraft.world.item.BlockItem.*;

public class DeepslateCoalOreFarmBlock extends BlockBase implements EntityBlock, IItemBlock {

    public static final EnumProperty<PickaxeType> PICKAXE_TYPE = EnumProperty.create("pickaxe_type", PickaxeType.class);

    public DeepslateCoalOreFarmBlock() {
        super(Properties.of().mapColor(MapColor.STONE).strength(2.5F).sound(SoundType.STONE).noOcclusion());

    }

    private void playSound(@NotNull Level level, BlockState state, SoundEvent sound, DeepslateCoalOreFarmTileentity farm) {
        Vec3i vec3i = state.getValue(FACING).getNormal();
        double d0 = farm.getBlockPos().getX() + 0.5D + (double) vec3i.getX() / 2.0D;
        double d1 = farm.getBlockPos().getY() + 0.5D + (double) vec3i.getY() / 2.0D;
        double d2 = farm.getBlockPos().getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
        level.playSound(null, d0, d1, d2, sound, SoundSource.BLOCKS, 5F, 10 * 0.1F + 0.9F);
    }

    @Override
    public Item toItem() {
        return new CustomRendererBlockItem(this, new Item.Properties()) {
            @OnlyIn(Dist.CLIENT)
            @Override
            public ItemRenderer createItemRenderer() {
                return new DeepslateCoalOreFarmItemRenderer();
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            ItemContainerContents defaultType = ItemContainerContents.fromItems(Collections.singletonList(new ItemStack(Items.WOODEN_PICKAXE)));
            ItemStack pickType = ItemContainerContents.fromItems(Collections.singletonList(Objects.requireNonNull(stack.getOrDefault(ModDataComponents.PICK_TYPE, defaultType)).copyOne())).copyOne();
            components.add(Component.literal("This farm has a " + convertToReadableName(pickType.getItem().getDefaultInstance().getDescriptionId()) + " on it.")
                    .withStyle(ChatFormatting.RED));
            if (stack.has(DataComponents.CUSTOM_DATA)) {
                components.add(Component.literal(
                        Arrays.stream(stack.get(DataComponents.CUSTOM_DATA)
                                        .toString()
                                        .replace("{}", " ")
                                        .replace("{Upgrade:\"", "")
                                        .replace("\"}", "")
                                        .split("_"))
                                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                                .collect(Collectors.joining(" "))
                ));
            }
        } else {
            components.add(Component.literal("Hold §4Shift§r to see tool").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, context, components, tooltipFlag);
        DeepslateCoalOreFarmTileentity trader = BlockEntityData.getAndStoreBlockEntity(stack, context.registries(), context.level(), () -> new DeepslateCoalOreFarmTileentity(BlockPos.ZERO, ModBlocks.DCOAL_FARM.get().defaultBlockState()));


    }

    private String convertToReadableName(String block) {

        String readableName = block.replace("item.minecraft.", "").replace('_', ' ');

        return Arrays.stream(readableName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);


        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DeepslateCoalOreFarmTileentity farmTileEntity) {

            farmTileEntity.upgradeEnabled = stack.has(DataComponents.CUSTOM_DATA);
            farmTileEntity.customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            ItemContainerContents pickType = stack.get(ModDataComponents.PICK_TYPE);
            if (pickType != null) {
                farmTileEntity.pickType = pickType.getStackInSlot(0);


                farmTileEntity.setChanged();
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putString("id", BuiltInRegistries.ITEM.getKey(farmTileEntity.pickType.getItem()).toString());
                compoundTag.putInt("count", farmTileEntity.pickType.getCount());
                setBlockEntityData(stack, blockEntity.getType(), compoundTag);
                updateCustomBlockEntityTag(level, placer instanceof Player ? (Player) placer : null, pos, pickType.getStackInSlot(0));
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (!(tileEntity instanceof DeepslateCoalOreFarmTileentity farm)) {
            return super.useItemOn(heldItem, state, worldIn, pos, player, handIn, hit);
        }
        playSound(Objects.requireNonNull(worldIn), state, ModSounds.PICKAXE_SOUND.get(), farm);

        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {

                return Component.translatable(state.getBlock().getDescriptionId());
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                return new OutputContainer(id, playerInventory, farm.getOutputInventory(), ContainerLevelAccess.create(worldIn, pos), ModBlocks.DCOAL_FARM::get);
            }
        });
        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return new SimpleBlockEntityTicker<>();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {


        Main.LOGGER.info("Success!");
        return new DeepslateCoalOreFarmTileentity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }
}
