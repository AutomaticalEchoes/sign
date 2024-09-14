package automaticalechoes.simplesign.simplesign.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin{
//    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"),ordinal = 3)
//    private boolean Flag2l(boolean flag2){
//        return ClientEvents.SIGNS.size() > 0 &&  Utils.ShouldEntityGlow();
//    }
}
