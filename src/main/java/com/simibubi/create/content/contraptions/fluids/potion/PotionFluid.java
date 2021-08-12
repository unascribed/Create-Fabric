package com.simibubi.create.content.contraptions.fluids.potion;

import java.util.Collection;
import java.util.List;

import com.simibubi.create.lib.transfer.fluid.FluidStack;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.contraptions.fluids.VirtualFluid;


public class PotionFluid extends VirtualFluid {

	public enum BottleType {
		REGULAR, SPLASH, LINGERING;
	}

	public PotionFluid(Properties properties) {
		super(properties);
	}

	public static FluidStack withEffects(int amount, Potion potion, List<MobEffectInstance> customEffects) {
		FluidStack fluidStack = new FluidStack(AllFluids.POTION.get()
			.getSource(), amount);
		addPotionToFluidStack(fluidStack, potion);
		appendEffects(fluidStack, customEffects);
		return fluidStack;
	}

//	public static class PotionFluidAttributes extends FluidAttributes {
//
//		public PotionFluidAttributes(Builder builder, Fluid fluid) {
//			super(builder, fluid);
//		}
//
//		public PotionFluidAttributes(FluidAttributes fluidAttributes, Fluid fluid) {
//			super();
//		}
//
//		@Override
//		public int getColor(FluidStack stack) {
//			CompoundNBT tag = stack.getOrCreateTag();
//			int color = PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xff000000;
//			return color;
//		}
//
//		@Override
//		public String getTranslationKey(FluidStack stack) {
//			CompoundNBT tag = stack.getOrCreateTag();
//			IItemProvider itemFromBottleType =
//				PotionFluidHandler.itemFromBottleType(NBTHelper.readEnum(tag, "Bottle", BottleType.class));
//			return PotionUtils.getPotion(tag)
//				.getName(itemFromBottleType.asItem()
//					.getDescriptionId() + ".effect.");
//		}
//
//	}

	public static FluidStack addPotionToFluidStack(FluidStack fs, Potion potion) {
		ResourceLocation resourcelocation = Registry.POTION.getKey(potion);
		if (potion == Potions.EMPTY) {
//			fs.removeChildTag("Potion");
			return fs;
		}
		fs.toTag()//.getOrCreateTag()
			.putString("Potion", resourcelocation.toString());
		return fs;
	}

	public static FluidStack appendEffects(FluidStack fs, Collection<MobEffectInstance> customEffects) {
		if (customEffects.isEmpty())
			return fs;
		CompoundTag compoundnbt = fs.toTag();//.getOrCreateTag();
		ListTag listnbt = compoundnbt.getList("CustomPotionEffects", 9);
		for (MobEffectInstance effectinstance : customEffects)
			listnbt.add(effectinstance.save(new CompoundTag()));
		compoundnbt.put("CustomPotionEffects", listnbt);
		return fs;
	}

}
