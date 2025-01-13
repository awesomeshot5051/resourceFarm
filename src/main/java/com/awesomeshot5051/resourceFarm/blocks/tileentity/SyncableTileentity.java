package com.awesomeshot5051.resourceFarm.blocks.tileentity;

import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;

import java.util.*;

public class SyncableTileentity extends BlockEntity {

    public SyncableTileentity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // Utility method to validate and parse the pick type
    public static Optional<ItemStack> loadPickType(CompoundTag compound, HolderLookup.Provider provider) {
        if (compound.contains("PickType")) {
            Tag pickTypeTag = compound.get("PickType");
            if (pickTypeTag != null && isValidPickaxe(pickTypeTag.toString()) || isValidShovel(Objects.requireNonNull(pickTypeTag).toString())) {
                return ItemStack.parse(provider, pickTypeTag);
            }
        }
        return Optional.empty();
    }

    // Utility method to validate and parse the pick type
    public static Optional<ItemStack> loadShovelType(CompoundTag compound, HolderLookup.Provider provider) {
        if (compound.contains("ShovelType")) {
            Tag shovelTypeTag = compound.get("ShovelType");
            if (shovelTypeTag != null && isValidShovel(shovelTypeTag.toString()) || isValidShovel(Objects.requireNonNull(shovelTypeTag).toString())) {
                return ItemStack.parse(provider, shovelTypeTag);
            }
        }
        return Optional.empty();
    }

    private static boolean isValidShovel(String itemId) {
        return itemId.contains("minecraft:wooden_shovel") ||
                itemId.contains("minecraft:stone_shovel") ||
                itemId.contains("minecraft:iron_shovel") ||
                itemId.contains("minecraft:diamond_shovel") ||
                itemId.contains("minecraft:golden_shovel") ||
                itemId.contains("minecraft:netherite_shovel");
    }

    // Helper method to check if an item is a valid pickaxe
    private static boolean isValidPickaxe(String itemId) {
        return itemId.contains("minecraft:wooden_pickaxe") ||
                itemId.contains("minecraft:stone_pickaxe") ||
                itemId.contains("minecraft:iron_pickaxe") ||
                itemId.contains("minecraft:diamond_pickaxe") ||
                itemId.contains("minecraft:golden_pickaxe") ||
                itemId.contains("minecraft:netherite_pickaxe");
    }

    public void sync() {
        if (level instanceof ServerLevel serverLevel) {
            LevelChunk chunk = serverLevel.getChunkAt(getBlockPos());
            if (chunk.getLevel().getChunkSource() instanceof ServerChunkCache chunkCache) {
                chunkCache.chunkMap.getPlayers(chunk.getPos(), false).forEach(this::syncContents);
            }
        }
    }

    public void syncContents(ServerPlayer player) {
        player.connection.send(getUpdatePacket());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag updateTag = new CompoundTag();
        saveAdditional(updateTag, provider);
        return updateTag;
    }

}
