package dev.lopyluna.tmmpatch.mixin.client;

import dev.doctor4t.trainmurdermystery.TMMConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TMMConfig.class)
public class TMMConfigMixin {
    @Inject(method = "writeChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getInstance()Lnet/minecraft/client/MinecraftClient;"), cancellable = true)
    private void nullGuard(String modid, CallbackInfo ci) {
        if (MinecraftClient.getInstance().options == null)
            ci.cancel();
    }
}
