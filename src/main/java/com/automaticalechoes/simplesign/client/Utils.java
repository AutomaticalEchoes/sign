package com.automaticalechoes.simplesign.client;

import net.minecraft.client.Minecraft;
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
            double d0 = (double) minecraft.gameMode.getPickRange();
            double entityReach = minecraft.player.getEntityReach(); // Note - MC-76493 - We must validate players cannot click-through objects.
            HitResult hitResult = entity.pick(Math.max(d0, entityReach), p_109088_, false); // Run pick() with the max of the two, so we can prevent click-through.
            Vec3 vec3 = entity.getEyePosition(p_109088_);
            boolean flag = false;
            int i = 3;
            double d1 = d0;
            if (minecraft.gameMode.hasFarPickRange()) {
                d1 = 6.0D;
                d0 = d1;
            } else {
                if (d0 > 3.0D) {
                    flag = true;
                }

                d0 = d0;
            }
            d0 = d1 = Math.max(d0, entityReach); // Pick entities with the max of both for the same reason.

            d1 *= d1;
            // If we picked a block, we need to ignore entities past that block. Added != MISS check to not truncate on failed picks.
            // Also fixes MC-250858
            if (hitResult.getType() != HitResult.Type.MISS) {
                d1 = hitResult.getLocation().distanceToSqr(vec3);
                double blockReach = minecraft.player.getBlockReach();
                // Discard the result as a miss if it is outside the block reach.
                if (d1 > blockReach * blockReach) {
                    Vec3 pos = hitResult.getLocation();
                    hitResult = BlockHitResult.miss(pos, Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(pos));
                }
            }

            Vec3 vec31 = entity.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
            float f = 1.0F;
            AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb, (p_234237_) -> {
                return !p_234237_.isSpectator();
            }, d1);
            if (entityhitresult != null) {
                Entity entity1 = entityhitresult.getEntity();
                Vec3 vec33 = entityhitresult.getLocation();
                double d2 = vec3.distanceToSqr(vec33);
                if (d2 > d1 || d2 > entityReach * entityReach) { // Discard if the result is behind a block, or past the entity reach max. The var "flag" no longer has a use.
                    hitResult = BlockHitResult.miss(vec33, Direction.getNearest(vec31.x, vec31.y, vec31.z), BlockPos.containing(vec33));
                } else if (d2 < d1) {
                    hitResult = entityhitresult;
                    if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                        minecraft.crosshairPickEntity = entity1;
                    }
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
