package dev.lopyluna.tmmpatch.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.command.argument.TMMGameModeArgumentType;
import dev.lopyluna.tmmpatch.TMMPatchMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ChangeOptionsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:select_mode").requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("gameMode", TMMGameModeArgumentType.gamemode())
                        .executes((context) -> {
                            var targetMode = TMMGameModeArgumentType.getGamemode(context, "gameMode");
                            if (targetMode == TMMPatchMod.gameWorldComponentAlternate.selectedGameMode || targetMode == null) {
                                if (targetMode != null) context.getSource().sendError(Text.of("Mode is already " + targetMode));
                                else context.getSource().sendError(Text.of("Mode is null"));
                                return -1;
                            }
                            var old = TMMPatchMod.gameWorldComponentAlternate.selectedGameMode;
                            TMMPatchMod.gameWorldComponentAlternate.selectedGameMode = targetMode;
                            context.getSource().sendFeedback(() -> Text.of("Changed Mode from " + old + " to " + targetMode), true);
                            return 1;
                        })));
        dispatcher.register(CommandManager.literal("tmm:select_modifier").requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("gameModifier", ModifierArgumentType.modifier())
                        .executes((context) -> {
                            var targetModifier = ModifierArgumentType.getModifier(context, "gameModifier");
                            if (targetModifier == TMMPatchMod.gameWorldComponentAlternate.selectedGameModifier || targetModifier == null) {
                                if (targetModifier != null) context.getSource().sendError(Text.of("Modifier is already " + targetModifier));
                                else context.getSource().sendError(Text.of("Modifier is null"));
                                return -1;
                            }
                            var old = TMMPatchMod.gameWorldComponentAlternate.selectedGameModifier;
                            TMMPatchMod.gameWorldComponentAlternate.selectedGameModifier = targetModifier;
                            context.getSource().sendFeedback(() -> Text.of("Changed Modifier from " + old + " to " + targetModifier), true);
                            return 1;
                        })));

    }
}
