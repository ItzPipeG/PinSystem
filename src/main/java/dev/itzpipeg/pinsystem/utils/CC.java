package dev.itzpipeg.pinsystem.utils;

import net.md_5.bungee.api.ChatColor;

public class CC {
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
