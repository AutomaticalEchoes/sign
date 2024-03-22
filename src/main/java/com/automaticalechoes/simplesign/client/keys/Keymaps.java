package com.automaticalechoes.simplesign.client.keys;

import com.automaticalechoes.simplesign.client.ClientConfig;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class Keymaps {
    public static final HashMap<KeyMapping,Runnable> KEYMAPS = new HashMap<>();


    public static KeyMapping Register(KeyMapping keyMapping,Runnable runnable){
        KEYMAPS.put(keyMapping,runnable);
        return keyMapping;
    }

    public static void Actions(){
        for (Map.Entry<KeyMapping, Runnable> entry : KEYMAPS.entrySet()) {
            if(entry.getKey().isDown()) {
                entry.getValue().run();
                return;
            }
        }
    }

    public static void init(){
        if(ClientConfig.SHOULD_REGISTER_KEYMAPPING_MARK.get()){
            Register(new KeyMapping("sign.post_sign",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,"sign.category"),Actions::PostSign);
        }

        if(ClientConfig.SHOULD_REGISTER_KEYMAPPING_GET_MARK.get()){
            Register(new KeyMapping("sign.get_sign",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G,"sign.category"),Actions::GetSign);
        }

        if(ClientConfig.SHOULD_REGISTER_KEYMAPPING_REMOVE_MARK.get()){
            Register(new KeyMapping("sign.remove_sign",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_R,"sign.category"),Actions::RemoveSign);
        }


        if(ClientConfig.SHOULD_REGISTER_KEYMAPPING_CLEAR_MARK.get()){
            Register(new KeyMapping("sign.clear_sign",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_C,"sign.category"),Actions::ClearSign);
        }

        if(ClientConfig.SHOULD_REGISTER_KEYMAPPING_SIGN_SLOT.get()){
            Register(new KeyMapping("sign.ping_head",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingHead);
            Register(new KeyMapping("sign.ping_chest",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingChest);
            Register(new KeyMapping("sign.ping_legs",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingLegs);
            Register(new KeyMapping("sign.ping_feet",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingFeet);
            Register(new KeyMapping("sign.ping_mainhand",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingMain);
            Register(new KeyMapping("sign.ping_offhand",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.CONTROL,
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingOff);
        }
        Minecraft.getInstance().options.keyMappings = ArrayUtils.addAll(Minecraft.getInstance().options.keyMappings, Keymaps.KEYMAPS.keySet().toArray(new KeyMapping[0]));
    }
}
