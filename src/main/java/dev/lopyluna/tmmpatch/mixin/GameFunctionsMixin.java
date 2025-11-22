package dev.lopyluna.tmmpatch.mixin;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.ScoreboardRoleSelectorComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "dev.doctor4t.trainmurdermystery.game.GameFunctions", remap = false)
public class GameFunctionsMixin {

    @ModifyConstant(method = "startGame", constant = @Constant(intValue = 6), remap = false)
    private static int changeMinPlayerCount(int original) {
        return 2;
    }

    @Inject(method = "assignRolesAndGetKillerCount(Lnet/minecraft/server/world/ServerWorld;Ljava/util/List;Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;)I", at = @At("HEAD"), remap = false, cancellable = true)
    private static void assignRolesAndGetKillerCount(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent, CallbackInfoReturnable<Integer> cir) {
        var roleSelector = ScoreboardRoleSelectorComponent.KEY.get(world.getScoreboard());
        var c = (int)Math.floor((float)players.size() * 0.2F);
        int killerCount = c == 0 ? 1 : c;
        int total = roleSelector.assignKillers(world, gameComponent, players, killerCount);
        roleSelector.assignVigilantes(world, gameComponent, players, killerCount);
        cir.setReturnValue(total);
    }
}

