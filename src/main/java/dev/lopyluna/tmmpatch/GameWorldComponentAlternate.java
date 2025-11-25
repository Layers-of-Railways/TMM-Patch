package dev.lopyluna.tmmpatch;

import dev.doctor4t.trainmurdermystery.cca.GameTimeComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.WorldBlackoutComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMProperties;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.lopyluna.tmmpatch.item.ModItems;
import dev.lopyluna.tmmpatch.mixin.WorldBlackoutComponentAccessor;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorldComponentAlternate {
    public GameWorldComponent.GameMode selectedGameMode = GameWorldComponent.GameMode.MURDER;
    public GameModifier selectedGameModifier = GameModifier.NONE;

    public GameModifier modifier = GameModifier.NONE;
    public List<ServerPlayerEntity> players = new ArrayList<>(); //Players Old - Pre | Players New - Post
    public int playerCount = 0;

    private static class ScheduledTask {
        int ticksRemaining;
        Runnable task;

        ScheduledTask(int ticks, Runnable task) {
            this.ticksRemaining = ticks;
            this.task = task;
        }
    }

    private final List<ScheduledTask> scheduledTasks = new ArrayList<>();

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

    public void tick(ServerWorld world) {
        tickScheduledTasks();
    }

    private void tickScheduledTasks() {
        if (scheduledTasks.isEmpty()) return;

        Iterator<ScheduledTask> iter = scheduledTasks.iterator();
        while (iter.hasNext()) {
            ScheduledTask task =  iter.next();
            task.ticksRemaining--;
            if (task.ticksRemaining <= 0) {
                task.task.run();
                iter.remove();
            }
        }
    }

    public void scheduleTask(int ticks, Runnable task) {
        scheduledTasks.add(new ScheduledTask(ticks, task));
    }

    public void initializeGamePre(ServerWorld world) {
        modifier = selectedGameModifier;
    }

    public void initializeGamePost(ServerWorld world) {
        triggerModifier(world);
    }


    public void triggerModifier(ServerWorld world) {
        if (modifier == GameModifier.NONE) return;
        //var game = GameWorldComponent.KEY.get(world);
        var trainWorld = TrainWorldComponent.KEY.get(world);
        int time = GameTimeComponent.KEY.get(world).getTime();

        scheduleTask(30 * 20, () -> tickTriggerModifier(world));

        if (modifier == GameModifier.MASQUERADE) { //TODO
        }

        if (modifier == GameModifier.BLACKOUT) {
            trainWorld.setTimeOfDay(TrainWorldComponent.TimeOfDay.NIGHT);
            for (ServerPlayerEntity player : players)
                player.giveItemStack(ModItems.FLASHLIGHT.getDefaultStack());
        }

        if (modifier == GameModifier.HAUNTED) { //TODO
        }
    }

    @SuppressWarnings("SuspiciousIndentAfterControlStatement")
    public void tickTriggerModifier(ServerWorld world) {
        var time = GameTimeComponent.KEY.get(world).getTime();

        if (modifier == GameModifier.BLACKOUT) {
            var blackout = WorldBlackoutComponent.KEY.get(world);
            var blackoutAcc = (WorldBlackoutComponentAccessor) blackout;

            blackout.reset();
            blackoutAcc.ticks(0);

            var area = GameConstants.PLAY_AREA;
            if (blackoutAcc.ticks() > 0)
                return;

            BlockPos.Mutable pos = new BlockPos.Mutable();
            for (var x = (int) area.minX; x <= (int) area.maxX; x++)
            for (var y = (int) area.minY; y <= (int) area.maxY; y++)
            for (var z = (int) area.minZ; z <= (int) area.maxZ; z++) {
                pos.set(x, y, z);
                var state = world.getBlockState(pos);
                if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE))
                    continue;

                var duration = time + (20 * 60) * playerCount;
                if (duration > blackoutAcc.ticks())
                    blackoutAcc.ticks(duration);

                var detail = new WorldBlackoutComponent.BlackoutDetails(pos, duration, state.get(Properties.LIT));
                detail.init(world);
                blackoutAcc.blackouts().add(detail);
            }

            for (var player : players)
                player.networkHandler.sendPacket(new PlaySoundS2CPacket(
                    Registries.SOUND_EVENT.getEntry(TMMSounds.AMBIENT_BLACKOUT), SoundCategory.PLAYERS,
                    player.getX(), player.getY(), player.getZ(),
                    100f, 1f,
                    world.random.nextLong()
                ));
        }
    }
}
