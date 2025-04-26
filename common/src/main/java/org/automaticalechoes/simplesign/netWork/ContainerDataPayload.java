package org.automaticalechoes.simplesign.netWork;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ContainerDataPayload implements CustomPacketPayload {
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
