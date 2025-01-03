package com.awesomeshot5051.resourceFarm.gui;

import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;

public class UpgradeScreen extends InputOutputScreen<UpgradeContainer> {
    public UpgradeScreen(UpgradeContainer container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    @Override
    protected MutableComponent getTopText() {
        return Component.translatable("gui.resource_farms.upgrade_items");
    }

    @Override
    protected MutableComponent getBottomText() {
        return Component.translatable("gui.resource_farms.output_items");
    }
}
