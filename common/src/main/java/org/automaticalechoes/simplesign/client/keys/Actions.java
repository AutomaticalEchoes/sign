package org.automaticalechoes.simplesign.client.keys;



import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringUtil;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.StringUtils;
import org.automaticalechoes.simplesign.Constants;
import org.automaticalechoes.simplesign.api.Config;
import org.automaticalechoes.simplesign.client.Utils;


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
        if(message.isEmpty()) return;
        String s1 = "/ssi 0 %s".formatted(message);
        SendCommand(s1);

    }

    public static void RemoveMark(){
        Constants.Client.CLIENT_SIGNS.removeLast();
    }

    public static void ClearMark(){
        Constants.Client.CLIENT_SIGNS.clear();
    }

    public static void Ping(String part){
        HitResult hitResult = Utils.IPick(1.0F);
        if(hitResult instanceof EntityHitResult entityHitResult){
           String uuid = entityHitResult.getEntity().getUUID().toString();
           String s1 = "/ssi ping %s %s".formatted(uuid, part);
           SendCommand(s1);
        }
    }

    public static void AutoReceive(String part){
        SendCommand(StringUtil.trimChatMessage(StringUtils.normalizeSpace((part + " %s".formatted( Config.AutoReceiveKeepTime() * 100)).trim())));
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
            Minecraft.getInstance().player.connection.sendCommand(command.substring(1));
//            if (!)) {
//                Constants.LOG.error("Not allowed to run command with signed argument from click event: '{}'", command);
//            }
        } else {
            Constants.LOG.error("Failed to run command without '/' prefix from click event: '{}'", command);
        }
    }
}
