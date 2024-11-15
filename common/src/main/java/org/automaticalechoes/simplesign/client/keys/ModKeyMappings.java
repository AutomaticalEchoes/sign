package org.automaticalechoes.simplesign.client.keys;


import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import org.automaticalechoes.simplesign.api.Config;
import org.automaticalechoes.simplesign.api.IOptions;

import java.util.HashMap;
import java.util.Map;


public class ModKeyMappings {
    public static final String MOD_CATEGORY = "sign.category";
    public static final HashMap<KeyMapping,Runnable> KEYMAPS = new HashMap<>();
    public static void Register(KeyMapping keyMapping,Runnable runnable){
        KEYMAPS.put(keyMapping,runnable);
//        return keyMapping;
    }

    public static void Actions(){
        for (Map.Entry<KeyMapping, Runnable> entry : KEYMAPS.entrySet()) {
            if(entry.getKey().isDown()) {
                entry.getValue().run();
                return;
            }
        }
    }

    public static void init(IOptions options){
        if(Config.KeyMark()){
            Register(new KeyMapping("sign.post_sign_default", InputConstants.KEY_V,MOD_CATEGORY), () -> Actions.PostSign());
        }

//        if(Config.getSsi$keyGetMark()){
//            Register(new KeyMapping("sign.get_sign",
//                    InputConstants.KEY_G,MOD_CATEGORY),Actions::GetSign);
//        }


        if(Config.KeyRemoveMark())
        Register(new KeyMapping("sign.remove_sign",
                InputConstants.KEY_R,MOD_CATEGORY),Actions::RemoveMark);


//
        if(Config.KeyClearMark()){
            Register(new KeyMapping("sign.clear_sign",
                    InputConstants.KEY_C,MOD_CATEGORY),Actions::ClearMark);
        }

        if(Config.KeyPing()){
            Register(new KeyMapping("sign.ping_head",
                    InputConstants.KEY_F,MOD_CATEGORY),Actions::PingHead);
            Register(new KeyMapping("sign.ping_chest",
                    InputConstants.KEY_F,MOD_CATEGORY),Actions::PingChest);
            Register(new KeyMapping("sign.ping_legs",
                    InputConstants.KEY_F,MOD_CATEGORY),Actions::PingLegs);
            Register(new KeyMapping("sign.ping_feet",
                    InputConstants.KEY_F,MOD_CATEGORY),Actions::PingFeet);
            Register(new KeyMapping("sign.ping_mainhand",
                    InputConstants.KEY_F,MOD_CATEGORY),Actions::PingMain);
            Register(new KeyMapping("sign.ping_offhand",
                    InputConstants.KEY_F,MOD_CATEGORY),Actions::PingOff);
        }
        options.ssi$loadKeyMappingsDiff();
    }
}
