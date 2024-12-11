package com.awesomeshot5051.resourceFarm.gui;

import com.awesomeshot5051.resourceFarm.Main;
import de.maxhenkel.corelib.inventory.ScreenBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OutputScreen extends ScreenBase<OutputContainer> {

    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/container/output.png");

    private final Inventory playerInventory;

    public OutputScreen(OutputContainer container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 133;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        drawCentered(guiGraphics, Component.translatable("gui.resource_farms.output"), 9, FONT_COLOR);
        guiGraphics.drawString(font, playerInventory.getDisplayName().getVisualOrderText(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR, false);
    }

    protected void drawCentered(GuiGraphics guiGraphics, MutableComponent text, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text.getVisualOrderText(), imageWidth / 2F - width / 2F, y, color, false);
    }

}