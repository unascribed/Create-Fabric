package com.simibubi.create.content.contraptions.components.structureMovement;

import com.jozufozu.flywheel.light.GridAlignedBB;
import com.jozufozu.flywheel.light.ILightUpdateListener;
import com.jozufozu.flywheel.light.LightUpdater;
import com.jozufozu.flywheel.light.LightVolume;
import com.simibubi.create.content.contraptions.components.structureMovement.render.RenderedContraption;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;

public abstract class ContraptionLighter<C extends Contraption> implements ILightUpdateListener {
    protected final C contraption;
    public final LightVolume lightVolume;

    protected GridAlignedBB bounds;

    protected boolean scheduleRebuild;

    protected ContraptionLighter(C contraption) {
        this.contraption = contraption;

        bounds = getContraptionBounds();

        lightVolume = new LightVolume(contraptionBoundsToVolume(bounds.copy()));

        lightVolume.initialize(contraption.entity.level);
        scheduleRebuild = true;

        startListening();
    }

    public void tick(RenderedContraption owner) {
        if (scheduleRebuild) {
            lightVolume.initialize(owner.contraption.entity.level);
            scheduleRebuild = false;
        }
    }

    public abstract GridAlignedBB getContraptionBounds();

    @Override
    public boolean onLightUpdate(BlockAndTintGetter world, LightLayer type, GridAlignedBB changed) {
        lightVolume.notifyLightUpdate(world, type, changed);
        return false;
    }

    @Override
    public boolean onLightPacket(BlockAndTintGetter world, int chunkX, int chunkZ) {
        lightVolume.notifyLightPacket(world, chunkX, chunkZ);
        return false;
    }

    protected void startListening() {
        LightUpdater.getInstance().startListening(bounds, this);
    }

    protected GridAlignedBB contraptionBoundsToVolume(GridAlignedBB bounds) {
        bounds.grow(2); // so we have at least enough data on the edges to avoid artifacts and have smooth lighting
        bounds.setMinY(Math.max(bounds.getMinY(), 0));
        bounds.setMaxY(Math.min(bounds.getMaxY(), 255));

        return bounds;
    }

	public GridAlignedBB getBounds() {
		return bounds;
	}
}
