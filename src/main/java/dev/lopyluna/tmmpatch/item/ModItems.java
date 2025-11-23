package dev.lopyluna.tmmpatch.item;

import dev.lopyluna.tmmpatch.TMMPatchMod;
import dev.lopyluna.tmmpatch.component.ModComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    
    public static final Item FLASHLIGHT = register(new FlashlightItem(new Item.Settings().maxCount(1).component(ModComponents.ON, false)), "flashlight");
    
    private static Item register(Item item, String name) {
        return Registry.register(Registries.ITEM, TMMPatchMod.id(name), item);
    }
    
    public static void initialize() {
        TMMPatchMod.LOGGER.info("Registering {} items", TMMPatchMod.MOD_ID);
    }
}

