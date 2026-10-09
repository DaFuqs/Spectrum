package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.event.entity.player.*;
import org.jspecify.annotations.*;

public class GemstoneChimeBlock extends Block {
	
	protected static final VoxelShape SHAPE = Shapes.or(
			Block.box(5.0D, 3.0D, 5.0D, 11.0D, 13.0D, 11.0D),
			Block.box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D)
	);
	
	protected final SoundEvent soundEvent;
	protected final ParticleOptions particleEffect;
	//Is Muted holder
	public static final BooleanProperty IS_MUTED = BooleanProperty.create("is_muted");
	
	public GemstoneChimeBlock(Properties settings, SoundEvent soundEvent, ParticleOptions particleEffect) {
		super(settings);
		this.soundEvent = soundEvent;
		this.particleEffect = particleEffect;
		registerDefaultState(stateDefinition.any().setValue(IS_MUTED, false));
	}

	@Override
	public @Nullable MapCodec<? extends GemstoneChimeBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		//Check if it's muted before running the following code.
		boolean muteState = state.getValue(IS_MUTED);
		if (!muteState) {
			super.animateTick(state, world, pos, random);
			world.addParticle(particleEffect,
					pos.getX() + 0.25 + random.nextDouble() * 0.5,
					pos.getY() + 0.15 + random.nextDouble() * 0.5,
					pos.getZ() + 0.25 + random.nextDouble() * 0.5,
					0, -0.02 - random.nextDouble() * 0.025, 0);
			
			if (random.nextFloat() < 0.2) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundSource.BLOCKS, 0.7F + random.nextFloat() * 0.4F, 0.75F + random.nextFloat() * 0.5F, true);
			}
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = Direction.UP;
		return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite());
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.UP && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	public ParticleOptions getParticleEffect() {
		return this.particleEffect;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder){
		pBuilder.add(IS_MUTED);
	}
	
	//Mute with wool, Unmute with Empty
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		//If the hand contains an item version of what is in the block tag #c:wools, switch block state to the muted variant and set isMuted to true
		
		if (handStack.is(SpectrumItemTags.WOOLS)){
			setMuted(state.setValue(IS_MUTED, true), world, pos);
			return ItemInteractionResult.SUCCESS;
		}else if (handStack.isEmpty()){
			setMuted(state.setValue(IS_MUTED, false), world, pos);
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	
	}
	//
	static void setMuted(BlockState state, Level world, BlockPos pos){
		world.setBlockAndUpdate(pos, state);
	}
	
	//Additional Helper for BlockStates
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		// code that determines which state will be used when
		// placing down this block, depending on the BlockPlaceContext
		return defaultBlockState();
	}
}
