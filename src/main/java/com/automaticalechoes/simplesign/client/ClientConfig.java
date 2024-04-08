package com.automaticalechoes.simplesign.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue SHOULD_ENTITY_GLOW;
    public static final ForgeConfigSpec.BooleanValue SHOULD_SHOW_DETAIL;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_MARK_DEFAULT;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_MARK_CARE;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_MARK_FOCUS;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_MARK_QUEST;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_GET_MARK;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_REMOVE_MARK;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_CLEAR_MARK;
    public static final ForgeConfigSpec.BooleanValue SHOULD_REGISTER_KEYMAPPING_SIGN_SLOT;
    static {
        BUILDER.push("simple sign config");
        SHOULD_ENTITY_GLOW = BUILDER.define("should show border",true);
        SHOULD_SHOW_DETAIL = BUILDER.define("should show detail(Dev)",true);
        SHOULD_REGISTER_KEYMAPPING_MARK_DEFAULT = BUILDER.define("should register keymapping mark DEFAULT(ctrl + v)", true);
        SHOULD_REGISTER_KEYMAPPING_MARK_CARE = BUILDER.define("should register keymapping mark CARE(ctrl + v)", true);
        SHOULD_REGISTER_KEYMAPPING_MARK_FOCUS = BUILDER.define("should register keymapping mark FOCUS(ctrl + v)", true);
        SHOULD_REGISTER_KEYMAPPING_MARK_QUEST = BUILDER.define("should register keymapping mark QUEST(ctrl + v)", true);
        SHOULD_REGISTER_KEYMAPPING_GET_MARK = BUILDER.define("should register keymapping get mark(ctrl + g)", true);
        SHOULD_REGISTER_KEYMAPPING_REMOVE_MARK = BUILDER.define("should register keymapping remove mark(ctrl + r)", true);
        SHOULD_REGISTER_KEYMAPPING_CLEAR_MARK = BUILDER.define("should register keymapping clear mark(ctrl + c)", true);
        SHOULD_REGISTER_KEYMAPPING_SIGN_SLOT = BUILDER.define("should register all keymapping of show equipment(ctrl + f)", false);
        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
