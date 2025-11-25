package dev.lopyluna.tmmpatch.component;

import com.mojang.serialization.Codec;
import dev.lopyluna.tmmpatch.TMMPatchMod;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModComponents {
    public static final ComponentType<Boolean> ON = register("on", b -> b
        .codec(Codec.BOOL)
        .packetCodec(PacketCodecs.BOOL));

    @SuppressWarnings("SameParameterValue")
    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, TMMPatchMod.id(name), builder.apply(ComponentType.builder()).build());
    }

    public static void initialize() {
        TMMPatchMod.LOGGER.info("Registering {} data components", TMMPatchMod.MOD_ID);
    }
}

