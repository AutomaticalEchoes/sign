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
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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

   public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
       p_249870_.register(MARK
               .then(ENTITY.executes(context -> Mark(context.getSource(),null ,EntityArgument.getEntity(context,"entity"))))
               .then(BLOCKPOS.executes(context -> Mark(context.getSource(),BlockPosArgument.getBlockPos(context,"blockPos"),null))));
   }

   public static int Mark(CommandSourceStack sourceStack, @Nullable BlockPos pos, @Nullable Entity entity) throws CommandSyntaxException {
//       Component senderName = sourceStack.getPlayer().getName();
       HoverEvent hoverEvent = null;
       MutableComponent markName = Component.empty().withStyle(ChatFormatting.BOLD);
       MutableComponent posMessage = Component.empty().withStyle(ChatFormatting.AQUA);
       Sign mark = null;
       if(pos != null){
           Block block = sourceStack.getLevel().getBlockState(pos).getBlock();
           markName.append(block.getName()).withStyle(ChatFormatting.GREEN);
           posMessage.append(Component.translatable(" [" + pos.toShortString() + "]"));
           ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
           mark = new BlockSign(pos, key);
       }else if(entity != null){
           if(entity instanceof ItemEntity || (entity instanceof ItemFrame itemFrame && !itemFrame.getItem().isEmpty())){
               ItemStack itemStack = entity instanceof ItemEntity itemEntity ? itemEntity.getItem() : ((ItemFrame)entity).getItem();
               markName.append(((MutableComponent)itemStack.getItem().getName(itemStack)).withStyle(ChatFormatting.BLUE));
               hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(itemStack));
           }else {
               Component showName = entity instanceof Player ? entity.getName() : entity.getType().getDescription();
               markName.append(((MutableComponent)showName).withStyle(ChatFormatting.LIGHT_PURPLE));
               hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY,new HoverEvent.EntityTooltipInfo(entity.getType(),entity.getUUID(),entity.getDisplayName()));
           }
           posMessage.append(Component.translatable(" [" + entity.blockPosition().toShortString() + "]"));
           mark = new EntitySign(entity.getUUID(),entity.blockPosition());
       }


       MutableComponent marked = Component.translatable("sign.marked");
       MutableComponent at = Component.translatable("sign.at");

       if(mark == null){
           sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
           return 0;
       }


       Sign finalMark = mark;
       HoverEvent finalHoverEvent = hoverEvent;
       MutableComponent mutableComponent = Component.empty()
//               .append(senderName.copy()
//                       .withStyle(ChatFormatting.GREEN))
               .append(marked)
               .append(markName)
               .append(at)
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
}
