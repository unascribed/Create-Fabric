package com.simibubi.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.components.actors.BellMovementBehaviour;
import com.simibubi.create.content.contraptions.components.actors.CampfireMovementBehaviour;
import com.simibubi.create.content.contraptions.components.actors.dispenser.DispenserMovementBehaviour;
import com.simibubi.create.content.contraptions.components.actors.dispenser.DropperMovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AllMovementBehaviours {
	private static final Map<Block, MovementBehaviour> BLOCK_BEHAVIOURS = new HashMap<>();
	private static final List<BehaviourProvider> GLOBAL_BEHAVIOURS = new ArrayList<>();

	public static void registerBehaviour(Block block, MovementBehaviour behaviour) {
		BLOCK_BEHAVIOURS.put(block, behaviour);
	}

	public static void registerBehaviourProvider(BehaviourProvider provider) {
		GLOBAL_BEHAVIOURS.add(provider);
	}

	@Nullable
	public static MovementBehaviour getBehaviour(BlockState state) {
		MovementBehaviour behaviour = BLOCK_BEHAVIOURS.get(state.getBlock());
		if (behaviour != null) {
			return behaviour;
		}

		for (BehaviourProvider provider : GLOBAL_BEHAVIOURS) {
			behaviour = provider.getBehaviour(state);
			if (behaviour != null) {
				return behaviour;
			}
		}

		return null;
	}

	public static <B extends Block> NonNullConsumer<? super B> movementBehaviour(
		MovementBehaviour behaviour) {
		return b -> registerBehaviour(b, behaviour);
	}

	static void registerDefaults() {
		registerBehaviour(Blocks.BELL, new BellMovementBehaviour());
		registerBehaviour(Blocks.CAMPFIRE, new CampfireMovementBehaviour());

		DispenserMovementBehaviour.gatherMovedDispenseItemBehaviours();
		registerBehaviour(Blocks.DISPENSER, new DispenserMovementBehaviour());
		registerBehaviour(Blocks.DROPPER, new DropperMovementBehaviour());
	}

	public interface BehaviourProvider {
		@Nullable
		MovementBehaviour getBehaviour(BlockState state);
	}
}
