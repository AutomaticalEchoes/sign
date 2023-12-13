package com.automaticalechoes.simplesign.mixin;

import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin{
    @Shadow private int ticks;

    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"),ordinal = 4)
    private boolean Flag2l(boolean flag2){
        return  ClientEvents.SIGNS.size() > 0 &&  Utils.ShouldEntityGlow();
    }

    @Inject(method = "renderLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 1),
    locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void RenderLevel(PoseStack p_109600_, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_109607_, CallbackInfo ci, ProfilerFiller profilerfiller, boolean flag, Vec3 vec3, double d0, double d1, double d2, Matrix4f matrix4f, boolean flag1, Frustum frustum){
        if(ClientEvents.AFTER_BLOCK_ENTITIES != null)
        ForgeHooksClient.dispatchRenderStage(ClientEvents.AFTER_BLOCK_ENTITIES, ((LevelRenderer)(Object)this), p_109600_, p_109607_, ticks, p_109604_, frustum);
    }

}
