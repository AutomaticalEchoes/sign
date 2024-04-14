package com.automaticalechoes.simplesign.client.sign;

import com.automaticalechoes.simplesign.common.sign.Sign;
import com.automaticalechoes.simplesign.common.sign.Signal;
import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ClientSignal{
    private final Signal signal;
    private int lifecycle = 200;

    public ClientSignal(CompoundTag tag){
        this.signal = new Signal(SignalTarget.FromTag(tag), Signal.Type.fromTag(tag));
        this.lifecycle = 200;
    }


    public SignalTarget getTarget() {
        return signal.getTarget();
    }

    public Signal.Type getType() {
        return signal.getType();
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
        return obj instanceof ClientSignal clientSignal  && clientSignal.getTarget().equals(this.getTarget());
    }
}
