package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.client.render.IRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedList;

@OnlyIn(Dist.CLIENT)
public class Utils {
    public static final RandomSource RANDOM = RandomSource.create();
    @Nullable
    public static HitResult IPick(float p_109088_) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity != null && minecraft.level != null) {
            minecraft.getProfiler().push("pick");
            minecraft.crosshairPickEntity = null;
            double d0 =  200.0F;
           // Note - MC-76493 - We must validate players cannot click-through objects.
            HitResult hitResult = entity.pick(d0, p_109088_, false); // Run pick() with the max of the two, so we can prevent click-through.
            Vec3 vec3 = entity.getEyePosition(p_109088_);
            if(hitResult.getType() == HitResult.Type.BLOCK){
                d0 = hitResult.getLocation().distanceToSqr(vec3);
            }

            Vec3 vec31 = entity.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);

            AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb, (p_234237_) -> !p_234237_.isSpectator(), d0);
//        scoping ? PickEntity(entity,vec31) :     EntityHitResult entityhitresult = PickEntity(entity, vec31,aabb);
            if (entityhitresult != null) {
                Entity entity1 = entityhitresult.getEntity();
                hitResult = entityhitresult;
                if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                    minecraft.crosshairPickEntity = entity1;
                }
            }

            minecraft.getProfiler().pop();
            return hitResult;
        }
        return null;
    }

    public static class LimitList<T> extends LinkedList<T>{
        private final int limitSize;
        public LimitList(int size){
            this.limitSize = size;
        }

        @Override
        public boolean add(T t) {
            if(this.contains(t))
            if(size() >= limitSize){
                poll();
            }
            return super.add(t);
        }
    }

    public static boolean ShouldEntityGlow(){
        return ClientConfig.SHOULD_ENTITY_GLOW.get();
    }
    public static boolean ShouldShowDetail(){
        return ClientConfig.SHOULD_SHOW_DETAIL.get();
    }

    @NotNull
    public static RenderType getFallbackItemRenderType(ItemStack stack, BakedModel model, boolean cull) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            var renderTypes = model.getRenderTypes(blockItem.getBlock().defaultBlockState(), RandomSource.create(42), ModelData.EMPTY);
            if (renderTypes.contains(RenderType.translucent())){
                return getEntityRenderType(RenderType.translucent(), cull);
            }else {
                return IRenderType.entityCutOut;
            }
        }else {
            return cull ? IRenderType.translucentCullBlock : IRenderType.translucentItem;
        }
//
    }

    public static RenderType getEntityRenderType(RenderType chunkRenderType, boolean cull)
    {
        if (chunkRenderType != RenderType.translucent())
            return IRenderType.entityCutOut;
        return cull || !Minecraft.useShaderTransparency() ? IRenderType.translucentCullBlock : IRenderType.translucentItem;
    }

}
