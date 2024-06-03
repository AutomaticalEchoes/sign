package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.client.command.ClientGetMarkCommand;
import com.automaticalechoes.simplesign.client.command.ClientSettingCommand;
import com.automaticalechoes.simplesign.client.keys.Actions;
import com.automaticalechoes.simplesign.client.keys.Keymaps;
import com.automaticalechoes.simplesign.client.sign.SignalRenderQue;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    public static final SignalRenderQue MARK_RENDER = new SignalRenderQue(15);
    @SubscribeEvent
    public static void Render(RenderGuiOverlayEvent.Post event){
        if(event.getOverlay().overlay() == VanillaGuiOverlay.HOTBAR.type().overlay()){
            MARK_RENDER.render2D(event);
//            SIGNS.renderViewRot(event);
        }
    }


//    @SubscribeEvent
//    public static void RenderTick(RenderLevelStageEvent event){
//        if(!SignalRender.isInitialize()){
//            SignalRender.init();
//        }
//        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES){
//            SIGNS.render3D(event);
//        }
//    }


    @SubscribeEvent
    public static void ClientReceivedChat(ClientChatReceivedEvent.Player event){
        if( event.getPlayerChatMessage().unsignedContent() instanceof MutableComponent mutableComponent
                && mutableComponent.getStyle().getClickEvent() != null
                && mutableComponent.getStyle().getClickEvent().getValue().startsWith("/ssi getmark")){
            Actions.GetSignWhenReceived(mutableComponent);
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
