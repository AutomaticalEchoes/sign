package com.automaticalechoes.simplesign.mixin;

import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.Utils;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin{
//    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"),ordinal = 3)
//    private boolean Flag2l(boolean flag2){
//        return ClientEvents.SIGNS.size() > 0 &&  Utils.ShouldEntityGlow();
//    }
}
