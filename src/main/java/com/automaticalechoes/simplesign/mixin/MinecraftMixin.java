package com.automaticalechoes.simplesign.mixin;

import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.Utils;
import com.automaticalechoes.simplesign.common.sign.Sign;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "shouldEntityAppearGlowing",at = @At("RETURN"),cancellable = true)
    public void shouldEntityAppearGlowing(Entity p_91315_, CallbackInfoReturnable<Boolean> cir){
        if(Utils.ShouldRenderBorder()){
            for(Sign sign :ClientEvents.SIGNS){
                if(sign.equals(p_91315_)) cir.setReturnValue(true);
            }
        }
    }
}
