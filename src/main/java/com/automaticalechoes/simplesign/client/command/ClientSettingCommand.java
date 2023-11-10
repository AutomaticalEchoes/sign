package com.automaticalechoes.simplesign.client.command;

import com.automaticalechoes.simplesign.client.ClientConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.nbt.Tag;

public class ClientSettingCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> SSI_SETTING =
            Commands.literal("ssi").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final LiteralArgumentBuilder<CommandSourceStack> SHOULD_BORDER = Commands.literal("shouldBorder");
    public static final RequiredArgumentBuilder<CommandSourceStack, Boolean> SHOULD_BORDER_VALUE =
            Commands.argument("border", BoolArgumentType.bool());

    public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
        p_249870_.register(SSI_SETTING
                .then(SHOULD_BORDER
                        .then(SHOULD_BORDER_VALUE.executes(context -> {
                            ClientConfig.SHOULD_SHOW_BORDER.set(BoolArgumentType.getBool(context,"border"));
                            return 1;
                        })
                        )
                )
        );

    }
}
