package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.command.ClientGetMarkCommand;
import com.automaticalechoes.simplesign.client.command.ClientSettingCommand;
import com.automaticalechoes.simplesign.client.keys.Keymaps;
import com.automaticalechoes.simplesign.client.render.SignalRender;
import com.automaticalechoes.simplesign.common.sign.Sign;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    public static final AbstractList<Sign> SIGNS = new ArrayList<>();
    public static final Utils.LimitList<MutableComponent> CHATS = new Utils.LimitList<>(10);

    @SubscribeEvent
    public static void RenderTick(RenderLevelStageEvent event){
        if(!SignalRender.isInitialize()) {
            SignalRender.init();
        }
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES){
            Iterator<Sign> iterator = SIGNS.iterator();
            while (iterator.hasNext()){
                Sign mark = iterator.next();
                if(!mark.CanUse()) {
                    iterator.remove();
                }else{
                    SignalRender.RenderMark(mark, event.getPoseStack(), event.getCamera(), event.getProjectionMatrix());
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void InitFOV(ViewportEvent.ComputeFov computeFov){
//        if(!SignalRender.isInitialize()) {
//            SignalRender.init();
//        }
//        SignalRender.initProjection(computeFov.getFOV(), computeFov.getPartialTick());
//    }

    @SubscribeEvent
    public static void ClientReceivedChat(ClientChatReceivedEvent.Player event){
        if( event.getPlayerChatMessage().unsignedContent() instanceof MutableComponent mutableComponent
                && mutableComponent.getStyle().getClickEvent() != null
                && mutableComponent.getStyle().getClickEvent().getValue().startsWith("/getmark")){
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
