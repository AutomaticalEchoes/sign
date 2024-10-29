package automaticalechoes.simplesign.mixin;

import automaticalechoes.simplesign.client.ILevelRender;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin implements ILevelRender {
//    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"),ordinal = 3)
//    private boolean Flag2l(boolean flag2){
//        return ClientEvents.SIGNS.size() > 0 &&  Utils.ShouldEntityGlow();
//    }

    @Shadow @Nullable
    private Frustum capturedFrustum;

    @Shadow private Frustum cullingFrustum;

    @Unique
    public Frustum getFrustum() {
        return capturedFrustum != null ? capturedFrustum : cullingFrustum;
    }
}
