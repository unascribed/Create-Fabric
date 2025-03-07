package com.simibubi.create.content.logistics.block.display.source;

import java.util.Random;

import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;

import io.github.fabricators_of_create.porting_lib.enchant.EnchantmentBonusBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantPowerDisplaySource extends NumericSingleLineDisplaySource {

	protected static final Random random = new Random();
	protected static final ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);

	@Override
	protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
		if (!(context.getSourceTE() instanceof EnchantmentTableBlockEntity enchantmentTile))
			return ZERO;

		BlockPos pos = context.getSourcePos();
		Level level = context.level();
		float enchantPower = 0;

		for(BlockPos offset : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
			if (!EnchantmentTableBlock.isValidBookShelf(level, pos, offset))
				continue;
			BlockPos bookPos = pos.offset(offset);
			BlockState state = level.getBlockState(bookPos);
			enchantPower += state.getBlock() instanceof EnchantmentBonusBlock bonus
					? bonus.getEnchantPowerBonus(state, level, pos)
					: state.is(Blocks.BOOKSHELF)
						? 1
						: 0;
		}


		int cost = EnchantmentHelper.getEnchantmentCost(random, 2, (int) enchantPower, stack);

		return new TextComponent(String.valueOf(cost));
	}

	@Override
	protected String getTranslationKey() {
		return "max_enchant_level";
	}

	@Override
	protected boolean allowsLabeling(DisplayLinkContext context) {
		return true;
	}
}
