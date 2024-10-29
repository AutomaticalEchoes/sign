package automaticalechoes.simplesign.client.keys;

import automaticalechoes.simplesign.api.Config;
import automaticalechoes.simplesign.api.IOptions;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModKeyMappings {
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
        ((IOptions)Minecraft.getInstance().options).equipset$removeKeyMappings();
        if(Config.getSsi$keyMarker()){
            Register(new KeyMapping("sign.post_sign_default", InputConstants.KEY_V,"sign.category"), () -> Actions.PostSign());
        }

//        if(Config.getSsi$keyGetMark()){
//            Register(new KeyMapping("sign.get_sign",
//                    InputConstants.KEY_G,"sign.category"),Actions::GetSign);
//        }

        if(Config.getSsi$keyRemoveMark()){
            Register(new KeyMapping("sign.remove_sign",
                    InputConstants.KEY_R,"sign.category"),Actions::RemoveMark);
        }


        if(Config.getSsi$keyClearMark()){
            Register(new KeyMapping("sign.clear_sign",
                    InputConstants.KEY_C,"sign.category"),Actions::ClearMark);
        }

        if(Config.getSsi$keySignSlot()){
            Register(new KeyMapping("sign.ping_head",
                    InputConstants.KEY_F,"sign.category"),Actions::PingHead);
            Register(new KeyMapping("sign.ping_chest",
                    InputConstants.KEY_F,"sign.category"),Actions::PingChest);
            Register(new KeyMapping("sign.ping_legs",
                    InputConstants.KEY_F,"sign.category"),Actions::PingLegs);
            Register(new KeyMapping("sign.ping_feet",
                    InputConstants.KEY_F,"sign.category"),Actions::PingFeet);
            Register(new KeyMapping("sign.ping_mainhand",
                    InputConstants.KEY_F,"sign.category"),Actions::PingMain);
            Register(new KeyMapping("sign.ping_offhand",
                    InputConstants.KEY_F,"sign.category"),Actions::PingOff);
        }
        ((IOptions)Minecraft.getInstance().options).equipset$loadKeyMappings();
    }
}
