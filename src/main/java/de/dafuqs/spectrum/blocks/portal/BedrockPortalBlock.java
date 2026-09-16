package de.dafuqs.spectrum.blocks.portal;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.portal.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

public abstract class BedrockPortalBlock extends Block {
	
	public static final BooleanProperty FACING_UP = BlockStateProperties.UP;
	
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4D, 16.0D);
	protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 4D, 0.0D, 16.0D, 16.0D, 16.0D);
	
	public BedrockPortalBlock(Properties settings) {
		super(settings);
		this.registerDefaultState((this.stateDefinition.any()).setValue(FACING_UP, false));
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (handStack.is(SpectrumItems.BEDROCK_DUST)) {
            if (world.isClientSide()) {
                return ItemInteractionResult.SUCCESS;
            } else {
                BlockState placedState = Blocks.BEDROCK.defaultBlockState();
                world.setBlockAndUpdate(pos, placedState);
                world.playSound(null, pos, placedState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.CONSUME;
            }
		}
		
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(FACING_UP) ? SHAPE_UP : SHAPE;
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING_UP);
	}
	
	public void teleportToSafePosition(Level world, Entity entity, BlockPos targetPos, int maxRadius) {
		for (BlockPos bp : BlockPos.withinManhattan(targetPos, maxRadius, maxRadius, maxRadius)) {
			entity.setPos(Vec3.atBottomCenterOf(bp));
			if (world.getBlockState(bp.below()).getCollisionShape(world, bp.below()) == Shapes.block()
					&& world.noCollision(entity)
					&& entity.getY() < (double) world.getMaxBuildHeight()
					&& entity.getY() > (double) world.getMinBuildHeight()) {
				
				entity.teleportTo(bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5);
				return;
			}
		}
		
		world.removeBlock(targetPos.above(1), false);
		world.removeBlock(targetPos, false);
		world.setBlockAndUpdate(targetPos.below(1), Blocks.COBBLED_DEEPSLATE.defaultBlockState());
		entity.teleportTo(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (!state.getValue(BedrockPortalBlock.FACING_UP) || random.nextInt(8) == 0) {
			spawnVoidFogParticle(world, pos, random);
		}
	}
	
	private static void spawnVoidFogParticle(Level world, BlockPos pos, RandomSource random) {
		double d = (double) pos.getX() + random.nextDouble();
		double e = (double) pos.getY() + 0.3D;
		double f = (double) pos.getZ() + random.nextDouble();
		world.addParticle(SpectrumParticleTypes.VOID_FOG, d, e, f, 0.0D, 0.1D, 0.0D);
	}
	
}
