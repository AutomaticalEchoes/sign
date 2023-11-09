package com.automaticalechoes.simplesign.common.command;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.common.sign.BlockSign;
import com.automaticalechoes.simplesign.common.sign.EntitySign;
import com.automaticalechoes.simplesign.common.sign.Sign;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class MarkCommand {
   public static final LiteralArgumentBuilder<CommandSourceStack> MARK =
           Commands.literal("mark").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
   public static final RequiredArgumentBuilder<CommandSourceStack, Coordinates> BLOCKPOS =
           Commands.argument("blockPos", BlockPosArgument.blockPos());
   public static final RequiredArgumentBuilder<CommandSourceStack, EntitySelector> ENTITY =
           Commands.argument("entity", EntityArgument.entity());
   public static final RequiredArgumentBuilder<CommandSourceStack,Integer> SLOT =
           Commands.argument("equip", SlotArgument.slot());

   public static final MutableComponent MARKED = Component.translatable("sign.marked");
   public static final MutableComponent AT = Component.translatable("sign.at");
   public static final MutableComponent _S = Component.translatable("sign._s");
   public static final MutableComponent NONE = Component.translatable("sign.none");

   public static final Style STYLE_ITEM = Style.EMPTY.applyFormats(ChatFormatting.BLUE);
   public static final Style STYLE_BLOCK = Style.EMPTY.applyFormats(ChatFormatting.GREEN);
   public static final Style STYLE_ENTITY = Style.EMPTY.applyFormats(ChatFormatting.LIGHT_PURPLE);
   public static final Style STYLE_BLOCK_POS = Style.EMPTY.applyFormats(ChatFormatting.AQUA);

   public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
       p_249870_.register(MARK
               .then(ENTITY.executes(context -> Mark(context.getSource(),null ,EntityArgument.getEntity(context,"entity")))
                       .then(SLOT.executes(context -> PingSlot(context.getSource(), EntityArgument.getEntity(context,"entity"),SlotArgument.getSlot(context,"equip")))))
               .then(BLOCKPOS.executes(context -> Mark(context.getSource(),BlockPosArgument.getBlockPos(context,"blockPos"),null))));
   }

   public static int Mark(CommandSourceStack sourceStack, @Nullable BlockPos pos, @Nullable Entity entity) throws CommandSyntaxException {
//       Component senderName = sourceStack.getPlayer().getName();
       HoverEvent hoverEvent = null;
       MutableComponent markName = Component.empty();
       MutableComponent posMessage = Component.empty().withStyle(STYLE_BLOCK_POS);
       Sign mark = null;
       if(pos != null){
           Block block = sourceStack.getLevel().getBlockState(pos).getBlock();
           markName.append(block.getName()).withStyle(STYLE_BLOCK);
           posMessage.append(Component.translatable(" [" + pos.toShortString() + "]"));
           ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
           mark = new BlockSign(pos, key);
       }else if(entity != null){
           if(entity instanceof ItemEntity || (entity instanceof ItemFrame itemFrame && !itemFrame.getItem().isEmpty())){
               ItemStack itemStack = entity instanceof ItemEntity itemEntity ? itemEntity.getItem() : ((ItemFrame)entity).getItem();
               markName.append(itemStack.getItem().getName(itemStack)).withStyle(STYLE_ITEM);
               hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(itemStack));
           }else {
               Component showName = entity instanceof Player ? entity.getName() : entity.getType().getDescription();
               markName.append(showName).withStyle(STYLE_ENTITY);
               hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY,new HoverEvent.EntityTooltipInfo(entity.getType(),entity.getUUID(),entity.getDisplayName()));
           }
           posMessage.append(Component.translatable(" [" + entity.blockPosition().toShortString() + "]"));
           mark = new EntitySign(entity.getUUID(),entity.blockPosition());
       }

       if(mark == null){
           sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
           return 0;
       }


       Sign finalMark = mark;
       HoverEvent finalHoverEvent = hoverEvent;
       MutableComponent mutableComponent = Component.empty()
//               .append(senderName.copy()
//                       .withStyle(ChatFormatting.GREEN))
               .append(MARKED)
               .append(markName)
               .append(AT)
               .append(posMessage)
               .withStyle(style -> style
                       .withColor(ChatFormatting.GRAY)
                       .withHoverEvent(finalHoverEvent)
                       .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/getmark " + Sign.ToTag(finalMark))));
       ChatType.Bound bind = sourceStack.getLevel().registryAccess().registryOrThrow(Registries.CHAT_TYPE).get(SimpleSign.SIGN_CHAT).bind(sourceStack.getDisplayName());
       sourceStack.getServer().getPlayerList()
               .broadcastChatMessage(PlayerChatMessage.unsigned(sourceStack.getPlayer().getUUID(),"")
                       .withUnsignedContent(mutableComponent),sourceStack.getPlayer(),bind);

//       sourceStack.sendSuccess(() ->

       return 1;
   }

   public static int PingSlot(CommandSourceStack sourceStack,Entity entity, Integer slotNum){
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
           sourceStack.getServer().getPlayerList()
                   .broadcastChatMessage(PlayerChatMessage.unsigned(sourceStack.getPlayer().getUUID(),"")
                           .withUnsignedContent(mutableComponent),sourceStack.getPlayer(),ChatType.bind(ChatType.CHAT,sourceStack));
       }
       return 0;
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
