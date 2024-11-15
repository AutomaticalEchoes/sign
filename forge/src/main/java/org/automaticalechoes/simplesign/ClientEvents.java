package org.automaticalechoes.simplesign;

import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.automaticalechoes.simplesign.client.keys.ModKeyMappings;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

//    @SubscribeEvent
//    public static void Render(RenderGuiOverlayEvent.Post event){
//        if(event.getOverlay().overlay() == VanillaGuiOverlay.HOTBAR.type().overlay()){
//            SimpleSign.Client.MARK_RENDER.render2D(event);
////            SIGNS.renderViewRot(event);
//        }
//    }


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
            GetSignWhenReceived(mutableComponent);
        }
    }

    public static void GetSignWhenReceived(MutableComponent component){
        String value = component.getStyle().getClickEvent().getValue();
//        ClientCommandHandler.runCommand(value.substring(1) + " " + Config.DEFAULT_MARK_KEEP_TIME.get() * 20);
    }

    @SubscribeEvent
    public static void RegisterClientCommand(RegisterClientCommandsEvent event){
        ForgeClientGetMarkCommand.register(event.getDispatcher());
//        ClientSettingCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void KeyPass(InputEvent.Key event){
        ModKeyMappings.Actions();
    }


}
