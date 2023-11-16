package net.rainbowcreation.serverExtension.utils;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MODID)
@Config(modid = Reference.MODID, name = Reference.NAME, category = "general")
public class Confighandler {
    public static Settings settings = new Settings();
    public static class Settings {
        @Config.Comment({"Time between each clear [H, M, S]"})
        public int[] TIME = Time.secondToTime(1800);

        @Comment({"Time tp clear is announced to chat before-hand, [H, M, S]"})
        public int[] WARNING_TIME = Time.secondToTime(900);

        @Comment({"Set to false to disable item clearing"})
        public boolean clearItems = true;

        @Comment({"Set Time Zone Prefix [GMT, UTC]"})
        public String TIME_ZONE_PREFIX = "UTC";

        @Comment({"Set Time Zone Offset -> '+07:00', '-09:00'"})
        public String TIME_ZONE_OFFSET = "+07:00";

        @Comment({"Set maintance time from [H, M, S] in 24 clock"})
        public int[] M_TIME_FROM = Time.secondToTime(3600*3);

        @Comment({"Set maintenance time to [H, M, S] in 24 clock"})
        public int[] M_TIME_TO = Time.secondToTime(3600*6);

        @Comment({"Mode of the instance [server, lobby]"})
        public String MODE = "server";
    }

    public static Whitelist whitelist = new Whitelist();
    public static Blacklist blacklist = new Blacklist();

    public static class Whitelist {
        @Comment({"Item IDs of items to be ignored when clearing, these items will not be cleared. Format: modid:itemid, ex: minecraft:diamond"})
        public String[] ITEM_WHITELIST = new String[] { "minecraft:diamond", "minecraft:diamond_block", "minecraft:dragon_egg" };
    }

    public static class Blacklist {
        @Comment({"Player Name That wont get Title but resieved as chat instead"})
        public String[] DO_NOT_BROADCAST_TITLE_TO = new String[]{"RainBowCreation"};
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID))
            ConfigManager.load(Reference.MODID, Config.Type.INSTANCE);
    }
}
