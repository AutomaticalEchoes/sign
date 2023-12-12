package com.automaticalechoes.simplesign.client.command;

import com.automaticalechoes.simplesign.client.ClientConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ClientSettingCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> SSI_SETTING =
            Commands.literal("ssi").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final LiteralArgumentBuilder<CommandSourceStack> ENTITY_GLOW = Commands.literal("shouldEntityGlow");
    public static final RequiredArgumentBuilder<CommandSourceStack, Boolean> SHOULD_ENTITY_GLOW =
            Commands.argument("entity_glow", BoolArgumentType.bool());

    public static final LiteralArgumentBuilder<CommandSourceStack> SHOW_DETAIL_DEV = Commands.literal("showDetail(Dev)");
    public static final RequiredArgumentBuilder<CommandSourceStack, Boolean> SHOULD_SHOW_DETAIL =
            Commands.argument("detail", BoolArgumentType.bool());

    public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
        p_249870_.register(SSI_SETTING
                .then(ENTITY_GLOW
                        .then(SHOULD_ENTITY_GLOW.executes(context -> {
                            ClientConfig.SHOULD_ENTITY_GLOW.set(BoolArgumentType.getBool(context,"entity_glow"));
                            return 1;
                        }))
                )
                .then(SHOW_DETAIL_DEV
                        .then(SHOULD_SHOW_DETAIL.executes(context -> {
                            ClientConfig.SHOULD_SHOW_DETAIL.set(BoolArgumentType.getBool(context, "detail"));
                            return 1;
                        }))
                )
        );

    }
}
