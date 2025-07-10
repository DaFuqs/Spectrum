package de.dafuqs.spectrum.blocks.mob_head.client;

import com.google.common.collect.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.models.*;
import de.dafuqs.spectrum.entity.render.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumSkullBlockEntityRenderer implements BlockEntityRenderer<SpectrumSkullBlockEntity> {
	
	private static Map<SkullBlock.Type, List<Tuple<SpectrumSkullModel, ResourceLocation>>> MODELS = new HashMap<>();
	
	public SpectrumSkullBlockEntityRenderer(BlockEntityRendererProvider.Context renderContext) {
		MODELS = getModels(renderContext.getModelSet());
    }
	
	public static Map<SkullBlock.Type, List<Tuple<SpectrumSkullModel, ResourceLocation>>> getModels(EntityModelSet modelLoader) {
		ImmutableMap.Builder<SkullBlock.Type, List<Tuple<SpectrumSkullModel, ResourceLocation>>> builder = ImmutableBiMap.builder();
        
        // Vanilla
		builder.put(SpectrumSkullType.ALLAY, List.of(new Tuple<>(new AllayHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ALLAY_HEAD)), ResourceLocation.parse("textures/entity/allay/allay.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_BLUE, List.of(new Tuple<>(new AxolotlHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.AXOLOTL_BLUE_HEAD)), ResourceLocation.parse("textures/entity/axolotl/axolotl_blue.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_CYAN, List.of(new Tuple<>(new AxolotlHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.AXOLOTL_CYAN_HEAD)), ResourceLocation.parse("textures/entity/axolotl/axolotl_cyan.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_GOLD, List.of(new Tuple<>(new AxolotlHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.AXOLOTL_GOLD_HEAD)), ResourceLocation.parse("textures/entity/axolotl/axolotl_gold.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_LEUCISTIC, List.of(new Tuple<>(new AxolotlHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.AXOLOTL_LEUCISTIC_HEAD)), ResourceLocation.parse("textures/entity/axolotl/axolotl_lucy.png"))));
		builder.put(SpectrumSkullType.AXOLOTL_WILD, List.of(new Tuple<>(new AxolotlHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.AXOLOTL_WILD_HEAD)), ResourceLocation.parse("textures/entity/axolotl/axolotl_wild.png"))));
		builder.put(SpectrumSkullType.BAT, List.of(new Tuple<>(new BatHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.BAT_HEAD)), ResourceLocation.parse("textures/entity/bat.png"))));
		builder.put(SpectrumSkullType.BEE, List.of(new Tuple<>(new BeeHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.BEE_HEAD)), ResourceLocation.parse("textures/entity/bee/bee.png"))));
		builder.put(SpectrumSkullType.BLAZE, List.of(new Tuple<>(new BlazeHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.BLAZE_HEAD)), ResourceLocation.parse("textures/entity/blaze.png"))));
		builder.put(SpectrumSkullType.CAMEL, List.of(new Tuple<>(new CamelHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.CAMEL_HEAD)), ResourceLocation.parse("textures/entity/camel/camel.png"))));
		builder.put(SpectrumSkullType.CAT, List.of(new Tuple<>(new CatHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.CAT_HEAD)), ResourceLocation.parse("textures/entity/cat/tabby.png"))));
		builder.put(SpectrumSkullType.CAVE_SPIDER, List.of(new Tuple<>(new SpiderHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.CAVE_SPIDER_HEAD)), ResourceLocation.parse("textures/entity/spider/cave_spider.png"))));
		builder.put(SpectrumSkullType.CHICKEN, List.of(new Tuple<>(new ChickenHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.CHICKEN_HEAD)), ResourceLocation.parse("textures/entity/chicken.png"))));
		builder.put(SpectrumSkullType.COD, List.of(new Tuple<>(new CodHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.COD_HEAD)), ResourceLocation.parse("textures/entity/fish/cod.png"))));
		builder.put(SpectrumSkullType.COW, List.of(new Tuple<>(new CowHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.COW_HEAD)), ResourceLocation.parse("textures/entity/cow/cow.png"))));
		builder.put(SpectrumSkullType.DOLPHIN, List.of(new Tuple<>(new DolphinHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.DOLPHIN_HEAD)), ResourceLocation.parse("textures/entity/dolphin.png"))));
		builder.put(SpectrumSkullType.DONKEY, List.of(new Tuple<>(new HorseHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.DONKEY_HEAD)), ResourceLocation.parse("textures/entity/horse/donkey.png"))));
		builder.put(SpectrumSkullType.DROWNED, List.of(
				new Tuple<>(new DrownedHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.DROWNED_HEAD)), ResourceLocation.parse("textures/entity/zombie/drowned.png")),
				new Tuple<>(new DrownedHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.DROWNED_HEAD_OUTER)), ResourceLocation.parse("textures/entity/zombie/drowned_outer_layer.png"))
		));
		builder.put(SpectrumSkullType.ELDER_GUARDIAN, List.of(new Tuple<>(new GuardianHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ELDER_GUARDIAN_HEAD)), ResourceLocation.parse("textures/entity/guardian_elder.png"))));
		builder.put(SpectrumSkullType.ENDERMAN, List.of(
				new Tuple<>(new EndermanHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ENDERMAN_HEAD)), ResourceLocation.parse("textures/entity/enderman/enderman.png")),
				new Tuple<>(new EndermanHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ENDERMAN_HEAD_OVERLAY)), ResourceLocation.parse("textures/entity/enderman/enderman_eyes.png"))
		));
		builder.put(SpectrumSkullType.ENDERMITE, List.of(new Tuple<>(new EndermiteHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ENDERMITE_HEAD)), ResourceLocation.parse("textures/entity/endermite.png"))));
		builder.put(SpectrumSkullType.EVOKER, List.of(new Tuple<>(new IllagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.EVOKER_HEAD)), ResourceLocation.parse("textures/entity/illager/evoker.png"))));
		builder.put(SpectrumSkullType.FOX, List.of(new Tuple<>(new FoxHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.FOX_HEAD)), ResourceLocation.parse("textures/entity/fox/fox.png"))));
		builder.put(SpectrumSkullType.FOX_ARCTIC, List.of(new Tuple<>(new FoxHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.FOX_ARCTIC_HEAD)), ResourceLocation.parse("textures/entity/fox/snow_fox.png"))));
		builder.put(SpectrumSkullType.FROG_COLD, List.of(new Tuple<>(new FrogHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.FROG_COLD_HEAD)), ResourceLocation.parse("textures/entity/frog/cold_frog.png"))));
		builder.put(SpectrumSkullType.FROG_TEMPERATE, List.of(new Tuple<>(new FrogHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.FROG_TEMPERATE_HEAD)), ResourceLocation.parse("textures/entity/frog/temperate_frog.png"))));
		builder.put(SpectrumSkullType.FROG_WARM, List.of(new Tuple<>(new FrogHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.FROG_WARM_HEAD)), ResourceLocation.parse("textures/entity/frog/warm_frog.png"))));
		builder.put(SpectrumSkullType.GHAST, List.of(new Tuple<>(new GhastHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.GHAST_HEAD)), ResourceLocation.parse("textures/entity/ghast/ghast.png"))));
		builder.put(SpectrumSkullType.GLOW_SQUID, List.of(new Tuple<>(new SquidHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.GLOW_SQUID_HEAD)), ResourceLocation.parse("textures/entity/squid/glow_squid.png"))));
		builder.put(SpectrumSkullType.GOAT, List.of(new Tuple<>(new GoatHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.GOAT_HEAD)), ResourceLocation.parse("textures/entity/goat/goat.png"))));
		builder.put(SpectrumSkullType.GUARDIAN, List.of(new Tuple<>(new GuardianHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.GUARDIAN_HEAD)), ResourceLocation.parse("textures/entity/guardian.png"))));
		builder.put(SpectrumSkullType.HOGLIN, List.of(new Tuple<>(new HoglinHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.HOGLIN_HEAD)), ResourceLocation.parse("textures/entity/hoglin/hoglin.png"))));
		builder.put(SpectrumSkullType.HORSE, List.of(new Tuple<>(new HorseHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.HORSE_HEAD)), ResourceLocation.parse("textures/entity/horse/horse_chestnut.png"))));
		builder.put(SpectrumSkullType.HUSK, List.of(new Tuple<>(new ZombieHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.HUSK_HEAD)), ResourceLocation.parse("textures/entity/zombie/husk.png"))));
		builder.put(SpectrumSkullType.ILLUSIONER, List.of(new Tuple<>(new IllagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ILLUSIONER_HEAD)), ResourceLocation.parse("textures/entity/illager/illusioner.png"))));
		builder.put(SpectrumSkullType.IRON_GOLEM, List.of(new Tuple<>(new IronGolemHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.IRON_GOLEM_HEAD)), ResourceLocation.parse("textures/entity/iron_golem/iron_golem.png"))));
		builder.put(SpectrumSkullType.LLAMA, List.of(new Tuple<>(new LlamaHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LLAMA_HEAD)), ResourceLocation.parse("textures/entity/llama/gray.png"))));
		builder.put(SpectrumSkullType.MAGMA_CUBE, List.of(new Tuple<>(new MagmaCubeHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.MAGMA_CUBE_HEAD)), ResourceLocation.parse("textures/entity/slime/magmacube.png"))));
		builder.put(SpectrumSkullType.MOOSHROOM_BROWN, List.of(new Tuple<>(new CowHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.MOOSHROOM_BROWN_HEAD)), ResourceLocation.parse("textures/entity/cow/brown_mooshroom.png"))));
		builder.put(SpectrumSkullType.MOOSHROOM_RED, List.of(new Tuple<>(new CowHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.MOOSHROOM_RED_HEAD)), ResourceLocation.parse("textures/entity/cow/red_mooshroom.png"))));
		builder.put(SpectrumSkullType.MULE, List.of(new Tuple<>(new HorseHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.MULE_HEAD)), ResourceLocation.parse("textures/entity/horse/mule.png"))));
		builder.put(SpectrumSkullType.OCELOT, List.of(new Tuple<>(new CatHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.OCELOT_HEAD)), ResourceLocation.parse("textures/entity/cat/ocelot.png"))));
		builder.put(SpectrumSkullType.PANDA, List.of(new Tuple<>(new PandaHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PANDA_HEAD)), ResourceLocation.parse("textures/entity/panda/panda.png")))); // pandas have variants
		builder.put(SpectrumSkullType.PARROT_BLUE, List.of(new Tuple<>(new ParrotHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PARROT_BLUE_HEAD)), ResourceLocation.parse("textures/entity/parrot/parrot_blue.png"))));
		builder.put(SpectrumSkullType.PARROT_CYAN, List.of(new Tuple<>(new ParrotHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PARROT_CYAN_HEAD)), ResourceLocation.parse("textures/entity/parrot/parrot_yellow_blue.png")))); // cyan vs. yellow_blue
		builder.put(SpectrumSkullType.PARROT_GRAY, List.of(new Tuple<>(new ParrotHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PARROT_GRAY_HEAD)), ResourceLocation.parse("textures/entity/parrot/parrot_grey.png")))); // gray vs.grey
		builder.put(SpectrumSkullType.PARROT_GREEN, List.of(new Tuple<>(new ParrotHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PARROT_GREEN_HEAD)), ResourceLocation.parse("textures/entity/parrot/parrot_green.png"))));
		builder.put(SpectrumSkullType.PARROT_RED, List.of(new Tuple<>(new ParrotHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PARROT_RED_HEAD)), ResourceLocation.parse("textures/entity/parrot/parrot_red_blue.png")))); // red vs. red_blue
		builder.put(SpectrumSkullType.PHANTOM, List.of(new Tuple<>(new PhantomHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PHANTOM_HEAD)), ResourceLocation.parse("textures/entity/phantom.png"))));
		builder.put(SpectrumSkullType.PIG, List.of(new Tuple<>(new PigHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PIG_HEAD)), ResourceLocation.parse("textures/entity/pig/pig.png"))));
		builder.put(SpectrumSkullType.PILLAGER, List.of(new Tuple<>(new IllagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PILLAGER_HEAD)), ResourceLocation.parse("textures/entity/illager/pillager.png"))));
		builder.put(SpectrumSkullType.POLAR_BEAR, List.of(new Tuple<>(new BearHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.POLAR_BEAR_HEAD)), ResourceLocation.parse("textures/entity/bear/polarbear.png"))));
		builder.put(SpectrumSkullType.PUFFERFISH, List.of(new Tuple<>(new PufferFishHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PUFFERFISH_HEAD)), ResourceLocation.parse("textures/entity/fish/pufferfish.png")))); // other fish?
		builder.put(SpectrumSkullType.RABBIT, List.of(new Tuple<>(new RabbitHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.RABBIT_HEAD)), ResourceLocation.parse("textures/entity/rabbit/brown.png")))); // rabbits have variant
		builder.put(SpectrumSkullType.RAVAGER, List.of(new Tuple<>(new RavagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.RAVAGER_HEAD)), ResourceLocation.parse("textures/entity/illager/ravager.png"))));
		builder.put(SpectrumSkullType.SALMON, List.of(new Tuple<>(new SalmonHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SALMON_HEAD)), ResourceLocation.parse("textures/entity/fish/salmon.png")))); // other fish?
		builder.put(SpectrumSkullType.SHEEP, List.of(new Tuple<>(new SheepHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHEEP_HEAD)), ResourceLocation.parse("textures/entity/sheep/sheep.png"))));
		builder.put(SpectrumSkullType.SHULKER, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker.png"))));
		builder.put(SpectrumSkullType.SHULKER_BLACK, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_BLACK_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_black.png"))));
		builder.put(SpectrumSkullType.SHULKER_BLUE, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_BLUE_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_blue.png"))));
		builder.put(SpectrumSkullType.SHULKER_BROWN, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_BROWN_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_brown.png"))));
		builder.put(SpectrumSkullType.SHULKER_CYAN, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_CYAN_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_cyan.png"))));
		builder.put(SpectrumSkullType.SHULKER_GRAY, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_GRAY_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_gray.png"))));
		builder.put(SpectrumSkullType.SHULKER_GREEN, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_GREEN_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_green.png"))));
		builder.put(SpectrumSkullType.SHULKER_LIGHT_BLUE, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_LIGHT_BLUE_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_light_blue.png"))));
		builder.put(SpectrumSkullType.SHULKER_LIGHT_GRAY, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_LIGHT_GRAY_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_light_gray.png"))));
		builder.put(SpectrumSkullType.SHULKER_LIME, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_LIME_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_lime.png"))));
		builder.put(SpectrumSkullType.SHULKER_MAGENTA, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_MAGENTA_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_magenta.png"))));
		builder.put(SpectrumSkullType.SHULKER_ORANGE, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_ORANGE_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_orange.png"))));
		builder.put(SpectrumSkullType.SHULKER_PINK, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_PINK_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_pink.png"))));
		builder.put(SpectrumSkullType.SHULKER_PURPLE, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_PURPLE_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_purple.png"))));
		builder.put(SpectrumSkullType.SHULKER_RED, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_RED_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_red.png"))));
		builder.put(SpectrumSkullType.SHULKER_WHITE, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_WHITE_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_white.png"))));
		builder.put(SpectrumSkullType.SHULKER_YELLOW, List.of(new Tuple<>(new ShulkerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SHULKER_YELLOW_HEAD)), ResourceLocation.parse("textures/entity/shulker/shulker_yellow.png"))));
		builder.put(SpectrumSkullType.SILVERFISH, List.of(new Tuple<>(new SilverfishHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SILVERFISH_HEAD)), ResourceLocation.parse("textures/entity/silverfish.png"))));
		builder.put(SpectrumSkullType.SKELETON_HORSE, List.of(new Tuple<>(new HorseHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SKELETON_HORSE_HEAD)), ResourceLocation.parse("textures/entity/horse/horse_skeleton.png"))));
		builder.put(SpectrumSkullType.SLIME, List.of(new Tuple<>(new SlimeHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SLIME_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.SLIME_HEAD_TRANSLUCENT)), ResourceLocation.parse("textures/entity/slime/slime.png"))));
		builder.put(SpectrumSkullType.SNIFFER, List.of(new Tuple<>(new SnifferHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SNIFFER_HEAD)), ResourceLocation.parse("textures/entity/sniffer/sniffer.png"))));
		builder.put(SpectrumSkullType.SNOW_GOLEM, List.of(new Tuple<>(new ZombieHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SNOW_GOLEM_HEAD)), ResourceLocation.parse("textures/entity/snow_golem.png"))));
		builder.put(SpectrumSkullType.SPIDER, List.of(new Tuple<>(new SpiderHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SPIDER_HEAD)), ResourceLocation.parse("textures/entity/spider/spider.png"))));
		builder.put(SpectrumSkullType.SQUID, List.of(new Tuple<>(new SquidHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.SQUID_HEAD)), ResourceLocation.parse("textures/entity/squid/squid.png"))));
		builder.put(SpectrumSkullType.STRAY, List.of(
				new Tuple<>(new StrayHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.STRAY_HEAD)), ResourceLocation.parse("textures/entity/skeleton/stray.png")),
				new Tuple<>(new StrayHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.STRAY_HEAD_OVERLAY)), ResourceLocation.parse("textures/entity/skeleton/stray_overlay.png"))
		));
		builder.put(SpectrumSkullType.STRIDER, List.of(new Tuple<>(new StriderHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.STRIDER_HEAD)), ResourceLocation.parse("textures/entity/strider/strider.png"))));
		builder.put(SpectrumSkullType.TADPOLE, List.of(new Tuple<>(new TadpoleHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.TADPOLE_HEAD)), ResourceLocation.parse("textures/entity/tadpole/tadpole.png"))));
		builder.put(SpectrumSkullType.TROPICAL_FISH, List.of(new Tuple<>(new TropicalFishHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.TROPICAL_FISH_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.TROPICAL_FISH_HEAD_PATTERN)), ResourceLocation.parse("textures/entity/fish/tropical_a.png")))); // oof
		builder.put(SpectrumSkullType.TURTLE, List.of(new Tuple<>(new TurtleHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.TURTLE_HEAD)), ResourceLocation.parse("textures/entity/turtle/big_sea_turtle.png"))));
		builder.put(SpectrumSkullType.VEX, List.of(new Tuple<>(new VexHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.VEX_HEAD)), ResourceLocation.parse("textures/entity/illager/vex.png"))));
		builder.put(SpectrumSkullType.VILLAGER, List.of(new Tuple<>(new VillagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.VILLAGER_HEAD)), ResourceLocation.parse("textures/entity/villager/villager.png"))));
		builder.put(SpectrumSkullType.VINDICATOR, List.of(new Tuple<>(new IllagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.VINDICATOR_HEAD)), ResourceLocation.parse("textures/entity/illager/vindicator.png"))));
		builder.put(SpectrumSkullType.WANDERING_TRADER, List.of(new Tuple<>(new VillagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WANDERING_TRADER_HEAD)), ResourceLocation.parse("textures/entity/wandering_trader.png"))));
		builder.put(SpectrumSkullType.WARDEN, List.of(
				new Tuple<>(new WardenHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WARDEN_HEAD)), ResourceLocation.parse("textures/entity/warden/warden.png")),
				new Tuple<>(new WardenHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WARDEN_HEAD_BIOLUMINESCENT)), ResourceLocation.parse("textures/entity/warden/warden_bioluminescent_layer.png")),
				new Tuple<>(new WardenHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WARDEN_HEAD_PULSATING_SPOTS_1)), ResourceLocation.parse("textures/entity/warden/warden_pulsating_spots_1.png")),
				new Tuple<>(new WardenHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WARDEN_HEAD_PULSATING_SPOTS_2)), ResourceLocation.parse("textures/entity/warden/warden_pulsating_spots_2.png"))
		));
		builder.put(SpectrumSkullType.WITCH, List.of(new Tuple<>(new WitchHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WITCH_HEAD)), ResourceLocation.parse("textures/entity/witch.png"))));
		builder.put(SpectrumSkullType.WITHER, List.of(new Tuple<>(new ZombieHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WITHER_HEAD)), ResourceLocation.parse("textures/entity/wither/wither.png"))));
		builder.put(SpectrumSkullType.WOLF, List.of(new Tuple<>(new WolfHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.WOLF_HEAD)), ResourceLocation.parse("textures/entity/wolf/wolf.png"))));
		builder.put(SpectrumSkullType.ZOGLIN, List.of(new Tuple<>(new HoglinHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ZOGLIN_HEAD)), ResourceLocation.parse("textures/entity/hoglin/zoglin.png"))));
		builder.put(SpectrumSkullType.ZOMBIE_HORSE, List.of(new Tuple<>(new HorseHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ZOMBIE_HORSE_HEAD)), ResourceLocation.parse("textures/entity/horse/horse_zombie.png"))));
		builder.put(SpectrumSkullType.ZOMBIE_VILLAGER, List.of(new Tuple<>(new VillagerHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ZOMBIE_VILLAGER_HEAD)), ResourceLocation.parse("textures/entity/zombie_villager/zombie_villager.png"))));
		builder.put(SpectrumSkullType.ZOMBIFIED_PIGLIN, List.of(new Tuple<>(new PiglinHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ZOMBIFIED_PIGLIN_HEAD)), ResourceLocation.parse("textures/entity/piglin/zombified_piglin.png"))));
		builder.put(SpectrumSkullType.PIGLIN_BRUTE, List.of(new Tuple<>(new PiglinHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PIGLIN_BRUTE_HEAD)), ResourceLocation.parse("textures/entity/piglin/piglin_brute.png"))));
		
		builder.put(SpectrumSkullType.ARMADILLO, List.of(new Tuple<>(new ArmadilloHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ARMADILLO_HEAD)), ResourceLocation.parse("textures/entity/armadillo.png"))));
		builder.put(SpectrumSkullType.BREEZE, List.of(new Tuple<>(new BreezeHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.BREEZE_HEAD)), ResourceLocation.parse("textures/entity/breeze/breeze.png"))));
		builder.put(SpectrumSkullType.BOGGED, List.of(
				new Tuple<>(new BoggedHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.BOGGED_HEAD)), ResourceLocation.parse("textures/entity/skeleton/bogged.png")),
				new Tuple<>(new BoggedHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.BOGGED_HEAD_OVERLAY)), ResourceLocation.parse("textures/entity/skeleton/bogged_overlay.png")
				)));

        // Spectrum
		builder.put(SpectrumSkullType.EGG_LAYING_WOOLY_PIG, List.of(new Tuple<>(new EggLayingWoolyPigHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.EGG_LAYING_WOOLY_PIG_HEAD)), EggLayingWoolyPigEntityRenderer.TEXTURE)));
		builder.put(SpectrumSkullType.ERASER, List.of(new Tuple<>(new SpiderHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.ERASER_HEAD)), SpectrumCommon.locate("textures/entity/eraser/eraser_combined.png"))));
		builder.put(SpectrumSkullType.KINDLING, List.of(new Tuple<>(new KindlingHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.KINDLING_HEAD)), SpectrumCommon.locate("textures/entity/kindling/kindling.png"))));
		builder.put(SpectrumSkullType.LIZARD_BLACK, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BLACK.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_BLUE, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BLUE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_BROWN, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BROWN.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_CYAN, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.CYAN.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_GRAY, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.GRAY.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_GREEN, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.GREEN.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_LIGHT_BLUE, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIGHT_BLUE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_LIGHT_GRAY, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIGHT_GRAY.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_LIME, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIME.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_MAGENTA, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.MAGENTA.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_ORANGE, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.ORANGE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_PINK, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.PINK.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_PURPLE, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.PURPLE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_RED, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.RED.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_WHITE, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.WHITE.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.LIZARD_YELLOW, List.of(new Tuple<>(new LizardHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD), modelLoader.bakeLayer(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.YELLOW.getColorInt()), LizardHeadModel.HEAD_TEXTURE)));
		builder.put(SpectrumSkullType.MONSTROSITY, List.of(new Tuple<>(new MonstrosityHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.MONSTROSITY_HEAD)), MonstrosityEntityRenderer.TEXTURE)));
		builder.put(SpectrumSkullType.PRESERVATION_TURRET, List.of(new Tuple<>(new PreservationTurretHeadModel(modelLoader.bakeLayer(SpectrumModelLayers.PRESERVATION_TURRET_HEAD)), PreservationTurretEntityRenderer.TEXTURE)));

        return builder.build();
    }

    @Override
	public void render(SpectrumSkullBlockEntity spectrumSkullBlockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int j) {
		BlockState blockState = spectrumSkullBlockEntity.getBlockState();
        Direction direction = null;
        float yaw = 22.5F;
        if (blockState.getBlock() instanceof WallSkullBlock) {
			direction = blockState.getValue(WallSkullBlock.FACING);
			yaw *= (2 + direction.get2DDataValue()) * 4;
        } else {
			yaw *= blockState.getValue(SkullBlock.ROTATION);
        }
		SpectrumSkullType skullType = SpectrumSkullBlock.getSkullType(spectrumSkullBlockEntity.getBlockState().getBlock());
		
		renderModels(tickDelta, poseStack, vertexConsumerProvider, light, skullType, direction, yaw);
		
	}
	
	public static void renderModels(float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, SpectrumSkullType skullType, Direction direction, float yaw) {
		List<Tuple<SpectrumSkullModel, ResourceLocation>> model = MODELS.get(skullType);
		for (Tuple<SpectrumSkullModel, ResourceLocation> entry : model) {
			RenderType renderLayer = RenderType.entityCutoutNoCullZOffset(entry.getB());
			renderSkull(direction, yaw, tickDelta, poseStack, vertexConsumerProvider, light, entry.getA(), renderLayer);
		}
	}
	
	private static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, SpectrumSkullModel model, RenderType renderLayer) {
		matrices.pushPose();
        if (direction == null) {
            matrices.translate(0.5D, 0.0D, 0.5D);
        } else {
			matrices.translate((0.5F - (float) direction.getStepX() * 0.25F), 0.25D, (0.5F - (float) direction.getStepZ() * 0.25F));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		model.setupAnim(animationProgress, yaw, 0.0F);
		model.render(matrices, vertexConsumer, vertexConsumers, light, OverlayTexture.NO_OVERLAY, -1);
		matrices.popPose();
    }

}
