package dev.lopyluna.tmmpatch.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.lopyluna.tmmpatch.ClientUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//OVERRIDES TMM MIXINS
@Mixin(value = KeyBinding.class)
public abstract class KeyBindingMixinOverride {
    @Shadow public abstract boolean equals(KeyBinding other);

    @Unique
    private boolean shouldSuppressKey() {
        if (TMMClient.isPlayerSpectatingOrCreative()) return false;
        if (ClientUtils.isGameInProgress()) {
            return this.equals(MinecraftClient.getInstance().options.swapHandsKey) ||
                    this.equals(MinecraftClient.getInstance().options.jumpKey) ||
                    this.equals(MinecraftClient.getInstance().options.dropKey) ||
                    this.equals(MinecraftClient.getInstance().options.advancementsKey) ||

                    this.equals(MinecraftClient.getInstance().options.chatKey) ||
                    this.equals(MinecraftClient.getInstance().options.commandKey) ||
                    this.equals(MinecraftClient.getInstance().options.togglePerspectiveKey);
        }
        return this.equals(MinecraftClient.getInstance().options.swapHandsKey) ||
                this.equals(MinecraftClient.getInstance().options.jumpKey) ||
                this.equals(MinecraftClient.getInstance().options.dropKey) ||
                this.equals(MinecraftClient.getInstance().options.advancementsKey);
    }

    @ModifyReturnValue(method = "wasPressed", at = @At("RETURN"))
    private boolean tmm$restrainWasPressedKeys(boolean original) {
        if (this.shouldSuppressKey()) return false;
        return original;
    }

    @ModifyReturnValue(method = "isPressed", at = @At("RETURN"))
    private boolean tmm$restrainIsPressedKeys(boolean original) {
        if (this.shouldSuppressKey()) return false;
        return original;
    }

    @ModifyReturnValue(method = "matchesKey", at = @At("RETURN"))
    private boolean tmm$restrainMatchesKey(boolean original) {
        if (this.shouldSuppressKey()) return false;
        return original;
    }
}
