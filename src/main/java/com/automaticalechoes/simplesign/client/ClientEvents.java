package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.client.command.ClientGetMarkCommand;
import com.automaticalechoes.simplesign.client.command.ClientSettingCommand;
import com.automaticalechoes.simplesign.client.keys.Keymaps;
import com.automaticalechoes.simplesign.client.sign.SignManager;
import com.automaticalechoes.simplesign.client.render.SignalRender;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    public static final SignManager SIGNS = new SignManager(15);
    public static final Utils.LimitList<MutableComponent> CHATS = new Utils.LimitList<>(10);


    @SubscribeEvent
    public static void Render(RenderGuiOverlayEvent.Post event){
        if(event.getOverlay().overlay() == VanillaGuiOverlay.HOTBAR.type().overlay()){
            SIGNS.render2D(event);
//            SIGNS.renderViewRot(event);
        }
    }


    @SubscribeEvent
    public static void RenderTick(RenderLevelStageEvent event){
        if(!SignalRender.isInitialize()){
            SignalRender.init();
        }
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES){
            SIGNS.render3D(event);
        }
    }


    @SubscribeEvent
    public static void ClientReceivedChat(ClientChatReceivedEvent.Player event){
        if( event.getPlayerChatMessage().unsignedContent() instanceof MutableComponent mutableComponent
                && mutableComponent.getStyle().getClickEvent() != null
                && mutableComponent.getStyle().getClickEvent().getValue().startsWith("/ssi getmark")){
            CHATS.add(mutableComponent);
        }
    }

    @SubscribeEvent
    public static void RegisterClientCommand(RegisterClientCommandsEvent event){
        ClientGetMarkCommand.register(event.getDispatcher());
        ClientSettingCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void KeyPass(InputEvent.Key event){
        Keymaps.Actions();
    }


}
