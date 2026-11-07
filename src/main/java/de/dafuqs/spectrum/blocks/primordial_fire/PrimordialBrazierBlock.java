package de.dafuqs.spectrum.blocks.primordial_fire;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

public class PrimordialBrazierBlock extends RedstoneLampBlock implements PaintbrushTriggered {
	
	protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 7.0F, 16.0F);
	public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
	
	public PrimordialBrazierBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(INVERTED);
	}
	
	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (isLit(state) && entity instanceof LivingEntity) {
			entity.hurt(SpectrumDamageTypes.primordialFire(level), 8);
		}
		super.entityInside(state, level, pos, entity);
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		
		if(isLit(state)) {
			level.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE,
					pos.getX() + 0.5F + random.nextDouble() / 4.0F * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4, pos.getZ() + 0.5F + random.nextDouble() / 4.0F * (random.nextBoolean() ? 1 : -1),
					0.0F, 0.005, 0.0F);
		}
	}
	
	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}
	
	public boolean isLit(BlockState state) {
		return state.getValue(INVERTED) != state.getValue(LIT);
	}
	
	@Override
	public ItemInteractionResult onPaintBrushTrigger(BlockState state, Level level, BlockPos pos, @Nullable Player player, InteractionHand hand) {
		level.setBlockAndUpdate(pos, state.setValue(INVERTED, !state.getValue(INVERTED)));
		return ItemInteractionResult.sidedSuccess(level.isClientSide());
	}
	
}
