package com.automaticalechoes.simplesign.mixin;

import com.automaticalechoes.simplesign.common.Iplayers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerMixin implements Iplayers {
    @Shadow @Final public MinecraftServer server;
    private static final String SSI_TIME = "ssi_use_time";
    private int ssiUseTime = 5;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci){
        if(this.server.getTickCount() % 300 == 0) ssiUseTime = 5;
    }

    public boolean canUse(){
        return ssiUseTime > 0;
    }

    public void trigger(){
        ssiUseTime --;
    }

}
