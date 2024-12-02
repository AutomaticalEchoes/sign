package org.automaticalechoes.simplesign.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.automaticalechoes.simplesign.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "renderItemHotbar", at = {@At("RETURN")})
    private void renderHotbar(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker, CallbackInfo ci) {
        Constants.Client.CLIENT_SIGNS.setup(pGuiGraphics);
    }
}
