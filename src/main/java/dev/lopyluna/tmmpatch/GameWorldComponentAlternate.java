package dev.lopyluna.tmmpatch;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.util.StringIdentifiable;

public class GameWorldComponentAlternate {
    public GameWorldComponent.GameMode selectedGameMode = GameWorldComponent.GameMode.MURDER;
    public GameModifier selectedGameModifier = GameModifier.NONE;

    public GameModifier modifier = GameModifier.NONE;

    public enum GameModifier implements StringIdentifiable {
        NONE,
        MASQUERADE,
        BLACKOUT,
        HAUNTED;

        @Override
        public String asString() {
            return name();
        }
    }
}
