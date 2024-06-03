package com.automaticalechoes.simplesign.client.sign;

import com.automaticalechoes.simplesign.common.sign.Sign;
import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ClientSign {
    private final Sign sign;
    private int lifecycle = 200;

    public ClientSign(CompoundTag tag, int lifecycle){
        this.sign = Sign.fromTag(tag);
        this.lifecycle = lifecycle;
    }

    public ClientSign(CompoundTag tag){
        this.sign = Sign.fromTag(tag);
        this.lifecycle = -1;
    }

    public SignalTarget getTarget() {
        return sign.getTarget();
    }

    public void tick(){
        if(lifecycle > 0) lifecycle --;
    }

    public Vec3 getPointPos() {
        return getTarget().getPointPos();
    }


    public Boolean CanUse() {
        return (this.lifecycle == -1 || this.lifecycle > 0) && getTarget().CanUse();
    }


    public Color getColor() {
        return getTarget().getColor();
    }

    @Nullable
    public ItemStack getItemStack() {
        return getTarget().getItemStack();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientSign clientSignal && clientSignal.getTarget().equals(this.getTarget());
    }
}
