package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MermaidsBrushBlock extends PlantBlock implements Fertilizable, RevelationAware, FluidLogging.SpectrumFluidFillable {
	
	private static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("milestones/reveal_mermaids_brush");
	
	public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.ANY_EXCLUDING_NONE;
	public static final IntProperty AGE = Properties.AGE_7;
	
	public MermaidsBrushBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0).with(LOGGED, FluidLogging.State.WATER));
		RevelationAware.register(this);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.MERMAIDS_GEM);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (world.isClient) {
			if (MinecraftClient.getInstance().player.getEquippedStack(EquipmentSlot.HEAD).isOf(SpectrumItems.GLOW_VISION_GOGGLES)) {
				StatusEffectInstance nightVisionEffectInstance = MinecraftClient.getInstance().player.getStatusEffect(StatusEffects.NIGHT_VISION);
				if (nightVisionEffectInstance != null && nightVisionEffectInstance.getDuration() > 0) {
					world.addParticle(ParticleTypes.GLOW, (double) pos.getX() + 0.2 + random.nextFloat() * 0.6, (double) pos.getY() + 0.1 + random.nextFloat() * 0.6, (double) pos.getZ() + 0.2 + random.nextFloat() * 0.6, 0.0D, 0.03D, 0.0D);
				}
			}
		}
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		BlockState cloakState = Blocks.SEAGRASS.getDefaultState();
		for (int i = 0; i < 8; i++) {
			hashtable.put(this.getDefaultState().with(AGE, i).with(LOGGED, FluidLogging.State.WATER), cloakState);
			hashtable.put(this.getDefaultState().with(AGE, i).with(LOGGED, FluidLogging.State.LIQUID_CRYSTAL), cloakState);
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return null;
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getPlacementState(ctx).with(LOGGED, FluidLogging.State.LIQUID_CRYSTAL);
		} else {
			return super.getPlacementState(ctx).with(LOGGED, FluidLogging.State.WATER);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			if (state.get(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL) {
				world.scheduleFluidTick(pos, SpectrumFluids.LIQUID_CRYSTAL, SpectrumFluids.LIQUID_CRYSTAL.getTickRate(world));
			} else {
				world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			}
			
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(LOGGED).getFluidState();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, LOGGED);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);
		if (age == 7) {
			ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(SpectrumItems.MERMAIDS_GEM, 1));
			world.spawnEntity(pearlEntity);
			world.setBlockState(pos, state.with(AGE, 0), 3);
		} else {
			float chance = state.get(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? 0.5F : 0.2F;
			if (random.nextFloat() < chance) {
				world.setBlockState(pos, state.with(AGE, age + 1), Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS);
			}
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		return (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL)) && world.getBlockState(pos.down()).isIn(SpectrumBlockTags.MERMAIDS_BRUSH_PLANTABLE);
	}
	
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}
	
	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		int attempts = 7;
		float chance = state.get(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? 1.0F : 0.5F;
		int nextAge = age + random.nextBetween(1, (int) Math.ceil(attempts * chance));
		
		if (nextAge >= 7) {
			ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(SpectrumItems.MERMAIDS_GEM, 1));
			world.spawnEntity(pearlEntity);
		}
		
		world.setBlockState(pos, state.with(AGE, nextAge % 8), Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS);
	}
	
}