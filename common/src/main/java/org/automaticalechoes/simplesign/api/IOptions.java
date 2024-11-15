package org.automaticalechoes.simplesign.api;

import org.spongepowered.asm.mixin.Unique;

public interface IOptions {

    @Unique
    void ssi$loadKeyMappingsDiff();
}
