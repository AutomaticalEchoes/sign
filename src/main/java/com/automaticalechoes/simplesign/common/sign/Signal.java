package com.automaticalechoes.simplesign.common.sign;

import com.automaticalechoes.simplesign.common.sign.target.SignalTarget;
import net.minecraft.nbt.CompoundTag;

public class Signal extends SignImp {
    private final Type type;
    public Signal(SignalTarget target,Type type) {
        super(target);
        this.type = type;
    }

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

    public enum Type {
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
