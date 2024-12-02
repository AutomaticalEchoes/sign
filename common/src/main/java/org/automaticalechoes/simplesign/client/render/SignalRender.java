package org.automaticalechoes.simplesign.client.render;

import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.automaticalechoes.simplesign.Constants;
import org.automaticalechoes.simplesign.client.ClientSign;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class SignalRender {
    public static ResourceLocation RESOURCE_DEFAULT = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,"textures/point_render/default.png");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");

    public static void render2D(GuiGraphics guiGraphics, ClientSign sign){
        Minecraft mc = Minecraft.getInstance();
        ItemStack itemStack = sign.getItemStack();
        MutableComponent distance = Component.literal(DECIMAL_FORMAT.format(sign.getDistance())).append(Component.translatable("B").withStyle(ChatFormatting.GOLD));
        Vec3 rP = sign.getRenderPosition();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(rP.x * mc.getWindow().getGuiScaledWidth(),  rP.y * mc.getWindow().getGuiScaledHeight(), rP.z);

        if(itemStack != null){
            guiGraphics.renderItem(itemStack, - 8,  - 8);
            if(rP.x > 0.45 && rP.x < 0.55 && rP.y > 0.45 && rP.y < 0.55 ){
                guiGraphics.renderTooltip(mc.font, itemStack, 0,0);
            }
        }else{
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(45.0F));
            guiGraphics.blit(RESOURCE_DEFAULT, - 12, - 12, 0, 0, 24, 24, 24, 24);
            guiGraphics.pose().popPose();
        }



        guiGraphics.drawString(mc.font, distance, - mc.font.width(distance) / 2, 10, sign.getColor().getRGB());
        guiGraphics.pose().popPose();
        sign.tick();
    }


}
