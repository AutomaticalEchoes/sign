package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.common.sign.Signal;
import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class ServerSignal implements Signal {
    private final SignalTarget target;
    private final Type type;

    public ServerSignal(SignalTarget target, Type type){
        this.target = target;
        this.type = type;
    }

    @Override
    public SignalTarget getTarget() {
        return target;
    }

    @Override
    public Type getType() {
        return type;
    }

//    @Override
//    public Vec3 getPointPos() {
//        return null;
//    }

    @Override
    public CompoundTag CreateTag() {
        CompoundTag compoundTag = target.CreateTag();
        compoundTag.putInt(RENDER_TYPE, type.num);
        return compoundTag;
    }
}
