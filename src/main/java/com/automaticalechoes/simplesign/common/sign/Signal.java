package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.common.sign.target.BlockTarget;
import com.automaticalechoes.simplesign.common.sign.target.EntityTarget;
import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.function.Function;

public interface Signal {
     String RENDER_TYPE = "point_render_type";

     SignalTarget getTarget();
     Type getType();
//     Vec3 getPointPos();
     CompoundTag CreateTag();

     enum Type {
          DEFAULT(0),
          FOCUS(1),
          CARE(2),
          QUESTION(3);
          final int num;
          static Type[] TYPES = new Type[]{DEFAULT,FOCUS,CARE,QUESTION};
          Type(int num){
               this.num = num;
          }

          public int getNum() {
               return num;
          }

          public static Type fromTag(CompoundTag tag){
               if(tag.contains(RENDER_TYPE)){
                    int anInt = tag.getInt(RENDER_TYPE);
                    return TYPES[anInt];
               }
               return DEFAULT;
          }

          public void toTag(CompoundTag compoundTag){
               compoundTag.putInt(RENDER_TYPE,num);
          }
     }
}
