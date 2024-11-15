package org.automaticalechoes.simplesign.api.sign;



import net.minecraft.nbt.CompoundTag;
import org.automaticalechoes.simplesign.api.sign.target.SignalTarget;

public interface Sign {
     String TYPE = "sign_type";
     SignalTarget target();
//     Vec3 getPointPos();
     CompoundTag CreateTag();
     int typeN();
     static Sign fromTag(CompoundTag tag){
          return new SignImp(SignalTarget.FromTag(tag), tag.getInt(TYPE));
     }

}
