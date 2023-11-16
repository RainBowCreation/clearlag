package net.rainbowcreation.serverExtension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.rainbowcreation.serverExtension.utils.Confighandler;
import net.rainbowcreation.serverExtension.utils.Packet;
import net.rainbowcreation.serverExtension.utils.Reference;

import java.io.File;
import java.util.*;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.rainbowcreation.serverExtension.utils.Time;

import static net.rainbowcreation.serverExtension.utils.Confighandler.settings;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class main {
    @Mod.Instance
    public static main instance;
    public static File config;
    private static int staticTime;

    private static int timeRemaining;
    private static int[] timePrevious = new int[3];
    private static List<String> whitelist;
    public static List<String> blacklist;
    private static int Tick = 20;
    private static int tick = Tick;
    private static int[] M_TIME_TO_1;
    private static TextComponentString MAINTENANCE_TEXT = new TextComponentString(TextFormatting.RED + "Daily Maintenance " + TextFormatting.RESET);


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        List<String> header = Arrays.asList("######################################################################################",
                "#  _____       _       ____                 _____                _   _               #",
                "# |  __ \\     (_)     |  _ \\               / ____|              | | (_)              #",
                "# | |__) |__ _ _ _ __ | |_) | _____      _| |     _ __ ___  __ _| |_ _  ___  _  __   #",
                "# |  _  // _` | | '_ \\|  _ < / _ \\ \\ /\\ / / |    | '__/ _ \\/ _`  | __| |/ _ \\| '_ \\  #",
                "# | | \\ \\ (_| | | | | | |_) | (_) \\ V  V /| |____|  | |  __/ (_| | |_| | (_) | | | | #",
                "# |_|  \\_\\__,_|_|_| |_|____/ \\___/ \\_/\\_/  \\_____|_|  \\___|\\__,_|\\__|_|\\___/|_|  |_| #",
                "#                                                                                    #",
                "################################################################sever-extension#######");
        System.out.println("Mode:" +settings.MODE);
        for (String txt : header)
            System.out.println(txt);
        Time.TIME = Time.getTimeInSecond(settings.TIME);
        staticTime = Time.TIME;
        timeRemaining = staticTime;
        whitelist = Arrays.asList(Confighandler.whitelist.ITEM_WHITELIST);
        blacklist = Arrays.asList(Confighandler.blacklist.DO_NOT_BROADCAST_TITLE_TO);
        Time.WARNING_TIME = Time.getTimeInSecond(settings.WARNING_TIME);
        int i = Time.WARNING_TIME;
        while (i > 10) {
            Time.WARNING_TIME_LIST.add(i);
            i/=2;
        }
        for (int j = 1; j <= 10; j++)
             Time.WARNING_TIME_LIST.add(j);
        Time.prefix = settings.TIME_ZONE_PREFIX;
        Time.offset = settings.TIME_ZONE_OFFSET;
        timePrevious = Time.getCurrentTime();
        String[] TIME_FROM = Time.timeToString(settings.M_TIME_FROM);
        String[] TIME_TO = Time.timeToString(settings.M_TIME_TO);
        M_TIME_TO_1 = Time.getSubstract(settings.M_TIME_TO, new int[]{0,0,1});
        MAINTENANCE_TEXT.appendSibling(new TextComponentString(TIME_FROM[0] + ":" + TIME_FROM[1] + TextFormatting.RED + "-" + TextFormatting.RESET + TIME_TO[0] + ":" + TIME_TO[1] + TextFormatting.RED));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
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
        if (timeRemaining == 0) {
            if (settings.clearItems) {
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
                if (settings.MODE.equals("server"))
                    playerList.sendMessage((ITextComponent) new TextComponentString(TextFormatting.BOLD + "[Clear Lag] " + TextFormatting.RESET + "Cleared " + TextFormatting.RED + amount + TextFormatting.RESET + " items!"));
            }
            timeRemaining = staticTime;
            return;
        }
        switch (settings.MODE) {
            case ("server"): {
                if (time[0] != timePrevious[0]) {
                    playerList.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Maintenance Scheduler] " + TextFormatting.RESET).appendSibling(MAINTENANCE_TEXT));
                }
                Time.alert(settings.M_TIME_FROM, "Maintenance Scheduler", "Server close in", playerList, true);
                Time.alert(timeRemaining, "Clear Lag", "Items will be cleared in", playerList);
                if (Time.getSubstractInSecond(settings.M_TIME_FROM, Time.getCurrentTime()) == 0) {
                    world.getMinecraftServer().initiateShutdown();
                    return;
                }
                break;
            }
            case ("lobby"): {
                if (time[0] >= settings.M_TIME_FROM[0] && time[0] < settings.M_TIME_TO[0]) {
                    TextComponentString text = new TextComponentString("Time remaining ");
                    if (time[0] < M_TIME_TO_1[0])
                        text.appendSibling(new TextComponentString(TextFormatting.RED + String.valueOf(M_TIME_TO_1[0] - time[0]) + TextFormatting.RESET + ":"));
                    String minute = String.valueOf(M_TIME_TO_1[1] - time[1]);
                    String second = String.valueOf(M_TIME_TO_1[2] - time[2]);
                    if (minute.length() == 1)
                        minute = "0" + minute;
                    if (second.length() == 1)
                        second = "0" + second;
                    text.appendSibling(new TextComponentString(TextFormatting.RED + minute + TextFormatting.RESET + ":" + TextFormatting.RED + second));
                    for (EntityPlayerMP playerMP : plist) {
                        Packet.sent(playerMP, MAINTENANCE_TEXT, SPacketTitle.Type.TITLE, 0, 100, 0);
                        Packet.sent(playerMP, text, SPacketTitle.Type.SUBTITLE, 0, 100, 0);
                    }
                }
                else if (time[0] == settings.M_TIME_TO[0] && time[1] == settings.M_TIME_TO[1] && time[2] == settings.M_TIME_TO[2]) {
                    for (EntityPlayerMP playerMP : plist)
                        playerMP.connection.sendPacket(Reference.PACKET_MAINTENANCE_COMPLETE);
                    playerList.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Maintenance Completed] " + TextFormatting.RESET + "please reconnect the server to enter the portal!"));
                }
                break;
            }
        }
        timeRemaining-= Time.getSubstractInSecond(time, timePrevious);
        timePrevious = time;
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        //V only in looby
        EntityPlayer player = event.player;
        switch (settings.MODE) {
            case ("lobby"): {
                double x, y, z;
                x = -23.5;
                y = 63.5;
                z = 0.5;
                player.setPositionAndUpdate(x, y, z);
                player.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Queue] " + TextFormatting.RESET + "enter the portal to join queue"));
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                SPacketTitle packetActionBar = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString(player.getName()), 20,120,20);
                playerMP.connection.sendPacket(Reference.PACKET_TITLE);
                playerMP.connection.sendPacket(Reference.PACKET_SUB_TITLE);
                playerMP.connection.sendPacket(packetActionBar);
                break;
            }
            case ("server"): {
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 60 * 20, 4));
                player.sendMessage(new TextComponentString(TextFormatting.BOLD + "[Auth] " + TextFormatting.RESET + "please login or register\n/login <password>\nor\n/register <password> <password>"));
                break;
            }
        }
        /*
        if (getSkinStorage().getSkin(event.player.func_110124_au()) == SkinStorage.DEFAULT_SKIN)
            getSkinStorage().setSkin(event.player.func_110124_au(), MojangSkinProvider.getSkin(event.player.func_146103_bH().getName()));
        applySkin((EntityPlayerMP)event.player, getSkinStorage().getSkin(event.player.func_110124_au()));
        */
    }
    /*

    @SubscribeEvent
    public void onPlayerLeaving(PlayerEvent.PlayerLoggedOutEvent event) {
        getSkinStorage().removeSkin(event.player.func_110124_au());
    }

    public static void onClosingServer(FMLServerStoppingEvent ev
                                       ent) {
        for (EntityPlayerMP player : server.func_184103_al().func_181057_v())
            getSkinStorage().removeSkin(player.func_110124_au());
    }

     */
}
