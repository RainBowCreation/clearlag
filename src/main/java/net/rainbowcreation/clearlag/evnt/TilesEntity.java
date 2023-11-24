package net.rainbowcreation.clearlag.evnt;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.utils.IChunk;
import net.rainbowcreation.clearlag.utils.Reference;

import static net.rainbowcreation.clearlag.config.GeneralConfig.settings;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class TilesEntity {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (settings.TILE_LIMIT == 0) {
            return;
        }
        World world = event.getWorld();
        int chunkX = event.getPos().getX() >> 4;
        int chunkZ = event.getPos().getZ() >> 4;
        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

        // Count the number of existing tile entities in the chunk
        int tileEntityCount = IChunk.countTileEntitiesInChunk(chunk);

        // Check if placing the new tile entity would exceed the limit
        if (tileEntityCount >= settings.TILE_LIMIT) {
            event.setCanceled(true);
            // You can optionally send a message to the player indicating that the limit has been reached.
            event.getPlayer().sendMessage(new TextComponentString(TextFormatting.BOLD + "[Clear Lag] " + TextFormatting.RESET + TextFormatting.RED + "TilesEntity reached limit in this chunk."));
        }
    }
}
