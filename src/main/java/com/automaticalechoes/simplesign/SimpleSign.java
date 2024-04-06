package com.automaticalechoes.simplesign;

import com.automaticalechoes.simplesign.client.ClientConfig;
import com.automaticalechoes.simplesign.client.Utils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleSign.MODID)
public class SimpleSign
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "simplesign";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final LiteralArgumentBuilder<CommandSourceStack> SSI =
            Commands.literal("ssi").requires(commandSourceStack -> commandSourceStack.hasPermission(0));

    public SimpleSign()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC,"simplesign_client.toml");
    }

}
