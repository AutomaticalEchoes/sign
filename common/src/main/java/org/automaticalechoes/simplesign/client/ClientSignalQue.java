package org.automaticalechoes.simplesign.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.automaticalechoes.simplesign.client.render.SignalRender;
import org.automaticalechoes.simplesign.mixin.IFrustum;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Iterator;
import java.util.LinkedList;

public class ClientSignalQue extends LinkedList<ClientSign> {
    private final int limitSize;
    public ClientSignalQue(int size){
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

    public void setup(GuiGraphics guiGraphics){
        if(this.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
        Camera mainCamera = mc.gameRenderer.getMainCamera();
        Frustum frustum = ((ILevelRender)mc.levelRenderer).getFrustum();
        IFrustum ifrustum = (IFrustum) frustum;
        Matrix4f matrix4f = ifrustum.getMatrix();
        Vec3 viewVec = new Vec3(mainCamera.getLookVector());
        Iterator<ClientSign> iterator = iterator();
        while (iterator.hasNext()) {
            ClientSign sign = iterator.next();
            if (!sign.CanUse()) {
                iterator.remove();
                continue;
            }
            Vec3 renderPos = sign.getPointPos().subtract(mainCamera.getPosition());
            double dotView = renderPos.dot(viewVec);
            if(dotView < 0){
                Vec3 vec3 = viewVec.normalize().scale(-2 * dotView);
                renderPos = renderPos.add(vec3);
            }
            Vector4f vector4f = new Vector4f(renderPos.toVector3f(), 1.0f);
            Vector4f transform = matrix4f.transform(vector4f);
            float w = transform.w;
            Vector4f div = transform.div(w);
            float xScale = (div.x + 1f) * 0.5f;
            float yScale = (1f - div.y) * 0.5f;
            float x = Mth.clamp(xScale, 0, 1.0F);
            if(dotView < 0) x = x > 0.5? 1.0f : 0;
            float y = Mth.clamp(yScale, 0, 1.0F);
            sign.setupRenderPos(x, y, w, renderPos.length());
            sign.tick();
            SignalRender.render2D(guiGraphics, sign);
        }
    }

}
