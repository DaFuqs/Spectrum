package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.pattern.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.predicate.block.*;
import net.minecraft.server.network.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.function.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

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
		
		// Trigger advancement if player builds a wither structure using wither skulls instead of wither skeleton skulls
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
		SHULKER(EntityType.SHULKER),
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
		ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN),
		FROG_TEMPERATE(EntityType.FROG),
		FROG_WARM(EntityType.FROG),
		FROG_COLD(EntityType.FROG),
		TADPOLE(EntityType.TADPOLE),
		WARDEN(EntityType.WARDEN),
		ALLAY(EntityType.ALLAY),
		
		EGG_LAYING_WOOLY_PIG(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, true),
		KINDLING(SpectrumEntityTypes.KINDLING, true),
		GUARDIAN_TURRET(SpectrumEntityTypes.GUARDIAN_TURRET, true),
		MONSTROSITY(SpectrumEntityTypes.MONSTROSITY, true),
		LIZARD(SpectrumEntityTypes.LIZARD, true);
		
		public final EntityType entityType;
		public final SkullType modelType;

		// most mob heads render with the player head renderer using a different texture, but some use unique renderers already
		// somewhen in the future hopefully all of them get their own unique head block model
		SpectrumSkullBlockType(EntityType entityType) {
			this(entityType, false);
		}

		// if you use this constructor you will also need to add that unique Renderer
		// to SpectrumSkullBlockEntityRenderer.getModels()
		SpectrumSkullBlockType(EntityType entityType, boolean useUniqueRenderer) {
			this.entityType = entityType;
			this.modelType = useUniqueRenderer ? this : Type.PLAYER;
		}
		
		public SkullType getModelType() {
			return this.modelType;
		}
		
	}
	
}
