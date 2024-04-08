package com.automaticalechoes.simplesign.client.sign;

import com.automaticalechoes.simplesign.common.sign.Sign;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ClientSign implements Sign{
    private final Sign sign;
    private int lifecycle = 200;

    public ClientSign(Sign sign){
        this.sign = sign;
    }

    public void tick(){
        if(lifecycle > 0) lifecycle --;
    }

    @Override
    public TargetType getTargetType() {
        return sign.getTargetType();
    }

    @Override
    public Type getType() {
        return sign.getType();
    }

    @Override
    public Vec3 getPointPos() {
        return sign.getPointPos();
    }

    @Override
    public CompoundTag CreateTag() {
        return sign.CreateTag();
    }

    @Override
    public Boolean CanUse() {
        return (this.lifecycle == -1 || this.lifecycle > 0) && sign.CanUse();
    }

    @Override
    public Color getColor() {
        return sign.getColor();
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return sign.getItemStack();
    }
}
