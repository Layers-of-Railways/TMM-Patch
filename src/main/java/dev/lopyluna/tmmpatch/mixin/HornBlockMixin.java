package dev.lopyluna.tmmpatch.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.lopyluna.tmmpatch.TMMPatchMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "dev.doctor4t.trainmurdermystery.block.HornBlock", remap = false)
public abstract class HornBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"))
    private void onUse(BlockState state, World w, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (w instanceof ServerWorld world) {
            if (!GameWorldComponent.KEY.get(world).isRunning()) return;
            if (GameWorldComponent.KEY.get(world).getGameMode() == GameWorldComponent.GameMode.DISCOVERY) {
                if (player == null || player.shouldCancelInteraction()) return;
                GameFunctions.stopGame(world);
            }
        }
    }


    @WrapOperation(method = "onUse", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/game/GameFunctions;startGame(Lnet/minecraft/server/world/ServerWorld;Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent$GameMode;I)V"))
    protected void onUse(ServerWorld world, GameWorldComponent.GameMode mode, int ticks, Operation<Void> original, @Local(argsOnly = true) PlayerEntity player) {
        if (GameWorldComponent.KEY.get(world).isRunning()) return;

        GameWorldComponent.GameMode newGameMode = TMMPatchMod.gameWorldComponentAlternate.selectedGameMode;
        GameFunctions.startGame(world, newGameMode, GameConstants.getInTicks(newGameMode.startTime, 0));

    }
}