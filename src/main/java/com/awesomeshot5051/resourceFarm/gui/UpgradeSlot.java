package com.awesomeshot5051.resourceFarm.gui;

import com.awesomeshot5051.resourceFarm.items.upgrade.*;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class UpgradeSlot extends Slot {
    public UpgradeSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    public static boolean isValid(ItemStack stack) {
        return Upgrades.UPGRADES.contains(stack.getItem());
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return isValid(stack);
    }
}
