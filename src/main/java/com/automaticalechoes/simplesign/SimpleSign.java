package com.automaticalechoes.simplesign;

import com.automaticalechoes.simplesign.client.ClientConfig;
import com.automaticalechoes.simplesign.client.Utils;
import com.mojang.logging.LogUtils;
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
    public static ResourceLocation SIGN_CHAT = new ResourceLocation("sign");

    public SimpleSign()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC,"simplesign_client.toml");
    }

}
