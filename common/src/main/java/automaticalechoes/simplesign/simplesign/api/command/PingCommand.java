package automaticalechoes.simplesign.simplesign.api.command;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import automaticalechoes.simplesign.simplesign.api.sign.message.PingMessageBuilder;
import automaticalechoes.simplesign.simplesign.common.Iplayers;
import automaticalechoes.simplesign.simplesign.mixin.PlayerListMixin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

public class PingCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> PING =
            Commands.literal("ping").requires(commandSourceStack -> commandSourceStack.hasPermission(0));
    public static final RequiredArgumentBuilder<CommandSourceStack, EntitySelector> ENTITY =
            Commands.argument("entity", EntityArgument.entity());
    public static final RequiredArgumentBuilder<CommandSourceStack, Integer> SLOT =
            Commands.argument("equip", SlotArgument.slot());

    public static void register(CommandDispatcher<CommandSourceStack> p_249870_) {
        p_249870_.register(SimpleSign.SSI
                .then(PING
                        .then(ENTITY
                                .then(SLOT.executes(context -> PingSlot(context.getSource(), EntityArgument.getEntity(context,"entity"),SlotArgument.getSlot(context,"equip")))))
                        ));
    }

    public static int PingSlot(CommandSourceStack sourceStack, net.minecraft.world.entity.Entity entity, Integer slotNum){
        SlotAccess slot = entity.getSlot(slotNum);
        EquipmentSlot equipmentSlot = getEquipmentSlot(slotNum);
        if(slot!= SlotAccess.NULL && equipmentSlot != null){
            ItemStack itemStack = slot.get();
            MutableComponent mutableComponent = PingMessageBuilder.BuildPingMessage(entity, itemStack, equipmentSlot);
            MarkCommand.SendMessage(sourceStack.getPlayer(), sourceStack.getServer().getPlayerList(), PlayerChatMessage.unsigned(sourceStack.getPlayer().getUUID(),"")
                    .withUnsignedContent(mutableComponent), ChatType.bind(ChatType.CHAT,sourceStack));
        }
        return 0;
    }

    @Nullable
    private static EquipmentSlot getEquipmentSlot(int p_147212_) {
        if (p_147212_ == 100 + EquipmentSlot.HEAD.getIndex()) {
            return EquipmentSlot.HEAD;
        } else if (p_147212_ == 100 + EquipmentSlot.CHEST.getIndex()) {
            return EquipmentSlot.CHEST;
        } else if (p_147212_ == 100 + EquipmentSlot.LEGS.getIndex()) {
            return EquipmentSlot.LEGS;
        } else if (p_147212_ == 100 + EquipmentSlot.FEET.getIndex()) {
            return EquipmentSlot.FEET;
        } else if (p_147212_ == 98) {
            return EquipmentSlot.MAINHAND;
        } else {
            return p_147212_ == 99 ? EquipmentSlot.OFFHAND : null;
        }
    }
}
