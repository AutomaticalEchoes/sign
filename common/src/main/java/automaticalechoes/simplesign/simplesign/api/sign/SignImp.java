package automaticalechoes.simplesign.simplesign.api.sign;

import automaticalechoes.simplesign.simplesign.api.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;

public record SignImp(SignalTarget target, int typeN) implements Sign {

    @Override
    public CompoundTag CreateTag() {
        CompoundTag compoundTag = target.CreateTag();
        compoundTag.putInt(TYPE, typeN);
        return compoundTag;
    }
}
