package com.portingdeadmods.moreplates.utils;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TextureUtils {
    public static TextureImage createRecoloredTexture(ResourceManager manager,ResourceLocation baseTexture, ResourceLocation ingotTexture) {

        try (TextureImage base_texture = TextureImage.open(manager, baseTexture);
             TextureImage ingot = TextureImage.open(manager, ingotTexture)) {

            Respriter respriter = Respriter.of(base_texture);

            Palette ingotPalette = PaletteUtil.modifyPaletteExample(ingot);

            return respriter.recolor(ingotPalette);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
