package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ThreatConfluxBlock extends PlacedItemBlock implements FluidLogging.SpectrumFluidLoggable {
	
	public enum ArmedState implements StringIdentifiable {
		NOT_ARMED("not_armed", false),
		ARMED("armed", true),
		FUSED("fused", true);
		
		private final String name;
		private final boolean explodesWhenBroken;
		
		ArmedState(String name, boolean explodesWhenBroken) {
			this.name = name;
			this.explodesWhenBroken = explodesWhenBroken;
		}
		
		@Override
		public String asString() {
			return this.name;
		}
		
		public boolean explodesWhenBroken() {
			return this.explodesWhenBroken;
		}
	}
	
	private static final int TICKS_TO_ARM = 50;
	private static final int TICKS_TO_DETONATE = 20;
	
	public static final VoxelShape UNARMED_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
	public static final VoxelShape ARMED_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 0.125, 16);
	
	public static final EnumProperty<ArmedState> ARMED = EnumProperty.of("armed", ArmedState.class);
	public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.ANY_INCLUDING_NONE;
	
	public ThreatConfluxBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(ARMED, ArmedState.NOT_ARMED).with(LOGGED, FluidLogging.State.NOT_LOGGED));
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient && state.get(ARMED).explodesWhenBroken()) {
			explode((ServerWorld) world, pos);
		}
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var handStack = player.getStackInHand(hand);
		if (state.get(ARMED).explodesWhenBroken() && handStack.isOf(SpectrumItems.MIDNIGHT_CHIP)) {
			world.setBlockState(pos, state.with(ARMED, ArmedState.NOT_ARMED));
			world.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SpectrumSoundEvents.BLOCK_THREAT_CONFLUX_DISARM, SoundCategory.BLOCKS, 1.0F, 1.0F);
			
			if (!world.isClient) {
				ServerWorld serverWorld = ((ServerWorld) world);
				for (int i = 0; i < 5; ++i) {
					serverWorld.spawnParticles(ParticleTypes.SMOKE,
							pos.getX() + serverWorld.random.nextDouble(), pos.getY() + serverWorld.random.nextDouble(), pos.getZ() + serverWorld.random.nextDouble(),
							5, 0.0, 0.0, 0.0, 0.05);
				}
			}
			
			if (!player.isCreative()) {
				handStack.decrement(1);
			}
			
			return ActionResult.success(world.isClient());
		}
		
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		if (!world.isClient) {
			world.scheduleBlockTick(pos, this, TICKS_TO_ARM);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (state.get(ARMED) == ArmedState.ARMED) {
			world.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SpectrumSoundEvents.BLOCK_THREAT_CONFLUX_PRIME, SoundCategory.BLOCKS, 1, 2F);
			world.setBlockState(pos, state.with(ARMED, ArmedState.FUSED));
			world.scheduleBlockTick(pos, this, TICKS_TO_DETONATE);
		}
		
		state.get(LOGGED).onEntityCollision(state, world, pos, entity);
		
		super.onEntityCollision(state, world, pos, entity);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(ARMED).explodesWhenBroken() ? ARMED_SHAPE : UNARMED_SHAPE;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		
		ArmedState s = state.get(ARMED);
		if (s == ArmedState.NOT_ARMED) {
			world.setBlockState(pos, state.with(ARMED, ArmedState.ARMED));
			world.playSound(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, SpectrumSoundEvents.BLOCK_THREAT_CONFLUX_ARM, SoundCategory.BLOCKS, 2F, 0.1F + world.getRandom().nextFloat() * 0.3F);
		} else if (s == ArmedState.FUSED) {
			explode(world, pos);
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(ARMED, LOGGED);
	}
	
	public void explode(@NotNull ServerWorld world, BlockPos pos) {
		if (!(world.getBlockEntity(pos) instanceof PlacedItemBlockEntity blockEntity)) {
			return;
		}
		ItemStack stack = blockEntity.getStack();
		PlayerEntity owner = blockEntity.getOwnerIfOnline();
		
		world.removeBlock(pos, false);
		
		ModularExplosionDefinition.explode(world, pos, owner, stack);
	}
	
}
