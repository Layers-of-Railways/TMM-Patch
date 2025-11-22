package dev.lopyluna.tmmpatch;

import dev.lopyluna.tmmpatch.commands.ChangeOptionsCommand;
import dev.lopyluna.tmmpatch.commands.ModifierArgumentType;
import dev.lopyluna.tmmpatch.commands.StartGameCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMMPatchMod implements ModInitializer {
    public static final String MOD_ID = "tmm_patch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static GameWorldComponentAlternate gameWorldComponentAlternate;

    public static @NotNull Identifier id(String name) {
        return Identifier.of("tmm_patch", name);
    }

    @Override
    public void onInitialize() {
        gameWorldComponentAlternate = new GameWorldComponentAlternate();

        ArgumentTypeRegistry.registerArgumentType(id("modifier"), ModifierArgumentType.class, ConstantArgumentSerializer.of(ModifierArgumentType::modifier));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            StartGameCommand.register(dispatcher);
            ChangeOptionsCommand.register(dispatcher);
        });
    }
}

