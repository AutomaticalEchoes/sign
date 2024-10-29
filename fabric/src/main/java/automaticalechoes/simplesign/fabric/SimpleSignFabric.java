package automaticalechoes.simplesign.fabric;

import automaticalechoes.simplesign.SimpleSign;
import automaticalechoes.simplesign.api.command.MarkCommand;
import automaticalechoes.simplesign.api.command.PingCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SimpleSignFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleSign.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PingCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MarkCommand.register(dispatcher));
    }


}