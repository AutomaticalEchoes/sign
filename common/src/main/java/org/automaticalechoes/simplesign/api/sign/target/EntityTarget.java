package org.automaticalechoes.simplesign.api.sign.target;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
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
    public EntityTarget(UUID uuid, BlockPos pos, @Nullable Entity entity, @Nullable ItemStack itemStack){
        this.uuid = uuid;
        this.pos = pos;
        this.entity = entity;
        this.itemStack = itemStack;
    }

    public EntityTarget(CompoundTag compoundTag){
        this.uuid = compoundTag.getUUID(UUID);
        this.pos = BlockPos.of(compoundTag.getLong(BLOCK_POS));
        this.itemStack = compoundTag.contains(ITEM)? ItemStack.parseOptional(Minecraft.getInstance().level.registryAccess(),compoundTag.getCompound(ITEM)) : null;
    }

    @Override
    public Color getColor() {
        return new Color(uuid.hashCode());
    }

    @Override
    public @Nullable ItemStack itemStack() {
        return this.itemStack;
    }


    public Vec3 getPointPos() {
        return entity != null ? entity.getEyePosition() : pos.getCenter();
    }

    @Override
    public List<Component> toolTipComponents() {
        return itemStack !=null && !itemStack.isEmpty()?  Screen.getTooltipFromItem(Minecraft.getInstance(), itemStack) : entity != null ? entity.getDisplayName().toFlatList() : List.of();
    }

    @Override
    public CompoundTag CreateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID(UUID,this.uuid);
        compoundTag.putLong(BLOCK_POS,this.pos.asLong());
        compoundTag.putString(TARGET_TYPE, ENTITY);
        if(itemStack !=null){
            compoundTag.put(ITEM,itemStack.save(entity.registryAccess()));
        }
        return compoundTag;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.entity != null && obj instanceof Entity entity){
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


    public boolean isLocalPlayer(){
        return this.uuid.equals(Minecraft.getInstance().player.getUUID());
    }
}
