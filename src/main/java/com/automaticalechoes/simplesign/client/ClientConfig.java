package com.automaticalechoes.simplesign.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue SHOULD_ENTITY_GLOW;
    public static final ForgeConfigSpec.BooleanValue SHOULD_SHOW_DETAIL;

    static {
        BUILDER.push("simple sign config");
        SHOULD_ENTITY_GLOW = BUILDER.define("should show border",true);
        SHOULD_SHOW_DETAIL = BUILDER.define("should show detail(Dev)",true);
        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
