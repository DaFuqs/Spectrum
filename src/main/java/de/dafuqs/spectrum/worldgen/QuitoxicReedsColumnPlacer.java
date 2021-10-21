package de.dafuqs.spectrum.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.blocks.conditional.QuitoxicReedsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.placer.BlockPlacerType;
import net.minecraft.world.gen.placer.ColumnPlacer;

import java.util.Random;

public class QuitoxicReedsColumnPlacer extends ColumnPlacer {

	public static final Codec<QuitoxicReedsColumnPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(IntProvider.field_33450.fieldOf("size").forGetter((columnPlacer) -> {
			return columnPlacer.size;
		})).apply(instance, QuitoxicReedsColumnPlacer::new);
	});

	private final IntProvider size;

	public QuitoxicReedsColumnPlacer(IntProvider size) {
		super(size);
		this.size = size;
	}

	protected BlockPlacerType<?> getType() {
		return SpectrumBlockPlacerTypes.QUITOXIC_REEDS_COLUMN_PLACER;
	}

	public void generate(WorldAccess world, BlockPos pos, BlockState state, Random random) {
		BlockPos.Mutable mutable = pos.mutableCopy();
		int i = this.size.get(random);

		for(int j = 0; j < i; ++j) {
			if(world.isWater(mutable)) {
				world.setBlockState(mutable, state.with(QuitoxicReedsBlock.WATERLOGGED, true), 2);
			} else {
				world.setBlockState(mutable, state, 2);
			}
			mutable.move(Direction.UP);
		}

	}
}