package com.awesomeshot5051.resourceFarm.gui;


import com.awesomeshot5051.resourceFarm.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.inventory.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.extensions.*;
import net.neoforged.neoforge.registries.*;

public class Containers {

    private static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, Main.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<OutputContainer>> OUTPUT_CONTAINER = MENU_TYPE_REGISTER.register("output", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new OutputContainer(windowId, inv))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<InventoryViewerContainer>> INVENTORY_VIEWER_CONTAINER = MENU_TYPE_REGISTER.register("inventory_viewer", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new InventoryViewerContainer(windowId, inv, data.readBlockPos()))
    );
    public static final DeferredHolder<MenuType<?>, MenuType<UpgradeContainer>> UPGRADE_CONTAINER = MENU_TYPE_REGISTER.register("upgrade", () ->
            IMenuTypeExtension.create((windowId, inv, data) -> new UpgradeContainer(windowId, inv))
    );

    public static void init(IEventBus eventBus) {
        MENU_TYPE_REGISTER.register(eventBus);
    }

    public static void initClient(IEventBus eventBus) {
        eventBus.addListener(Containers::onRegisterScreens);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onRegisterScreens(RegisterMenuScreensEvent containers) {
        containers.register(OUTPUT_CONTAINER.get(), OutputScreen::new);
        containers.register(INVENTORY_VIEWER_CONTAINER.get(), InventoryViewerScreen::new);
        containers.register(UPGRADE_CONTAINER.get(), UpgradeScreen::new);
    }

}
