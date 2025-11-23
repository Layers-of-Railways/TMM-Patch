package dev.lopyluna.tmmpatch.mixin;

import dev.doctor4t.trainmurdermystery.cca.WorldBlackoutComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = WorldBlackoutComponent.class, remap = false)
public interface WorldBlackoutComponentAccessor {
    @Accessor("ticks")
    int ticks();
    @Accessor("ticks")
    void ticks(int ticks);
    @Accessor("blackouts")
    List<WorldBlackoutComponent.BlackoutDetails> blackouts();
}
