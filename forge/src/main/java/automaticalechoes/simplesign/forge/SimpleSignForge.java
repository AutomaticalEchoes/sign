package automaticalechoes.simplesign.forge;

import dev.architectury.platform.forge.EventBuses;
import automaticalechoes.simplesign.SimpleSign;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleSign.MOD_ID)
public class SimpleSignForge {
    public SimpleSignForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SimpleSign.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SimpleSign.init();
    }
}