package net.rainbowcreation.clearlag.utils;

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

        @Comment({"Time to start Announced to chat before clear, [H, M, S]"})
        public int[] WARNING_TIME = Time.secondToTime(900);

        @Comment({"Set to false to disable item clearing"})
        public boolean CLEAR_ITEM = true;

        @Comment({"Should player get exps after clear lag? (default false)"})
        public boolean GIFT = false;

        @Comment({"Amount of exp give to player each round (Default 10)"})
        public int EXP = 10;

        @Comment({"Message sent to player after recieved exp (use {EXP} to replace with exp amount , {TIME} to replace with time in second"})
        public String GIFT_MESSAGE = "{TIME} seconds has passed, Here was {EXP}exp for you, Thank you for playing our server";

    }

    public static Whitelist whitelist = new Whitelist();

    public static class Whitelist {
        @Comment({"Item IDs of items to be ignored when clearing, these items will not be cleared. Format: modid:itemid, ex: minecraft:diamond"})
        public String[] ITEM_WHITELIST = new String[] { "minecraft:diamond", "minecraft:diamond_block", "minecraft:dragon_egg" };
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID))
            ConfigManager.load(Reference.MODID, Config.Type.INSTANCE);
    }
}
