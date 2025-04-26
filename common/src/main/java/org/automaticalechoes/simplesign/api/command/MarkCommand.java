package org.automaticalechoes.simplesign.api.command;



import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.scores.Team;
import org.automaticalechoes.simplesign.Constants;
import org.automaticalechoes.simplesign.api.sign.Sign;
import org.automaticalechoes.simplesign.api.sign.SignImp;
import org.automaticalechoes.simplesign.api.sign.message.MessageBuilder;
import org.automaticalechoes.simplesign.api.sign.message.SignMessageBuilder;
import org.automaticalechoes.simplesign.api.sign.target.BlockTarget;
import org.automaticalechoes.simplesign.api.sign.target.EntityTarget;
import org.automaticalechoes.simplesign.common.Iplayers;
import org.automaticalechoes.simplesign.mixin.PlayerListMixin;
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
       p_249870_.register(Constants.SSI
               .then(MARK_TYPE
                       .then(ENTITY.executes(context -> Mark(context.getSource(), IntegerArgumentType.getInteger(context, "mark_type"),null ,EntityArgument.getEntity(context,"entity"))))
                       .then(BLOCKPOS.executes(context -> Mark(context.getSource(),IntegerArgumentType.getInteger(context, "mark_type"), BlockPosArgument.getBlockPos(context,"blockPos"),null)))));
   }

   public static int Mark(CommandSourceStack sourceStack, int type, @Nullable BlockPos pos, @Nullable Entity entity) throws CommandSyntaxException {
//       if(sourceStack.getLevel()) throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand(), Component.literal("can not run in client")) ;
//       Component senderName = sourceStack.getPlayer().getName();
       SignMessageBuilder builder = new SignMessageBuilder();
       ServerPlayer player = sourceStack.getPlayer();

       if(player == null || !((Iplayers)player).simpleSign$canUse()){
           sourceStack.sendFailure(Component.translatable("sign.no_useful_time"));
           return 0;
       }
       Level serverlevel = player.level();

       Sign mark = null;
       ItemStack itemStack = null;
       if(pos != null){
           Block block = sourceStack.getLevel().getBlockState(pos).getBlock();
           ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
           boolean hasBlockEntity = serverlevel.getBlockEntity(pos) != null;
           builder.WithBlock(block).WithPos(pos);
           mark = new SignImp(BlockTarget.Create(pos, key,hasBlockEntity), type);
       }else if(entity != null){
           if(entity instanceof ItemEntity || (entity instanceof ItemFrame itemFrame && !itemFrame.getItem().isEmpty())){
               itemStack = entity instanceof ItemEntity itemEntity ? itemEntity.getItem() : ((ItemFrame)entity).getItem();
               builder.WithItemStack(itemStack);
           }else {
               builder.WithEntity(entity);
           }
           builder.WithPos(entity.blockPosition());
           mark = new SignImp(new EntityTarget(entity.getUUID(), entity.blockPosition(), entity, itemStack),type);
       }

       if(mark == null){
           sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
           return 0;
       }
       MutableComponent signMessage = builder.BuildSignMessage(mark, player, entity);
       SendMessage(player, sourceStack.getServer().getPlayerList(), PlayerChatMessage.unsigned(player.getUUID(),"")
               .withUnsignedContent(signMessage), ChatType.bind(ChatType.CHAT,sourceStack));
       return 1;
   }


    public static void SendMessage(ServerPlayer serverPlayer, PlayerList playerList, PlayerChatMessage playerChatMessage , ChatType.Bound bound){
        ((PlayerListMixin)playerList).invokeBroadcastChatMessage(playerChatMessage, serverPlayer1 -> {
            if(serverPlayer.getTeam() == null) return true;
            Team team1 = serverPlayer1.getTeam();
            return team1 == serverPlayer.getTeam();
        }, serverPlayer, bound);
        ((Iplayers)serverPlayer).simpleSign$trigger();
    }





}
