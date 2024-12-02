package org.automaticalechoes.simplesign;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.automaticalechoes.simplesign.api.command.MarkCommand;
import org.automaticalechoes.simplesign.api.command.PingCommand;

@EventBusSubscriber
public class CommonEvent {
    @SubscribeEvent
    public static void RegisterCommand(RegisterCommandsEvent event){
        MarkCommand.register(event.getDispatcher());
        PingCommand.register(event.getDispatcher());
    }

}
