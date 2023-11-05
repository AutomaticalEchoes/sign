package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.SimpleSign;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT,modid = SimpleSign.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    public static final KeyMapping POST_SIGN = new KeyMapping("sign.post_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,"sign.category");

    public static final KeyMapping GET_SIGN = new KeyMapping("sign.get_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G,"sign.category");

    public static final KeyMapping REMOVE_SIGN = new KeyMapping("sign.remove_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_R,"sign.category");

    public static final KeyMapping CLEAR_SIGN = new KeyMapping("sign.clear_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_C,"sign.category");

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        });

    }

    @SubscribeEvent
    public static void RegisterKeyMapping(RegisterKeyMappingsEvent event){
        event.register(POST_SIGN);
        event.register(GET_SIGN);
        event.register(REMOVE_SIGN);
        event.register(CLEAR_SIGN);
    }







}
