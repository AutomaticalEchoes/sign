package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.keys.Keymaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT,modid = SimpleSign.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {


    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @SubscribeEvent
    public static void load(FMLLoadCompleteEvent event){
        Keymaps.init();
    }

    @SubscribeEvent
    public static void RenderLevelStageRegister(RenderLevelStageEvent.RegisterStageEvent event){
        ClientEvents.AFTER_BLOCK_ENTITIES = event.register(new ResourceLocation("after_block_entities"), null);
    }


}
