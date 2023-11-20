package net.rainbowcreation.clearlag;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.rainbowcreation.clearlag.config.GeneralConfig;
import net.rainbowcreation.clearlag.utils.IString;
import net.rainbowcreation.clearlag.utils.Reference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.rainbowcreation.clearlag.utils.Time;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static net.rainbowcreation.clearlag.config.GeneralConfig.settings;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class Main {
    public static Logger LOGGER = FMLLog.log;
    private static int staticTime;
    private static int timeRemaining;
    private static int[] timePrevious;
    private static List<String> whitelist;
    private static int Tick = 20;
    private static int tick = Tick;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        if (!settings.CLEAR_ITEM)
            return;
        for (String txt : Reference.HEADER) {
            LOGGER.info(txt);
        }
        LOGGER.info(IString.genHeader(Reference.NAME+":"+Reference.VERSION));
        Time.TIME = Time.getTimeInSecond(settings.TIME);
        staticTime = Time.TIME;
        timeRemaining = staticTime;
        whitelist = Arrays.asList(GeneralConfig.whitelist.ITEM_WHITELIST);
        Time.WARNING_TIME = Time.getTimeInSecond(settings.WARNING_TIME);
        int i = Time.WARNING_TIME;
        while (i > 10) {
            Time.WARNING_TIME_LIST.add(i);
            i/=2;
        }
        for (int j = 1; j <= 10; j++)
             Time.WARNING_TIME_LIST.add(j);
        timePrevious = Time.getCurrentTime();
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (!settings.CLEAR_ITEM)
            return;
        if (tick > 0) {
            tick--;
            return;
        }
        else
            tick = Tick;
        int[] time = Time.getCurrentTime();
        if (time[2] == timePrevious[2])
            return;
        World world = event.world;
        PlayerList playerList = world.getMinecraftServer().getPlayerList();
        List<EntityPlayerMP> plist = playerList.getPlayers();
        if (timeRemaining != 0) {
            Time.alert(timeRemaining, "Clear Lag", "Items will be cleared in", playerList);
            timeRemaining -= Time.getSubstractInSecond(time, timePrevious);
            timePrevious = time;
            return;
        }
        int amount = 0;
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                EntityItem item = (EntityItem) entity;
                if (!whitelist.contains(((ResourceLocation) Item.REGISTRY.getNameForObject(item.getItem().getItem())).toString())) {
                    item.setDead();
                    amount++;
                }
            }
        }
        playerList.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Clear Lag] " + TextFormatting.RESET + "Cleared " + TextFormatting.RED  + amount + TextFormatting.RESET + " items."));
        if (settings.GIFT) {
            for (EntityPlayerMP playerMP : plist) {
                playerMP.addExperience(settings.EXP);
            }
            String text = settings.GIFT_MESSAGE;
            playerList.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Gift] " + TextFormatting.RESET + text.replace("{EXP}", String.valueOf(settings.EXP)).replace("{TIME}", String.valueOf(Time.getTimeInSecond(settings.TIME)))));
        }
        timeRemaining = staticTime;
    }
}
