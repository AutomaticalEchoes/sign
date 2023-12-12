package com.automaticalechoes.simplesign;

import com.automaticalechoes.simplesign.client.ClientConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.core.BlockPos;
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

    public SimpleSign()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC,"simplesign_client.toml");
    }

    public static BlockPos getBlockPos(CommandContext<CommandSourceStack> p_265651_, String p_265039_) {
        return p_265651_.getArgument(p_265039_, Coordinates.class).getBlockPos(p_265651_.getSource());
    }

}
