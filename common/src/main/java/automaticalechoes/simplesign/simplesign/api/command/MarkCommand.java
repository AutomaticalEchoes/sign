package automaticalechoes.simplesign.simplesign.api.command;


import automaticalechoes.simplesign.simplesign.SimpleSign;
import automaticalechoes.simplesign.simplesign.api.sign.message.MessageBuilder;
import automaticalechoes.simplesign.simplesign.api.sign.message.SignMessageBuilder;
import automaticalechoes.simplesign.simplesign.common.Iplayers;
import automaticalechoes.simplesign.simplesign.api.sign.Sign;
import automaticalechoes.simplesign.simplesign.api.sign.SignImp;
import automaticalechoes.simplesign.simplesign.api.sign.target.BlockTarget;
import automaticalechoes.simplesign.simplesign.api.sign.target.EntityTarget;
import automaticalechoes.simplesign.simplesign.mixin.PlayerListMixin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;


public class MarkCommand {
//    public static final LiteralArgumentBuilder<CommandSourceStack> =
//            Commands.literal("mark").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final RequiredArgumentBuilder<CommandSourceStack, Coordinates> BLOCKPOS =
            Commands.argument("blockPos", BlockPosArgument.blockPos());
    public static final RequiredArgumentBuilder<CommandSourceStack, EntitySelector> ENTITY =
            Commands.argument("entity", EntityArgument.entity());
    public static final RequiredArgumentBuilder<CommandSourceStack, Integer> MARK_TYPE =
            Commands.argument("mark_type", IntegerArgumentType.integer(0, MessageBuilder.TYPES.size() - 1));


   public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
       p_249870_.register(SimpleSign.SSI
               .then(MARK_TYPE
                       .then(ENTITY.executes(context -> Mark(context.getSource(), IntegerArgumentType.getInteger(context, "mark_type"),null ,EntityArgument.getEntity(context,"entity"))))
                       .then(BLOCKPOS.executes(context -> Mark(context.getSource(),IntegerArgumentType.getInteger(context, "mark_type"), BlockPosArgument.getBlockPos(context,"blockPos"),null)))));
   }

   public static int Mark(CommandSourceStack sourceStack, int type, @Nullable BlockPos pos, @Nullable Entity entity) throws CommandSyntaxException {
       if(sourceStack instanceof ClientCommandRegistrationEvent.ClientCommandSourceStack) throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand(), Component.literal("can not run in client")) ;
//       Component senderName = sourceStack.getPlayer().getName();
       SignMessageBuilder builder = new SignMessageBuilder();
       ServerPlayer player = sourceStack.getPlayer();

       if(player == null || !((Iplayers)player).canUse()){
           sourceStack.sendFailure(Component.translatable("sign.no_useful_time"));
           return 0;
       }

       Sign mark = null;
       ItemStack itemStack = null;
       if(pos != null){
           Block block = sourceStack.getLevel().getBlockState(pos).getBlock();
           ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
           mark = new SignImp(new BlockTarget(pos, key), type);
           builder.WithBlock(block).WithPos(pos);
       }else if(entity != null){
           if(entity instanceof ItemEntity || (entity instanceof ItemFrame itemFrame && !itemFrame.getItem().isEmpty())){
               itemStack = entity instanceof ItemEntity itemEntity ? itemEntity.getItem() : ((ItemFrame)entity).getItem();
               builder.WithItemStack(itemStack);
           }else {
               builder.WithEntity(entity);
               builder.WithHoverEntity(entity);
           }
           builder.WithPos(entity.blockPosition());
           mark = new SignImp(new EntityTarget(entity.getUUID(), entity.blockPosition(),itemStack),type);
       }

       if(mark == null){
           sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
           return 0;
       }

       SendMessage(player, sourceStack.getServer().getPlayerList(), PlayerChatMessage.unsigned(player.getUUID(),"")
               .withUnsignedContent(builder.BuildSignMessage(mark, player, entity)), ChatType.bind(ChatType.CHAT,sourceStack));
       return 1;
   }


    public static void SendMessage(ServerPlayer serverPlayer, PlayerList playerList, PlayerChatMessage playerChatMessage , ChatType.Bound bound){
        ((PlayerListMixin)playerList).invokeBroadcastChatMessage(playerChatMessage, serverPlayer1 -> {
            if(serverPlayer.getTeam() == null) return true;
            Team team1 = serverPlayer1.getTeam();
            return team1 == serverPlayer.getTeam();
        }, serverPlayer, bound);
        ((Iplayers)serverPlayer).trigger();
    }





}
