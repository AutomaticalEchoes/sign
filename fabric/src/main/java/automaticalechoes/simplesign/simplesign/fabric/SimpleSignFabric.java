package automaticalechoes.simplesign.simplesign.fabric;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import automaticalechoes.simplesign.simplesign.api.command.MarkCommand;
import automaticalechoes.simplesign.simplesign.api.command.PingCommand;
import automaticalechoes.simplesign.simplesign.client.keys.ModKeyMappings;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;

public class SimpleSignFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleSign.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PingCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MarkCommand.register(dispatcher));
    }


}