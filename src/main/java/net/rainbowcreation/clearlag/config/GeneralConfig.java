package net.rainbowcreation.clearlag.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.utils.Reference;
import net.rainbowcreation.clearlag.utils.Time;

@EventBusSubscriber(modid = Reference.MODID)
@Config(modid = Reference.MODID, name = Reference.NAME, category = "general")
public class GeneralConfig {
    public static Settings settings = new Settings();
    public static class Settings {
        @Config.Comment({"Time between each clear [H, M, S]"})
        public int[] TIME = Time.secondToTime(1800);

        @Comment({"Time to start Announced to chat before clear, [H, M, S]"})
        public int[] WARNING_TIME = Time.secondToTime(900);

        @Comment({"Set to false to disable item clearing"})
        public boolean CLEAR_ITEM = true;

        @Comment({"Set to false to disable moister limit per chunk"})
        public boolean MONSTER_LIMIT = true;

        @Comment({"Amount of monster with same type limit per chunk default is 5"})
        public int MONSTER_LIMIT_NUMBER = 5;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID))
            ConfigManager.load(Reference.MODID, Config.Type.INSTANCE);
    }
}
