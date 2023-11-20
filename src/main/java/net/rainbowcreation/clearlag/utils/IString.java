package net.rainbowcreation.clearlag.utils;

public class IString {
    public static String genHeader(String name) {
        StringBuilder header = new StringBuilder();
        String c = "#";
        int len = name.length();
        int x = 40; // prefix
        int y = 6; // suffix
        int z = 86-x-y;
        int tmp = z - len; // pre body
        for (int i = 0; i < x; i++) // do prefix
            header.append(c);
        for (int i = 0; i < tmp; i++) // do pre body
            header.append(c);
        header.append(name); // do body
        for (int i = 0; i < y; i++) // do suffix
            header.append(c);
        return header.toString();
    }
}