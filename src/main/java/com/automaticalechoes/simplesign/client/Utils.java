package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.SimpleSign;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class Utils {
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
            if(size() >= limitSize){
                poll();
            }
            return super.add(t);
        }
    }
}
