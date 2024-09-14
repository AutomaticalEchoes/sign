package automaticalechoes.simplesign.simplesign.mixin;

import automaticalechoes.simplesign.simplesign.api.EquipSetOptions;
import automaticalechoes.simplesign.simplesign.client.keys.ModKeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.*;

@Mixin(Options.class)
public class OptionMixin implements EquipSetOptions {
    @Unique
    private static boolean equipset$KeyMappingInit = false;
    @Mutable
    @Final
    @Shadow public KeyMapping[] keyMappings;

    @Unique
    public void equipset$loadKeyMappings() {
        KeyMapping[] modKeyMappings = ModKeyMappings.KEYMAPS.keySet().toArray(new KeyMapping[0]);
        keyMappings = ArrayUtils.addAll(Minecraft.getInstance().options.keyMappings, modKeyMappings);
        equipset$KeyMappingInit = true;
    }

    @Override
    public void equipset$removeKeyMappings() {
        KeyMapping[] modKeyMappings = ModKeyMappings.KEYMAPS.keySet().toArray(new KeyMapping[0]);
        if(equipset$KeyMappingInit) keyMappings = ArrayUtils.removeElements(keyMappings, modKeyMappings);
    }
}
