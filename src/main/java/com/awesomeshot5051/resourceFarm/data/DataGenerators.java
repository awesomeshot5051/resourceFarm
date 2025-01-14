package com.awesomeshot5051.resourceFarm.data;

import com.awesomeshot5051.resourceFarm.Main;
import com.awesomeshot5051.resourceFarm.data.providers.models.ModBlockLootTableProvider;
import com.awesomeshot5051.resourceFarm.data.providers.models.ModBlockModelProvider;
import com.awesomeshot5051.resourceFarm.data.providers.models.ModBlockStateProvider;
import com.awesomeshot5051.resourceFarm.data.providers.models.ModItemModelProvider;
import com.awesomeshot5051.resourceFarm.data.providers.recipe.ModRecipeProvider;
import com.awesomeshot5051.resourceFarm.data.providers.sound.BaseSoundProvider;
import com.awesomeshot5051.resourceFarm.data.providers.tags.BlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModBlockModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(event.includeServer(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new BlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(
                event.includeClient(),
                new BaseSoundProvider(packOutput, existingFileHelper)
        );
    }
}