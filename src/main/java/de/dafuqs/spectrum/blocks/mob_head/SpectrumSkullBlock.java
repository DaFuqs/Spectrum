package de.dafuqs.spectrum.blocks.mob_head;

import com.google.common.collect.*;
import de.dafuqs.spectrum.helpers.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.pattern.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.predicate.block.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumSkullBlock extends SkullBlock {
	
	public static BiMap<SpectrumSkullType, Block> MOB_HEADS = EnumHashBiMap.create(SpectrumSkullType.class);
	private static Map<EntityType<?>, SpectrumSkullType> ENTITY_TYPE_TO_SKULL_TYPE = new Object2ObjectOpenHashMap<>();
	
	@Nullable
	private static BlockPattern witherBossPattern;
	
	public SpectrumSkullBlock(SpectrumSkullType skullType, Settings settings) {
		super(skullType, settings);
		MOB_HEADS.put(skullType, this);
		ENTITY_TYPE_TO_SKULL_TYPE.put(skullType.getEntityType(), skullType);
	}
	
	@Override
	public SpectrumSkullType getSkullType() {
		return (SpectrumSkullType) super.getSkullType();
	}
	
	public static Optional<EntityType<?>> getEntityTypeOfSkullStack(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item instanceof SpectrumSkullBlockItem spectrumSkullBlockItem) {
			return Optional.of(spectrumSkullBlockItem.type.getEntityType());
		}
		if (Items.CREEPER_HEAD == item) {
			return Optional.of(EntityType.CREEPER);
		} else if (Items.DRAGON_HEAD == item) {
			return Optional.of(EntityType.ENDER_DRAGON);
		} else if (Items.ZOMBIE_HEAD == item) {
			return Optional.of(EntityType.ZOMBIE);
		} else if (Items.SKELETON_SKULL == item) {
			return Optional.of(EntityType.SKELETON);
		} else if (Items.WITHER_SKELETON_SKULL == item) {
			return Optional.of(EntityType.WITHER_SKELETON);
		} else if (Items.PIGLIN_HEAD == item) {
			return Optional.of(EntityType.PIGLIN);
		}
		return Optional.empty();
	}
	
	public static Optional<SkullBlock.SkullType> getSkullType(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item instanceof SpectrumSkullBlockItem spectrumSkullBlockItem) {
			return Optional.of(spectrumSkullBlockItem.type);
		}
		if (Items.CREEPER_HEAD == item) {
			return Optional.of(SkullBlock.Type.CREEPER);
		} else if (Items.DRAGON_HEAD == item) {
			return Optional.of(SkullBlock.Type.DRAGON);
		} else if (Items.ZOMBIE_HEAD == item) {
			return Optional.of(SkullBlock.Type.ZOMBIE);
		} else if (Items.SKELETON_SKULL == item) {
			return Optional.of(SkullBlock.Type.SKELETON);
		} else if (Items.WITHER_SKELETON_SKULL == item) {
			return Optional.of(SkullBlock.Type.WITHER_SKELETON);
		} else if (Items.PIGLIN_HEAD == item) {
			return Optional.of(SkullBlock.Type.PIGLIN);
		}
		return Optional.empty();
	}
	
	public static Optional<Block> getBlock(SkullBlock.SkullType skullType) {
		if (skullType instanceof SpectrumSkullType spectrumSkullType) {
			return Optional.of(MOB_HEADS.get(spectrumSkullType));
		}
		if (SkullBlock.Type.CREEPER == skullType) {
			return Optional.of(Blocks.CREEPER_HEAD);
		} else if (SkullBlock.Type.DRAGON == skullType) {
			return Optional.of(Blocks.DRAGON_HEAD);
		} else if (SkullBlock.Type.ZOMBIE == skullType) {
			return Optional.of(Blocks.ZOMBIE_HEAD);
		} else if (SkullBlock.Type.SKELETON == skullType) {
			return Optional.of(Blocks.SKELETON_SKULL);
		} else if (SkullBlock.Type.WITHER_SKELETON == skullType) {
			return Optional.of(Blocks.WITHER_SKELETON_SKULL);
		} else if (SkullBlock.Type.PIGLIN == skullType) {
			return Optional.of(Blocks.PIGLIN_HEAD);
		}
		return Optional.empty();
	}
	
	public static SpectrumSkullType getSkullType(Block block) {
		if (block instanceof SpectrumWallSkullBlock) {
			return SpectrumWallSkullBlock.MOB_WALL_HEADS.inverse().get(block);
		} else {
			return MOB_HEADS.inverse().get(block);
		}
	}
	
	public static Optional<SkullBlock.SkullType> getSkullType(EntityType<?> entityType) {
		if (EntityType.CREEPER == entityType) {
			return Optional.of(SkullBlock.Type.CREEPER);
		} else if (EntityType.ENDER_DRAGON == entityType) {
			return Optional.of(SkullBlock.Type.DRAGON);
		} else if (EntityType.ZOMBIE == entityType) {
			return Optional.of(SkullBlock.Type.ZOMBIE);
		} else if (EntityType.SKELETON == entityType) {
			return Optional.of(SkullBlock.Type.SKELETON);
		} else if (EntityType.WITHER_SKELETON == entityType) {
			return Optional.of(SkullBlock.Type.WITHER_SKELETON);
		} else if (EntityType.PIGLIN == entityType) {
			return Optional.of(SkullBlock.Type.PIGLIN);
		}
		
		return Optional.ofNullable(ENTITY_TYPE_TO_SKULL_TYPE.get(entityType));
	}
	
	@Contract(pure = true)
	public static @NotNull Collection<Block> getMobHeads() {
		return MOB_HEADS.values();
	}
	
	private static BlockPattern getWitherSkullPattern() {
		if (witherBossPattern == null) {
			witherBossPattern = BlockPatternBuilder.start().aisle("^^^", "###", "~#~")
					.where('#', (pos) -> pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
					.where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(SpectrumSkullBlock.getBlock(SpectrumSkullType.WITHER).get()).or(BlockStatePredicate.forBlock(SpectrumWallSkullBlock.getMobWallHead(SpectrumSkullType.WITHER)))))
					.where('~', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.AIR))).build();
		}
		
		return witherBossPattern;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		// Trigger advancement if a player builds a wither structure using wither skulls instead of wither skeleton skulls
		if (getSkullType().equals(SpectrumSkullType.WITHER) && placer instanceof ServerPlayerEntity serverPlayerEntity) {
			if (pos.getY() >= world.getBottomY()) {
				BlockPattern blockPattern = getWitherSkullPattern();
				BlockPattern.Result result = blockPattern.searchAround(world, pos);
				if (result != null) {
					Support.grantAdvancementCriterion(serverPlayerEntity, "midgame/build_wither_using_wither_heads", "built_wither_using_wither_heads");
				}
			}
		}
	}
	
}
