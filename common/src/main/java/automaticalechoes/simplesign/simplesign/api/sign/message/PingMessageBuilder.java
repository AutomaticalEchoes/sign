package automaticalechoes.simplesign.simplesign.api.sign.message;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PingMessageBuilder implements MessageBuilder{

    public static MutableComponent BuildPingMessage(Entity entity, ItemStack itemStack, EquipmentSlot equipmentSlot) {
        Component markName = entity instanceof Player ? entity.getName() : entity.getType().getDescription();
        MutableComponent itemName = itemStack.isEmpty()? NONE.withStyle(ChatFormatting.GRAY) :
                ((MutableComponent) itemStack.getItem().getName(itemStack)).withStyle(STYLE_ITEM);
        return Component.empty()
                .append(MARKED)
                .append(((MutableComponent)markName).withStyle(STYLE_ENTITY))
                .append(_S)
                .append(Component.translatable("sign." + equipmentSlot.getName()))
                .append(itemName)
                .withStyle(style -> style.withColor(ChatFormatting.GRAY).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(itemStack))
                ));
    }
}
