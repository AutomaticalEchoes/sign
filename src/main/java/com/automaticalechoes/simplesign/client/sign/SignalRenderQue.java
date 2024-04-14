package com.automaticalechoes.simplesign.client.sign;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.Utils;
import com.automaticalechoes.simplesign.client.render.SignalRender;
import com.automaticalechoes.simplesign.mixin.IFrustum;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;
import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class SignalRenderQue extends Utils.LimitList<ClientSignal> {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");
    public static final ResourceLocation VIEW_FACE = new ResourceLocation(SimpleSign.MODID,"textures/view_face.png");
    public SignalRenderQue(int size){
        super(size);
    }

    @Deprecated
    public void render3D(RenderLevelStageEvent event){
        if(this.isEmpty()) return;
        PoseStack poseStack = event.getPoseStack();
        Frustum frustum = event.getFrustum();
        Minecraft mc = Minecraft.getInstance();
        Camera mainCamera = mc.gameRenderer.getMainCamera();
        Vec3 upVec3 = new Vec3(mainCamera.getUpVector());
        Vec3 leftVec3 = new Vec3(mainCamera.getLeftVector());
        Vec3 viewVec3 = new Vec3(mainCamera.getLookVector());
        for (ClientSignal sign : this) {
            if (!sign.CanUse()) {
                remove(sign);
                continue;
            }

            boolean inSide = frustum.isVisible(new AABB(sign.getPointPos().x, sign.getPointPos().y, sign.getPointPos().z, sign.getPointPos().x, sign.getPointPos().y, sign.getPointPos().z));
            if(!inSide) continue;
            Vec3 renderPos = sign.getPointPos().subtract(mainCamera.getPosition());
            double length = renderPos.length();
            double upDot = renderPos.dot(upVec3);
            double leftDot = renderPos.dot(leftVec3);
            double viewDot = renderPos.dot(viewVec3);
            double angY = Math.atan(upDot / viewDot);
            double angX = Math.atan(leftDot / viewDot);

            Color color = sign.getColor();
            MutableComponent distance = Component.literal(DECIMAL_FORMAT.format(length)).append(Component.translatable("B").withStyle(ChatFormatting.GOLD));
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();


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
                poseStack.mulPose(Axis.YP.rotation((float) angX));
                poseStack.mulPose(Axis.XP.rotation((float) angY));
                SignalRender.renderItem(itemStack, poseStack, bufferSource);
                poseStack.popPose();
            }else{
                SignalRender.RenderPointTexture(poseStack, sign.getType(), mc.renderBuffers().outlineBufferSource(),-1,color);
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.scale(0.05f,0.05f,1);
            SignalRender.renderText(poseStack, distance);
            poseStack.popPose();

        }
    }


    public void render2D(RenderGuiOverlayEvent event){
        if(this.isEmpty()) return;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
//        float aspect = (float)mc.getWindow().getWidth() / (float)mc.getWindow().getHeight();
        Camera mainCamera = mc.gameRenderer.getMainCamera();
        Frustum frustum = mc.levelRenderer.getFrustum();
        IFrustum ifrustum = (IFrustum) frustum;
        Matrix4f matrix4f = ifrustum.getMatrix();
//        float Ytan = 1.0f / matrix4f.m00();
        Vec3 viewVec = new Vec3(mainCamera.getLookVector());
        for (ClientSignal sign : this) {
            if (!sign.CanUse()) {
                remove(sign);
                continue;
            }
            Vec3 renderPos = sign.getPointPos().subtract(mainCamera.getPosition());
//            boolean out = !frustum.isVisible(AABB.ofSize(sign.getPointPos(), 0, 0, 0));
            double dotView = renderPos.dot(viewVec);
            if(dotView < 0){
                Vec3 vec3 = viewVec.normalize().scale(-2 * dotView);
                renderPos = renderPos.add(vec3);
            }

            double length = renderPos.length();
            Color color = sign.getColor();
            MutableComponent distance = Component.literal(DECIMAL_FORMAT.format(length)).append(Component.translatable("B").withStyle(ChatFormatting.GOLD));

            Vector4f vector4f = new Vector4f(renderPos.toVector3f(), 1.0f);
            Vector4f transform = matrix4f.transform(vector4f);
            float w = transform.w;
            Vector4f div = transform.div(w);
            float xScale = (div.x + 1f) * 0.5f;
            float yScale = (1f - div.y) * 0.5f;
            float x = Mth.clamp(xScale, 0, 1.0F);
            if(dotView < 0) x = x > 0.5? 1.0f : 0;
            float y = Mth.clamp(yScale, 0, 1.0F);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x * mc.getWindow().getGuiScaledWidth(),  y * mc.getWindow().getGuiScaledHeight(), w);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(45.0F));
            guiGraphics.blit(SignalRender.resourceLocation(sign.getType()), - 12, - 12, 0, 0, 24, 24, 24, 24);
            guiGraphics.pose().popPose();

            guiGraphics.drawCenteredString(mc.font, distance, 0, 10, color.getRGB());
            guiGraphics.pose().popPose();
            sign.tick();
        }
    }

}
