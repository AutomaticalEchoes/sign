package com.automaticalechoes.simplesign.client.command;

import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.common.sign.EntitySign;
import com.automaticalechoes.simplesign.common.sign.Sign;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientGetMarkCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> GETMARK =
            Commands.literal("getmark").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final RequiredArgumentBuilder<CommandSourceStack, Tag> NBT =
            Commands.argument("nbt", NbtTagArgument.nbtTag());

    public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
        p_249870_.register(GETMARK
                .then(NBT.executes(context -> GetMark(context.getSource(), NbtTagArgument.getNbtTag(context,"nbt")))));
    }

    public static int GetMark(CommandSourceStack sourceStack,Tag nbt){
        Sign mark = Sign.FromTag(nbt);
        if(mark != null){
            if(CheckMark(sourceStack,mark)) ClientEvents.SIGNS.add(mark);
        } else{
            sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
        }
        return 1;
    }

    public static boolean CheckMark(CommandSourceStack sourceStack, Sign mark){
        if(!mark.CanUse()){
            sourceStack.sendFailure(Component.translatable("sign.source_discord"));
            return false;
        }
        if(ClientEvents.SIGNS.contains(mark)){
            sourceStack.sendFailure(Component.translatable("sign.exist"));
            return false;
        }
        if(mark instanceof EntitySign entitySign && entitySign.isLocalPlayer()){
            sourceStack.sendFailure(Component.translatable("sign.self"));
            return false;
        }

        return true;
    }
}
