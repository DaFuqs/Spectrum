package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

import java.util.*;

public class StuckStormStoneBlock extends HorizontalDirectionalBlock implements RevelationAware {
	
	public static final MapCodec<StuckStormStoneBlock> CODEC = simpleCodec(StuckStormStoneBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 11.0D, 2.0D, 11.0D);
	
	public StuckStormStoneBlock(Properties settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends StuckStormStoneBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).isRedstoneConductor(world, pos);
	}
	
	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1.0F;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
		super.wasExploded(world, pos, explosion);
		
		if (world.canSeeSky(pos)) {
			LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.moveTo(Vec3.atBottomCenterOf(pos));
				world.addFreshEntity(lightningEntity);
			}
		}
		
		int power = 2;
		Biome biomeAtPos = world.getBiome(pos).value();
		if (!biomeAtPos.hasPrecipitation() && !biomeAtPos.coldEnoughToSnow(pos)) {
			// there is no rain in deserts or snow
			power = world.isThundering() ? 4 : world.isRaining() ? 3 : 2;
		}
		world.explode(null, pos.getX(), pos.getY(), pos.getZ(), power, Level.ExplosionInteraction.BLOCK);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_STORM_STONES;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity contextEntity = entityShapeContext.getEntity();
			if (contextEntity instanceof Player player) {
				if (this.isVisibleTo(player)) {
					return SHAPE;
				} else {
					return Shapes.empty();
				}
			}
		}
		return Shapes.block(); // like breaking particles
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			map.put(this.defaultBlockState().setValue(FACING, direction), Blocks.AIR.defaultBlockState());
		}
		return map;
	}

	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return null;
	}
	
	/**
	 * If it gets ticked, there is a chance to vanish
	 */
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.1) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.STORM_STONE.get());
	}
	
}
