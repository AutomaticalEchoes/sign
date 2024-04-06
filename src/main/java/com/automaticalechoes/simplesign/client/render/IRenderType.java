package com.automaticalechoes.simplesign.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class IRenderType extends RenderType {
    private static final Function<ResourceLocation, RenderType> ITEM_ENTITY_TRANSLUCENT_CULL_SEE_T = Util.memoize((p_286155_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286155_, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOutputState(ITEM_ENTITY_TARGET)
                .setOverlayState(OVERLAY)
                .setDepthTestState(NO_DEPTH_TEST)
                .createCompositeState(true);
        return RenderType.create("item_entity_translucent_see_t", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });

    private static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT_CULL_SEE_T = Util.memoize((p_286165_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286165_, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setDepthTestState(NO_DEPTH_TEST)
                .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return create("entity_translucent_cull_see_t", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });

    private static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT_SEE_T = Util.memoize((p_286173_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_286173_, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setDepthTestState(NO_DEPTH_TEST)
                .setOverlayState(OVERLAY).createCompositeState(true);
        return create("entity_cutout_see_t", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    });

    public static final RenderType translucentCullBlock = entityTranslucentCullSeeT(TextureAtlas.LOCATION_BLOCKS);
    public static final RenderType translucentItem = itemEntityTranslucentCullSeeT(TextureAtlas.LOCATION_BLOCKS);
    public static final RenderType entityCutOut = entityCutoutSeeT(TextureAtlas.LOCATION_BLOCKS);

    public static RenderType itemEntityTranslucentCullSeeT(ResourceLocation resourceLocation){
        return ITEM_ENTITY_TRANSLUCENT_CULL_SEE_T.apply(resourceLocation);
    }
    public static RenderType entityTranslucentCullSeeT(ResourceLocation p_110471_) {
        return ENTITY_TRANSLUCENT_CULL_SEE_T.apply(p_110471_);
    }

    public static RenderType entityCutoutSeeT(ResourceLocation p_110453_) {
        return ENTITY_CUTOUT_SEE_T.apply(p_110453_);
    }
    public IRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
}
