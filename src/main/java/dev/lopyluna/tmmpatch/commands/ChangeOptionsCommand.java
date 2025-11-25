package dev.lopyluna.tmmpatch.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent.GameMode;
import dev.doctor4t.trainmurdermystery.command.argument.TMMGameModeArgumentType;
import dev.lopyluna.tmmpatch.GameWorldComponentAlternate;
import dev.lopyluna.tmmpatch.GameWorldComponentAlternate.GameModifier;
import dev.lopyluna.tmmpatch.TMMPatchMod;
import dev.lopyluna.tmmpatch.commands.arguments.ModifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ChangeOptionsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("tmm:select_mode")
                .requires(src -> src.hasPermissionLevel(2))
                .then(argument("gameMode", TMMGameModeArgumentType.gamemode())
                    .executes(ctx -> $select_mode(
                        ctx.getSource(),
                        TMMGameModeArgumentType.getGamemode(ctx, "gameMode")
                    ))
                )
        );

        dispatcher.register(
            literal("tmm:select_modifier")
                .requires(src -> src.hasPermissionLevel(2))
                .then(argument("gameModifier", ModifierArgumentType.modifier())
                    .executes((context) -> $select_modifier(
                        context.getSource(),
                        ModifierArgumentType.getModifier(context, "gameModifier")
                    ))
                )
        );
    }

    private static int $select_mode(ServerCommandSource source, GameMode targetMode) {
        GameWorldComponentAlternate alternateGameWorld = TMMPatchMod.gameWorldComponentAlternate;

        if (targetMode == alternateGameWorld.selectedGameMode) {
            source.sendError(Text.translatable("commands.tmm_patch.select_mode.error.unchanged", targetMode));
            return -1;
        }

        if (targetMode == null) {
            source.sendError(Text.translatable("commands.tmm_patch.select_mode.error.null"));
            return -1;
        }

        GameMode old = alternateGameWorld.selectedGameMode;
        alternateGameWorld.selectedGameMode = targetMode;
        source.sendFeedback(() -> Text.translatable("commands.tmm_patch.select_mode.success", old, targetMode), true);
        return 1;
    }

    private static int $select_modifier(ServerCommandSource source, GameModifier targetModifier) {
        GameWorldComponentAlternate alternateGameWorld = TMMPatchMod.gameWorldComponentAlternate;

        if (targetModifier == alternateGameWorld.selectedGameModifier) {
            source.sendError(Text.translatable("commands.tmm_patch.select_modifier.error.unchanged", targetModifier));
            return -1;
        }

        if (targetModifier == null) {
            source.sendError(Text.translatable("commands.tmm_patch.select_modifier.error.null"));
            return -1;
        }

        GameModifier old = alternateGameWorld.selectedGameModifier;
        alternateGameWorld.selectedGameModifier = targetModifier;
        source.sendFeedback(() -> Text.translatable("commands.tmm_patch.select_modifier.success", old, targetModifier), true);
        return 1;
    }
}
