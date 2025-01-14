package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.explosion.*;

import java.util.*;

public class StuckStormStoneBlock extends HorizontalFacingBlock implements RevelationAware {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 11.0D, 2.0D, 11.0D);
	
	public StuckStormStoneBlock(Settings settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSolidBlock(world, pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}
	
	@Override
	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		super.onDestroyedByExplosion(world, pos, explosion);
		
		if (world.isSkyVisible(pos)) {
			LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
				world.spawnEntity(lightningEntity);
			}
		}
		
		int power = 2;
		Biome biomeAtPos = world.getBiome(pos).value();
		if (!biomeAtPos.hasPrecipitation() && !biomeAtPos.isCold(pos)) {
			// there is no rain in deserts or snow
			power = world.isThundering() ? 4 : world.isRaining() ? 3 : 2;
		}
		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), power, World.ExplosionSourceType.BLOCK);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_STORM_STONES;
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (this.isVisibleTo(context)) {
			return SHAPE;
		}
		return VoxelShapes.empty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity var4 = entityShapeContext.getEntity();
			if (var4 instanceof PlayerEntity player) {
				return this.isVisibleTo(player) ? SHAPE : VoxelShapes.empty();
			}
		}
		return VoxelShapes.empty();
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction direction : Direction.Type.HORIZONTAL) {
			map.put(this.getDefaultState().with(FACING, direction), Blocks.AIR.getDefaultState());
		}
		return map;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return null;
	}
	
	/**
	 * If it gets ticked, there is a chance to vanish
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextFloat() < 0.1) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.STORM_STONE);
	}
	
}
