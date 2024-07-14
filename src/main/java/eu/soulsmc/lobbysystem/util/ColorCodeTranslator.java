package eu.soulsmc.lobbysystem.util;

import org.bukkit.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorCodeTranslator {

    public static Color toRGB(char colorCode) {
        Map<Character, Color> colorMap = new HashMap<>();
        colorMap.put('0', Color.fromRGB(0, 0, 0));         // Black
        colorMap.put('1', Color.fromRGB(0, 0, 170));       // Dark Blue
        colorMap.put('2', Color.fromRGB(0, 170, 0));       // Dark Green
        colorMap.put('3', Color.fromRGB(0, 170, 170));     // Dark Aqua
        colorMap.put('4', Color.fromRGB(170, 0, 0));       // Dark Red
        colorMap.put('5', Color.fromRGB(170, 0, 170));     // Dark Purple
        colorMap.put('6', Color.fromRGB(255, 170, 0));     // Gold
        colorMap.put('7', Color.fromRGB(170, 170, 170));   // Gray
        colorMap.put('8', Color.fromRGB(85, 85, 85));      // Dark Gray
        colorMap.put('9', Color.fromRGB(85, 85, 255));     // Blue
        colorMap.put('a', Color.fromRGB(85, 255, 85));     // Green
        colorMap.put('b', Color.fromRGB(85, 255, 255));    // Aqua
        colorMap.put('c', Color.fromRGB(255, 85, 85));     // Red
        colorMap.put('d', Color.fromRGB(255, 85, 255));    // Light Purple
        colorMap.put('e', Color.fromRGB(255, 255, 85));    // Yellow
        colorMap.put('f', Color.fromRGB(255, 255, 255));   // White

        // Default to white if the color code is not found
        return colorMap.getOrDefault(colorCode, Color.WHITE);
    }
}