package dev.lopyluna.tmmpatch.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import dev.lopyluna.tmmpatch.GameWorldComponentAlternate;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.Locale;

public class ModifierArgumentType extends EnumArgumentType<GameWorldComponentAlternate.GameModifier> {
    private static final Codec<GameWorldComponentAlternate.GameModifier> CODEC = StringIdentifiable.createCodec(
        ModifierArgumentType::getValues,
        name -> name.toLowerCase(Locale.ROOT)
    );

    private static GameWorldComponentAlternate.GameModifier[] getValues() {
        return Arrays.stream(GameWorldComponentAlternate.GameModifier.values())
            .toArray(GameWorldComponentAlternate.GameModifier[]::new);
    }

    private ModifierArgumentType() {
        super(CODEC, ModifierArgumentType::getValues);
    }

    public static ModifierArgumentType modifier() {
        return new ModifierArgumentType();
    }

    public static GameWorldComponentAlternate.GameModifier getModifier(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, GameWorldComponentAlternate.GameModifier.class);
    }

    @Override protected String transformValueName(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
