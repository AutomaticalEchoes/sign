package org.automaticalechoes.simplesign;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.automaticalechoes.simplesign.api.command.MarkCommand;
import org.automaticalechoes.simplesign.api.command.PingCommand;

@Mod.EventBusSubscriber
public class CommonEvent {
    @SubscribeEvent
    public static void RegisterCommand(RegisterCommandsEvent event){
        MarkCommand.register(event.getDispatcher());
        PingCommand.register(event.getDispatcher());
    }

}
