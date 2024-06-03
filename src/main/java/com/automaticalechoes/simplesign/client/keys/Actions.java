package com.automaticalechoes.simplesign.client.keys;

import com.automaticalechoes.simplesign.SimpleSign;
import com.automaticalechoes.simplesign.client.ClientConfig;
import com.automaticalechoes.simplesign.client.ClientEvents;
import com.automaticalechoes.simplesign.client.Utils;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientCommandHandler;

@OnlyIn(Dist.CLIENT)
public class Actions {
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
        String s1 = SharedConstants.filterText("/ssi mark " + message);
        SendCommand(s1);

    }

    public static void GetSignWhenReceived(MutableComponent component){
        String value = component.getStyle().getClickEvent().getValue();
        ClientCommandHandler.runCommand(value.substring(1) + " " + ClientConfig.DEFAULT_MARK_KEEP_TIME.get());
    }

    public static void RemoveMark(){
        if(ClientEvents.MARK_RENDER.size() > 0){
            ClientEvents.MARK_RENDER.remove(ClientEvents.MARK_RENDER.size() - 1);
        }
    }

    public static void ClearMark(){
        ClientEvents.MARK_RENDER.clear();
    }

    public static void Ping(String part){
        HitResult hitResult = Utils.IPick(1.0F);
        if(hitResult instanceof EntityHitResult entityHitResult){
           String uuid = entityHitResult.getEntity().getUUID().toString();
            String s1 = SharedConstants.filterText("/ssi mark " + uuid + " " + part);
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
