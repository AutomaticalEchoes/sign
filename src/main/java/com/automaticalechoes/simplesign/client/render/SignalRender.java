package com.automaticalechoes.simplesign.client.render;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.common.sign.BlockSign;
import com.automaticalechoes.simplesign.register.BlockRegister;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class SignalRender {
    public static final  DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");
//    protected static final PoseStack poseStack = new PoseStack();
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SimpleSign.MODID,"textures/point.png");
    private static final RenderType RENDER_TYPE = RenderType.textSeeThrough(TEXTURE_LOCATION);
    protected static BlockRenderDispatcher blockRenderDispatcher;
    protected static ResourceLocation RESOURCE = new ResourceLocation(SimpleSign.MODID,"textures/block/mark_block.png");
    protected static Minecraft minecraft;
    protected static BlockState fakeBlock;
    protected static MultiBufferSource bufferSource;
    protected static Matrix4f IMatrix4f;
    protected static boolean initialize = false;

    public static void init(){
        minecraft = Minecraft.getInstance();
        blockRenderDispatcher = minecraft.getBlockRenderer();
        fakeBlock = BlockRegister.MARK_BLOCK.get().defaultBlockState();
        bufferSource = minecraft.renderBuffers().bufferSource();
        initialize = true;
    }

    public static boolean isInitialize() {
        return initialize;
    }

    public static boolean isReady(){
        return IMatrix4f != null;
    }

    public static void initProjection(double fov,double tick){
        PoseStack posestack = new PoseStack();
        posestack.mulPoseMatrix(minecraft.gameRenderer.getProjectionMatrix(fov));
//        float f = minecraft.options.screenEffectScale().get().floatValue();
//        float f1 = Mth.lerp(1.0F, minecraft.player.oSpinningEffectIntensity, minecraft.player.spinningEffectIntensity) * f * f;
//        if (f1 > 0.0F) {
//            int i = minecraft.player.hasEffect(MobEffects.CONFUSION) ? 7 : 20;
//            float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
//            f2 *= f2;
//            Axis axis = Axis.of(new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F));
//            posestack.mulPose(axis.rotationDegrees(((float)tick + 1.0F) * (float)i));
//            posestack.scale(1.0F / f2, 1.0F, 1.0F);
//            float f3 = -((float)tick + 1.0F) * (float)i;
//            posestack.mulPose(axis.rotationDegrees(f3));
//        }
        IMatrix4f = posestack.last().pose();
    }

    public static void RenderMark(com.automaticalechoes.simplesign.common.sign.Sign mark, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix){
        Vec3 pos = mark.getPointPos();
        Vec3 subtract = pos.subtract(camera.getPosition());
        double length = subtract.length();
        Vec3 pointPos = length > 2 ? subtract.normalize().multiply(2.0D,2.0D,2.0D) : subtract;
        MutableComponent distance = Component.literal(DECIMAL_FORMAT.format(length)).append(Component.translatable("B").withStyle(ChatFormatting.GOLD));
        RenderPoint(pointPos, poseStack, camera, distance, mark.getColor(), minecraft.player.isScoping() ? minecraft.player.getFieldOfViewModifier() : 1.0F,projectionMatrix);
        if(length > 24) return;
        if(mark instanceof BlockSign blockMark){
            RenderBlock(blockMark.getBlockPos(),poseStack,camera);
        }
    }

    public static void RenderBlock(BlockPos pos, PoseStack poseStack, Camera camera){
        Vec3 position = camera.getPosition();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        poseStack.pushPose();
        poseStack.translate(pos.getX() - position.x,pos.getY() - position.y,pos.getZ() - position.z);
        blockRenderDispatcher.renderSingleBlock(fakeBlock,poseStack,bufferSource,15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.outline(RESOURCE));
        poseStack.popPose();
    }

    public static void RenderPoint(Vec3 pointPos, PoseStack poseStack, Camera camera, Component distanceMessage,Color pointColor ,float scale, Matrix4f projectionMatrix){
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(IMatrix4f, VertexSorting.DISTANCE_TO_ORIGIN);
        poseStack.pushPose();
        poseStack.translate(pointPos.x, pointPos.y, pointPos.z);
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        RenderPointTexture(poseStack,bufferSource,-1,pointColor,scale);

        int width = minecraft.font.width(distanceMessage);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.scale(scale * 0.01F,scale * 0.01F,1.0F);
        minecraft.font.drawInBatch(distanceMessage,- width / 2F ,10,-1,false,poseStack.last().pose(),bufferSource, Font.DisplayMode.SEE_THROUGH,0,15728880);
        poseStack.popPose();

        poseStack.popPose();
        RenderSystem.restoreProjectionMatrix();
    }

    public static void RenderPointTexture(PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_, Color color, float scale) {
        p_114083_.pushPose();
//        RenderSystem.backupProjectionMatrix();
        p_114083_.scale(scale * 0.2F, scale * 0.2F,0.1F);
        p_114083_.translate(scale * 0.15F , - scale * 0.2F, 0);
        p_114083_.mulPose(Axis.ZP.rotationDegrees(45.0F));
        PoseStack.Pose posestack$pose = p_114083_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0, 0, 1, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0, 1, 1, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1, 1, 0, color);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1, 0, 0, color);
//        RenderSystem.restoreProjectionMatrix();
        p_114083_.popPose();
    }

    private static void vertex(VertexConsumer p_254095_, Matrix4f p_254477_, Matrix3f p_253948_, int p_253829_, float p_253995_, int p_254031_, int p_253641_, int p_254243_,Color color) {
        p_254095_.vertex(p_254477_, p_253995_ - 0.5F, (float)p_254031_ - 0.25F, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv((float)p_253641_, (float)p_254243_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_253829_).normal(p_253948_, 0.0F, 1.0F, 0.0F).endVertex();
    }


}
