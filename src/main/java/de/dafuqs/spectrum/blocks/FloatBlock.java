package de.dafuqs.spectrum.blocks;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

public class FloatBlock extends FallingBlock {
	
	public static final MapCodec<FloatBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			propertiesCodec(),
			Codec.FLOAT.fieldOf("gravity_mod").forGetter(FloatBlock::getGravityMod)
	).apply(i, FloatBlock::new));
	
	private final float gravityMod;
	
	public FloatBlock(Properties settings, float gravityMod) {
		super(settings);
		this.gravityMod = gravityMod;
	}
	
	@Override
	public MapCodec<? extends FloatBlock> codec() {
		return CODEC;
	}
	
	public float getGravityMod() {
		return gravityMod;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos blockPos, BlockState oldState, boolean notify) {
		world.scheduleTick(blockPos, this, this.getDelayAfterPlace());
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!world.isClientSide()) {
			if (gravityMod == 0) {
				fall(world, pos, state);
				return;
			}
			
			BlockPos collisionBlockPos;
			if (gravityMod > 0) {
				collisionBlockPos = pos.above();
			} else {
				collisionBlockPos = pos.below();
			}
			
			if (world.isEmptyBlock(collisionBlockPos) || isFree(world.getBlockState(collisionBlockPos))) {
				fall(world, pos, state);
			}
		}
	}
	
	public static FallingBlockEntity fall(Level level, BlockPos pos, BlockState blockState) {
		FloatBlockEntity blockEntity = new FloatBlockEntity(level, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, blockState.hasProperty(BlockStateProperties.WATERLOGGED)
						? blockState.setValue(BlockStateProperties.WATERLOGGED, false)
						: blockState
		);
		level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3);
		level.addFreshEntity(blockEntity);
		return blockEntity;
	}
	
	@Override
	public DamageSource getFallDamageSource(Entity entity) {
		return SpectrumDamageTypes.floatblock(entity.level());
	}
	
}
