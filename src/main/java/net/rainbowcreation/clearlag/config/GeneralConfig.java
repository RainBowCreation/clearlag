package net.rainbowcreation.clearlag.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rainbowcreation.clearlag.utils.Reference;
import net.rainbowcreation.clearlag.utils.ITime;

@EventBusSubscriber(modid = Reference.MODID)
@Config(modid = Reference.MODID, name = Reference.NAME, category = "general")
public class GeneralConfig {
    public static Settings settings = new Settings();
    public static class Settings {
        @Config.Comment({"ITime between each clear [H, M, S]"})
        public int[] TIME = ITime.secondToTime(1800);

        @Comment({"ITime to start Announced to chat before clear, [H, M, S]"})
        public int[] WARNING_TIME = ITime.secondToTime(900);

        @Comment({"Disable xp drops xp will give to players instead of dropping to the ground"})
        public boolean DISABLE_XP_DROP = true;

        @Comment({"Set to false to disable item clearing"})
        public boolean CLEAR_ITEM = true;

        @Comment({"Amount of monster with same type limit per chunk default is 5 set it to 0 to disable"})
        public int MONSTER_LIMIT = 5;

        @Comment({"Limit tiles entity can be cleared per chunk default is 100 set it to 0 to disable"})
        public int TILE_LIMIT = 100;

        @Comment({"Limit redstone actions per second default is 20 set it to 0 to disable"})
        public int REDSTON_LIMIT = 100;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID))
            ConfigManager.load(Reference.MODID, Config.Type.INSTANCE);
    }
}
