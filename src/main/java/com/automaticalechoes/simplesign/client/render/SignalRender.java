package com.automaticalechoes.simplesign.client.render;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.Utils;
import com.automaticalechoes.simplesign.common.sign.BlockSign;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.awt.*;
import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class SignalRender {
    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");
    public static final  DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");
    protected static ResourceLocation RESOURCE = new ResourceLocation(SimpleSign.MODID,"textures/point.png");
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

    public static void RenderMark(com.automaticalechoes.simplesign.common.sign.Sign mark, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix){
        Vec3 pos = mark.getPointPos();
        Vec3 subtract = pos.subtract(camera.getPosition());
        double length = subtract.length();
        Vec3 pointPos = length > 2 ? subtract.normalize().scale(2.0F) : subtract;
        MutableComponent distance = Component.literal(DECIMAL_FORMAT.format(length)).append(Component.translatable("B").withStyle(ChatFormatting.GOLD));
        RenderPoint(pointPos, poseStack, camera, distance, mark.getColor(), minecraft.player.isScoping() ? minecraft.player.getFieldOfViewModifier() * 0.2F : 0.2F,projectionMatrix, mark.getItemStack());
//        if(!Utils.ShouldRenderBorder() || length > 26) return;
//        if(mark instanceof BlockSign blockMark && Utils.ShouldRenderBorder()){
//            BlockState blockState = minecraft.level.getBlockState(blockMark.getBlockPos());
//            if(!blockState.isAir())
//                RenderBlock(blockState,blockMark.getBlockPos(),poseStack,camera);
//        }
        minecraft.renderBuffers().bufferSource().endBatch();
    }

//    public static void RenderBlock(BlockState blockState,BlockPos pos, PoseStack poseStack, Camera camera){
//        Vec3 position = camera.getPosition();
//        poseStack.pushPose();
//        poseStack.translate(pos.getX() - position.x - 0.005F,pos.getY() - position.y- 0.005F,pos.getZ() - position.z - 0.005F);
//        poseStack.scale(1.01F,1.01F,1.01F);
//        minecraft.getBlockRenderer().renderSingleBlock(blockState,poseStack,minecraft.renderBuffers().outlineBufferSource(),15728880,OverlayTexture.NO_OVERLAY);
////        model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(TextureAtlas.LOCATION_BLOCKS)),0,OverlayTexture.pack(0,10),1.0F,1.0F,1.0F,0.15F);
//        poseStack.popPose();
//    }

    public static void RenderPoint(Vec3 pointPos, PoseStack poseStack, Camera camera, Component distanceMessage,Color pointColor ,float scale, Matrix4f projectionMatrix, @Nullable ItemStack itemStack){
        poseStack.pushPose();
        poseStack.translate(pointPos.x, pointPos.y , pointPos.z);
        poseStack.scale(scale,scale,scale);
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        if(itemStack != null && Utils.ShouldShowDetail()){
            renderItem(itemStack, poseStack, minecraft.renderBuffers().bufferSource());
        }else{
            RenderPointTexture(poseStack, minecraft.renderBuffers().outlineBufferSource(),-1,pointColor);
        }
        int width = minecraft.font.width(distanceMessage);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.scale(0.05f,0.05f,1);
        minecraft.font.drawInBatch(distanceMessage,- width / 2F ,10,-1,false,poseStack.last().pose(),minecraft.renderBuffers().bufferSource(), Font.DisplayMode.SEE_THROUGH,0,15728880);
        poseStack.popPose();
    }

    public static void RenderPointTexture(PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_, Color color) {
        p_114083_.pushPose();
        p_114083_.mulPose(Axis.ZP.rotationDegrees(45.0F));
        PoseStack.Pose posestack$pose = p_114083_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RenderType.textIntensitySeeThrough(RESOURCE));
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0.0F, 0.0F, 0.0F, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0.0F, 1.0F, 0.0F, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1.0F, 1.0F, 1.0F, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1.0F, 0.0F, 1.0F, color);
        p_114083_.popPose();
    }

    private static void vertex(VertexConsumer p_254095_, Matrix4f p_254477_, Matrix3f p_253948_, int p_253829_, float x, float y, float uvx, float uvy,Color color) {
        p_254095_.vertex(p_254477_, x- 0.5F, y - 0.5F, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_253829_).normal(p_253948_, 0.0F, 1.0F, 0.0F).endVertex();
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


}
