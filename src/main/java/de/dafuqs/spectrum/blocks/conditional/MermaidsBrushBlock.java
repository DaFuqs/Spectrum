package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.shapes.*;
import javax.annotation.*;

import java.util.*;

public class MermaidsBrushBlock extends BushBlock implements BonemealableBlock, RevelationAware, FluidLogging.SpectrumFluidFillable {
	
	public static final MapCodec<MermaidsBrushBlock> CODEC = simpleCodec(MermaidsBrushBlock::new);
	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
	
	public static final EnumProperty<FluidLogging.State> LOGGED = FluidLogging.ANY_EXCLUDING_NONE;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	
	public MermaidsBrushBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(LOGGED, FluidLogging.State.WATER));
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends MermaidsBrushBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.MERMAIDS_GEM.get());
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.entityInside(state, world, pos, entity);
		state.getValue(LOGGED).onEntityCollision(state, world, pos, entity);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_MERMAIDS_BRUSH;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		BlockState cloakState = Blocks.SEAGRASS.defaultBlockState();
		for (int i = 0; i < 8; i++) {
			map.put(this.defaultBlockState().setValue(AGE, i).setValue(LOGGED, FluidLogging.State.WATER), cloakState);
			map.put(this.defaultBlockState().setValue(AGE, i).setValue(LOGGED, FluidLogging.State.LIQUID_CRYSTAL), cloakState);
		}
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return null;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		if (fluidState.getType() == SpectrumFluids.LIQUID_CRYSTAL.get()) {
			return super.getStateForPlacement(ctx).setValue(LOGGED, FluidLogging.State.LIQUID_CRYSTAL);
		} else if (fluidState.is(FluidTags.WATER)) {
			return super.getStateForPlacement(ctx).setValue(LOGGED, FluidLogging.State.WATER);
		}
		return null;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(world, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if (state.getValue(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL) {
				world.scheduleTick(pos, SpectrumFluids.LIQUID_CRYSTAL.get(), SpectrumFluids.LIQUID_CRYSTAL.get().getTickDelay(world));
			} else {
				world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			}
			
			return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
		}
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(LOGGED).getFluidState();
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, LOGGED);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		int age = state.getValue(AGE);
		if (age == 7) {
			ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(SpectrumItems.MERMAIDS_GEM.get()));
			world.addFreshEntity(pearlEntity);
			world.setBlock(pos, state.setValue(AGE, 0), 3);
		} else {
			float chance = state.getValue(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? 0.5F : 0.2F;
			if (random.nextFloat() < chance) {
				world.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
			}
		}
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		return (fluidState.is(FluidTags.WATER) || fluidState.is(SpectrumFluidTags.LIQUID_CRYSTAL)) && world.getBlockState(pos.below()).is(SpectrumBlockTags.MERMAIDS_BRUSH_PLANTABLE);
	}
	
	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}
	
	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		int attempts = 7;
		float chance = state.getValue(LOGGED) == FluidLogging.State.LIQUID_CRYSTAL ? 1.0F : 0.5F;
		int nextAge = age + random.nextIntBetweenInclusive(1, (int) Math.ceil(attempts * chance));
		
		if (nextAge >= 8) {
			ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(SpectrumItems.MERMAIDS_GEM.get()));
			world.addFreshEntity(pearlEntity);
		}
		
		world.setBlock(pos, state.setValue(AGE, nextAge % 8), Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
	}
	
}
