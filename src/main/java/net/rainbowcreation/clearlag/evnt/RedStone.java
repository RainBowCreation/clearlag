package net.rainbowcreation.clearlag.evnt;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.Main;
import net.rainbowcreation.clearlag.utils.IChunk;
import net.rainbowcreation.clearlag.utils.ISpawn;
import net.rainbowcreation.clearlag.utils.Reference;

import static net.rainbowcreation.clearlag.config.GeneralConfig.settings;
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class RedStone {
    /*
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRedstoneActivation(BlockEvent.NeighborNotifyEvent event) {
        event.setCanceled(true);
        /*
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        // Calculate chunk coordinates
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        long chunkKey = IChunk.getChunkKey(chunkX,  chunkZ);
        // Increment the redstone activation count for the chunk
        Main.redstoneCounts.put(chunkKey, Main.redstoneCounts.getOrDefault(chunkKey, 0) + 1);
        //Main.LOGGER.info("Redstone activated " + world.getBlockState(pos).getBlock().getRegistryName().toString() + " for chunk " + chunkKey + " = " + Main.redstoneCounts.getOrDefault(chunkKey, 0));

        // Cancel the redstone activation if the threshold is exceeded
        if (Main.redstoneCounts.getOrDefault(chunkKey, 0) > settings.REDSTON_LIMIT) {
            String name = world.getBlockState(pos).getBlock().getRegistryName().toString();
            //ISpawn.removeBlockAndSpawnAsItem(world, pos);
            ISpawn.removeBlockAndSpawnAsItem(world, pos);
        }


    }
    */
}
