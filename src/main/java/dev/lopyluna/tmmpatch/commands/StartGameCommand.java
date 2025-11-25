package dev.lopyluna.tmmpatch.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.command.argument.TMMGameModeArgumentType;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StartGameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("tmm:start_alt")
                .requires(src -> src.hasPermissionLevel(2))
                .then(argument("gameMode", TMMGameModeArgumentType.gamemode())
                    .then(argument("startTimeInMinutes", IntegerArgumentType.integer(1))
                        .executes(ctx -> $start_alt(
                            ctx.getSource(),
                            TMMGameModeArgumentType.getGamemode(ctx, "gameMode"),
                            IntegerArgumentType.getInteger(ctx, "startTimeInMinutes")
                        ))
                    )
                    .executes(ctx -> $start_alt(
                        ctx.getSource(),
                        TMMGameModeArgumentType.getGamemode(ctx, "gameMode")
                    ))
                )
        );
    }

    private static int $start_alt(ServerCommandSource source, GameWorldComponent.GameMode gameMode) {
        return $start_alt(source, gameMode, gameMode.startTime);
    }

    private static int $start_alt(ServerCommandSource source, GameWorldComponent.GameMode gameMode, int minutes) {
        if (GameWorldComponent.KEY.get(source.getWorld()).isRunning()) {
            source.sendError(Text.translatable("game.start_error.game_running"));
            return -1;
        } else {
            GameFunctions.startGame(source.getWorld(), gameMode, GameConstants.getInTicks(minutes, 0));
            return 1;
        }
    }
}

