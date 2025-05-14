package de.dafuqs.spectrum.worldgen.tree_decorators;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;

public class FrondsDecorator extends TreeDecorator {
	
	public static final MapCodec<FrondsDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("chance").forGetter(FrondsDecorator::getChance),
			BlockStateProvider.CODEC.fieldOf("middle_block").forGetter(FrondsDecorator::getMiddleBlock),
			BlockStateProvider.CODEC.fieldOf("last_block").forGetter(FrondsDecorator::getBottomBlock),
			IntProvider.POSITIVE_CODEC.fieldOf("length").forGetter(FrondsDecorator::getLengthProvider)
	).apply(instance, FrondsDecorator::new));
	
	public final float chance;
	public final BlockStateProvider middleBlock;
	public final BlockStateProvider bottomBlock;
	public final IntProvider lengthProvider;
	
	public FrondsDecorator(float chance, BlockStateProvider middleBlock, BlockStateProvider bottomBlock, IntProvider lengthProvider) {
		this.chance = chance;
		this.middleBlock = middleBlock;
		this.bottomBlock = bottomBlock;
		this.lengthProvider = lengthProvider;
	}
	
	public BlockStateProvider getMiddleBlock() {
		return this.middleBlock;
	}
	
	public BlockStateProvider getBottomBlock() {
		return this.bottomBlock;
	}
	
	public IntProvider getLengthProvider() {
		return this.lengthProvider;
	}
	
	public float getChance() {
		return this.chance;
	}
	
	@Override
	protected TreeDecoratorType<?> type() {
		return SpectrumTreeDecoratorTypes.FRONDS;
	}
	
	@Override
	public void place(Context generator) {
		RandomSource random = generator.random();
		
		BlockPos.MutableBlockPos mutable;
		for (BlockPos pos : generator.leaves()) {
			if (!generator.isAir(pos.below())) {
				continue;
			}
			
			if (random.nextFloat() > this.chance) continue;
			int length = this.lengthProvider.sample(random);
			
			int i = 1;
			mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() - i, pos.getZ());
			while (i < length && generator.isAir(mutable) && generator.isAir(mutable.below())) {
				generator.setBlock(mutable, this.middleBlock.getState(random, mutable));
				i++;
				mutable.set(pos.getX(), pos.getY() - i, pos.getZ());
			}
			if (generator.isAir(mutable)) {
				generator.setBlock(mutable, this.bottomBlock.getState(random, mutable));
			}
		}
	}
	
}
