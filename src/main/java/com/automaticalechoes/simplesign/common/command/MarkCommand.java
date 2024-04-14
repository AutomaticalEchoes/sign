package com.automaticalechoes.simplesign.common.command;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.common.Iplayers;
import com.automaticalechoes.simplesign.common.sign.Signal;
import com.automaticalechoes.simplesign.common.sign.target.BlockTarget;
import com.automaticalechoes.simplesign.common.sign.target.EntityTarget;
import com.automaticalechoes.simplesign.common.sign.Sign;
import com.automaticalechoes.simplesign.mixin.PlayerListMixin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;
import net.minecraftforge.client.ClientCommandSourceStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.EnumArgument;

import javax.annotation.Nullable;

public class MarkCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> MARK =
            Commands.literal("mark").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final RequiredArgumentBuilder<CommandSourceStack, Sign.Type> SIGN =
            Commands.argument("sign_type", EnumArgument.enumArgument(Sign.Type.class));
    public static final RequiredArgumentBuilder<CommandSourceStack, Coordinates> BLOCKPOS =
            Commands.argument("blockPos", BlockPosArgument.blockPos());
    public static final RequiredArgumentBuilder<CommandSourceStack, EntitySelector> ENTITY =
            Commands.argument("entity", EntityArgument.entity());
    public static final RequiredArgumentBuilder<CommandSourceStack, Integer> SLOT =
            Commands.argument("equip", SlotArgument.slot());

   public static final MutableComponent MARKED = Component.translatable("sign.marked");
   public static final MutableComponent AT = Component.translatable("sign.at");
   public static final MutableComponent _S = Component.translatable("sign._s");
   public static final MutableComponent NONE = Component.translatable("sign.none");
    public static final MutableComponent FAR_AWAY = Component.translatable("sign.marked_care_pos");
    public static final MutableComponent CARE_NAME = Component.translatable("sign.marked_care_name");
    public static final MutableComponent FOCUS_POS = Component.translatable("sign.marked_focus_pos");
    public static final MutableComponent FOCUS_NAME = Component.translatable("sign.marked_focus_name");
    public static final MutableComponent QUEST_POS = Component.translatable("sign.marked_quest");
   public static final Style STYLE_ITEM = Style.EMPTY.applyFormats(ChatFormatting.BLUE);
   public static final Style STYLE_BLOCK = Style.EMPTY.applyFormats(ChatFormatting.GREEN);
   public static final Style STYLE_ENTITY = Style.EMPTY.applyFormats(ChatFormatting.LIGHT_PURPLE);
   public static final Style STYLE_BLOCK_POS = Style.EMPTY.applyFormats(ChatFormatting.AQUA);

   public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
       p_249870_.register(SimpleSign.SSI
               .then(MARK
                       .then(SIGN
                               .then(ENTITY.executes(context -> Mark(context.getSource(), context.getArgument("sign_type", Sign.Type.class),null ,EntityArgument.getEntity(context,"entity")))
                                       .then(SLOT.executes(context -> PingSlot(context.getSource(), EntityArgument.getEntity(context,"entity"),SlotArgument.getSlot(context,"equip")))))
                               .then(BLOCKPOS.executes(context -> Mark(context.getSource(), context.getArgument("sign_type", Sign.Type.class), BlockPosArgument.getBlockPos(context,"blockPos"),null))))));
   }

   public static int Mark(CommandSourceStack sourceStack, @Nullable Sign.Type renderType, @Nullable BlockPos pos, @Nullable net.minecraft.world.entity.Entity entity) throws CommandSyntaxException {
       if(sourceStack instanceof ClientCommandSourceStack) throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand(), Component.literal("can not run in client")) ;
