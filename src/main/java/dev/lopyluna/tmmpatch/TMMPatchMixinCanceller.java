package dev.lopyluna.tmmpatch;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class TMMPatchMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return mixinClassName.equals("dev.doctor4t.trainmurdermystery.mixin.client.restrictions.ChatHudMixin") ||
               mixinClassName.equals("dev.doctor4t.trainmurdermystery.mixin.client.restrictions.GameOptionsMixin") ||
               mixinClassName.equals("dev.doctor4t.trainmurdermystery.mixin.client.restrictions.KeyBindingMixin");
    }
}

