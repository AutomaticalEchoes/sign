package com.automaticalechoes.simplesign.common.sign.target;

import com.automaticalechoes.simplesign.common.sign.Signal;
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

public interface SignalTarget {

    String TARGET_TYPE = "target_type";
    HashMap<String, Function<CompoundTag, SignalTarget>> TARGET_TYPES = new HashMap<>();
    String BLOCK = Register("block", BlockTarget::new);
    String ENTITY = Register("entity", EntityTarget::new);
    CompoundTag CreateTag();
    @OnlyIn(Dist.CLIENT)
    Boolean CanUse();
    @OnlyIn(Dist.CLIENT)
    Color getColor();
    @Nullable
    ItemStack getItemStack();
    Vec3 getPointPos();

    static SignalTarget FromTag(CompoundTag compoundTag){
        String name = compoundTag.getString(TARGET_TYPE);
        return GetBuilder(name).apply(compoundTag);
    }

    static Function<CompoundTag, SignalTarget> GetBuilder(String name){
        return TARGET_TYPES.get(name);
    }

    static String Register(String name, Function<CompoundTag, SignalTarget> Builder){
        TARGET_TYPES.put(name, Builder);
        return name;
    }


}
