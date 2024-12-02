package org.automaticalechoes.simplesign;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.automaticalechoes.simplesign.api.Config;
import org.automaticalechoes.simplesign.client.keys.Actions;
import org.automaticalechoes.simplesign.client.keys.ModKeyMappings;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    static Integer KeyTime = 0;

    @SubscribeEvent
    public static void ClientTick(TickEvent.ClientTickEvent.Pre event){
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
