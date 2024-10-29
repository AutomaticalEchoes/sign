package automaticalechoes.simplesign.forge;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvent {
    @SubscribeEvent
    public static void RegisterCommand(RegisterCommandsEvent event){
        ForgeClientGetMarkCommand.register(event.getDispatcher());
    }

}
