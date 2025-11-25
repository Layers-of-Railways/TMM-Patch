package dev.lopyluna.tmmpatch.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.client.gui.LobbyPlayersRenderer;
import dev.lopyluna.tmmpatch.client.ClientUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = LobbyPlayersRenderer.class, remap = false)
public class LobbyPlayersRendererMixin {

    @ModifyConstant(method = "renderHud", constant = @Constant(intValue = 6), remap = false)
    private static int changeMinPlayerCount(int original) {
        return 2;
    }

    @WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;", ordinal = 0))
    private static MutableText tmmpatch$modifyJoinProgressMessage(String key, Object[] args, Operation<MutableText> original, @Local(name = "readyPlayerCount") int playerCount) {
        var c = (int)Math.floor((float)playerCount * 0.2F);
        int killerCount = c == 0 ? 1 : c;
        return original.call(key, args).append(" -").append(Text.literal(" " + killerCount).formatted(Formatting.RED));
    }

    @WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private static MutableText tmmpatch$modifyThankYouMessageColor(String key, Operation<MutableText> original) {
        if (ClientUtils.isChatVisible()) return original.call("");
        return original.call(key);
    }
}

