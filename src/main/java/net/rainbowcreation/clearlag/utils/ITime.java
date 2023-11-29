package net.rainbowcreation.clearlag.utils;

import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ITime {
    public static int TIME;
    public static int WARNING_TIME;
    public static List<Integer> WARNING_TIME_LIST = new ArrayList<>();

    public static int[] getCurrentTime() {
        int[] lst = new int[3];

        ZonedDateTime time = ZonedDateTime.now();
        lst[0] = time.getHour();
        lst[1] = time.getMinute();
        lst[2] = time.getSecond();
        return lst;
    }

    public static int getTimeInSecond(int[] time) {
        return ((time[0]*3600)+(time[1]*60)+time[2]);
    }

    public static int[] secondToTime(int seconds) {
        int[] time = new int[3];

        time[0] = seconds/3600;
        seconds%=3600;
        time[1] = seconds/60;
        seconds%=60;
        time[2] = seconds;
        return time;
    }

    public static int[] getSubstract(int[] x, int[] y) {
        return secondToTime(getSubstractInSecond(x, y));
    }

    public static int getSubstractInSecond(int[] x, int[] y) {
        int ix = getTimeInSecond(x);
        int iy = getTimeInSecond(y);
        if (ix >= iy)
            return ix - iy;
        int[] time = new int[3];
        time[0] = 24;
        return getTimeInSecond(time) + ix - iy;
    }

    public static String[] timeToString(int[] time) {
        String[] to_return = new String[3];
        for (int i = 0; i<3; i++) {
            to_return[i] = String.valueOf(time[i]);
            if (to_return[i].length() == 1)
                to_return[i] = "0" + to_return[i];
        }
        return to_return;
    }
    public static Boolean alert(int timeRemaining, String prefix, String str, PlayerList playerList) {
        if (!WARNING_TIME_LIST.contains(timeRemaining))
            return false;
        int[] lst = ITime.secondToTime(timeRemaining);
        TextComponentString text = new TextComponentString(TextFormatting.BOLD + "[" + prefix + "] " + TextFormatting.RESET + str + " :");
        if (lst[0] > 0)
            text.appendSibling(new TextComponentString(" " + TextFormatting.RED + lst[0] + TextFormatting.RESET + " hours"));
        if (lst[1] > 0)
            text.appendSibling(new TextComponentString(" " + TextFormatting.RED + lst[1] + TextFormatting.RESET + " minutes"));
        if (lst[2] > 0)
            text.appendSibling(new TextComponentString(" " + TextFormatting.RED + lst[2] + TextFormatting.RESET + " seconds"));
        text.appendText("!!.");
        playerList.sendMessage(text);
        return true;
    }

    public static Boolean alert(int[] timeTarget, String prefix, String str, PlayerList playerList) {
        int timeRemaining = getSubstractInSecond(timeTarget, getCurrentTime());
        if (!WARNING_TIME_LIST.contains(timeRemaining))
            return false;
        int[] lst = ITime.secondToTime(timeRemaining);
        TextComponentString text = new TextComponentString(TextFormatting.BOLD + "[" + prefix + "]  " + TextFormatting.RESET + str + " :");
        if (lst[0] > 0)
            text.appendSibling(new TextComponentString(" " + TextFormatting.RED + lst[0] + TextFormatting.RESET + " hours"));
        if (lst[1] > 0)
            text.appendSibling(new TextComponentString(" " + TextFormatting.RED + lst[1] + TextFormatting.RESET + " minutes"));
        if (lst[2] > 0)
            text.appendSibling(new TextComponentString(" " + TextFormatting.RED + lst[2] + TextFormatting.RESET + " seconds"));
        text.appendText("!!.");
        playerList.sendMessage(text);
        return true;
    }
}