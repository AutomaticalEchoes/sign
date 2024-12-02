package org.automaticalechoes.simplesign;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.automaticalechoes.simplesign.client.ClientSignalQue;
import org.automaticalechoes.simplesign.client.render.SignalRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "simplesign";
    public static final String MOD_NAME = "SimpleSign";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final LiteralArgumentBuilder<CommandSourceStack> SSI =
            Commands.literal("ssi").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static void init() {


    }

    public static class Client{
        public static final ClientSignalQue CLIENT_SIGNS = new ClientSignalQue(15);

    }
}
