package com.awesomeshot5051.resourceFarm.blocks.overworld.ores.common.regular;


import com.awesomeshot5051.corelib.block.*;
import com.awesomeshot5051.corelib.blockentity.*;
import com.awesomeshot5051.corelib.client.*;
import com.awesomeshot5051.resourceFarm.blocks.*;
import com.awesomeshot5051.resourceFarm.blocks.tileentity.overworld.ores.common.regular.*;
import com.awesomeshot5051.resourceFarm.datacomponents.*;
import com.awesomeshot5051.resourceFarm.enums.*;
import com.awesomeshot5051.resourceFarm.gui.*;
import com.awesomeshot5051.resourceFarm.items.render.overworld.ores.common.regular.*;
import com.awesomeshot5051.resourceFarm.sounds.*;
import net.minecraft.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
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

public class CopperOreFarmBlock extends BlockBase implements EntityBlock, IItemBlock {

    public static final EnumProperty<PickaxeType> PICKAXE_TYPE = EnumProperty.create("pickaxe_type", PickaxeType.class);

    public CopperOreFarmBlock() {
        super(Properties.of().mapColor(MapColor.STONE).strength(2.5F).sound(SoundType.COPPER).noOcclusion()); // Adjusted for enderman farm
    }

    @Override
    public Item toItem() {
        return new CustomRendererBlockItem(this, new Item.Properties()) {
            @OnlyIn(Dist.CLIENT)
            @Override
            public ItemRenderer createItemRenderer() {
                return new CopperOreFarmItemRenderer();
            }
        };
    }

    private void playSound(@NotNull Level level, BlockState state, SoundEvent sound, CopperOreFarmTileentity farm) {
        Vec3i vec3i = state.getValue(FACING).getNormal();
        double d0 = farm.getBlockPos().getX() + 0.5D + (double) vec3i.getX() / 2.0D;
        double d1 = farm.getBlockPos().getY() + 0.5D + (double) vec3i.getY() / 2.0D;
        double d2 = farm.getBlockPos().getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
        level.playSound(null, d0, d1, d2, sound, SoundSource.BLOCKS, 5F, 10 * 0.1F + 0.9F);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        CopperOreFarmTileentity trader = VillagerBlockEntityData.getAndStoreBlockEntity(stack, context.registries(), context.level(), () -> new CopperOreFarmTileentity(BlockPos.ZERO, ModBlocks.COPPER_FARM.get().defaultBlockState()));
        super.appendHoverText(stack, context, components, tooltipFlag);
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
                                        .replace("{Upgrade:\"", "") // Remove the prefix
                                        .replace("\"}", "") // Remove the suffix
                                        .split("_")) // Split by underscores
                                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1)) // Capitalize each word
                                .collect(Collectors.joining(" ")) // Join words back with spaces
                ));
            }
        } else {
            components.add(Component.literal("Hold §4Shift§r to see tool").withStyle(ChatFormatting.YELLOW));
        }
        // Removed villager-related tooltip information

    }

    private String convertToReadableName(String block) {
        // Remove "item.minecraft." and replace underscores with spaces
        String readableName = block.replace("item.minecraft.", "").replace('_', ' ');
        // Capitalize the first letter of each word
        return Arrays.stream(readableName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        // Get the block entity
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperOreFarmTileentity farmTileEntity) {
            // Check for the pick type in the item stack
            farmTileEntity.upgradeEnabled = stack.has(DataComponents.CUSTOM_DATA);
            farmTileEntity.customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            ItemContainerContents pickType = stack.get(ModDataComponents.PICK_TYPE);
            if (pickType != null) {
                farmTileEntity.pickType = pickType.getStackInSlot(0);
                // Ensure the tile entity is marked as changed and synced
                farmTileEntity.setChanged();
                updateCustomBlockEntityTag(level, placer instanceof Player ? (Player) placer : null, pos, pickType.getStackInSlot(0));
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (!(tileEntity instanceof CopperOreFarmTileentity farm)) {// Check for EndermanFarmTileentity
            return super.useItemOn(heldItem, state, worldIn, pos, player, handIn, hit);
        }
        playSound(Objects.requireNonNull(worldIn), state, ModSounds.PICKAXE_SOUND.get(), farm);
        // Directly open the container without villager checks
        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {

                return Component.translatable(state.getBlock().getDescriptionId());
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                return new OutputContainer(id, playerInventory, farm.getOutputInventory(), ContainerLevelAccess.create(worldIn, pos), ModBlocks.COPPER_FARM::get); // Adjust for enderman farm
            }
        });
        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level1, BlockState state, BlockEntityType<T> type) {
        return new SimpleBlockEntityTicker<>(); // Keeps default behavior
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
//        Objects.requireNonNull(this.asItem().getDefaultInstance().get(ModDataComponents.PICK_TYPE)).getStackInSlot(0);
        return new CopperOreFarmTileentity(blockPos, blockState); // Spawn EndermanFarmTileentity
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
