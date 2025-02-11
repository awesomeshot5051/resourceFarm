package com.awesomeshot5051.resourceFarm.integration.waila;

import com.awesomeshot5051.corelib.blockentity.*;
import com.awesomeshot5051.resourceFarm.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.entity.*;
import org.jetbrains.annotations.*;
import snownee.jade.api.*;
import snownee.jade.api.config.*;
import snownee.jade.api.ui.*;
import snownee.jade.impl.ui.*;

import java.util.*;
import java.util.stream.*;

public class HUDHandlerFarms implements IBlockComponentProvider {

    public static final HUDHandlerFarms INSTANCE = new HUDHandlerFarms();

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Main.MODID, "pick_type");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof FarmTileentity blockEntity) {
            ItemStack pickType = blockEntity.getPickType();
            if (blockEntity.getPickType() == ItemStack.EMPTY) {
                pickType = blockEntity.getShovelType();
            }
            if (pickType != ItemStack.EMPTY) {
                iTooltip.add(Component.translatable(convertToReadableName(pickType.getDescriptionId())).withStyle(ChatFormatting.RED));
            }
            if (!blockEntity.getCustomData().isEmpty()) {
                iTooltip.add(Component.literal(
                        Arrays.stream(blockEntity.getCustomData()
                                        .toString()
                                        .replace("{}", " ")
                                        .replace("{Upgrade:\"", "")
                                        .replace("\"}", "")
                                        .split("_"))
                                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                                .collect(Collectors.joining(" "))
                ));
            }
        }

    }


    private String convertToReadableName(String block) {

        String readableName = block.replace("item.minecraft.", "").replace('_', ' ');

        return Arrays.stream(readableName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        BlockEntity te = accessor.getBlockEntity();
        ItemStack stack2 = new ItemStack(te.getBlockState().getBlock().asItem());

        if (te.getLevel() != null) {
            CompoundTag blockEntityTag = te.saveWithoutMetadata(te.getLevel().registryAccess());
            stack2.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));
        }
        return ItemStackElement.of(stack2);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

}