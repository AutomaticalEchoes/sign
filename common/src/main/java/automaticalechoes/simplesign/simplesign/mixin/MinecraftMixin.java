package automaticalechoes.simplesign.simplesign.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Minecraft.class)
public class MinecraftMixin {
//    @Inject(method = "shouldEntityAppearGlowing",at = @At("RETURN"),cancellable = true)
//    public void shouldEntityAppearGlowing(Entity p_91315_, CallbackInfoReturnable<Boolean> cir){
//        if(Utils.ShouldEntityGlow()){
//            for(Sign sign :ClientEvents.SIGNS){
//                if(sign.equals(p_91315_)) cir.setReturnValue(true);
//            }
//        }
//    }
}
