package com.automaticalechoes.simplesign.client;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.render.SignalRender;
import com.automaticalechoes.simplesign.client.command.ClientGetMarkCommand;
import com.automaticalechoes.simplesign.common.sign.Sign;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    public static final AbstractList<Sign> SIGNS = new ArrayList<>();
    public static final Utils.LimitList<MutableComponent> CHATS = new Utils.LimitList<>(10);
    public static ChatType CHAT_TYPE = null;

    public static SignalRender signalBlockRender;

    @SubscribeEvent
    public static void RenderTick(RenderLevelStageEvent event){
        if(signalBlockRender == null) signalBlockRender = new SignalRender();
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES){
            Iterator<Sign> iterator = SIGNS.iterator();
            while (iterator.hasNext()){
                Sign mark = iterator.next();
                if(!mark.CanUse()) {
                    iterator.remove();
                }else{
                    signalBlockRender.RenderMark(mark, event.getPoseStack(), event.getCamera());
                }

            }

        }
    }

    @SubscribeEvent
    public static void ClientReceivedChat(ClientChatReceivedEvent.Player event){
        if(CHAT_TYPE == null){
            Registry<ChatType> registry = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.CHAT_TYPE);
            CHAT_TYPE = registry.get(SimpleSign.SIGN_CHAT);
        }
        if(event.getBoundChatType().chatType() == CHAT_TYPE && event.getPlayerChatMessage().unsignedContent() instanceof MutableComponent mutableComponent){
            CHATS.add(mutableComponent);
        }
    }

    @SubscribeEvent
    public static void RegisterClientCommand(RegisterClientCommandsEvent event){
        ClientGetMarkCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void KeyPass(InputEvent.Key event){
        if(ClientModEvents.POST_SIGN.isDown()){
            PostSign();
        }else if(ClientModEvents.GET_SIGN.isDown()){
            GetSign();
        }else if(ClientModEvents.REMOVE_SIGN.isDown() && SIGNS.size() > 0){
            SIGNS.remove(SIGNS.size() - 1);
        }else if(ClientModEvents.CLEAR_SIGN.isDown()){
            SIGNS.clear();
        }
    }

    public static void PostSign(){
        HitResult hitResult = Utils.IPick(1.0F);
        String message = "";
        if(hitResult instanceof BlockHitResult blockHitResult && blockHitResult.getType() != HitResult.Type.MISS){
            BlockPos blockPos = blockHitResult.getBlockPos();
            message = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
        }else if(hitResult instanceof EntityHitResult entityHitResult){
            message = entityHitResult.getEntity().getUUID().toString();
        }

        if(message.equals("")) return;

        String s1 = SharedConstants.filterText("/mark " + message);
        if (s1.startsWith("/")) {
            if (!Minecraft.getInstance().player.connection.sendUnsignedCommand(s1.substring(1))) {
                SimpleSign.LOGGER.error("Not allowed to run command with signed argument from click event: '{}'", (Object)s1);
            }
        } else {
            SimpleSign.LOGGER.error("Failed to run command without '/' prefix from click event: '{}'", (Object)s1);
        }
    }

    public static void GetSign(){
        if(CHATS.isEmpty())return;
        MutableComponent component = CHATS.getLast();
        String value = component.getStyle().getClickEvent().getValue();
        ClientCommandHandler.runCommand(value.substring(1));
    }

}
