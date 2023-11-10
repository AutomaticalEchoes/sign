package com.automaticalechoes.simplesign.mixin;

import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin{
    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"),ordinal = 3)
    private boolean Flag2l(boolean flag2){
        return Utils.ShouldRenderBorder() && ClientEvents.SIGNS.size() > 0;
    }
}
