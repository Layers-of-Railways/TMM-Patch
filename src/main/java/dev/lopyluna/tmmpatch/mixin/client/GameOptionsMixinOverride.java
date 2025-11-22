package dev.lopyluna.tmmpatch.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.lopyluna.tmmpatch.ClientUtils;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameOptions.class, priority = 2000)
public class GameOptionsMixinOverride {
    @ModifyReturnValue(method = "getPerspective", at =@At("RETURN"))
    public Perspective getPerspective(Perspective original) {
        if (TMMClient.isPlayerSpectatingOrCreative()) return original;
        if (ClientUtils.isGameInProgress()) return Perspective.FIRST_PERSON;
        return original;
    }
}
