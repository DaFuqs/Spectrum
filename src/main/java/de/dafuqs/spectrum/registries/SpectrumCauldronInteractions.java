package de.dafuqs.spectrum.registries;

import net.minecraft.core.cauldron.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;

public class SpectrumCauldronInteractions {
	
	public static String LIQUID_CRYSTAL_INTERACTION = "spectrum_liquid_crystal";
	public static String SLUDGE_INTERACTION = "spectrum_sludge";
	public static String MIDNIGHT_SOLUTION_INTERACTION = "spectrum_midnight_solution";
	public static String DRAGONROT_INTERACTION = "spectrum_dragonrot";
	
	
	public static final CauldronInteraction FILL_LIQUID_CRYSTAL = (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
			level, pos, player, hand, stack,
			SpectrumBlocks.LIQUID_CRYSTAL_CAULDRON.get().defaultBlockState(),
			SoundEvents.BUCKET_EMPTY
	);
	public static final CauldronInteraction EMPTY_LIQUID_CRYSTAL = (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
			state, level, pos, player, hand, stack,
			new ItemStack(SpectrumItems.LIQUID_CRYSTAL_BUCKET.get()),
			state1 -> true,
			SoundEvents.BUCKET_FILL
	);
	public static final CauldronInteraction FILL_SLUDGE = (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
			level, pos, player, hand, stack,
			SpectrumBlocks.SLUDGE_CAULDRON.get().defaultBlockState(),
			SoundEvents.BUCKET_EMPTY
	);
	public static final CauldronInteraction EMPTY_SLUDGE = (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
			state, level, pos, player, hand, stack,
			new ItemStack(SpectrumItems.SLUDGE_BUCKET.get()),
			state1 -> true,
			SoundEvents.BUCKET_FILL
	);
	public static final CauldronInteraction FILL_MIDNIGHT_SOLUTION = (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
			level, pos, player, hand, stack,
			SpectrumBlocks.MIDNIGHT_SOLUTION_CAULDRON.get().defaultBlockState(),
			SoundEvents.BUCKET_EMPTY
	);
	public static final CauldronInteraction EMPTY_MIDNIGHT_SOLUTION = (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
			state, level, pos, player, hand, stack,
			new ItemStack(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET.get()),
			state1 -> true,
			SoundEvents.BUCKET_FILL
	);
	public static final CauldronInteraction FILL_DRAGONROT = (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
			level, pos, player, hand, stack,
			SpectrumBlocks.DRAGONROT_CAULDRON.get().defaultBlockState(),
			SoundEvents.BUCKET_EMPTY
	);
	public static final CauldronInteraction EMPTY_DRAGONROT = (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
			state, level, pos, player, hand, stack,
			new ItemStack(SpectrumItems.DRAGONROT_BUCKET.get()),
			state1 -> true,
			SoundEvents.BUCKET_FILL
	);
	
	public static void register() {
		// filling empty cauldrons
		CauldronInteraction.INTERACTIONS.get("empty").map().put(SpectrumItems.LIQUID_CRYSTAL_BUCKET.get(), FILL_LIQUID_CRYSTAL);
		CauldronInteraction.INTERACTIONS.get("empty").map().put(SpectrumItems.SLUDGE_BUCKET.get(), FILL_SLUDGE);
		CauldronInteraction.INTERACTIONS.get("empty").map().put(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET.get(), FILL_MIDNIGHT_SOLUTION);
		CauldronInteraction.INTERACTIONS.get("empty").map().put(SpectrumItems.DRAGONROT_BUCKET.get(), FILL_DRAGONROT);
		
		// emptying filled cauldrons
		CauldronInteraction.INTERACTIONS.get(LIQUID_CRYSTAL_INTERACTION).map().put(Items.BUCKET, EMPTY_LIQUID_CRYSTAL);
		CauldronInteraction.INTERACTIONS.get(SLUDGE_INTERACTION).map().put(Items.BUCKET, EMPTY_SLUDGE);
		CauldronInteraction.INTERACTIONS.get(MIDNIGHT_SOLUTION_INTERACTION).map().put(Items.BUCKET, EMPTY_MIDNIGHT_SOLUTION);
		CauldronInteraction.INTERACTIONS.get(DRAGONROT_INTERACTION).map().put(Items.BUCKET, EMPTY_DRAGONROT);
	}
}
