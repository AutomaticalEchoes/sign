package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;

public record Mark(SignalTarget target) implements Sign{
    @Override
    public SignalTarget getTarget() {
        return target;
    }

    @Override
    public CompoundTag CreateTag() {
        return target.CreateTag();
    }
}
