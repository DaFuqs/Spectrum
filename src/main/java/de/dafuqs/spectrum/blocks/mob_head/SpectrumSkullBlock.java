package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpectrumSkullBlock extends SkullBlock {
	
	@Nullable
	private static BlockPattern witherBossPattern;
	
	public SpectrumSkullBlock(SkullType skullType, Settings settings) {
		super(skullType, settings);
	}

	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}

	public enum Type implements SkullBlock.SkullType {
		AXOLOTL_BLUE,
		AXOLOTL_BROWN,
		AXOLOTL_CYAN,
		AXOLOTL_GOLD,
		AXOLOTL_LEUCISTIC,
		BAT,
		BEE,
		BLAZE,
		CAT,
		CAVE_SPIDER,
		CHICKEN,
		CLOWNFISH,
		COW,
		DONKEY,
		DROWNED,
		ELDER_GUARDIAN,
		ENDERMAN,
		ENDERMITE,
		EVOKER,
		FOX,
		FOX_ARCTIC,
		GHAST,
		GLOW_SQUID,
		GOAT,
		GUARDIAN,
		HOGLIN,
		HORSE,
		HUSK,
		ILLUSIONER,
		IRON_GOLEM,
		LLAMA,
		MAGMA_CUBE,
		MOOSHROOM_BROWN,
		MOOSHROOM_RED,
		MULE,
		OCELOT,
		PANDA,
		PARROT_BLUE,
		PARROT_CYAN,
		PARROT_GRAY,
		PARROT_GREEN,
		PARROT_RED,
		PHANTOM,
		PIG,
		PIGLIN,
		POLAR_BEAR,
		PUFFERFISH,
		RABBIT,
		RAVAGER,
		SALMON,
		SHEEP_BLACK,
		SHEEP_BLUE,
		SHEEP_BROWN,
		SHEEP_CYAN,
		SHEEP_GRAY,
		SHEEP_GREEN,
		SHEEP_LIGHT_BLUE,
		SHEEP_LIGHT_GRAY,
		SHEEP_LIME,
		SHEEP_MAGENTA,
		SHEEP_ORANGE,
		SHEEP_PINK,
		SHEEP_PURPLE,
		SHEEP_RED,
		SHEEP_WHITE,
		SHEEP_YELLOW,
		SHULKER_BLACK,
		SHULKER_BLUE,
		SHULKER_BROWN,
		SHULKER_CYAN,
		SHULKER_GRAY,
		SHULKER_GREEN,
		SHULKER_LIGHT_BLUE,
		SHULKER_LIGHT_GRAY,
		SHULKER_LIME,
		SHULKER_MAGENTA,
		SHULKER_ORANGE,
		SHULKER_PINK,
		SHULKER_PURPLE,
		SHULKER_RED,
		SHULKER_WHITE,
		SHULKER_YELLOW,
		SILVERFISH,
		SLIME,
		SNOW_GOLEM,
		SPIDER,
		SQUID,
		STRAY,
		STRIDER,
		TRADER_LLAMA,
		TURTLE,
		VEX,
		VILLAGER,
		VINDICATOR,
		WANDERING_TRADER,
		WITCH,
		WITHER,
		WOLF,
		ZOGLIN,
		ZOMBIE_VILLAGER,
		ZOMBIFIED_PIGLIN
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);

		// Trigger advancement if player builds a wither structure with using wither skulls
		if (getSkullType().equals(Type.WITHER) && placer instanceof ServerPlayerEntity serverPlayerEntity) {
			if (pos.getY() >= world.getBottomY()) {
				BlockPattern blockPattern = getWitherSkullPattern();
				BlockPattern.Result result = blockPattern.searchAround(world, pos);
				if (result != null) {
					Support.grantAdvancementCriterion(serverPlayerEntity, "midgame/build_wither_using_wither_heads", "built_wither_using_wither_heads");
				}
			}
		}
	}
	
	private static BlockPattern getWitherSkullPattern() {
		if (witherBossPattern == null) {
			witherBossPattern = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', (pos) ->
					pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
					.where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(SpectrumBlocks.getMobHead(Type.WITHER))
							.or(BlockStatePredicate.forBlock(SpectrumBlocks.getMobWallHead(Type.WITHER)))))
					.where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}
		
		return witherBossPattern;
	}
	
}
