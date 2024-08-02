package de.dafuqs.spectrum.worldgen;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.gen.stateprovider.*;
import net.minecraft.world.gen.treedecorator.*;

public class FrondsDecorator extends TreeDecorator {
	
	public static final Codec<FrondsDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("chance").forGetter(FrondsDecorator::getChance),
			BlockStateProvider.TYPE_CODEC.fieldOf("middle_block").forGetter(FrondsDecorator::getMiddleBlock),
			BlockStateProvider.TYPE_CODEC.fieldOf("last_block").forGetter(FrondsDecorator::getBottomBlock),
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
	protected TreeDecoratorType<?> getType() {
		return SpectrumTreeDecoratorTypes.FRONDS;
	}
	
	@Override
	public void generate(Generator generator) {
		Random random = generator.getRandom();
		
		BlockPos.Mutable mutable;
		for (BlockPos pos : generator.getLeavesPositions()) {
			if (!generator.isAir(pos.down())) {
				continue;
			}
			
			if (random.nextFloat() > this.chance) continue;
			int length = this.lengthProvider.get(random);
			
			int i = 1;
			mutable = new BlockPos.Mutable(pos.getX(), pos.getY() - i, pos.getZ());
			while (i < length && generator.isAir(mutable) && generator.isAir(mutable.down())) {
				generator.replace(mutable, this.middleBlock.get(random, mutable));
				i++;
				mutable.set(pos.getX(), pos.getY() - i, pos.getZ());
			}
			if (generator.isAir(mutable)) {
				generator.replace(mutable, this.bottomBlock.get(random, mutable));
			}
		}
	}
	
}