package net.rainbowcreation.clearlag;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.rainbowcreation.clearlag.utils.*;

import org.apache.logging.log4j.Logger;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.rainbowcreation.clearlag.config.GeneralConfig.settings;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class Main {
    public static Logger LOGGER = FMLLog.log;
    public static Map<Long, Integer> redstoneCounts = new HashMap<>();
    private static int staticTime;
    private static int timeRemaining;
    private static int previousTime = ITime.getCurrentTime()[2];
    private static int tick = 20;
    private static int tickRemaining = tick;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        if (!settings.CLEAR_ITEM)
            return;
        for (String txt : Reference.HEADER) {
            LOGGER.info(txt);
        }
        LOGGER.info(IString.genHeader(Reference.NAME+":"+Reference.VERSION));
        ITime.TIME = ITime.getTimeInSecond(settings.TIME);
        staticTime = ITime.TIME;
        timeRemaining = staticTime;
        ITime.WARNING_TIME = ITime.getTimeInSecond(settings.WARNING_TIME);
        int i = ITime.WARNING_TIME;
        while (i > 10) {
            ITime.WARNING_TIME_LIST.add(i);
            i/=2;
        }
        for (int j = 1; j <= 10; j++)
             ITime.WARNING_TIME_LIST.add(j);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
        if (tickRemaining > 0) {
           tickRemaining--;
           return;
        }
        tickRemaining = tick;
        int current = ITime.getCurrentTime()[2];
        if (current == previousTime) {
            return;
        }
        previousTime = current;
        if (settings.REDSTON_LIMIT > 0)
            redstoneCounts.clear();
        if (!settings.CLEAR_ITEM)
            return;
        World world = event.world;
        MinecraftServer minecraftServer = world.getMinecraftServer();
        PlayerList playerList = minecraftServer.getPlayerList();
        if (timeRemaining != 0) {
            ITime.alert(timeRemaining, "Clear Lag", "Items will be cleared in", playerList);
            timeRemaining -= 1;
            return;
        }

        WorldServer worldServer = (WorldServer) world;
        int amount = worldServer.loadedEntityList.size();
        /*
        Collection<Chunk> loadedChunks = worldServer.getChunkProvider().getLoadedChunks();
        playerList.sendMessage(new TextComponentString("[Clear Lag] " + TextFormatting.RESET + "loadedchunks found " + TextFormatting.RED + loadedChunks.size() + TextFormatting.RESET + " chunks."));

        // Iterate through all loaded chunks
        for (Chunk chunk : loadedChunks) {
            // Get all entities in the current chunk
            List<Entity> entitiesInChunk = chunk.getWorld().getEntitiesWithinAABB(Entity.class, IChunk.getChunkBoundingBox(chunk));
            int size = entitiesInChunk.size();
            playerList.sendMessage(new TextComponentString("[Clear Lag] " + TextFormatting.RESET + "AMOUNT+= " + TextFormatting.RED + size + TextFormatting.RESET + " entities."));
            amount += size;
            // Group entities in the current chunk
            IEntity.groupEntitiesByNameTag(world, entitiesInChunk, 10);
        }

        playerList.sendMessage(new TextComponentString("[Clear Lag] " + TextFormatting.RESET + "AMOUNT: " + TextFormatting.RED + amount + TextFormatting.RESET + " entities."));

        for (Entity entity : world.loadedEntityList) {
            entity.setDead();
            amount++;
        }
         */
        minecraftServer.getCommandManager().executeCommand(minecraftServer, "kill @e[type=item] @e[type=xp_orb]");
        playerList.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Clear Lag] " + TextFormatting.RESET + "Cleared " + TextFormatting.RED  + amount + TextFormatting.RESET + " items."));
        timeRemaining = staticTime;
    }
}
