package com.automaticalechoes.simplesign.common.sign;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.function.Function;

public interface Sign {
     String RENDER_TYPE = "point_render_type";
     String TARGET_TYPE = "target_type";
     HashMap<String, TargetType> TARGET_TYPES = new HashMap<>();
     TargetType BLOCK = Register("block", BlockSign::new);
     TargetType ENTITY = Register("entity", EntitySign::new);
     String ITEM = "show_item";

     TargetType getTargetType();

     Type getType();
     Vec3 getPointPos();
     CompoundTag CreateTag();
     @OnlyIn(Dist.CLIENT)
     Boolean CanUse();
     @OnlyIn(Dist.CLIENT)
     Color getColor();
     @Nullable
     ItemStack getItemStack();

     static Sign FromTag(Tag tag){
          if(tag instanceof CompoundTag compoundTag){
               String name = compoundTag.getString(TARGET_TYPE);
               return GetBuilder(name).apply(compoundTag);
          }
         return null;
     }

     static CompoundTag ToTag(Sign mark){
          CompoundTag compoundTag = mark.CreateTag();
          String name = mark.getTargetType().Name();
          compoundTag.putString(TARGET_TYPE,name);
          mark.getType().toTag(compoundTag);
          return compoundTag;
     }

     static TargetType Register(String name, Function<CompoundTag, Sign> Builder){
          TargetType type = new TargetType(name, Builder);
          TARGET_TYPES.put(name,type);
          return type;
     }

     static Function<CompoundTag, Sign> GetBuilder(String name){
          TargetType type = TARGET_TYPES.get(name);
          return type.Builder();
     }

     record TargetType(String Name, Function<CompoundTag, Sign> Builder) {
     }

     enum Type {
          DEFAULT(0, "default"),
          FOCUS(1, "focus"),
          CARE(2, "care"),
          QUESTION(3, "question");
          final int num;
          final String name;
          static Type[] TYPES = new Type[]{DEFAULT,FOCUS,CARE,QUESTION};
          Type(int num, String name){
               this.num = num;
               this.name = name;
          }

          public int getNum() {
               return num;
          }

          public String getName() {
               return name;
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
