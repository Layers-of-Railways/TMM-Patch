package dev.lopyluna.tmmpatch.item;

import dev.lopyluna.tmmpatch.TMMPatchMod;
import dev.lopyluna.tmmpatch.component.ModComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final FlashlightItem FLASHLIGHT = register("flashlight", new FlashlightItem(
        new Item.Settings()
            .maxCount(1)
            .component(ModComponents.ON, false)
    ));

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, TMMPatchMod.id(name), item);
    }

    public static void initialize() {
        TMMPatchMod.LOGGER.info("Registering {} items", TMMPatchMod.MOD_ID);
    }
}

