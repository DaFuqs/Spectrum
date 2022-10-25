package de.dafuqs.spectrum.blocks.rock_candy;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffect;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SugarStickBlock extends Block implements RockCandy {
	
	private static final Identifier PARTICLE_SPRITE_IDENTIFIER = SpectrumCommon.locate("particle/spitit_sallow");
	protected final static Map<RockCandyVariant, Block> SUGAR_STICK_BLOCKS = new EnumMap<>(RockCandyVariant.class);
	
	protected final RockCandyVariant rockCandyVariant;
	
	public static final int ITEM_SEARCH_RANGE = 5;
	public static final int REQUIRED_ITEM_COUNT_PER_STAGE = 4;
	
	public static final IntProperty AGE = Properties.AGE_2;
	public static final BooleanProperty LIQUID_CRYSTAL_LOGGED  = SpectrumCommon.LIQUID_CRYSTAL_LOGGED;
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 13.0D, 11.0D);
	
	public SugarStickBlock(Settings settings, RockCandyVariant rockCandyVariant) {
		super(settings);
		this.rockCandyVariant = rockCandyVariant;
		SUGAR_STICK_BLOCKS.put(this.rockCandyVariant, this);
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0).with(LIQUID_CRYSTAL_LOGGED, false));
	}
	
	@Override
	public RockCandyVariant getVariant() {
		return this.rockCandyVariant;
	}
	
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getPlacementState(ctx).with(LIQUID_CRYSTAL_LOGGED, true);
		} else {
			return super.getPlacementState(ctx);
		}
	}
	
	public FluidState getFluidState(BlockState state) {
		return state.get(LIQUID_CRYSTAL_LOGGED) ? SpectrumFluids.LIQUID_CRYSTAL.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, LIQUID_CRYSTAL_LOGGED);
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(LIQUID_CRYSTAL_LOGGED) && state.get(AGE) < Properties.AGE_2_MAX;
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if(!state.get(LIQUID_CRYSTAL_LOGGED)) {
			int age = state.get(AGE);
			
			if(age == 2 || (age == 1 ? random.nextBoolean() : random.nextFloat() < 0.25)) {
				world.addParticle(new ParticleSpawnerParticleEffect(PARTICLE_SPRITE_IDENTIFIER, 0.1F, ColorHelper.getVec(rockCandyVariant.getDyeColor()), 0.5F, 120, true, true),
						pos.getX() + 0.25 + random.nextFloat() * 0.5,
						pos.getY() + 0.25 + random.nextFloat() * 0.5,
						pos.getZ() + 0.25 + random.nextFloat() * 0.5,
						0.08 - random.nextFloat() * 0.16,
						0.04 - random.nextFloat() * 0.16,
						0.08 - random.nextFloat() * 0.16);
			}
			
		}
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		if(state.get(LIQUID_CRYSTAL_LOGGED)) {
			int age = state.get(AGE);
			if(age < Properties.AGE_2_MAX) {
				List<ItemEntity> itemEntities = world.getNonSpectatingEntities(ItemEntity.class, Box.of(Vec3d.ofCenter(pos), ITEM_SEARCH_RANGE, ITEM_SEARCH_RANGE, ITEM_SEARCH_RANGE));
				for (ItemEntity itemEntity : itemEntities) {
					// is the item also submerged? (mostly the same pool)
					if(!itemEntity.isSubmergedIn(SpectrumFluidTags.LIQUID_CRYSTAL)) {
						continue;
					}itemEntity.setNeverDespawn();
					
					ItemStack stack = itemEntity.getStack();
					if (stack.getCount() >= REQUIRED_ITEM_COUNT_PER_STAGE) {
						if(stack.isOf(Items.SUGAR)) {
							stack.decrement(REQUIRED_ITEM_COUNT_PER_STAGE);
							age++;
							BlockState newState = state.with(AGE, age);
							world.setBlockState(pos, newState);
							world.playSound(null, pos, newState.getSoundGroup().getHitSound(), SoundCategory.BLOCKS, 0.5F, 1.0F);
							return;
						} else if(rockCandyVariant == RockCandyVariant.NONE && stack.isIn(SpectrumItemTags.GEMSTONE_POWDERS)) {
							stack.decrement(REQUIRED_ITEM_COUNT_PER_STAGE);
							RockCandyVariant newVariant = RockCandyVariant.fromGemstonePowder(stack.getItem());
							BlockState newState = SUGAR_STICK_BLOCKS.get(newVariant).getDefaultState().with(AGE, age).with(LIQUID_CRYSTAL_LOGGED, state.get(LIQUID_CRYSTAL_LOGGED));
							world.setBlockState(pos, newState);
							world.playSound(null, pos, newState.getSoundGroup().getHitSound(), SoundCategory.BLOCKS, 0.5F, 1.0F);
							return;
						}
					}
				}
			}
		}
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = Direction.UP;
		return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
	}
	
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.UP && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		NbtCompound nbt = stack.getNbt();
		if(nbt != null && nbt.contains("BlockStateTag")) {
			NbtCompound blockStateTag = nbt.getCompound("BlockStateTag");
			if(blockStateTag.contains("age", NbtElement.STRING_TYPE)) {
				String age = blockStateTag.getString("age");
				if("1".equals(age)) {
					tooltip.add(new TranslatableText("block.spectrum.sugar_stick.tooltip.medium"));
				} else if("2".equals(age)) {
					tooltip.add(new TranslatableText("block.spectrum.sugar_stick.tooltip.large"));
				}

			}
		}
	}
	
}
