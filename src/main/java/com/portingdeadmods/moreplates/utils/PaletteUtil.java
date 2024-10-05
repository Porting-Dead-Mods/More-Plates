package com.portingdeadmods.moreplates.utils;

import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.PaletteColor;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;

public class PaletteUtil {
    public static Palette modifyPaletteExample(TextureImage input) {
        Palette originalPalette = Palette.fromImage(input);

        originalPalette.remove(originalPalette.getDarkest());

        if (originalPalette.getLuminanceSpan() < 0.2) {
            originalPalette.increaseUp();
        }

        originalPalette.increaseInner();

        originalPalette.reduceAndAverage();

        RGBColor darkestColor = originalPalette.getColorAtSlope(0.2f).rgb();
        RGBColor newColor = RGBColorUtil.modifyColor(darkestColor);

        originalPalette.add(new PaletteColor(newColor));

        return originalPalette;
    }
}
