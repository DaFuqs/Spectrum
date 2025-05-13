package de.dafuqs.spectrum.blocks.mob_head.client;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.models.*;
import de.dafuqs.spectrum.entity.render.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumSkullBlockEntityRenderer implements BlockEntityRenderer<SpectrumSkullBlockEntity> {
	
	private static Map<SkullBlock.SkullType, List<Pair<SpectrumSkullModel, Identifier>>> MODELS = new HashMap<>();

    public SpectrumSkullBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
        MODELS = getModels(renderContext.getLayerRenderDispatcher());
    }
	
	public static Map<SkullBlock.SkullType, List<Pair<SpectrumSkullModel, Identifier>>> getModels(EntityModelLoader modelLoader) {
		ImmutableMap.Builder<SkullBlock.SkullType, List<Pair<SpectrumSkullModel, Identifier>>> builder = ImmutableBiMap.builder();
        
        // Vanilla
		builder.put(SpectrumSkullType.ALLAY, List.of(new Pair<>(new AllayHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ALLAY_HEAD)), Identifier.of("textures/entity/allay/allay.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_BLUE, List.of(new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_BLUE_HEAD)), Identifier.of("textures/entity/axolotl/axolotl_blue.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_CYAN, List.of(new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_CYAN_HEAD)), Identifier.of("textures/entity/axolotl/axolotl_cyan.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_GOLD, List.of(new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_GOLD_HEAD)), Identifier.of("textures/entity/axolotl/axolotl_gold.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_LEUCISTIC, List.of(new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_LEUCISTIC_HEAD)), Identifier.of("textures/entity/axolotl/axolotl_lucy.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_WILD, List.of(new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_WILD_HEAD)), Identifier.of("textures/entity/axolotl/axolotl_wild.png"))));
		builder.put(SpectrumSkullType.BAT, List.of(new Pair<>(new BatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BAT_HEAD)), Identifier.of("textures/entity/bat.png"))));
		builder.put(SpectrumSkullType.BEE, List.of(new Pair<>(new BeeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BEE_HEAD)), Identifier.of("textures/entity/bee/bee.png"))));
		builder.put(SpectrumSkullType.BLAZE, List.of(new Pair<>(new BlazeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BLAZE_HEAD)), Identifier.of("textures/entity/blaze.png"))));
		builder.put(SpectrumSkullType.CAMEL, List.of(new Pair<>(new CamelHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CAMEL_HEAD)), Identifier.of("textures/entity/camel/camel.png"))));
		builder.put(SpectrumSkullType.CAT, List.of(new Pair<>(new CatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CAT_HEAD)), Identifier.of("textures/entity/cat/tabby.png"))));
		builder.put(SpectrumSkullType.CAVE_SPIDER, List.of(new Pair<>(new SpiderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CAVE_SPIDER_HEAD)), Identifier.of("textures/entity/spider/cave_spider.png"))));
		builder.put(SpectrumSkullType.CHICKEN, List.of(new Pair<>(new ChickenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CHICKEN_HEAD)), Identifier.of("textures/entity/chicken.png"))));
		builder.put(SpectrumSkullType.COW, List.of(new Pair<>(new CowHeadModel(modelLoader.getModelPart(SpectrumModelLayers.COW_HEAD)), Identifier.of("textures/entity/cow/cow.png"))));
		builder.put(SpectrumSkullType.DOLPHIN, List.of(new Pair<>(new DolphinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DOLPHIN_HEAD)), Identifier.of("textures/entity/dolphin.png"))));
		builder.put(SpectrumSkullType.DONKEY, List.of(new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DONKEY_HEAD)), Identifier.of("textures/entity/horse/donkey.png"))));
		builder.put(SpectrumSkullType.DROWNED, List.of(
				new Pair<>(new DrownedHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DROWNED_HEAD)), Identifier.of("textures/entity/zombie/drowned.png")),
				new Pair<>(new DrownedHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DROWNED_HEAD_OUTER)), Identifier.of("textures/entity/zombie/drowned_outer_layer.png"))
		));
		builder.put(SpectrumSkullType.ELDER_GUARDIAN, List.of(new Pair<>(new GuardianHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ELDER_GUARDIAN_HEAD)), Identifier.of("textures/entity/guardian_elder.png"))));
		builder.put(SpectrumSkullType.ENDERMAN, List.of(
				new Pair<>(new EndermanHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ENDERMAN_HEAD)), Identifier.of("textures/entity/enderman/enderman.png")),
				new Pair<>(new EndermanHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ENDERMAN_HEAD_OVERLAY)), Identifier.of("textures/entity/enderman/enderman_eyes.png"))
		));
		builder.put(SpectrumSkullType.ENDERMITE, List.of(new Pair<>(new EndermiteHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ENDERMITE_HEAD)), Identifier.of("textures/entity/endermite.png"))));
		builder.put(SpectrumSkullType.EVOKER, List.of(new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.EVOKER_HEAD)), Identifier.of("textures/entity/illager/evoker.png"))));
		builder.put(SpectrumSkullType.FOX, List.of(new Pair<>(new FoxHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FOX_HEAD)), Identifier.of("textures/entity/fox/fox.png"))));
		builder.put(SpectrumSkullType.FOX_ARCTIC, List.of(new Pair<>(new FoxHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FOX_ARCTIC_HEAD)), Identifier.of("textures/entity/fox/snow_fox.png"))));
		builder.put(SpectrumSkullType.FROG_COLD, List.of(new Pair<>(new FrogHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FROG_COLD_HEAD)), Identifier.of("textures/entity/frog/cold_frog.png"))));
		builder.put(SpectrumSkullType.FROG_TEMPERATE, List.of(new Pair<>(new FrogHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FROG_TEMPERATE_HEAD)), Identifier.of("textures/entity/frog/temperate_frog.png"))));
		builder.put(SpectrumSkullType.FROG_WARM, List.of(new Pair<>(new FrogHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FROG_WARM_HEAD)), Identifier.of("textures/entity/frog/warm_frog.png"))));
		builder.put(SpectrumSkullType.GHAST, List.of(new Pair<>(new GhastHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GHAST_HEAD)), Identifier.of("textures/entity/ghast/ghast.png"))));
		builder.put(SpectrumSkullType.GLOW_SQUID, List.of(new Pair<>(new SquidHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GLOW_SQUID_HEAD)), Identifier.of("textures/entity/squid/glow_squid.png"))));
		builder.put(SpectrumSkullType.GOAT, List.of(new Pair<>(new GoatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GOAT_HEAD)), Identifier.of("textures/entity/goat/goat.png"))));
		builder.put(SpectrumSkullType.GUARDIAN, List.of(new Pair<>(new GuardianHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GUARDIAN_HEAD)), Identifier.of("textures/entity/guardian.png"))));
		builder.put(SpectrumSkullType.HOGLIN, List.of(new Pair<>(new HoglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.HOGLIN_HEAD)), Identifier.of("textures/entity/hoglin/hoglin.png"))));
		builder.put(SpectrumSkullType.HORSE, List.of(new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.HORSE_HEAD)), Identifier.of("textures/entity/horse/horse_chestnut.png"))));
		builder.put(SpectrumSkullType.HUSK, List.of(new Pair<>(new ZombieHeadModel(modelLoader.getModelPart(SpectrumModelLayers.HUSK_HEAD)), Identifier.of("textures/entity/zombie/husk.png"))));
		builder.put(SpectrumSkullType.ILLUSIONER, List.of(new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ILLUSIONER_HEAD)), Identifier.of("textures/entity/illager/illusioner.png"))));
		builder.put(SpectrumSkullType.IRON_GOLEM, List.of(new Pair<>(new IronGolemHeadModel(modelLoader.getModelPart(SpectrumModelLayers.IRON_GOLEM_HEAD)), Identifier.of("textures/entity/iron_golem/iron_golem.png"))));
		builder.put(SpectrumSkullType.LLAMA, List.of(new Pair<>(new LlamaHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LLAMA_HEAD)), Identifier.of("textures/entity/llama/gray.png"))));
		builder.put(SpectrumSkullType.MAGMA_CUBE, List.of(new Pair<>(new SlimeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MAGMA_CUBE_HEAD)), Identifier.of("textures/entity/slime/magmacube.png"))));
		builder.put(SpectrumSkullType.MOOSHROOM_BROWN, List.of(new Pair<>(new CowHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MOOSHROOM_BROWN_HEAD)), Identifier.of("textures/entity/cow/brown_mooshroom.png"))));
		builder.put(SpectrumSkullType.MOOSHROOM_RED, List.of(new Pair<>(new CowHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MOOSHROOM_RED_HEAD)), Identifier.of("textures/entity/cow/red_mooshroom.png"))));
		builder.put(SpectrumSkullType.MULE, List.of(new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MULE_HEAD)), Identifier.of("textures/entity/horse/mule.png"))));
		builder.put(SpectrumSkullType.OCELOT, List.of(new Pair<>(new CatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.OCELOT_HEAD)), Identifier.of("textures/entity/cat/ocelot.png"))));
		builder.put(SpectrumSkullType.PANDA, List.of(new Pair<>(new PandaHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PANDA_HEAD)), Identifier.of("textures/entity/panda/panda.png")))); // pandas have variants
		builder.put(SpectrumSkullType.PARROT_BLUE, List.of(new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_BLUE_HEAD)), Identifier.of("textures/entity/parrot/parrot_blue.png"))));
		builder.put(SpectrumSkullType.PARROT_CYAN, List.of(new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_CYAN_HEAD)), Identifier.of("textures/entity/parrot/parrot_yellow_blue.png")))); // cyan vs. yellow_blue
		builder.put(SpectrumSkullType.PARROT_GRAY, List.of(new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_GRAY_HEAD)), Identifier.of("textures/entity/parrot/parrot_grey.png")))); // gray vs.grey
		builder.put(SpectrumSkullType.PARROT_GREEN, List.of(new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_GREEN_HEAD)), Identifier.of("textures/entity/parrot/parrot_green.png"))));
		builder.put(SpectrumSkullType.PARROT_RED, List.of(new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_RED_HEAD)), Identifier.of("textures/entity/parrot/parrot_red_blue.png")))); // red vs. red_blue
		builder.put(SpectrumSkullType.PHANTOM, List.of(new Pair<>(new PhantomHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PHANTOM_HEAD)), Identifier.of("textures/entity/phantom.png"))));
		builder.put(SpectrumSkullType.PIG, List.of(new Pair<>(new PigHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PIG_HEAD)), Identifier.of("textures/entity/pig/pig.png"))));
		builder.put(SpectrumSkullType.PILLAGER, List.of(new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PILLAGER_HEAD)), Identifier.of("textures/entity/illager/pillager.png"))));
		builder.put(SpectrumSkullType.POLAR_BEAR, List.of(new Pair<>(new BearHeadModel(modelLoader.getModelPart(SpectrumModelLayers.POLAR_BEAR_HEAD)), Identifier.of("textures/entity/bear/polarbear.png"))));
		builder.put(SpectrumSkullType.PUFFERFISH, List.of(new Pair<>(new PufferFishHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PUFFERFISH_HEAD)), Identifier.of("textures/entity/fish/pufferfish.png")))); // other fish?
		builder.put(SpectrumSkullType.RABBIT, List.of(new Pair<>(new RabbitHeadModel(modelLoader.getModelPart(SpectrumModelLayers.RABBIT_HEAD)), Identifier.of("textures/entity/rabbit/brown.png")))); // rabbits have variant
		builder.put(SpectrumSkullType.RAVAGER, List.of(new Pair<>(new RavagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.RAVAGER_HEAD)), Identifier.of("textures/entity/illager/ravager.png"))));
		builder.put(SpectrumSkullType.SALMON, List.of(new Pair<>(new SalmonHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SALMON_HEAD)), Identifier.of("textures/entity/fish/salmon.png")))); // other fish?
		builder.put(SpectrumSkullType.SHEEP, List.of(new Pair<>(new SheepHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHEEP_HEAD)), Identifier.of("textures/entity/sheep/sheep.png"))));
		builder.put(SpectrumSkullType.SHULKER, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_HEAD)), Identifier.of("textures/entity/shulker/shulker.png"))));
		builder.put(SpectrumSkullType.SHULKER_BLACK, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_BLACK_HEAD)), Identifier.of("textures/entity/shulker/shulker_black.png"))));
		builder.put(SpectrumSkullType.SHULKER_BLUE, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_BLUE_HEAD)), Identifier.of("textures/entity/shulker/shulker_blue.png"))));
		builder.put(SpectrumSkullType.SHULKER_BROWN, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_BROWN_HEAD)), Identifier.of("textures/entity/shulker/shulker_brown.png"))));
		builder.put(SpectrumSkullType.SHULKER_CYAN, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_CYAN_HEAD)), Identifier.of("textures/entity/shulker/shulker_cyan.png"))));
		builder.put(SpectrumSkullType.SHULKER_GRAY, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_GRAY_HEAD)), Identifier.of("textures/entity/shulker/shulker_gray.png"))));
		builder.put(SpectrumSkullType.SHULKER_GREEN, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_GREEN_HEAD)), Identifier.of("textures/entity/shulker/shulker_green.png"))));
		builder.put(SpectrumSkullType.SHULKER_LIGHT_BLUE, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_LIGHT_BLUE_HEAD)), Identifier.of("textures/entity/shulker/shulker_light_blue.png"))));
		builder.put(SpectrumSkullType.SHULKER_LIGHT_GRAY, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_LIGHT_GRAY_HEAD)), Identifier.of("textures/entity/shulker/shulker_light_gray.png"))));
		builder.put(SpectrumSkullType.SHULKER_LIME, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_LIME_HEAD)), Identifier.of("textures/entity/shulker/shulker_lime.png"))));
		builder.put(SpectrumSkullType.SHULKER_MAGENTA, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_MAGENTA_HEAD)), Identifier.of("textures/entity/shulker/shulker_magenta.png"))));
		builder.put(SpectrumSkullType.SHULKER_ORANGE, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_ORANGE_HEAD)), Identifier.of("textures/entity/shulker/shulker_orange.png"))));
		builder.put(SpectrumSkullType.SHULKER_PINK, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_PINK_HEAD)), Identifier.of("textures/entity/shulker/shulker_pink.png"))));
		builder.put(SpectrumSkullType.SHULKER_PURPLE, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_PURPLE_HEAD)), Identifier.of("textures/entity/shulker/shulker_purple.png"))));
		builder.put(SpectrumSkullType.SHULKER_RED, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_RED_HEAD)), Identifier.of("textures/entity/shulker/shulker_red.png"))));
		builder.put(SpectrumSkullType.SHULKER_WHITE, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_WHITE_HEAD)), Identifier.of("textures/entity/shulker/shulker_white.png"))));
		builder.put(SpectrumSkullType.SHULKER_YELLOW, List.of(new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_YELLOW_HEAD)), Identifier.of("textures/entity/shulker/shulker_yellow.png"))));
		builder.put(SpectrumSkullType.SILVERFISH, List.of(new Pair<>(new SilverfishHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SILVERFISH_HEAD)), Identifier.of("textures/entity/silverfish.png"))));
		builder.put(SpectrumSkullType.SKELETON_HORSE, List.of(new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SKELETON_HORSE_HEAD)), Identifier.of("textures/entity/horse/horse_skeleton.png"))));
		builder.put(SpectrumSkullType.SLIME, List.of(new Pair<>(new SlimeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SLIME_HEAD)), Identifier.of("textures/entity/slime/slime.png"))));
		builder.put(SpectrumSkullType.SNIFFER, List.of(new Pair<>(new SnifferHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SNIFFER_HEAD)), Identifier.of("textures/entity/sniffer/sniffer.png"))));
		builder.put(SpectrumSkullType.SNOW_GOLEM, List.of(new Pair<>(new ZombieHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SNOW_GOLEM_HEAD)), Identifier.of("textures/entity/snow_golem.png"))));
		builder.put(SpectrumSkullType.SPIDER, List.of(new Pair<>(new SpiderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SPIDER_HEAD)), Identifier.of("textures/entity/spider/spider.png"))));
		builder.put(SpectrumSkullType.SQUID, List.of(new Pair<>(new SquidHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SQUID_HEAD)), Identifier.of("textures/entity/squid/squid.png"))));
		builder.put(SpectrumSkullType.STRAY, List.of(
				new Pair<>(new StrayHeadModel(modelLoader.getModelPart(SpectrumModelLayers.STRAY_HEAD)), Identifier.of("textures/entity/skeleton/stray.png")),
				new Pair<>(new StrayHeadModel(modelLoader.getModelPart(SpectrumModelLayers.STRAY_HEAD_OVERLAY)), Identifier.of("textures/entity/skeleton/stray_overlay.png"))
		));
		builder.put(SpectrumSkullType.STRIDER, List.of(new Pair<>(new StriderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.STRIDER_HEAD)), Identifier.of("textures/entity/strider/strider.png"))));
		builder.put(SpectrumSkullType.TADPOLE, List.of(new Pair<>(new TadpoleHeadModel(modelLoader.getModelPart(SpectrumModelLayers.TADPOLE_HEAD)), Identifier.of("textures/entity/tadpole/tadpole.png"))));
		builder.put(SpectrumSkullType.TROPICAL_FISH, List.of(new Pair<>(new TropicalFishHeadModel(modelLoader.getModelPart(SpectrumModelLayers.TROPICAL_FISH_HEAD), modelLoader.getModelPart(SpectrumModelLayers.TROPICAL_FISH_HEAD_PATTERN)), Identifier.of("textures/entity/fish/tropical_a.png")))); // oof
		builder.put(SpectrumSkullType.TURTLE, List.of(new Pair<>(new TurtleHeadModel(modelLoader.getModelPart(SpectrumModelLayers.TURTLE_HEAD)), Identifier.of("textures/entity/turtle/big_sea_turtle.png"))));
		builder.put(SpectrumSkullType.VEX, List.of(new Pair<>(new VexHeadModel(modelLoader.getModelPart(SpectrumModelLayers.VEX_HEAD)), Identifier.of("textures/entity/illager/vex.png"))));
		builder.put(SpectrumSkullType.VILLAGER, List.of(new Pair<>(new VillagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.VILLAGER_HEAD)), Identifier.of("textures/entity/villager/villager.png"))));
		builder.put(SpectrumSkullType.VINDICATOR, List.of(new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.VINDICATOR_HEAD)), Identifier.of("textures/entity/illager/vindicator.png"))));
		builder.put(SpectrumSkullType.WANDERING_TRADER, List.of(new Pair<>(new VillagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WANDERING_TRADER_HEAD)), Identifier.of("textures/entity/wandering_trader.png"))));
		builder.put(SpectrumSkullType.WARDEN, List.of(
				new Pair<>(new WardenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WARDEN_HEAD)), Identifier.of("textures/entity/warden/warden.png")),
				new Pair<>(new WardenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WARDEN_HEAD_BIOLUMINESCENT)), Identifier.of("textures/entity/warden/warden_bioluminescent_layer.png")),
				new Pair<>(new WardenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WARDEN_HEAD_PULSATING_SPOTS_1)), Identifier.of("textures/entity/warden/warden_pulsating_spots_1.png")),
				new Pair<>(new WardenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WARDEN_HEAD_PULSATING_SPOTS_2)), Identifier.of("textures/entity/warden/warden_pulsating_spots_2.png"))
		));
		builder.put(SpectrumSkullType.WITCH, List.of(new Pair<>(new WitchHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WITCH_HEAD)), Identifier.of("textures/entity/witch.png"))));
		builder.put(SpectrumSkullType.WITHER, List.of(new Pair<>(new ZombieHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WITHER_HEAD)), Identifier.of("textures/entity/wither/wither.png"))));
		builder.put(SpectrumSkullType.WOLF, List.of(new Pair<>(new WolfHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WOLF_HEAD)), Identifier.of("textures/entity/wolf/wolf.png"))));
		builder.put(SpectrumSkullType.ZOGLIN, List.of(new Pair<>(new HoglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOGLIN_HEAD)), Identifier.of("textures/entity/hoglin/zoglin.png"))));
		builder.put(SpectrumSkullType.ZOMBIE_HORSE, List.of(new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOMBIE_HORSE_HEAD)), Identifier.of("textures/entity/horse/horse_zombie.png"))));
		builder.put(SpectrumSkullType.ZOMBIE_VILLAGER, List.of(new Pair<>(new VillagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOMBIE_VILLAGER_HEAD)), Identifier.of("textures/entity/zombie_villager/zombie_villager.png"))));
		builder.put(SpectrumSkullType.ZOMBIFIED_PIGLIN, List.of(new Pair<>(new PiglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOMBIFIED_PIGLIN_HEAD)), Identifier.of("textures/entity/piglin/zombified_piglin.png"))));
		builder.put(SpectrumSkullType.PIGLIN_BRUTE, List.of(new Pair<>(new PiglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PIGLIN_BRUTE_HEAD)), Identifier.of("textures/entity/piglin/piglin_brute.png"))));
		
		builder.put(SpectrumSkullType.ARMADILLO, List.of(new Pair<>(new ArmadilloHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ARMADILLO_HEAD)), Identifier.of("textures/entity/armadillo.png"))));
		builder.put(SpectrumSkullType.BREEZE, List.of(new Pair<>(new BreezeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BREEZE_HEAD)), Identifier.of("textures/entity/breeze/breeze.png"))));
		builder.put(SpectrumSkullType.BOGGED, List.of(
				new Pair<>(new BoggedHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BOGGED_HEAD)), Identifier.of("textures/entity/skeleton/bogged.png")),
				new Pair<>(new BoggedHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BOGGED_HEAD_OVERLAY)), Identifier.of("textures/entity/skeleton/bogged_overlay.png")
				)));

        // Spectrum
		builder.put(SpectrumSkullType.EGG_LAYING_WOOLY_PIG, List.of(new Pair<>(new EggLayingWoolyPigHeadModel(modelLoader.getModelPart(SpectrumModelLayers.EGG_LAYING_WOOLY_PIG_HEAD)), EggLayingWoolyPigEntityRenderer.TEXTURE)));
		builder.put(SpectrumSkullType.ERASER, List.of(new Pair<>(new SpiderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ERASER_HEAD)), SpectrumCommon.locate("textures/entity/eraser/eraser_combined.png"))));
		builder.put(SpectrumSkullType.KINDLING, List.of(new Pair<>(new KindlingHeadModel(modelLoader.getModelPart(SpectrumModelLayers.KINDLING_HEAD)), KindlingVariant.DEFAULT.getDefaultTexture())));
		builder.put(SpectrumSkullType.LIZARD_BLACK, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BLACK.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_BLUE, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BLUE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_BROWN, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BROWN.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_CYAN, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.CYAN.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_GRAY, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.GRAY.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_GREEN, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.GREEN.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_LIGHT_BLUE, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIGHT_BLUE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_LIGHT_GRAY, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIGHT_GRAY.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_LIME, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIME.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_MAGENTA, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.MAGENTA.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_ORANGE, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.ORANGE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_PINK, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.PINK.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_PURPLE, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.PURPLE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_RED, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.RED.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_WHITE, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.WHITE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_YELLOW, List.of(new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.YELLOW.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.MONSTROSITY, List.of(new Pair<>(new MonstrosityHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MONSTROSITY_HEAD)), MonstrosityEntityRenderer.TEXTURE)));
		builder.put(SpectrumSkullType.PRESERVATION_TURRET, List.of(new Pair<>(new PreservationTurretHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PRESERVATION_TURRET_HEAD)), PreservationTurretEntityRenderer.TEXTURE)));

        return builder.build();
    }

    @Override
    public void render(SpectrumSkullBlockEntity spectrumSkullBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        BlockState blockState = spectrumSkullBlockEntity.getCachedState();
        Direction direction = null;
        float yaw = 22.5F;
        if (blockState.getBlock() instanceof WallSkullBlock) {
            direction = blockState.get(WallSkullBlock.FACING);
            yaw *= (2 + direction.getHorizontal()) * 4;
        } else {
            yaw *= blockState.get(SkullBlock.ROTATION);
        }
        SpectrumSkullType skullType = SpectrumSkullBlock.getSkullType(spectrumSkullBlockEntity.getCachedState().getBlock());
		
		renderModels(tickDelta, matrixStack, vertexConsumerProvider, light, skullType, direction, yaw);
		
	}
	
	public static void renderModels(float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, SpectrumSkullType skullType, Direction direction, float yaw) {
		List<Pair<SpectrumSkullModel, Identifier>> model = MODELS.get(skullType);
		for (Pair<SpectrumSkullModel, Identifier> entry : model) {
			RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(entry.getRight());
			renderSkull(direction, yaw, tickDelta, matrixStack, vertexConsumerProvider, light, entry.getLeft(), renderLayer);
		}
	}
	
	private static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SpectrumSkullModel model, RenderLayer renderLayer) {
        matrices.push();
        if (direction == null) {
            matrices.translate(0.5D, 0.0D, 0.5D);
        } else {
            matrices.translate((0.5F - (float) direction.getOffsetX() * 0.25F), 0.25D, (0.5F - (float) direction.getOffsetZ() * 0.25F));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.setHeadRotation(animationProgress, yaw, 0.0F);
        model.render(matrices, vertexConsumer, vertexConsumers, light, OverlayTexture.DEFAULT_UV, -1);
        matrices.pop();
    }

}
