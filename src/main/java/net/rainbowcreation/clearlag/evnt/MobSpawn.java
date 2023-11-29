package net.rainbowcreation.clearlag.evnt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.utils.IChunk;
import net.rainbowcreation.clearlag.utils.IEntity;
import net.rainbowcreation.clearlag.utils.Reference;

import java.util.List;

import static net.rainbowcreation.clearlag.config.GeneralConfig.settings;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class MobSpawn {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (settings.MONSTER_LIMIT == -1)
            return;
        World world = event.getWorld();
        Entity entity = event.getEntity();

        // Check if the entity is a mob (you can customize this check based on your needs)
        if (entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
            // Check the number of entities in the chunk
            int entityCount = world.getEntitiesWithinAABB(entity.getClass(), entity.getEntityBoundingBox().grow(16.0D)).size();
            // If the entity count exceeds the limit, cancel the spawn event
            if (entityCount >= settings.MONSTER_LIMIT) {
                event.setCanceled(true);
            }
        }
        /*
         else if (entity.isCreatureType(EnumCreatureType.CREATURE, false)) {
            // Check the number of entities in the chunk
            List<Entity> entitys = world.getEntitiesWithinAABB(entity.getClass(), entity.getEntityBoundingBox().grow(16.0D));
            int entityCount = entitys.size();
            for (Entity entityc : entitys) {
                String nametag = entityc.getCustomNameTag();
                StringBuilder sb = new StringBuilder(nametag);
                if (nametag.contains("X")) {
                    entityCount+= Integer.parseInt(nametag.substring(sb.indexOf("X")+1)) - 1;
                }
                entityc.setDead();
            }
            //IEntity.groupEntitiesByNameTag(world, world.getEntitiesWithinAABB(entity.getClass(), IChunk.getChunkBoundingBox(world.getChunkFromBlockCoords(entity.getPosition()))), 10);
            // If the entity count exceeds the limit, cancel the spawn event
            entity.setCustomNameTag(entity.getClass().getSimpleName() + " X" + (entityCount + 1));
            /*
            if (entityCount > 1) {
                event.setCanceled(true);
            }


        }
         */
    }
}
