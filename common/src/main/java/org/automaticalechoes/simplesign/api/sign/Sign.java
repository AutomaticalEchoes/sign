package org.automaticalechoes.simplesign.api.sign;



import net.minecraft.nbt.CompoundTag;
import org.automaticalechoes.simplesign.api.sign.target.SignalTarget;

public interface Sign {
     String TYPE = "sign_type";
     SignalTarget target();
//     Vec3 getPointPos();
     CompoundTag createTag();
     int typeN();


}
