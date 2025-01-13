package com.awesomeshot5051.resourceFarm.events;

import com.awesomeshot5051.resourceFarm.*;
import net.minecraft.sounds.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.event.*;

public class ModSoundEvents {

    @SubscribeEvent
    public void onSound(PlayLevelSoundEvent.AtEntity event) {
        if (event.getSound() != null) {
            event.getSource();
            if (isVillagerSound(event.getSound().value()) && event.getSource().equals(SoundSource.BLOCKS)) {
                event.setNewVolume(Main.CLIENT_CONFIG.villagerVolume.get().floatValue());
            }
        }
    }

    @SubscribeEvent
    public void onSound(PlayLevelSoundEvent.AtPosition event) {
        if (event.getSound() != null) {
            event.getSound().value();
            event.getSource();
            if (isVillagerSound(event.getSound().value()) && event.getSource().equals(SoundSource.BLOCKS)) {
                event.setNewVolume(Main.CLIENT_CONFIG.villagerVolume.get().floatValue());
            }
        }
    }

    private boolean isVillagerSound(SoundEvent event) {
        return event.equals(SoundEvents.VILLAGER_NO)
                || event.equals(SoundEvents.VILLAGER_CELEBRATE)
                || event.equals(SoundEvents.VILLAGER_DEATH)
                || event.equals(SoundEvents.VILLAGER_AMBIENT)
                || event.equals(SoundEvents.VILLAGER_HURT)
                || event.equals(SoundEvents.VILLAGER_TRADE)
                || event.equals(SoundEvents.VILLAGER_WORK_ARMORER)
                || event.equals(SoundEvents.VILLAGER_WORK_BUTCHER)
                || event.equals(SoundEvents.VILLAGER_WORK_CARTOGRAPHER)
                || event.equals(SoundEvents.VILLAGER_WORK_CLERIC)
                || event.equals(SoundEvents.VILLAGER_WORK_FARMER)
                || event.equals(SoundEvents.VILLAGER_WORK_FISHERMAN)
                || event.equals(SoundEvents.VILLAGER_WORK_FLETCHER)
                || event.equals(SoundEvents.VILLAGER_WORK_LEATHERWORKER)
                || event.equals(SoundEvents.VILLAGER_WORK_LIBRARIAN)
                || event.equals(SoundEvents.VILLAGER_WORK_MASON)
                || event.equals(SoundEvents.VILLAGER_WORK_SHEPHERD)
                || event.equals(SoundEvents.VILLAGER_WORK_TOOLSMITH)
                || event.equals(SoundEvents.VILLAGER_WORK_WEAPONSMITH)
                || event.equals(SoundEvents.VILLAGER_YES)
                || event.equals(SoundEvents.IRON_GOLEM_HURT)
                || event.equals(SoundEvents.IRON_GOLEM_DEATH)
                || event.equals(SoundEvents.ZOMBIE_AMBIENT)
                || event.equals(SoundEvents.ZOMBIE_INFECT)
                || event.equals(SoundEvents.ZOMBIE_VILLAGER_AMBIENT)
                || event.equals(SoundEvents.ZOMBIE_VILLAGER_CONVERTED)
                || event.equals(SoundEvents.ZOMBIE_VILLAGER_CURE);
    }

}
