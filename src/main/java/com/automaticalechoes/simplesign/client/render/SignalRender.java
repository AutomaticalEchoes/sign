package com.automaticalechoes.simplesign.client.render;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.Utils;
import com.automaticalechoes.simplesign.common.sign.Sign;
import com.automaticalechoes.simplesign.common.sign.Signal;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@Deprecated
public class SignalRender {
    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");
    protected static ResourceLocation RESOURCE_DEFAULT = new ResourceLocation(SimpleSign.MODID,"textures/point_render/default.png");
    protected static ResourceLocation RESOURCE_CARE = new ResourceLocation(SimpleSign.MODID,"textures/point_render/care.png");
    protected static ResourceLocation RESOURCE_FOCUS = new ResourceLocation(SimpleSign.MODID,"textures/point_render/focus.png");
    protected static ResourceLocation RESOURCE_QUESTION = new ResourceLocation(SimpleSign.MODID,"textures/point_render/question.png");
    protected static Minecraft minecraft;
    protected static ItemRenderer itemRenderer;
    protected static boolean initialize = false;

    public static void init(){
        minecraft = Minecraft.getInstance();
        initialize = true;
        itemRenderer = minecraft.getItemRenderer();
    }

    public static boolean isInitialize() {
        return initialize;
    }

    public static void renderText(PoseStack poseStack, Component distanceMessage){
        int width = minecraft.font.width(distanceMessage);
        minecraft.font.drawInBatch(distanceMessage, - width / 2F , 10,-1,false,poseStack.last().pose(),minecraft.renderBuffers().bufferSource(), Font.DisplayMode.SEE_THROUGH,0,15728880);
    }

    public static void RenderPointTexture(PoseStack p_114083_, Signal.Type renderType, MultiBufferSource p_114084_, int p_114085_, Color color) {
        p_114083_.pushPose();
        p_114083_.mulPose(Axis.ZP.rotationDegrees(45.0F));
        PoseStack.Pose posestack$pose = p_114083_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RenderType.textIntensitySeeThrough(resourceLocation(renderType)));
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0.0F, 0.0F, 0.0F, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0.0F, 1.0F, 0.0F, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1.0F, 1.0F, 1.0F, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1.0F, 0.0F, 1.0F, color);
        p_114083_.popPose();
    }

    private static void vertex(VertexConsumer p_254095_, Matrix4f p_254477_, Matrix3f p_253948_, int p_253829_, float x, float y, float uvx, float uvy,Color color) {
        p_254095_.vertex(p_254477_, x - 0.5F, y - 0.5F, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_253829_).normal(p_253948_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void renderItem(ItemStack p_115144_,  PoseStack p_115147_, MultiBufferSource p_115148_) {
        BakedModel model = itemRenderer.getModel(p_115144_,null,null,0);
        if (!p_115144_.isEmpty()) {
            p_115147_.pushPose();
            if (p_115144_.is(Items.TRIDENT)) {
                model = itemRenderer.getItemModelShaper().getModelManager().getModel(TRIDENT_MODEL);
            } else if (p_115144_.is(Items.SPYGLASS)) {
                model = itemRenderer.getItemModelShaper().getModelManager().getModel(SPYGLASS_MODEL);
            }
            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(p_115147_, model, ItemDisplayContext.GUI, false);
            p_115147_.translate(- 0.5F, 0, - 0.5F);
            if(!model.usesBlockLight())Lighting.setupForFlatItems();
            RenderType renderType = Utils.getFallbackItemRenderType(p_115144_, model, true);
            VertexConsumer vertexconsumer = p_115148_.getBuffer(renderType);
            itemRenderer.renderModelLists(model, p_115144_, 15728880, OverlayTexture.NO_OVERLAY, p_115147_, vertexconsumer);
            if(!model.usesBlockLight()) Lighting.setupFor3DItems();
            p_115147_.popPose();
        }
    }


    public static ResourceLocation resourceLocation(Signal.Type renderType) {
        return switch (renderType){
            case DEFAULT -> RESOURCE_DEFAULT;
            case FOCUS -> RESOURCE_FOCUS;
            case CARE -> RESOURCE_CARE;
            case QUESTION -> RESOURCE_QUESTION;
        };
    }



}
