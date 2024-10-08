package com.portingdeadmods.moreplates.utils;

import net.minecraft.resources.ResourceLocation;

public class IngotUtil {

    public static boolean isValidIngotPlate(ResourceLocation location, boolean onlyVanilla) {
        String path = location.getPath();
        return !path.contains("_hot") && (path.startsWith("ingot_") || path.endsWith("_ingot")) && !path.contains("block") && (!onlyVanilla || "minecraft".equals(location.getNamespace()));
    }

    public static boolean isValidIngot(ResourceLocation location, boolean onlyVanilla) {
        String path = location.getPath();
        return !path.contains("_double_") && !path.contains("_hot") && (path.startsWith("ingot_") || path.endsWith("_ingot")) && !path.contains("block") && (!onlyVanilla || "minecraft".equals(location.getNamespace()));
    }

    public static String getIngotType(ResourceLocation itemId) {
        String path = itemId.getPath();
        int length = path.length();
        return path.substring(path.startsWith("ingot_")? 6 : 0, length - (path.endsWith("_ingot")? 6 : 0));
    }
}
