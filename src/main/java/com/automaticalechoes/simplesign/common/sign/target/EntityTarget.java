package com.automaticalechoes.simplesign.common.sign.target;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.UUID;

public class EntityTarget implements SignalTarget {
    public static final String UUID = "uuid";
    public static final String BLOCK_POS = "block_pos";
    @Nullable
    protected Entity entity;
    String ITEM = "show_item";
    protected final BlockPos pos;
    protected final UUID uuid;
    protected final ItemStack itemStack;
    public EntityTarget(UUID uuid, BlockPos pos, @Nullable ItemStack itemStack){
        this.uuid = uuid;
        this.pos = pos;
        this.itemStack = itemStack;
    }

    public EntityTarget(CompoundTag compoundTag){
        this.uuid = compoundTag.getUUID(UUID);
        this.pos = BlockPos.of(compoundTag.getLong(BLOCK_POS));
        this.itemStack = compoundTag.contains(ITEM)? ItemStack.of(compoundTag.getCompound(ITEM)) : null;
    }

    @Override
    public Color getColor() {
        return new Color(uuid.hashCode());
    }


    public Vec3 getPointPos() {
        return entity != null ? entity.getEyePosition() : pos.getCenter();
    }

    @Override
    public CompoundTag CreateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID(UUID,this.uuid);
        compoundTag.putLong(BLOCK_POS,this.pos.asLong());
        compoundTag.putString(TARGET_TYPE, ENTITY);
        if(itemStack !=null){
            compoundTag.put(ITEM,itemStack.save(new CompoundTag()));
        }
        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.entity != null && obj instanceof net.minecraft.world.entity.Entity entity){
            return this.entity == entity;
        }
        if(obj.getClass() != this.getClass()) return false;
        EntityTarget obj1 = (EntityTarget) obj;
        return obj1.pos.equals(this.pos) && obj1.uuid.equals(this.uuid);
    }

    @Override
    public Boolean CanUse() {
        if(Minecraft.getInstance().player.position().subtract(this.pos.getCenter()).length() < 32.0D
                &&( this.entity == null || (this.entity.isRemoved()))){
            this.entity = null;
            for (net.minecraft.world.entity.Entity next : Minecraft.getInstance().level.entitiesForRendering()) {
                if (next.getUUID().equals(this.uuid)) {
                    this.entity = next;
                    break;
                }
            }

            return this.entity != null;
        }
        return entity == null || entity.isAlive() || entity.getRemovalReason() == net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;
    }

    @Nullable
    public net.minecraft.world.entity.Entity getEntity() {
        return entity;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isLocalPlayer(){
        return this.uuid.equals(Minecraft.getInstance().player.getUUID());
    }


    @Override
    @Nullable
    public ItemStack getItemStack(){
       return this.itemStack;
    }
}
