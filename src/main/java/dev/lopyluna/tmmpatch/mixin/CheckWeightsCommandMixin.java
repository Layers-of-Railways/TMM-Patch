package dev.lopyluna.tmmpatch.mixin;

import com.mojang.brigadier.context.CommandContext;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.command.CheckWeightsCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CheckWeightsCommand.class)
public class CheckWeightsCommandMixin {
    @Inject(method = "lambda$register$1", at = @At("HEAD"))
    private static void showCurrentStatus(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir) {
        boolean enabled = GameWorldComponent.KEY.get(context.getSource().getWorld()).areWeightsEnabled();
        context.getSource().sendFeedback(() -> Text.literal(enabled ? "Role weights are enabled." : "Role weights are disabled."), false);
    }
}
