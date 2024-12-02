package org.automaticalechoes.simplesign.api.sign.message;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.automaticalechoes.simplesign.api.sign.Sign;
import org.jetbrains.annotations.Nullable;

public class SignMessageBuilder implements MessageBuilder{

    private MutableComponent markName;
    private MutableComponent pos;
    private HoverEvent hoverEvent;

    public SignMessageBuilder WithMarkName(MutableComponent markName, Style style) {
        this.markName = markName.withStyle(style);
        return this;
    }

    public SignMessageBuilder WithBlock(Block block) {
        this.markName = block.getName().withStyle(STYLE_BLOCK);
        return this;
    }

    public SignMessageBuilder WithEntity(Entity entity) {
        Component component = entity instanceof Player ? entity.getName() : entity.getType().getDescription();
        this.markName = ((MutableComponent)component).withStyle(STYLE_ENTITY);
        this.WithHoverEntity(entity);
        return this;
    }

    public SignMessageBuilder WithItemStack(ItemStack itemStack) {
        this.markName = ((MutableComponent)itemStack.getItem().getName(itemStack)).withStyle(STYLE_ITEM);
        this.WithHoverItem(itemStack);
        return this;
    }

    public SignMessageBuilder WithPos(BlockPos pos) {
        MutableComponent translatable = Component.translatable(" [" + pos.toShortString() + "]");
        WithPos(translatable, STYLE_BLOCK_POS);
        return this;
    }

    public SignMessageBuilder WithPos(MutableComponent pos, Style style) {
        this.pos = pos.withStyle(style);
        return this;
    }

    public SignMessageBuilder WithHoverItem(ItemStack stack) {
        this.hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack));
        return this;
    }
    public SignMessageBuilder WithHoverEntity(Entity entity) {
        this.hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY,new HoverEvent.EntityTooltipInfo(entity.getType(),entity.getUUID(),entity.getDisplayName()));
        return this;
    }

    public MutableComponent BuildSignMessage(Sign sign, ServerPlayer player, @Nullable Entity entity) {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ssi_client getmark " + sign.CreateTag());
        return MessageBuilder.getBuilder(sign, player, entity).apply(markName, pos).withStyle(style -> style
                .withColor(ChatFormatting.GRAY)
                .withHoverEvent(hoverEvent)
                .withClickEvent(clickEvent));
    }


}
