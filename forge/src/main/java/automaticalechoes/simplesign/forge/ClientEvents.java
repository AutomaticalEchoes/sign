package automaticalechoes.simplesign.forge;

import automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.command.ClientSettingCommand;
import com.automaticalechoes.simplesign.client.keys.Keymaps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void Render(RenderGuiOverlayEvent.Post event){
        if(event.getOverlay().overlay() == VanillaGuiOverlay.HOTBAR.type().overlay()){
            SimpleSign.Client.MARK_RENDER.render2D(event);
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
            GetSignWhenReceived(mutableComponent);
        }
    }

    public static void GetSignWhenReceived(MutableComponent component){
        String value = component.getStyle().getClickEvent().getValue();
        ClientCommandHandler.runCommand(value.substring(1) + " " + ClientConfig.DEFAULT_MARK_KEEP_TIME.get() * 20);
    }

    @SubscribeEvent
    public static void RegisterClientCommand(RegisterClientCommandsEvent event){
        ForgeClientGetMarkCommand.register(event.getDispatcher());
        ClientSettingCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void KeyPass(InputEvent.Key event){
        Keymaps.Actions();
    }


}
