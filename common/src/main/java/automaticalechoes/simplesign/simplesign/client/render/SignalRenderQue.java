package automaticalechoes.simplesign.simplesign.client.render;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import automaticalechoes.simplesign.simplesign.api.Config;
import automaticalechoes.simplesign.simplesign.client.ClientSign;
import automaticalechoes.simplesign.simplesign.client.ILevelRender;
import automaticalechoes.simplesign.simplesign.client.Utils;
import automaticalechoes.simplesign.simplesign.mixin.IFrustum;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

@Environment(EnvType.CLIENT)
public class SignalRenderQue extends LinkedList<ClientSign> {
    public static ResourceLocation RESOURCE_DEFAULT = new ResourceLocation(SimpleSign.MOD_ID,"textures/point_render/default.png");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");
    public static final ResourceLocation VIEW_FACE = new ResourceLocation(SimpleSign.MOD_ID,"textures/view_face.png");

    private final int limitSize;
    public SignalRenderQue(int size){
        this.limitSize = size;
    }

    @Override
    public boolean add(ClientSign clientSign) {
        int i = this.indexOf(clientSign);

        if(i >= 0){
            ClientSign clientSign1 = this.get(i);
            if (clientSign1.getLifecycle() == -1) return true;
            clientSign1.setLifecycle(clientSign.getLifecycle());
            return true;
        }

        if(this.size() >= limitSize) this.poll();
        return super.add(clientSign);
    }


    public void render2D(GuiGraphics guiGraphics){
        if(this.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
//        float aspect = (float)mc.getWindow().getWidth() / (float)mc.getWindow().getHeight();
        Camera mainCamera = mc.gameRenderer.getMainCamera();
        Frustum frustum = ((ILevelRender)mc.levelRenderer).getFrustum();
        IFrustum ifrustum = (IFrustum) frustum;
        Matrix4f matrix4f = ifrustum.getMatrix();
//        float Ytan = 1.0f / matrix4f.m00();
        Vec3 viewVec = new Vec3(mainCamera.getLookVector());
        for (ClientSign sign : this) {
            if (!sign.CanUse()) {
                remove(sign);
                continue;
            }
            ItemStack itemStack = sign.getItemStack();
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

            if(itemStack != null && Config.getSsi$shouldEntityGlow()){
                guiGraphics.renderItem(itemStack, - 8,  - 8);
            }else{
                guiGraphics.pose().pushPose();
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(45.0F));
                guiGraphics.blit(RESOURCE_DEFAULT, - 12, - 12, 0, 0, 24, 24, 24, 24);
                guiGraphics.pose().popPose();
            }

            FormattedCharSequence formattedcharsequence = distance.getVisualOrderText();
            guiGraphics.drawString(mc.font, formattedcharsequence, - mc.font.width(formattedcharsequence) / 2, 10, color.getRGB(),false);
            guiGraphics.pose().popPose();
            sign.tick();
        }
    }

}
