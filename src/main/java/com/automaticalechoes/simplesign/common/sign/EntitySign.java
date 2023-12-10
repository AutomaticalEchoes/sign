package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.SimpleSign;
import com.mojang.serialization.DataResult;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

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
    protected final ItemStack itemStack;

    public EntitySign(UUID uuid, BlockPos pos, @Nullable ItemStack itemStack){
        this.uuid = uuid;
        this.pos = pos;
        this.itemStack = itemStack;
    }

    public EntitySign(CompoundTag compoundTag){
        this.uuid = compoundTag.getUUID(UUID);
        this.pos = BlockPos.of(compoundTag.getLong(BLOCK_POS));
        this.itemStack = compoundTag.contains(ITEM)? ItemStack.of(compoundTag.getCompound(ITEM)) : null;
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
        if(itemStack !=null){
            compoundTag.put(ITEM,itemStack.save(new CompoundTag()));
        }
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
                &&( this.entity == null || (this.entity.isRemoved()))){
            this.entity = null;
            for (Entity next : Minecraft.getInstance().level.entitiesForRendering()) {
                if (next.getUUID().equals(this.uuid)) {
                    this.entity = next;
                    break;
                }
            }

            return this.entity != null;
        }
        return entity == null || entity.isAlive() || entity.getRemovalReason() == Entity.RemovalReason.DISCARDED;
    }

    @Nullable
    public Entity getEntity() {
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
