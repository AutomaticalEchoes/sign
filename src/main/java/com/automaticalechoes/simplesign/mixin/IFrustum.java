package com.automaticalechoes.simplesign.mixin;

import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Frustum.class)
public interface IFrustum {
    @Accessor
    Matrix4f getMatrix();
}
