package dev.lopyluna.tmmpatch.mixin;

import dev.doctor4t.trainmurdermystery.entity.PlayerBodyEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = PlayerBodyEntity.class, remap = false)
public abstract class PlayerBodyEntityMixin extends LivingEntity {
    protected PlayerBodyEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author _
     * @reason _
     */
    @Overwrite
    @Override
    public Iterable<ItemStack> getArmorItems() {
        return new ArrayList<>(List.of());
    }
}
