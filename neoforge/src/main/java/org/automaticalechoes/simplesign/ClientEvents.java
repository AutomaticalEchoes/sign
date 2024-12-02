package org.automaticalechoes.simplesign;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import org.automaticalechoes.simplesign.client.keys.ModKeyMappings;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    static Integer KeyTime = 0;

    @SubscribeEvent
    public static void ClientTick(ClientTickEvent.Pre event){
        if(KeyTime > 0) KeyTime--;
    }

    @SubscribeEvent
    public static void RegisterClientCommand(RegisterClientCommandsEvent event){
        ForgeClientGetMarkCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void KeyPass(InputEvent.Key event){
        if(KeyTime <= 0 && ModKeyMappings.Actions()) KeyTime = 20;
    }


}
