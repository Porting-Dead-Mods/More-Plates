package com.portingdeadmods.moreplates.utils;

import net.mehvahdjukaar.moonlight.api.util.math.colors.LABColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;

public class RGBColorUtil {
    public static RGBColor modifyColor(RGBColor color){
        LABColor lab = color.asLAB();
        LABColor pureRed = new RGBColor(1,0,0,1).asLAB();

        // lab.mixWith(pureRed, 0.2f);

        lab.withLuminance(0.4f);
        return lab.asRGB();
    }
}
