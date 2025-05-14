package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ColoredSporeBlossomBlock extends SporeBlossomBlock {
	
	private static final Map<InkColor, ColoredSporeBlossomBlock> BLOSSOMS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	protected final ParticleOptions fallingParticleType;
	protected final ParticleOptions airParticleType;
	
	public ColoredSporeBlossomBlock(Properties settings, InkColor color, ParticleOptions fallingParticleType, ParticleOptions airParticleType) {
		super(settings);
		this.color = color;
		this.fallingParticleType = fallingParticleType;
		this.airParticleType = airParticleType;
		BLOSSOMS.put(color, this);
	}

//	@Override
//	public MapCodec<? extends ColoredSporeBlossomBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredSporeBlossomBlock byColor(InkColor color) {
		return BLOSSOMS.get(color);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		double d = (double) i + random.nextDouble();
		double e = (double) j + 0.7D;
		double f = (double) k + random.nextDouble();
		world.addParticle(this.fallingParticleType, d, e, f, 0.0D, 0.0D, 0.0D);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		
		for (int l = 0; l < 14; ++l) {
			mutable.set(i + Mth.nextInt(random, -10, 10), j - random.nextInt(10), k + Mth.nextInt(random, -10, 10));
			BlockState blockState = world.getBlockState(mutable);
			if (!blockState.isCollisionShapeFullBlock(world, mutable)) {
				world.addParticle(this.airParticleType, (double) mutable.getX() + random.nextDouble(), (double) mutable.getY() + random.nextDouble(), (double) mutable.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

}
