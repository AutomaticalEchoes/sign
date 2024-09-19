package automaticalechoes.simplesign.simplesign;

import automaticalechoes.simplesign.simplesign.client.keys.ModKeyMappings;
import automaticalechoes.simplesign.simplesign.client.render.SignalRenderQue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;


public class SimpleSign
{
	public static final String MOD_ID = "simplesign";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final LiteralArgumentBuilder<CommandSourceStack> SSI =
			Commands.literal("ssi").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
	public static void init() {


	}

	@Environment(EnvType.CLIENT)
	public static class Client{
		public static final SignalRenderQue MARK_RENDER = new SignalRenderQue(15);
		public static void init(){

		}
	}
}
