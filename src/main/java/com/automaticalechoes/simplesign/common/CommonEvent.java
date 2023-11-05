package com.automaticalechoes.simplesign.common;

import com.automaticalechoes.simplesign.common.command.MarkCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvent {
    @SubscribeEvent
    public static void RegisterCommand(RegisterCommandsEvent event){
        MarkCommand.register(event.getDispatcher());
    }

}
