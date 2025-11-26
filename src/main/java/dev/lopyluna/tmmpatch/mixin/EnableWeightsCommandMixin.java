package dev.lopyluna.tmmpatch.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.cca.ScoreboardRoleSelectorComponent;
import dev.doctor4t.trainmurdermystery.command.EnableWeightsCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnableWeightsCommand.class)
public class EnableWeightsCommandMixin {
    @WrapOperation(method = "lambda$register$1", at = @At(value = "INVOKE", target = "Lorg/ladysnake/cca/api/v3/component/ComponentKey;get(Ljava/lang/Object;)Lorg/ladysnake/cca/api/v3/component/Component;"))
    private static <C extends Component> C fixScoreboardSource(ComponentKey<C> instance, Object e, Operation<C> original) {
        if (instance == ScoreboardRoleSelectorComponent.KEY && e instanceof ServerCommandSource source)
            e = source.getWorld().getScoreboard();

        return original.call(instance, e);
    }
}
