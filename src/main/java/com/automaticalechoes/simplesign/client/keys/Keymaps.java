package com.automaticalechoes.simplesign.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class Keymaps {
    public static final HashMap<KeyMapping,Runnable> KEYMAPS = new HashMap<>();

    public static final KeyMapping POST_SIGN = Register(new KeyMapping("sign.post_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,"sign.category"),Actions::PostSign);

    public static final KeyMapping GET_SIGN = Register(new KeyMapping("sign.get_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G,"sign.category"),Actions::GetSign);

    public static final KeyMapping REMOVE_SIGN = Register(new KeyMapping("sign.remove_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_R,"sign.category"),Actions::RemoveSign);

    public static final KeyMapping CLEAR_SIGN = Register(new KeyMapping("sign.clear_sign",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_C,"sign.category"),Actions::ClearSign);

    public static final KeyMapping PING_ENTITY_HEAD = Register(new KeyMapping("sign.ping_head",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingHead);

    public static final KeyMapping PING_ENTITY_CHEST = Register(new KeyMapping("sign.ping_chest",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingChest);

    public static final KeyMapping PING_ENTITY_LEGS = Register(new KeyMapping("sign.ping_legs",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingLegs);

    public static final KeyMapping PING_ENTITY_FEET = Register(new KeyMapping("sign.ping_feet",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingFeet);

    public static final KeyMapping PING_ENTITY_MAIN = Register(new KeyMapping("sign.ping_mainhand",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingMain);

    public static final KeyMapping PING_ENTITY_OFF = Register(new KeyMapping("sign.ping_offhand",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"sign.category"),Actions::PingOff);


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

//    public static void init(RegisterKeyMappingsEvent event){
//        for (KeyMapping keymap : KEYMAPS.keySet()) {
//            event.register(keymap);
//        }
//
//    }
}
