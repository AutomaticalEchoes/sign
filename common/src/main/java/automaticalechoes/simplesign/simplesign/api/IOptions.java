package automaticalechoes.simplesign.simplesign.api;

import org.spongepowered.asm.mixin.Unique;

public interface IOptions {

    @Unique
    void equipset$loadKeyMappings();

    @Unique
    void equipset$removeKeyMappings();
}
