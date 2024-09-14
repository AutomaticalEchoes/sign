package automaticalechoes.simplesign.simplesign.api.sign;


import automaticalechoes.simplesign.simplesign.api.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;

public interface Sign {
     String TYPE = "sign_type";
     SignalTarget target();
//     Vec3 getPointPos();
     CompoundTag CreateTag();

     static Sign fromTag(CompoundTag tag){
          return new SignImp(SignalTarget.FromTag(tag), tag.getInt(TYPE));
     }

}
