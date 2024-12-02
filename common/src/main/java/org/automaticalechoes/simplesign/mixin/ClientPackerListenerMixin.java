package org.automaticalechoes.simplesign.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import org.automaticalechoes.simplesign.api.Config;
import org.automaticalechoes.simplesign.client.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPackerListenerMixin {
    @Inject(method = "handlePlayerChat",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handlePlayerChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/network/chat/ChatType$Bound;)V"))
    public void onHandlePlayerChat(ClientboundPlayerChatPacket pPacket, CallbackInfo ci){
        if(Config.AutoReceive() && pPacket.unsignedContent() instanceof MutableComponent mutableComponent
                && mutableComponent.getStyle().getClickEvent() != null
                && mutableComponent.getStyle().getClickEvent().getValue().startsWith("/ssi_client getmark")){
            Utils.GetSignWhenReceived(mutableComponent);
        }
    }


}