//       Component senderName = sourceStack.getPlayer().getName();
       HoverEvent hoverEvent = null;
       MutableComponent markName = Component.empty();
       MutableComponent posMessage = Component.empty().withStyle(STYLE_BLOCK_POS);
       ServerPlayer player = sourceStack.getPlayer();

       if(player == null || !((Iplayers)player).canUse()){
           sourceStack.sendFailure(Component.translatable("sign.no_useful_time"));
           return 0;
       }

       Sign mark = null;
       ItemStack itemStack = null;
       if(pos != null){
           net.minecraft.world.level.block.Block block = sourceStack.getLevel().getBlockState(pos).getBlock();
           markName.append(block.getName()).withStyle(STYLE_BLOCK);
           posMessage.append(Component.translatable(" [" + pos.toShortString() + "]"));
           ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
           mark = new Signal(new BlockTarget(pos, key), renderType);
       }else if(entity != null){
           if(entity instanceof ItemEntity || (entity instanceof ItemFrame itemFrame && !itemFrame.getItem().isEmpty())){
               itemStack = entity instanceof ItemEntity itemEntity ? itemEntity.getItem() : ((ItemFrame)entity).getItem();
               markName.append(itemStack.getItem().getName(itemStack)).withStyle(STYLE_ITEM);
               hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(itemStack));
           }else {
               Component showName = entity instanceof Player ? entity.getName() : entity.getType().getDescription();
               markName.append(showName).withStyle(STYLE_ENTITY);
               hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY,new HoverEvent.EntityTooltipInfo(entity.getType(),entity.getUUID(),entity.getDisplayName()));
           }
           posMessage.append(Component.translatable(" [" + entity.blockPosition().toShortString() + "]"));
           mark = new Signal(new EntityTarget(entity.getUUID(), entity.blockPosition(),itemStack), renderType);
       }

       if(mark == null){
           sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
           return 0;
       }

       Sign finalMark = mark;
       HoverEvent finalHoverEvent = hoverEvent;

       MutableComponent mutableComponent = DecorateMessage(finalMark.getType(), markName, posMessage).withStyle(style -> style
                       .withColor(ChatFormatting.GRAY)
                       .withHoverEvent(finalHoverEvent)
                       .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ssi getmark " + finalMark.CreateTag())));
       SendMessage(player, sourceStack.getServer().getPlayerList(), PlayerChatMessage.unsigned(player.getUUID(),"")
               .withUnsignedContent(mutableComponent), ChatType.bind(ChatType.CHAT,sourceStack));
       return 1;
   }

   public static int PingSlot(CommandSourceStack sourceStack, net.minecraft.world.entity.Entity entity, Integer slotNum){
       SlotAccess slot = entity.getSlot(slotNum);
       EquipmentSlot equipmentSlot = getEquipmentSlot(slotNum);
       if(slot!= SlotAccess.NULL && equipmentSlot != null){
           MutableComponent markName = Component.empty().withStyle(STYLE_ENTITY);
           MutableComponent itemName = Component.empty();
           MutableComponent partName = Component.translatable("sign." + equipmentSlot.getName());
           Component showName = entity instanceof Player ? entity.getName() : entity.getType().getDescription();
           markName.append(showName);

           ItemStack itemStack = slot.get();
           if(itemStack.isEmpty())
               itemName.append(NONE).withStyle(ChatFormatting.GRAY);
           else
               itemName.append(itemStack.getItem().getName(itemStack)).withStyle(STYLE_ITEM);

           MutableComponent mutableComponent = Component.empty()
                   .append(MARKED)
                   .append(markName)
                   .append(_S)
                   .append(partName)
                   .append(itemName)
                   .withStyle(style -> style.withColor(ChatFormatting.GRAY).withHoverEvent(
                           new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(itemStack))
                   ));
           SendMessage(sourceStack.getPlayer(), sourceStack.getServer().getPlayerList(), PlayerChatMessage.unsigned(sourceStack.getPlayer().getUUID(),"")
                   .withUnsignedContent(mutableComponent), ChatType.bind(ChatType.CHAT,sourceStack));
       }
       return 0;
   }
    public static void SendMessage(ServerPlayer serverPlayer, PlayerList playerList, PlayerChatMessage playerChatMessage , ChatType.Bound bound){
        ((PlayerListMixin)playerList).invokeBroadcastChatMessage(playerChatMessage, serverPlayer1 -> {
            if(serverPlayer.getTeam() == null) return true;
            Team team1 = serverPlayer1.getTeam();
            if(team1 == serverPlayer.getTeam()) return true;
            return false;
        }, serverPlayer, bound);
        ((Iplayers)serverPlayer).trigger();
    }

    public static MutableComponent DecorateMessage(Sign.Type type, @Nullable Component markName, @Nullable Component pos){
        MutableComponent mutableComponent = Component.empty();
        switch (type){
            case DEFAULT -> mutableComponent.append(MARKED);
            case CARE -> mutableComponent.append(CARE_NAME);
            case FOCUS -> mutableComponent.append(FOCUS_NAME);
            case QUESTION -> mutableComponent.append(QUEST_POS);
        }
//
        mutableComponent.append(markName);
        if(pos != null){
            mutableComponent.append(AT).append(pos);
        }
        return mutableComponent;
    }

    @Nullable
    private static EquipmentSlot getEquipmentSlot(int p_147212_) {
        if (p_147212_ == 100 + EquipmentSlot.HEAD.getIndex()) {
            return EquipmentSlot.HEAD;
        } else if (p_147212_ == 100 + EquipmentSlot.CHEST.getIndex()) {
            return EquipmentSlot.CHEST;
        } else if (p_147212_ == 100 + EquipmentSlot.LEGS.getIndex()) {
            return EquipmentSlot.LEGS;
        } else if (p_147212_ == 100 + EquipmentSlot.FEET.getIndex()) {
            return EquipmentSlot.FEET;
        } else if (p_147212_ == 98) {
            return EquipmentSlot.MAINHAND;
        } else {
            return p_147212_ == 99 ? EquipmentSlot.OFFHAND : null;
        }
    }

}
