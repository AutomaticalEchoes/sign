package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.common.sign.target.BlockTarget;
import com.automaticalechoes.simplesign.common.sign.target.EntityTarget;
import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.function.Function;

public interface Sign {
     String RENDER_TYPE = "point_render_type";
     SignalTarget getTarget();
//     Vec3 getPointPos();
     CompoundTag CreateTag();

     static Sign fromTag(CompoundTag tag){
          if(tag.contains(RENDER_TYPE)){
               return new Signal(SignalTarget.FromTag(tag), Signal.Type.fromTag(tag));
          }else {
               return new SignImp(SignalTarget.FromTag(tag));
          }
     }

}
