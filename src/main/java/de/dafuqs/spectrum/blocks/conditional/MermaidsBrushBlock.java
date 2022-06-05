package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class MermaidsBrushBlock extends PlantBlock implements Cloakable, FluidFillable {
	
	public static final BooleanProperty IN_LIQUID_CRYSTAL = BooleanProperty.of("in_liquid_crystal");
	public static final IntProperty AGE = Properties.AGE_7;
	
	public MermaidsBrushBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0).with(IN_LIQUID_CRYSTAL, false));
		registerCloak();
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
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_mermaids_brush");
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int i = 0; i < 8; i++) {
			hashtable.put(this.getDefaultState().with(AGE, i).with(IN_LIQUID_CRYSTAL, false), Blocks.SEAGRASS.getDefaultState());
			hashtable.put(this.getDefaultState().with(AGE, i).with(IN_LIQUID_CRYSTAL, true), Blocks.SEAGRASS.getDefaultState());
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.SEAGRASS.asItem());
	}
	
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}
	
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		if (fluidState.getFluid() == SpectrumFluids.LIQUID_CRYSTAL) {
			return super.getPlacementState(ctx).with(IN_LIQUID_CRYSTAL, true);
		} else {
			return super.getPlacementState(ctx).with(IN_LIQUID_CRYSTAL, false);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			if (state.get(IN_LIQUID_CRYSTAL)) {
				world.createAndScheduleFluidTick(pos, SpectrumFluids.LIQUID_CRYSTAL, SpectrumFluids.LIQUID_CRYSTAL.getTickRate(world));
			} else {
				world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			}
			
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		if (state.get(IN_LIQUID_CRYSTAL)) {
			return SpectrumFluids.LIQUID_CRYSTAL.getStill(false);
		} else {
			return Fluids.WATER.getStill(false);
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, IN_LIQUID_CRYSTAL);
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);
		if (age == 7) {
			ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(SpectrumItems.MERMAIDS_GEM, 1));
			world.spawnEntity(pearlEntity);
			world.setBlockState(pos, state.with(AGE, 0), 3);
		} else {
			float chance = state.get(IN_LIQUID_CRYSTAL) ? 1.0F : 0.5F;
			if (random.nextFloat() < chance) {
				world.setBlockState(pos, state.with(AGE, age + 1), 3);
			}
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		return (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL)) && world.getBlockState(pos.down()).isIn(SpectrumBlockTags.MERMAIDS_BRUSH_PLANTABLE);
	}
	
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}
	
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
}