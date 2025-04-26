package org.automaticalechoes.simplesign.mixin;


import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.automaticalechoes.simplesign.common.Iplayers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerMixin implements Iplayers {
    @Shadow @Final public MinecraftServer server;
    private static final String SSI_TIME = "ssi_use_time";
    @Unique
    private int simpleSign$ssiUseTime = 5;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci){
        if( simpleSign$ssiUseTime <= 10 && this.server.getTickCount() % 100 == 0) simpleSign$ssiUseTime++;
    }

    public boolean simpleSign$canUse(){
        return simpleSign$ssiUseTime > 0;
    }

    public void simpleSign$trigger(){
        simpleSign$ssiUseTime--;
    }

}
