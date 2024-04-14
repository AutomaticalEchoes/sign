package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;

public class SignImp implements Sign{
    protected final SignalTarget target;
    public SignImp(SignalTarget target){
        this.target = target;
    }

    @Override
    public SignalTarget getTarget() {
        return target;
    }

    @Override
    public CompoundTag CreateTag() {
        return target.CreateTag();
    }
}
