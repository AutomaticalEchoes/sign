package com.automaticalechoes.simplesign.client.keys;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.Utils;
import com.automaticalechoes.simplesign.common.sign.EntitySign;
import com.automaticalechoes.simplesign.common.sign.Sign;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.AbstractList;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class Actions {
    public static final AbstractList<Sign> SIGNS = ClientEvents.SIGNS;
    public static final Utils.LimitList<MutableComponent> CHATS = ClientEvents.CHATS;
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
        SendCommand(s1);

    }

    public static void GetSign(){
        if(CHATS.isEmpty())return;
        MutableComponent component = CHATS.getLast();
        String value = component.getStyle().getClickEvent().getValue();
        ClientCommandHandler.runCommand(value.substring(1));
    }
    
    public static void RemoveSign(){
        if(SIGNS.size() > 0){
            SIGNS.remove(SIGNS.size() - 1);
        }
    }
    
    public static void ClearSign(){
        SIGNS.clear();
    }

    public static void Ping(String part){
        HitResult hitResult = Utils.IPick(1.0F);
        if(hitResult instanceof EntityHitResult entityHitResult){
           String uuid = entityHitResult.getEntity().getUUID().toString();
            String s1 = SharedConstants.filterText("/mark " + uuid + " " + part);
            SendCommand(s1);
        }
    }

    public static void PingMain(){
        Ping("weapon.mainhand");
    }

    public static void PingOff(){
        Ping("weapon.offhand");
    }

    public static void PingHead(){
        Ping("armor.head");
    }

    public static void PingChest(){
        Ping("armor.chest");
    }

    public static void PingLegs(){
        Ping("armor.legs");
    }

    public static void PingFeet(){
        Ping("armor.feet");
    }

    public static void SendCommand(String command){
        if (command.startsWith("/")) {
            if (!Minecraft.getInstance().player.connection.sendUnsignedCommand(command.substring(1))) {
                SimpleSign.LOGGER.error("Not allowed to run command with signed argument from click event: '{}'", command);
            }
        } else {
            SimpleSign.LOGGER.error("Failed to run command without '/' prefix from click event: '{}'", command);
        }
    }
}
