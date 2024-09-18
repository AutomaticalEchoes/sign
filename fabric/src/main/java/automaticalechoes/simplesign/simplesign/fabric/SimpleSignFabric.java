package automaticalechoes.simplesign.simplesign.fabric;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import automaticalechoes.simplesign.simplesign.api.command.MarkCommand;
import automaticalechoes.simplesign.simplesign.api.command.PingCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SimpleSignFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleSign.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PingCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MarkCommand.register(dispatcher));
    }
}