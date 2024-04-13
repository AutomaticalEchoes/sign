package com.automaticalechoes.simplesign.client.sign;

import com.automaticalechoes.simplesign.common.sign.Signal;
import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.accesstransformer.TargetType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ClientSignal implements Signal {
    private final SignalTarget target;
    private final Type type;
    private int lifecycle = 200;

    public ClientSignal(CompoundTag tag){
        this.target = SignalTarget.FromTag(tag);
        this.type = Signal.Type.values()[tag.getInt(RENDER_TYPE)];
        this.lifecycle = type == Type.DEFAULT ? -1 : 200;
    }

    @Override
    public SignalTarget getTarget() {
        return target;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public CompoundTag CreateTag() {
        return null;
    }

    public void tick(){
        if(lifecycle > 0) lifecycle --;
    }

    public Vec3 getPointPos() {
        return target.getPointPos();
    }


    public Boolean CanUse() {
        return (this.lifecycle == -1 || this.lifecycle > 0) && target.CanUse();
    }


    public Color getColor() {
        return target.getColor();
    }

    @Nullable
    public ItemStack getItemStack() {
        return target.getItemStack();
    }
}
