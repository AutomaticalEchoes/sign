package org.automaticalechoes.simplesign;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.automaticalechoes.simplesign.api.command.MarkCommand;
import org.automaticalechoes.simplesign.api.command.PingCommand;

public class Simplesign implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PingCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MarkCommand.register(dispatcher));
    }
}
