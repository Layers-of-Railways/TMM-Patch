package dev.lopyluna.tmmpatch;

import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.lopyluna.tmmpatch.mixin.client.ChatHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

public class ClientUtils {
    public static boolean isGameInProgress() {
        if (TMMClient.gameComponent == null) return false;
        return TMMClient.gameComponent.isRunning();
    }

    public static boolean isChatVisible() {
        var hud = MinecraftClient.getInstance().inGameHud.getChatHud();
        return !((ChatHudAccessor) hud).getVisibleMessages().isEmpty() || MinecraftClient.getInstance().currentScreen instanceof ChatScreen;
    }
}
