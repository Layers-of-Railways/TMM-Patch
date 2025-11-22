package dev.lopyluna.tmmpatch.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.command.argument.TMMGameModeArgumentType;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class StartGameCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("tmm:start_alt").requires((source) -> source.hasPermissionLevel(2))).then(((RequiredArgumentBuilder)CommandManager.argument("gameMode", TMMGameModeArgumentType.gamemode()).then(CommandManager.argument("startTimeInMinutes", IntegerArgumentType.integer(1)).executes((context) -> execute((ServerCommandSource)context.getSource(), TMMGameModeArgumentType.getGamemode(context, "gameMode"), IntegerArgumentType.getInteger(context, "startTimeInMinutes"))))).executes((context) -> {
            GameWorldComponent.GameMode gameMode = TMMGameModeArgumentType.getGamemode(context, "gameMode");
            return execute((ServerCommandSource)context.getSource(), gameMode, gameMode.startTime);
        })));
    }

    private static int execute(ServerCommandSource source, GameWorldComponent.GameMode gameMode, int minutes) {
        if (GameWorldComponent.KEY.get(source.getWorld()).isRunning()) {
            source.sendError(Text.translatable("game.start_error.game_running"));
            return -1;
        } else {
            GameFunctions.startGame(source.getWorld(), gameMode, GameConstants.getInTicks(minutes, 0));
            return 1;
        }
    }
}

