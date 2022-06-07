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
import net.minecraft.entity.EntityType;
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
	
	private static BlockPattern getWitherSkullPattern() {
		if (witherBossPattern == null) {
			witherBossPattern = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', (pos) ->
							pos.getBlockState().isIn(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
					.where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(SpectrumBlocks.getMobHead(SpectrumSkullBlockType.WITHER))
							.or(BlockStatePredicate.forBlock(SpectrumBlocks.getMobWallHead(SpectrumSkullBlockType.WITHER)))))
					.where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}
		
		return witherBossPattern;
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		// Trigger advancement if player builds a wither structure with using wither skulls
		if (getSkullType().equals(SpectrumSkullBlockType.WITHER) && placer instanceof ServerPlayerEntity serverPlayerEntity) {
			if (pos.getY() >= world.getBottomY()) {
				BlockPattern blockPattern = getWitherSkullPattern();
				BlockPattern.Result result = blockPattern.searchAround(world, pos);
				if (result != null) {
					Support.grantAdvancementCriterion(serverPlayerEntity, "midgame/build_wither_using_wither_heads", "built_wither_using_wither_heads");
				}
			}
		}
	}
	
	// TODO: differentiate parrot / fox / ... colors
	public enum SpectrumSkullBlockType implements SkullBlock.SkullType {
		AXOLOTL_BLUE(EntityType.AXOLOTL),
		AXOLOTL_BROWN(EntityType.AXOLOTL),
		AXOLOTL_CYAN(EntityType.AXOLOTL),
		AXOLOTL_GOLD(EntityType.AXOLOTL),
		AXOLOTL_LEUCISTIC(EntityType.AXOLOTL),
		BAT(EntityType.BAT),
		BEE(EntityType.BEE),
		BLAZE(EntityType.BLAZE),
		CAT(EntityType.CAT),
		CAVE_SPIDER(EntityType.CAVE_SPIDER),
		CHICKEN(EntityType.CHICKEN),
		CLOWNFISH(EntityType.TROPICAL_FISH),
		COW(EntityType.COW),
		DONKEY(EntityType.DONKEY),
		DROWNED(EntityType.DROWNED),
		ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN),
		ENDERMAN(EntityType.ENDERMAN),
		ENDERMITE(EntityType.ENDERMITE),
		EVOKER(EntityType.EVOKER),
		FOX(EntityType.FOX),
		FOX_ARCTIC(EntityType.FOX),
		GHAST(EntityType.GHAST),
		GLOW_SQUID(EntityType.GLOW_SQUID),
		GOAT(EntityType.GOAT),
		GUARDIAN(EntityType.GUARDIAN),
		HOGLIN(EntityType.HOGLIN),
		HORSE(EntityType.HORSE),
		HUSK(EntityType.HUSK),
		ILLUSIONER(EntityType.ILLUSIONER),
		IRON_GOLEM(EntityType.IRON_GOLEM),
		LLAMA(EntityType.LLAMA),
		MAGMA_CUBE(EntityType.MAGMA_CUBE),
		MOOSHROOM_BROWN(EntityType.MOOSHROOM),
		MOOSHROOM_RED(EntityType.MOOSHROOM),
		MULE(EntityType.MULE),
		OCELOT(EntityType.OCELOT),
		PANDA(EntityType.PANDA),
		PARROT_BLUE(EntityType.PARROT),
		PARROT_CYAN(EntityType.PARROT),
		PARROT_GRAY(EntityType.PARROT),
		PARROT_GREEN(EntityType.PARROT),
		PARROT_RED(EntityType.PARROT),
		PHANTOM(EntityType.PHANTOM),
		PIG(EntityType.PIG),
		PIGLIN(EntityType.PIGLIN),
		POLAR_BEAR(EntityType.POLAR_BEAR),
		PUFFERFISH(EntityType.PUFFERFISH),
		RABBIT(EntityType.RABBIT),
		RAVAGER(EntityType.RAVAGER),
		SALMON(EntityType.SALMON),
		SHEEP_BLACK(EntityType.SHEEP),
		SHEEP_BLUE(EntityType.SHEEP),
		SHEEP_BROWN(EntityType.SHEEP),
		SHEEP_CYAN(EntityType.SHEEP),
		SHEEP_GRAY(EntityType.SHEEP),
		SHEEP_GREEN(EntityType.SHEEP),
		SHEEP_LIGHT_BLUE(EntityType.SHEEP),
		SHEEP_LIGHT_GRAY(EntityType.SHEEP),
		SHEEP_LIME(EntityType.SHEEP),
		SHEEP_MAGENTA(EntityType.SHEEP),
		SHEEP_ORANGE(EntityType.SHEEP),
		SHEEP_PINK(EntityType.SHEEP),
		SHEEP_PURPLE(EntityType.SHEEP),
		SHEEP_RED(EntityType.SHEEP),
		SHEEP_WHITE(EntityType.SHEEP),
		SHEEP_YELLOW(EntityType.SHEEP),
		SHULKER_BLACK(EntityType.SHULKER),
		SHULKER_BLUE(EntityType.SHULKER),
		SHULKER_BROWN(EntityType.SHULKER),
		SHULKER_CYAN(EntityType.SHULKER),
		SHULKER_GRAY(EntityType.SHULKER),
		SHULKER_GREEN(EntityType.SHULKER),
		SHULKER_LIGHT_BLUE(EntityType.SHULKER),
		SHULKER_LIGHT_GRAY(EntityType.SHULKER),
		SHULKER_LIME(EntityType.SHULKER),
		SHULKER_MAGENTA(EntityType.SHULKER),
		SHULKER_ORANGE(EntityType.SHULKER),
		SHULKER_PINK(EntityType.SHULKER),
		SHULKER_PURPLE(EntityType.SHULKER),
		SHULKER_RED(EntityType.SHULKER),
		SHULKER_WHITE(EntityType.SHULKER),
		SHULKER_YELLOW(EntityType.SHULKER),
		SILVERFISH(EntityType.SILVERFISH),
		SLIME(EntityType.SLIME),
		SNOW_GOLEM(EntityType.SNOW_GOLEM),
		SPIDER(EntityType.SPIDER),
		SQUID(EntityType.SQUID),
		STRAY(EntityType.STRAY),
		STRIDER(EntityType.STRIDER),
		TRADER_LLAMA(EntityType.TRADER_LLAMA),
		TURTLE(EntityType.TURTLE),
		VEX(EntityType.VEX),
		VILLAGER(EntityType.VILLAGER),
		VINDICATOR(EntityType.VINDICATOR),
		WANDERING_TRADER(EntityType.WANDERING_TRADER),
		WITCH(EntityType.WITCH),
		WITHER(EntityType.WITHER),
		WOLF(EntityType.WOLF),
		ZOGLIN(EntityType.ZOGLIN),
		ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER),
		ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN);
		
		public final EntityType entityType;
		
		SpectrumSkullBlockType(EntityType entityType) {
			this.entityType = entityType;
		}
		
	}
	
}
