package com.automaticalechoes.simplesign.client.command;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.sign.ClientSign;
import com.automaticalechoes.simplesign.common.sign.target.EntityTarget;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.nbt.CompoundTag;
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

    public static final RequiredArgumentBuilder<CommandSourceStack, Integer> LIFECYCLE =
            Commands.argument("lifecycle", IntegerArgumentType.integer(-1, 1200));

    public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
        p_249870_.register(SimpleSign.SSI
                .then(GETMARK
                        .then(NBT
                                .executes(context -> GetMark(context.getSource(), NbtTagArgument.getNbtTag(context,"nbt"), -1))
                                .then(LIFECYCLE.executes(context -> GetMark(context.getSource(), NbtTagArgument.getNbtTag(context,"nbt"), IntegerArgumentType.getInteger(context,"lifecycle")))))));
    }

    public static int GetMark(CommandSourceStack sourceStack, Tag nbt, int lifecycle){
        if(!(nbt instanceof CompoundTag compoundTag)){
            sourceStack.sendFailure(Component.translatable("sign.unvalid_mark"));
            return 0;
        }
        ClientSign clientSignal = new ClientSign(compoundTag, lifecycle);
        if(CheckMark(sourceStack, clientSignal)){
            ClientEvents.MARK_RENDER.add(clientSignal);
        }

        return 1;
    }

    public static boolean CheckMark(CommandSourceStack sourceStack, ClientSign mark){
        if(!mark.CanUse()){
            sourceStack.sendFailure(Component.translatable("sign.source_discord"));
            return false;
        }
        if(mark.getTarget() instanceof EntityTarget entitySign && entitySign.isLocalPlayer()){
            sourceStack.sendFailure(Component.translatable("sign.self"));
            return false;
        }

        return true;
    }
}
