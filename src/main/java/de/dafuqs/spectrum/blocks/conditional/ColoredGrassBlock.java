package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.data.worldgen.placement.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColoredGrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock, RevelationAware {
	
	private static final Map<InkColor, ColoredGrassBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredGrassBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
		RevelationAware.register(this);
	}
	
	public static Collection<ColoredGrassBlock> all() {
		return BLOCKS.values();
	}
	
	@Override
	protected MapCodec<ColoredGrassBlock> codec() {
		return null;
	}
	
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return level.getBlockState(pos.above()).isAir();
	}
	
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		BlockPos abovePos = pos.above();
		BlockState shortGrass = ColoredShortGrassBlock.byColor(this.color).defaultBlockState();
		ColoredShortGrassBlock shortGrassBlock = (ColoredShortGrassBlock)shortGrass.getBlock();
		
		outer:
		for (int i = 0; i < 128; i++) {
			BlockPos currPos = abovePos;
			
			for (int j = 0; j < i / 16; j++) {
				currPos = currPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if (!level.getBlockState(currPos.below()).is(this) || level.getBlockState(currPos).isCollisionShapeFullBlock(level, currPos)) {
					continue outer;
				}
			}
			
			BlockState currState = level.getBlockState(currPos);
			if (currState.is(shortGrassBlock) && random.nextInt(10) == 0) {
				shortGrassBlock.performBonemeal(level, random, currPos, currState);
			}
			
			if (currState.isAir()) {
				Holder<PlacedFeature> holder;
				if (random.nextInt(8) == 0) {
					List<ConfiguredFeature<?, ?>> list = level.getBiome(currPos).value().getGenerationSettings().getFlowerFeatures();
					if (list.isEmpty()) {
						continue;
					}
					
					holder = ((RandomPatchConfiguration)list.get(0).config()).feature();
					holder.value().place(level, level.getChunkSource().getGenerator(), random, currPos);
					continue;
				}
				level.setBlockAndUpdate(currPos, shortGrass);
			}
		}
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_COLORED_GRASS;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> blockStateCloaks = new Object2ObjectArrayMap<>();
		blockStateCloaks.put(this.defaultBlockState().setValue(SNOWY, false), Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, false));
		blockStateCloaks.put(this.defaultBlockState().setValue(SNOWY, true), Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, true));
		return blockStateCloaks;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.GRASS_BLOCK.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.coloredGrassBlockColorProvider != null && SpectrumColorProviders.coloredGrassItemColorProvider != null) {
			SpectrumColorProviders.coloredGrassBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.coloredGrassItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.coloredGrassBlockColorProvider != null && SpectrumColorProviders.coloredGrassItemColorProvider != null) {
			SpectrumColorProviders.coloredGrassBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.coloredGrassItemColorProvider.setShouldApply(true);
		}
	}
	
	public BonemealableBlock.Type getType() {
		return Type.NEIGHBOR_SPREADER;
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredGrassBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
}
