package automaticalechoes.simplesign.simplesign.mixin;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "renderHotbar", at = {@At("RETURN")})
    private void renderHotbar(float f, GuiGraphics guiGraphics, CallbackInfo ci) {
        SimpleSign.Client.MARK_RENDER.render2D(guiGraphics);
    }
}
