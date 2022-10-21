package de.dafuqs.spectrum.blocks.conditional.amaranth;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.client.SpectrumColorProviders;
import net.id.incubus_core.block.TallCropBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.Map;

public class AmaranthCropBlock extends TallCropBlock implements RevelationAware {
	
	public static final Identifier ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_amaranth");
	protected static final int LAST_SINGLE_BLOCK_AGE = 3;
	protected static final int MAX_AGE = 7;
	
	public AmaranthCropBlock(Settings settings) {
		super(settings, LAST_SINGLE_BLOCK_AGE);
		RevelationAware.register(this);
	}
	
	public int getMaxAge() {
		return MAX_AGE;
	}
	
	@Override
	protected ItemConvertible getSeedsItem() {
		return SpectrumItems.AMARANTH_GRAINS;
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		BlockState smallFern = Blocks.FERN.getDefaultState();
		BlockState largeFernLower = Blocks.LARGE_FERN.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER);
		BlockState largeFernUpper = Blocks.LARGE_FERN.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER);
		
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (int age = 0; age < LAST_SINGLE_BLOCK_AGE; age++) {
			map.put(this.withAge(age), smallFern);
		}
		for (int age = LAST_SINGLE_BLOCK_AGE; age < MAX_AGE; age++) {
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.LOWER), largeFernLower);
			map.put(this.withAgeAndHalf(age, DoubleBlockHalf.UPPER), largeFernUpper);
		}
		return map;
	}
	
	@Override
	public @Nullable Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.LARGE_FERN.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.amaranthCropBlockColorProvider != null && SpectrumColorProviders.amaranthCropItemColorProvider != null) {
			SpectrumColorProviders.amaranthCropBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.amaranthCropItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.amaranthCropBlockColorProvider != null && SpectrumColorProviders.amaranthCropItemColorProvider != null) {
			SpectrumColorProviders.amaranthCropBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.amaranthCropItemColorProvider.setShouldApply(true);
		}
	}
	
}