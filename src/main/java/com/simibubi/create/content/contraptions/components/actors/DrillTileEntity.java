package com.simibubi.create.content.contraptions.components.actors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DrillTileEntity extends BlockBreakingKineticTileEntity {

	public DrillTileEntity(BlockEntityType<? extends DrillTileEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected BlockPos getBreakingPos() {
		return getBlockPos().relative(getBlockState().getValue(DrillBlock.FACING));
	}

}
