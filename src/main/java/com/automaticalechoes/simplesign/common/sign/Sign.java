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
     String TYPE = "mark_type";
     HashMap<String, Type> MARK_TYPES = new HashMap<>();
     Type BLOCK = Register("block", BlockSign::new);
     Type ENTITY = Register("entity", EntitySign::new);
     String ITEM = "show_item";

     Type getMarkType();
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
               String name = compoundTag.getString(TYPE);
               return GetBuilder(name).apply(compoundTag);
          }
         return null;
     }

     static CompoundTag ToTag(Sign mark){
          CompoundTag compoundTag = mark.CreateTag();
          String name = mark.getMarkType().Name();
          compoundTag.putString(TYPE,name);
          return compoundTag;
     }


     static Type Register(String name, Function<CompoundTag, Sign> Builder){
          Type type = new Type(name, Builder);
          MARK_TYPES.put(name,type);
          return type;
     }

     static Function<CompoundTag, Sign> GetBuilder(String name){
          Type type = MARK_TYPES.get(name);
          return type.Builder();
     }

     record Type(String Name,Function<CompoundTag, Sign> Builder) {
     }
}
