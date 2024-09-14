package automaticalechoes.simplesign.simplesign.fabric;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import net.fabricmc.api.ModInitializer;

public class SimpleSignFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleSign.init();
    }
}