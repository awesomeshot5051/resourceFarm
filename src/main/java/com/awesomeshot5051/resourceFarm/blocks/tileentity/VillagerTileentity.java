package com.awesomeshot5051.resourceFarm.blocks.tileentity;

//import com.awesomeshot5051.resourceFarm.datacomponents.VillagerData;

import com.awesomeshot5051.resourceFarm.datacomponents.*;
import com.awesomeshot5051.resourceFarm.entity.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import javax.annotation.*;

public class VillagerTileentity extends FakeWorldTileentity {

    protected ItemStack villager;
    protected EasyVillagerEntity villagerEntity;

    public VillagerTileentity(BlockEntityType<?> type, BlockState defaultState, BlockPos pos, BlockState state) {
        super(type, defaultState, pos, state);
        villager = ItemStack.EMPTY;
    }

    public boolean hasVillager() {
        return !villager.isEmpty();
    }

    @Nullable
    public com.awesomeshot5051.resourceFarm.entity.EasyVillagerEntity getVillagerEntity() {
        if (villagerEntity == null && !villager.isEmpty()) {
            villagerEntity = VillagerData.createEasyVillager(villager, level);
        }
        return villagerEntity;
    }

    public long getTimer() {
        return 0;
    }

    protected void onAddVillager(EasyVillagerEntity villager) {

    }

}
