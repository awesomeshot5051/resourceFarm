package com.awesomeshot5051.resourceFarm.gui;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.*;

import javax.annotation.*;
import java.util.function.*;

public class UpgradeContainer extends InputOutputContainer {
    @Nullable
    protected Supplier<Block> blockSupplier;

    public UpgradeContainer(int id, Inventory playerInventory, Container inputInventory, Container outputInventory, ContainerLevelAccess access, Supplier<Block> blockSupplier) {
        super(Containers.UPGRADE_CONTAINER.get(), id, playerInventory, inputInventory, outputInventory, access);
        this.blockSupplier = blockSupplier;
    }

    public UpgradeContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(4), new SimpleContainer(4), ContainerLevelAccess.NULL, null);
        this.access = ContainerLevelAccess.NULL;
    }

    @Override
    public Slot getInputSlot(Container inventory, int id, int x, int y) {
        return new UpgradeSlot(inventory, id, x, y);
    }

    @Override
    public Block getBlock() {
        if (blockSupplier == null) {
            return Blocks.AIR;
        }
        return blockSupplier.get();
    }
}

