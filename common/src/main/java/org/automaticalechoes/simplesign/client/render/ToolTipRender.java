package org.automaticalechoes.simplesign.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.automaticalechoes.simplesign.client.ClientSign;
import org.automaticalechoes.simplesign.client.ClientSignalManager;

import java.util.List;
import java.util.Optional;

public class ToolTipRender {
    public static void RenderToolTip(Minecraft mc, GuiGraphics guiGraphics, ClientSignalManager.ILink focusSigns) {
        if(focusSigns.isEmpty()) return;
        int renderX = (int) ((0.5 + 0.02) * mc.getWindow().getGuiScaledWidth());
        int renderY = (int) (0.5 * mc.getWindow().getGuiScaledHeight());
        if(focusSigns.size() > 1){
            int i = 0;
            int offset;
            int renderY0;
            for (ClientSign sign : focusSigns){
                offset =  ((i & 1) == 0) ? (i / 2) * 20 : - (i + 1) / 2 * 20;
                renderY0 = renderY + offset;
                Component tooltipComponent = sign.getTooltipComponents().getFirst();
                guiGraphics.renderTooltip(mc.font, tooltipComponent, renderX, renderY0);
                i++;
            };
            return;
        }
        ClientSign first = focusSigns.getFirst();
        if(first.getItemStack() != null && !first.getItemStack().isEmpty()) {
            guiGraphics.renderTooltip(mc.font, first.getItemStack(), renderX, renderY);
            return;
        }
        guiGraphics.renderTooltip(mc.font, first.getTooltipComponents(), Optional.empty(), renderX, renderY);
    }
//    public static void RenderToolTip(Minecraft mc, GuiGraphics guiGraphics, ClientSign clientSign, ItemStack itemStack, int x, int y) {
//        if(dataSign instanceof ContainerDataSign containerDataSign){
//            List<Component> tooltipFromItem = Screen.getTooltipFromItem(mc, itemStack);
//            List<ItemStack> itemStacks = containerDataSign.getItemStacks();
//            Optional<TooltipComponent> bundleTooltip = Optional.of(new BundleTooltip(new BundleContents(itemStacks)));
//            guiGraphics.renderTooltip(mc.font, tooltipFromItem, bundleTooltip, x ,y);
//        }

//    }
}
