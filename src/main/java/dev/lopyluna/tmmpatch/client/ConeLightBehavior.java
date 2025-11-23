package dev.lopyluna.tmmpatch.client;

import dev.lambdaurora.lambdynlights.api.behavior.LineLightBehavior;
import dev.lambdaurora.lambdynlights.engine.DynamicLightingEngine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Range;
import org.joml.Vector3d;

@Environment(EnvType.CLIENT)
public class ConeLightBehavior extends LineLightBehavior {
    private final Vector3d startPointClamped;

    private final Vector3d cachedStartFull = new Vector3d();
    private final Vector3d cachedStart = new Vector3d();
    private final Vector3d cachedEnd = new Vector3d();
    private final Vector3d cachedLine = new Vector3d();
    private double cachedLum;
    private double cachedFallOff;
    private boolean cacheValid = false;

    public ConeLightBehavior() {
        super(new Vector3d(), new Vector3d(), 11);
        this.startPointClamped = new Vector3d();
    }

    public void update(LivingEntity entity) {
        var world = entity.getWorld();
        var start = entity.getEyePos();
        var end = entity.getEyePos().add(entity.getRotationVector().multiply(7));
        var pos = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, entity)).getPos();

        this.startPointClamped.set(pos.x, pos.y, pos.z);
        this.setStartPoint(end.x, end.y, end.z);
        this.setEndPoint(start.x, start.y, start.z);

        cacheValid = false;
    }

    private void updateCache() {
        if (!cacheValid) {
            cachedStartFull.set(getStartPoint());
            cachedStart.set(this.startPointClamped);
            cachedEnd.set(getEndPoint());
            cachedLine.set(cachedEnd).sub(cachedStart);
            cachedLum = getLuminance();
            cachedFallOff = 9.0 / DynamicLightingEngine.MAX_RADIUS;
            cacheValid = true;
        }
    }

    @Override
    public @Range(from = 0, to = 15) double lightAtPos(BlockPos pos, double falloffRatio) {
        updateCache();

        double blockX = pos.getX() + 0.5, blockY = pos.getY() + 0.5, blockZ = pos.getZ() + 0.5;
        double ptStartX = blockX - cachedStart.x, ptStartY = blockY - cachedStart.y, ptStartZ = blockZ - cachedStart.z;

        double dotStart = ptStartX * cachedLine.x + ptStartY * cachedLine.y + ptStartZ * cachedLine.z;
        if (dotStart <= 0.0) {
            double dist = Math.sqrt(ptStartX * ptStartX + ptStartY * ptStartY + ptStartZ * ptStartZ);
            return cachedLum - dist * cachedFallOff;
        }

        double ptEndX = blockX - cachedStart.x, ptEndY = blockY - cachedStart.y, ptEndZ = blockZ - cachedStart.z;
        double dotEnd = ptEndX * cachedLine.x + ptEndY * cachedLine.y + ptEndZ * cachedLine.z;
        if (dotEnd >= 0.0) {
            double dist = Math.sqrt(ptEndX * ptEndX + ptEndY * ptEndY + ptEndZ * ptEndZ);
            return cachedLum - dist * cachedFallOff;
        }

        double crossX = cachedLine.y * ptStartZ - cachedLine.z * ptStartY, crossY = cachedLine.z * ptStartX - cachedLine.x * ptStartZ, crossZ = cachedLine.x * ptStartY - cachedLine.y * ptStartX;
        double crossLength = Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ);
        double lineLength = Math.sqrt(cachedLine.x * cachedLine.x + cachedLine.y * cachedLine.y + cachedLine.z * cachedLine.z);
        double distance = crossLength / lineLength;

        double ptStartFullX = blockX - cachedStartFull.x, ptStartFullY = blockY - cachedStartFull.y, ptStartFullZ = blockZ - cachedStartFull.z;
        double distancePastStart = Math.sqrt(ptStartFullX * ptStartFullX + ptStartFullY * ptStartFullY + ptStartFullZ * ptStartFullZ);

        double rapidFalloff = Math.max(0, distancePastStart * 2.5);

        return Math.max(0, cachedLum - distance * cachedFallOff - rapidFalloff);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConeLightBehavior other)) return false;
        if (other.cacheValid != cacheValid) return false;
        if (other.cachedFallOff != cachedFallOff) return false;
        if (other.cachedLum != cachedLum) return false;
        if (!other.cachedStart.equals(cachedStart)) return false;
        if (!other.cachedEnd.equals(cachedEnd)) return false;
        if (!other.cachedLine.equals(cachedLine)) return false;
        if (!other.cachedStartFull.equals(cachedStartFull)) return false;
        if (!other.startPointClamped.equals(startPointClamped)) return false;
        if (!other.getStartPoint().equals(getStartPoint())) return false;
        if (!other.getEndPoint().equals(getEndPoint())) return false;
        return other.getLuminance() == getLuminance();
    }
}
