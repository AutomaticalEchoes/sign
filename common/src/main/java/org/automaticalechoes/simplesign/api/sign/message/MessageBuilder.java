package org.automaticalechoes.simplesign.api.sign.message;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.automaticalechoes.simplesign.api.sign.Sign;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


public interface MessageBuilder {

    MutableComponent MARKED = Component.translatable("sign.marked");
    MutableComponent AT = Component.translatable("sign.at");
    MutableComponent _S = Component.translatable("sign._s");
    MutableComponent NONE = Component.translatable("sign.none");
    MutableComponent ATTENTION = Component.translatable("sign.attention");
    MutableComponent ATTACK_ENEMY = Component.translatable("sign.attack_enemy");


    Style STYLE_ITEM = Style.EMPTY.applyFormats(ChatFormatting.BLUE);
    Style STYLE_BLOCK = Style.EMPTY.applyFormats(ChatFormatting.GREEN);
    Style STYLE_ENTITY = Style.EMPTY.applyFormats(ChatFormatting.LIGHT_PURPLE);
    Style STYLE_BLOCK_POS = Style.EMPTY.applyFormats(ChatFormatting.AQUA);

    List<BiFunction<ServerPlayer, Entity, BiFunction<Component, Component,MutableComponent>>> TYPES = new ArrayList<>();
    Integer DEFAULT = RegisterType((serverPlayer, entity) -> MessageBuilder::DecorateMessage);
    Integer WARN = RegisterType((serverPlayer, entity) -> MessageBuilder::DecorateAttentionMessage);
    Integer ATTACK = RegisterType((serverPlayer, entity) -> MessageBuilder::DecorateAttackMessage);

    static int RegisterType(BiFunction<ServerPlayer, Entity, BiFunction<Component, Component,MutableComponent>> func) {
        TYPES.add(func);
        return TYPES.size() - 1;
    }

    static MutableComponent DecorateAttackMessage(@Nullable Component markName, @Nullable Component pos) {
        return Component.empty()
                .append(ATTACK_ENEMY)
                .append(markName);
    }

    static MutableComponent DecorateAttentionMessage(@Nullable Component markName, @Nullable Component pos) {
        return Component.empty()
                .append(ATTENTION)
                .append(markName)
                .append(AT)
                .append(pos);
    }

    static MutableComponent DecorateMessage(@Nullable Component markName, @Nullable Component pos){
        MutableComponent mutableComponent = Component.empty();
        mutableComponent.append(MARKED);
        mutableComponent.append(markName);
        if(pos != null){
            mutableComponent.append(AT).append(pos);
        }
        return mutableComponent;
    }

    static BiFunction<Component, Component, MutableComponent> getBuilder(Sign sign, ServerPlayer serverPlayer , @Nullable Entity entity){
        BiFunction<ServerPlayer, Entity, BiFunction<Component, Component, MutableComponent>> function = TYPES.get(sign.typeN());
        return function != null ? function.apply(serverPlayer, entity) : MessageBuilder::DecorateMessage;
    }
}
