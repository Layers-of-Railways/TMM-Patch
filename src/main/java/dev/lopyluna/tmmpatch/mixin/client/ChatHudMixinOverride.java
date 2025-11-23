package dev.lopyluna.tmmpatch.mixin.client;

import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.lopyluna.tmmpatch.client.ClientUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//OVERRIDES TMM MIXINS
@Mixin(value = ChatHud.class, priority = 2000)
public abstract class ChatHudMixinOverride {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (TMMClient.isPlayerSpectatingOrCreative()) return;
        if (ClientUtils.isGameInProgress()) ci.cancel();
    }
}

