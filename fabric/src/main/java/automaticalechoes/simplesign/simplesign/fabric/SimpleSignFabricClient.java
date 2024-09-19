package automaticalechoes.simplesign.simplesign.fabric;

import automaticalechoes.simplesign.simplesign.client.keys.ModKeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
@Environment(EnvType.CLIENT)
public class SimpleSignFabricClient implements ClientModInitializer  {

    @Override
    public void onInitializeClient() {
//        ModKeyMappings.init();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> FabricClientGetMarkCommand.register(dispatcher));
    }
}
