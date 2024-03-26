package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.client.render.SignalRender;
import com.automaticalechoes.simplesign.common.sign.Sign;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.awt.*;
import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class SignManager extends Utils.LimitList<Sign> {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");
    public SignManager(int size){
        super(size);
    }

    public void renderTick(RenderLevelStageEvent event){
        if(this.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
        Frustum frustum = event.getFrustum();
        Camera mainCamera = mc.gameRenderer.getMainCamera();
        Camera.NearPlane nearPlane = mainCamera.getNearPlane();
        PoseStack poseStack = event.getPoseStack();
        Vec3 upVec3 = new Vec3(mainCamera.getUpVector());
        Vec3 leftVec3 = new Vec3(mainCamera.getLeftVector());
        Vec3 viewVec3 = new Vec3(mainCamera.getLookVector());
        double specific = (double)mc.getWindow().getWidth() / (double)mc.getWindow().getHeight();
        double xLength = Math.tan((double)((float) mc.options.fov().get() * ((float)Math.PI / 180F)) / 2.0D) * (double)0.05F;
        double yLength = specific * xLength;
        for (Sign sign : this) {
            if(!sign.CanUse()){
                remove(sign);
                continue;
            }
            boolean inSide = frustum.isVisible(new AABB(sign.getPointPos().x, sign.getPointPos().y, sign.getPointPos().z, sign.getPointPos().x,sign.getPointPos().y,sign.getPointPos().z));
            Vec3 renderPos = sign.getPointPos().subtract(mainCamera.getPosition());
            double length = renderPos.length();
            double upDot = renderPos.dot(upVec3);
            double leftDot = renderPos.dot(leftVec3);
            double viewDot = renderPos.dot(viewVec3);
            double cosY = upDot / Math.sqrt(upDot * upDot + viewDot * viewDot);
            double cosX = leftDot / Math.sqrt(leftDot * leftDot + viewDot * viewDot);
//            boolean inSide = viewDot > 0 && Math.abs(cosX) < fovXSin && Math.abs(cosY) < fovYSin;

            Color color = sign.getColor();
            MutableComponent distance = Component.literal(DECIMAL_FORMAT.format(length)).append(Component.translatable("B").withStyle(ChatFormatting.GOLD));
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            if(inSide){
                float cos = (float) (viewDot / length);
                float scale = (float) (0.1F * length * cos);
                scale = mc.player.isScoping() ? mc.player.getFieldOfViewModifier() * scale : scale;
                poseStack.pushPose();
                poseStack.translate(renderPos.x, renderPos.y, renderPos.z);
                poseStack.scale(scale, scale, scale);
                poseStack.mulPose(mainCamera.rotation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                ItemStack itemStack = sign.getItemStack();

                if(itemStack != null && Utils.ShouldShowDetail()){
                    poseStack.pushPose();
                    poseStack.mulPose(Axis.YP.rotation((float) cosX));
                    poseStack.mulPose(Axis.XP.rotation((float) cosY));
                    SignalRender.renderItem(itemStack, poseStack, bufferSource);
                    poseStack.popPose();
                }else{
                    SignalRender.RenderPointTexture(poseStack, mc.renderBuffers().outlineBufferSource(),-1,color);
                }

                poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                poseStack.scale(0.05f,0.05f,1);
                SignalRender.renderText(poseStack, distance);
                poseStack.popPose();

            }else {
                double xScale = - Mth.clamp(leftDot * 0.05 / (xLength * viewDot), - 1, 1);
                double yScale = Mth.clamp(upDot * 0.05 / (yLength * viewDot), -1, 1);
                renderPos = nearPlane.getPointOnPlane((float) xScale,(float) yScale);
                poseStack.pushPose();
                poseStack.translate(20 * renderPos.x, 20 * renderPos.y, 20 * renderPos.z);
                poseStack.mulPose(mainCamera.rotation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                SignalRender.RenderPointTexture(poseStack, mc.renderBuffers().outlineBufferSource(),-1,color);
                poseStack.popPose();
            }
            bufferSource.endBatch();
        }
    }

}
