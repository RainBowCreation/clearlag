package net.rainbowcreation.clearlag;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.rainbowcreation.clearlag.utils.Confighandler;
import net.rainbowcreation.clearlag.utils.Reference;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.rainbowcreation.clearlag.utils.Time;

import java.util.Arrays;
import java.util.List;

import static net.rainbowcreation.clearlag.utils.Confighandler.settings;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class main {
    private static int staticTime;
    private static int timeRemaining;
    private static int[] timePrevious;
    private static List<String> whitelist;
    private static int Tick = 20;
    private static int tick = Tick;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        List<String> header = Arrays.asList("######################################################################################",
                "#  _____       _       ____                 _____                _   _               #",
                "# |  __ \\     (_)     |  _ \\               / ____|              | | (_)              #",
                "# | |__) |__ _ _ _ __ | |_) | _____      _| |     _ __ ___  __ _| |_ _  ___  _  __   #",
                "# |  _  // _` | | '_ \\|  _ < / _ \\ \\ /\\ / / |    | '__/ _ \\/ _`  | __| |/ _ \\| '_ \\  #",
                "# | | \\ \\ (_| | | | | | |_) | (_) \\ V  V /| |____|  | |  __/ (_| | |_| | (_) | | | | #",
                "# |_|  \\_\\__,_|_|_| |_|____/ \\___/ \\_/\\_/  \\_____|_|  \\___|\\__,_|\\__|_|\\___/|_|  |_| #",
                "#                                                                                    #",
                "#######################################################################clearlag#######");
        for (String txt : header)
            System.out.println(txt);
        Time.TIME = Time.getTimeInSecond(settings.TIME);
        staticTime = Time.TIME;
        timeRemaining = staticTime;
        whitelist = Arrays.asList(Confighandler.whitelist.ITEM_WHITELIST);
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
        timeRemaining = staticTime;
    }
}
