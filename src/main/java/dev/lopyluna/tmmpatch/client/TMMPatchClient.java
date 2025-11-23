package dev.lopyluna.tmmpatch.client;

import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import dev.lopyluna.tmmpatch.TMMPatchMod;
import dev.lopyluna.tmmpatch.item.FlashlightItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.LivingEntity;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TMMPatchClient implements ClientModInitializer, DynamicLightsInitializer {
    public static final TMMPatchClient INSTANCE = new TMMPatchClient();
    private DynamicLightsContext context;
    public static Map<LivingEntity, ConeLightBehavior> coneLightBehaviors = new HashMap<>();
    public static List<ConeLightBehavior> coneLightBehaviorsList = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(level -> {
            if (context == null) return;

            if (level.getTime() % 2 != 0) return;

            for (var entity : level.getEntities()) if (entity instanceof LivingEntity living) {
                ConeLightBehavior behavior = coneLightBehaviors.get(living);
                if (behavior == null) {
                    behavior = new ConeLightBehavior();
                    behavior.update(living);
                    coneLightBehaviors.put(living, behavior);
                    TMMPatchMod.LOGGER.info("Created new cone light behavior for {}", living.getName().getString());
                } else behavior.update(living);
                if (level.random.nextFloat() > 0.01f && FlashlightItem.isHoldingPoweredFlashlight(living)) {
                    if (coneLightBehaviorsList.contains(behavior)) continue;
                    coneLightBehaviorsList.add(behavior);
                    context.dynamicLightBehaviorManager().add(behavior);
                } else if (coneLightBehaviorsList.contains(behavior)) {
                    coneLightBehaviorsList.remove(behavior);
                    context.dynamicLightBehaviorManager().remove(behavior);
                }
            }

            coneLightBehaviors.forEach((entity, behavior) -> {
                if (!entity.isAlive() || entity.isRemoved()) {
                    coneLightBehaviorsList.remove(behavior);
                    context.dynamicLightBehaviorManager().remove(behavior);
                }
            });
            coneLightBehaviors.entrySet().removeIf(entry -> entry.getKey().isRemoved() || entry.getValue() == null);
            coneLightBehaviorsList.removeIf(Objects::isNull);
        });
    }

    @Override
    public void onInitializeDynamicLights(DynamicLightsContext context) {
        this.context = context;
    }

    @SuppressWarnings({"removal", "UnstableApiUsage"})
    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {
        throw new UnsupportedOperationException("This mod requires LambDynamicLights v4.");
    }
}
