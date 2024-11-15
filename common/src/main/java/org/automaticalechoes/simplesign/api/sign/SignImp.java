package org.automaticalechoes.simplesign.api.sign;


import net.minecraft.nbt.CompoundTag;
import org.automaticalechoes.simplesign.api.sign.target.SignalTarget;

public record SignImp(SignalTarget target, int typeN) implements Sign {

    @Override
    public CompoundTag CreateTag() {
        CompoundTag compoundTag = target.CreateTag();
        compoundTag.putInt(TYPE, typeN);
        return compoundTag;
    }
}
