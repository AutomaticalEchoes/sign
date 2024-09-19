package automaticalechoes.simplesign.simplesign.fabric;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import automaticalechoes.simplesign.simplesign.api.sign.target.EntityTarget;
import automaticalechoes.simplesign.simplesign.client.ClientSign;
import automaticalechoes.simplesign.simplesign.client.command.ClientGetMarkCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

public class FabricClientGetMarkCommand {
    public static final LiteralArgumentBuilder<FabricClientCommandSource> SSI =
            ClientCommandManager.literal("ssi2").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final LiteralArgumentBuilder<FabricClientCommandSource> GETMARK =
            ClientCommandManager.literal("getmark").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final RequiredArgumentBuilder<FabricClientCommandSource, Tag> NBT =
            ClientCommandManager.argument("nbt", NbtTagArgument.nbtTag());
    public static final RequiredArgumentBuilder<FabricClientCommandSource, Integer> LIFECYCLE =
            ClientCommandManager.argument("lifecycle", IntegerArgumentType.integer(-1, 1200));

    public static void register(CommandDispatcher<FabricClientCommandSource> p_249870_) {
        p_249870_.register(SSI
                .then(GETMARK
                        .then(NBT
                                .executes(context -> GetMark(context.getSource(), NbtTagArgument.getNbtTag(context,"nbt"), -1))
                                .then(LIFECYCLE.executes(context -> GetMark(context.getSource(), NbtTagArgument.getNbtTag(context,"nbt"), IntegerArgumentType.getInteger(context,"lifecycle")))))));
    }

    public static int GetMark(FabricClientCommandSource sourceStack, Tag nbt, int lifecycle){
        if(!(nbt instanceof CompoundTag compoundTag)){
            sourceStack.sendError(Component.translatable("sign.unvalid_mark"));
            return 0;
        }
        ClientSign clientSignal = new ClientSign(compoundTag, lifecycle);
        if(CheckMark(sourceStack, clientSignal)){
            SimpleSign.Client.MARK_RENDER.add(clientSignal);
        }
        return 1;
    }

    public static boolean CheckMark(FabricClientCommandSource sourceStack, ClientSign mark){
        if(!mark.CanUse()){
            sourceStack.sendError(Component.translatable("sign.source_discord"));
            return false;
        }
        if(mark.getTarget() instanceof EntityTarget entitySign && entitySign.isLocalPlayer()){
            sourceStack.sendError(Component.translatable("sign.self"));
            return false;
        }
        return true;
    }
}
