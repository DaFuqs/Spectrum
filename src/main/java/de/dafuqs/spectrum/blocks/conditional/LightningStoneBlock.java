package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class LightningStoneBlock extends Block implements Cloakable {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 11.0D, 2.0D, 11.0D);
	
	public LightningStoneBlock(Settings settings) {
		super(settings);
		registerCloak();
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSolidBlock(world, pos);
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
		if (!biomeAtPos.isHot(pos) && !biomeAtPos.isCold(pos)) {
			// there is no rain in deserts or snow
			power = world.isThundering() ? 4 : world.isRaining() ? 3 : 2;
		}
		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), power, Explosion.DestructionType.BREAK);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_lightning_stones");
	}
	
	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (this.isVisibleTo(context)) {
			return SHAPE;
		}
		return EMPTY_SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (this.isVisibleTo(context)) {
			return SHAPE;
		}
		return EMPTY_SHAPE;
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		hashtable.put(this.getDefaultState(), Blocks.AIR.getDefaultState());
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return null;
	}
	
	/**
	 * If it gets ticked there is a chance to vanish
	 */
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextFloat() < 0.1) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}
	
}
