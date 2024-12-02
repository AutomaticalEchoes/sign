package org.automaticalechoes.simplesign.client;



import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.automaticalechoes.simplesign.api.sign.Sign;
import org.automaticalechoes.simplesign.api.sign.target.SignalTarget;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ClientSign implements Sign {
    private final SignalTarget target;
    private final int typeN;
    private int lifecycle;
    private Vec3 renderPosition;
    private double distance;


    public ClientSign(CompoundTag tag, int lifecycle){
        this.target = SignalTarget.FromTag(tag);
        this.typeN = tag.getInt(TYPE);
        this.lifecycle = lifecycle;
    }

    public void tick(){
        if(lifecycle > 0) lifecycle --;
    }

    public Vec3 getPointPos() {
        return this.target.getPointPos();
    }


    public Boolean CanUse() {
        return (this.lifecycle == -1 || this.lifecycle > 0) && this.target.CanUse();
    }


    public Color getColor() {
        return this.target.getColor();
    }

    @Nullable
    public ItemStack getItemStack() {
        return this.target.getItemStack();
    }

    public int getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(int lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientSign clientSignal && clientSignal.target.equals(this.target);
    }

    public void setupRenderPos(float x, float y, float w, double distance){
        this.renderPosition = new Vec3(x, y, w);
        this.distance = distance;
    }

    public Vec3 getRenderPosition() {
        return renderPosition;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public SignalTarget target() {
        return target;
    }

    @Override
    public CompoundTag CreateTag() {
        return null;
    }

    @Override
    public int typeN() {
        return typeN;
    }
}
