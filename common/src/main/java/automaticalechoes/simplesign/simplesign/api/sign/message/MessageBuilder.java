package automaticalechoes.simplesign.simplesign.api.sign.message;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface MessageBuilder {

    MutableComponent MARKED = Component.translatable("sign.marked");
    MutableComponent AT = Component.translatable("sign.at");
    MutableComponent _S = Component.translatable("sign._s");
    MutableComponent NONE = Component.translatable("sign.none");

    MutableComponent EVACUATION = Component.translatable("sign.evacuation");
    MutableComponent ATTENTION = Component.translatable("sign.attention");



    Style STYLE_ITEM = Style.EMPTY.applyFormats(ChatFormatting.BLUE);
    Style STYLE_BLOCK = Style.EMPTY.applyFormats(ChatFormatting.GREEN);
    Style STYLE_ENTITY = Style.EMPTY.applyFormats(ChatFormatting.LIGHT_PURPLE);
    Style STYLE_BLOCK_POS = Style.EMPTY.applyFormats(ChatFormatting.AQUA);

    static MutableComponent GetDecorate(ServerPlayer source, Entity target, int type) {

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
}
