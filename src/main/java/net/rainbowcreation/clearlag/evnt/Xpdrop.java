package net.rainbowcreation.clearlag.evnt;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.utils.Reference;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class Xpdrop {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        EntityLivingBase entity = event.getAttackingPlayer(); // Get the player that killed the entity
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            // Check if the player killed the entity
            if (player != null && player.isEntityAlive()) {
                // Transfer the experience directly to the player
                player.addExperience(event.getDroppedExperience());
            }
        }
        // Cancel the original event to avoid spawning XP orbs
        event.setCanceled(true);
    }
}
