package com.automaticalechoes.simplesign;

import com.automaticalechoes.simplesign.register.BlockRegister;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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

        // Register the commonSetup method for modloading

        // Register the Deferred Register to the mod event bus so blocks get registered
        BlockRegister.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
    }

}
