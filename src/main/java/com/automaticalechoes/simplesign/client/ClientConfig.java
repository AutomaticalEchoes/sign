package com.automaticalechoes.simplesign.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue SHOULD_SHOW_BORDER;

    static {
        BUILDER.push("simple sign config");
        SHOULD_SHOW_BORDER = BUILDER.define("should show border",true);
        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
