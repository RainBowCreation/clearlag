package net.rainbowcreation.clearlag.evnt;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.Main;
import net.rainbowcreation.clearlag.utils.Reference;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class Xpdrop {
    /*
    @SubscribeEvent
    public void onEntityDead(LivingDeathEvent event) {
        System.out.println("WHAT");
        Main.LOGGER.info(event.getEntityLiving().toString() + " DEAD BY " + event.getSource().toString() + " result " + event.getResult().toString());
    }

    @SubscribeEvent
    public void onXPDrop(LivingExperienceDropEvent event) {
        System.out.println("WHAT");
        Main.LOGGER.info(event.getEntityLiving() + " <-base, " + event.getEntity() + " <-E " + event.getAttackingPlayer() + " <-Player");
    }

     */
}
