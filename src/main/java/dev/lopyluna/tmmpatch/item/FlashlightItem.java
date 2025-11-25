package dev.lopyluna.tmmpatch.item;

import dev.lopyluna.tmmpatch.component.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class FlashlightItem extends Item {
    public FlashlightItem(Settings settings) {
        super(settings.maxDamage(18000)); // 15 minutes
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!(entity instanceof PlayerEntity player)) return;
        if (stack.getDamage() >= stack.getMaxDamage()) return;

        Hand hand = player.getInventory().selectedSlot == slot ? Hand.MAIN_HAND : Hand.OFF_HAND;
        if (isHoldingPoweredFlashlight(player, hand))
            tickDrain(stack, player, world);
    }

    public void tickDrain(ItemStack stack, PlayerEntity player, World world) {
        if (player.isCreative() || player.isSpectator()) return;
        if (stack.getDamage() >= stack.getMaxDamage()) return;

        if (stack.getDamage() == stack.getMaxDamage() - 1) {
            stack.set(ModComponents.ON, false);
            if (world.isClient())
                player.playSound(SoundEvents.BLOCK_CANDLE_EXTINGUISH, 1F, 1F);
        }

        stack.setDamage(stack.getDamage() + 1);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int maxDamage = stack.getMaxDamage();
        float remaining = Math.max(0.0F, ((float)maxDamage - (float)stack.getDamage()) / (float)maxDamage);
        return MathHelper.hsvToRgb(
            MathHelper.abs((remaining / 8.0F) + 0.05f),
            0.6F,
            1.0F
        );
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (hand == Hand.OFF_HAND)
            return TypedActionResult.pass(stack);

        if (stack.getDamage() >= stack.getMaxDamage())
            return TypedActionResult.fail(stack);

        boolean currentState = stack.getOrDefault(ModComponents.ON, false);

        if (world.isClient()) {
            user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0F, currentState ? 0.5f : 0.65F);
            return TypedActionResult.fail(stack);
        }

        stack.set(ModComponents.ON, !currentState);
        return TypedActionResult.fail(stack);
    }

    public static boolean isHoldingPoweredFlashlight(LivingEntity living) {
        return isHoldingPoweredFlashlight(living, Hand.MAIN_HAND) || isHoldingPoweredFlashlight(living, Hand.OFF_HAND);
    }

    public static boolean isHoldingPoweredFlashlight(LivingEntity living, Hand hand) {
        if (living == null)
            return false;

        ItemStack stack = living.getStackInHand(hand);
        return stack.isOf(ModItems.FLASHLIGHT) && stack.getOrDefault(ModComponents.ON, false);
    }

    public static boolean maySwapFlashlightWithHands(LivingEntity living) {
        if (living == null)
            return false;

        var main = living.getMainHandStack();
        var off = living.getOffHandStack();

        return (main.isEmpty() && off.isOf(ModItems.FLASHLIGHT))
            || (off.isEmpty() && main.isOf(ModItems.FLASHLIGHT));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.getDamage() >= stack.getMaxDamage()) {
            tooltip.add(Text.translatable("item.tmm_patch.flashlight.battery.empty").formatted(Formatting.RED));
        } else {
            int percentage = (int) ((1 - (float) stack.getDamage() / stack.getMaxDamage()) * 99 + 1);
            tooltip.add(Text.translatable("item.tmm_patch.flashlight.battery.percentage", percentage).withColor(getItemBarColor(stack)));
        }
    }
}

