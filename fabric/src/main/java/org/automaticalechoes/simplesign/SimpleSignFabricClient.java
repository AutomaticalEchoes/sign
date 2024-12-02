package org.automaticalechoes.simplesign;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import org.automaticalechoes.simplesign.client.keys.ModKeyMappings;

@Environment(EnvType.CLIENT)
public class SimpleSignFabricClient implements ClientModInitializer  {
    static Integer KeyTime = 0;

    @Override
    public void onInitializeClient() {
        KeyBindingRegistryImpl.addCategory(ModKeyMappings.MOD_CATEGORY);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {if(KeyTime <= 0 && ModKeyMappings.Actions()) KeyTime = 20;});
        ClientTickEvents.END_CLIENT_TICK.register(client -> {if(KeyTime > 0) KeyTime--;});
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> FabricClientGetMarkCommand.register(dispatcher));

    }
}
