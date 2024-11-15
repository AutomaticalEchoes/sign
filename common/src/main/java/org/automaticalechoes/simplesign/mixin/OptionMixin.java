package org.automaticalechoes.simplesign.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.automaticalechoes.simplesign.api.IOptions;
import org.automaticalechoes.simplesign.client.keys.ModKeyMappings;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Arrays;

@Mixin(Options.class)
public abstract class OptionMixin implements IOptions {
    @Mutable
    @Final
    @Shadow public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Minecraft pMinecraft, File pGameDirectory, CallbackInfo info) {
        ModKeyMappings.init(this);
    }

    @Shadow public abstract void load();

    @Unique
    public void ssi$loadKeyMappingsDiff() {
        KeyMapping[] modKeyMappings = ModKeyMappings.KEYMAPS.keySet().toArray(new KeyMapping[0]);
        KeyMapping[] registedKeyMapping = Arrays.stream(keyMappings).filter(keyMapping -> keyMapping.getCategory().equals(ModKeyMappings.MOD_CATEGORY)).toArray(KeyMapping[]::new);
        KeyMapping[] removal = Arrays.stream(registedKeyMapping).filter(keyMapping -> Arrays.stream(modKeyMappings).noneMatch(keyMapping1 -> keyMapping == keyMapping1)).toArray(KeyMapping[]::new);
        KeyMapping[] addon = Arrays.stream(modKeyMappings).filter(keyMapping -> Arrays.stream(registedKeyMapping).noneMatch(keyMapping1 -> keyMapping == keyMapping1)).toArray(KeyMapping[]::new);
        keyMappings = ArrayUtils.removeElements(keyMappings, removal);
        keyMappings = ArrayUtils.addAll(keyMappings, addon);
        load();
    }

}
