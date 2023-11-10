package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.SimpleSign;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import java.awt.*;
import java.util.UUID;

public class EntitySign implements Sign {
    public static final String UUID = "uuid";
    public static final String BLOCK_POS = "block_pos";
    @Nullable
    protected Entity entity;
    protected final BlockPos pos;
    protected final UUID uuid;

    public EntitySign(UUID uuid, BlockPos pos){
        this.uuid = uuid;
        this.pos = pos;
    }

    public EntitySign(CompoundTag compoundTag){
        this.uuid = compoundTag.getUUID(UUID);
        this.pos = BlockPos.of(compoundTag.getLong(BLOCK_POS));
    }

    @Override
    public Color getColor() {
        return new Color(uuid.hashCode());
    }

    @Override
    public Type getMarkType() {
        return ENTITY;
    }

    @Override
    public Vec3 getPointPos() {
        return entity != null ? entity.getEyePosition() : pos.getCenter();
    }

    @Override
    public CompoundTag CreateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID(UUID,this.uuid);
        compoundTag.putLong(BLOCK_POS,this.pos.asLong());
        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.entity != null && obj instanceof Entity entity){
            return this.entity == entity;
        }
        if(obj.getClass() != this.getClass()) return false;
        EntitySign obj1 = (EntitySign) obj;
        return obj1.pos.equals(this.pos) && obj1.uuid.equals(this.uuid);
    }

    @Override
    public Boolean CanUse() {
        if(Minecraft.getInstance().player.position().subtract(this.pos.getCenter()).length() < 32.0D
                &&( this.entity == null || (this.entity.isRemoved() && this.entity.getRemovalReason() == Entity.RemovalReason.DISCARDED))){
            for (Entity next : Minecraft.getInstance().level.entitiesForRendering()) {
                if (next.getUUID().equals(this.uuid)) {
                    this.entity = next;
                    break;
                }
            }

            return this.entity != null;
        }
        return entity == null || !entity.isRemoved() || entity.getRemovalReason() == Entity.RemovalReason.DISCARDED;
    }

    @Nullable
    public Entity getEntity() {
        return entity;
    }



    @OnlyIn(Dist.CLIENT)
    public boolean isLocalPlayer(){
        return this.uuid.equals(Minecraft.getInstance().player.getUUID());
    }
}
