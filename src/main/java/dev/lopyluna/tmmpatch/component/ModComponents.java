package dev.lopyluna.tmmpatch.component;

import com.mojang.serialization.Codec;
import dev.lopyluna.tmmpatch.TMMPatchMod;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModComponents {
    public static final ComponentType<Boolean> ON = Registry.register(Registries.DATA_COMPONENT_TYPE, TMMPatchMod.id("on"),
        ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build());

    
    public static void initialize() {
        TMMPatchMod.LOGGER.info("Registering {} data components", TMMPatchMod.MOD_ID);
    }
}

