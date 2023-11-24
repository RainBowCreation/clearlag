package net.rainbowcreation.clearlag.evnt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.utils.Reference;

import static net.rainbowcreation.clearlag.config.GeneralConfig.settings;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class Xpdrop {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        if (!settings.DISABLE_XP_DROP)
            return;
        EntityPlayer entity = event.getAttackingPlayer(); // Get the player that killed the entity
        if (entity!= null && entity.isEntityAlive()) {
            // Check if the player killed the entity
            // Transfer the experience directly to the player
            entity.addExperience(event.getDroppedExperience());
        }
        // Cancel the original event to avoid spawning XP orbs
        event.setCanceled(true);
    }
}
