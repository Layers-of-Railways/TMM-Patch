package dev.lopyluna.tmmpatch.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "dev.doctor4t.trainmurdermystery.TMM", remap = false)
public class TMMMixin {

    @Inject(method = "isSupporter", at = @At("HEAD"), cancellable = true)
    private static void isSupporter(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "executeSupporterCommand", at = @At("HEAD"), cancellable = true)
    private static void executeSupporterCommand(ServerCommandSource source, Runnable runnable, CallbackInfoReturnable<Integer> cir) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) cir.setReturnValue(0);
        else {
            runnable.run();
            cir.setReturnValue(1);
        }
    }
}
