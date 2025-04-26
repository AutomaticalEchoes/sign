package org.automaticalechoes.simplesign.api.sign.target;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public interface SignalTarget {

    String TARGET_TYPE = "target_type";
    HashMap<String, Function<CompoundTag, SignalTarget>> TARGET_TYPES = new HashMap<>();
    String BLOCK = Register("block", BlockTarget::FromTag);
    String ENTITY = Register("entity", EntityTarget::new);
    CompoundTag CreateTag();
    Boolean CanUse();
    Color getColor();
    @Nullable
    ItemStack itemStack();
    Vec3 getPointPos();
    List<Component> toolTipComponents();

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
