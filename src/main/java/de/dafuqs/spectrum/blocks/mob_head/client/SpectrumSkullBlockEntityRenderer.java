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
    
    private static Map<SkullBlock.SkullType, Pair<SpectrumSkullModel, Identifier>> MODELS = new HashMap<>();

    public SpectrumSkullBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
        MODELS = getModels(renderContext.getLayerRenderDispatcher());
    }
    
    public static Map<SkullBlock.SkullType, Pair<SpectrumSkullModel, Identifier>> getModels(EntityModelLoader modelLoader) {
        ImmutableMap.Builder<SkullBlock.SkullType, Pair<SpectrumSkullModel, Identifier>> builder = ImmutableBiMap.builder();
        
        // Vanilla
        builder.put(SpectrumSkullType.ALLAY, new Pair<>(new AllayHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ALLAY_HEAD)), new Identifier("textures/entity/allay/allay.png")));
        builder.put(SpectrumSkullType.AXOLOTL_BLUE, new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_BLUE_HEAD)), new Identifier("textures/entity/axolotl/axolotl_blue.png")));
        builder.put(SpectrumSkullType.AXOLOTL_CYAN, new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_CYAN_HEAD)), new Identifier("textures/entity/axolotl/axolotl_cyan.png")));
        builder.put(SpectrumSkullType.AXOLOTL_GOLD, new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_GOLD_HEAD)), new Identifier("textures/entity/axolotl/axolotl_gold.png")));
        builder.put(SpectrumSkullType.AXOLOTL_LEUCISTIC, new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_LEUCISTIC_HEAD)), new Identifier("textures/entity/axolotl/axolotl_lucy.png")));
        builder.put(SpectrumSkullType.AXOLOTL_WILD, new Pair<>(new AxolotlHeadModel(modelLoader.getModelPart(SpectrumModelLayers.AXOLOTL_WILD_HEAD)), new Identifier("textures/entity/axolotl/axolotl_wild.png")));
        builder.put(SpectrumSkullType.BAT, new Pair<>(new BatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BAT_HEAD)), new Identifier("textures/entity/bat.png")));
        builder.put(SpectrumSkullType.BEE, new Pair<>(new BeeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BEE_HEAD)), new Identifier("textures/entity/bee/bee.png")));
        builder.put(SpectrumSkullType.BLAZE, new Pair<>(new BlazeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.BLAZE_HEAD)), new Identifier("textures/entity/blaze.png")));
        builder.put(SpectrumSkullType.CAMEL, new Pair<>(new CamelHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CAMEL_HEAD)), new Identifier("textures/entity/camel/camel.png")));
        builder.put(SpectrumSkullType.CAT, new Pair<>(new CatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CAT_HEAD)), new Identifier("textures/entity/cat/tabby.png")));
        builder.put(SpectrumSkullType.CAVE_SPIDER, new Pair<>(new SpiderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CAVE_SPIDER_HEAD)), new Identifier("textures/entity/spider/cave_spider.png")));
        builder.put(SpectrumSkullType.CHICKEN, new Pair<>(new ChickenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.CHICKEN_HEAD)), new Identifier("textures/entity/chicken.png")));
        builder.put(SpectrumSkullType.COW, new Pair<>(new CowHeadModel(modelLoader.getModelPart(SpectrumModelLayers.COW_HEAD)), new Identifier("textures/entity/cow/cow.png")));
        builder.put(SpectrumSkullType.DOLPHIN, new Pair<>(new DolphinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DOLPHIN_HEAD)), new Identifier("textures/entity/dolphin.png")));
        builder.put(SpectrumSkullType.DONKEY, new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DONKEY_HEAD)), new Identifier("textures/entity/horse/donkey.png")));
        builder.put(SpectrumSkullType.DROWNED, new Pair<>(new DrownedHeadModel(modelLoader.getModelPart(SpectrumModelLayers.DROWNED_HEAD)), new Identifier("textures/entity/zombie/drowned.png")));
        builder.put(SpectrumSkullType.ELDER_GUARDIAN, new Pair<>(new GuardianHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ELDER_GUARDIAN_HEAD)), new Identifier("textures/entity/guardian_elder.png")));
        builder.put(SpectrumSkullType.ENDERMAN, new Pair<>(new EndermanHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ENDERMAN_HEAD), modelLoader.getModelPart(SpectrumModelLayers.ENDERMAN_HEAD)), new Identifier("textures/entity/enderman/enderman.png")));
        builder.put(SpectrumSkullType.ENDERMITE, new Pair<>(new EndermiteHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ENDERMITE_HEAD)), new Identifier("textures/entity/endermite.png")));
        builder.put(SpectrumSkullType.EVOKER, new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.EVOKER_HEAD)), new Identifier("textures/entity/illager/evoker.png")));
        builder.put(SpectrumSkullType.FOX, new Pair<>(new FoxHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FOX_HEAD)), new Identifier("textures/entity/fox/fox.png")));
        builder.put(SpectrumSkullType.FOX_ARCTIC, new Pair<>(new FoxHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FOX_ARCTIC_HEAD)), new Identifier("textures/entity/fox/snow_fox.png")));
        builder.put(SpectrumSkullType.FROG_COLD, new Pair<>(new FrogHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FROG_COLD_HEAD)), new Identifier("textures/entity/frog/cold_frog.png")));
        builder.put(SpectrumSkullType.FROG_TEMPERATE, new Pair<>(new FrogHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FROG_TEMPERATE_HEAD)), new Identifier("textures/entity/frog/temperate_frog.png")));
        builder.put(SpectrumSkullType.FROG_WARM, new Pair<>(new FrogHeadModel(modelLoader.getModelPart(SpectrumModelLayers.FROG_WARM_HEAD)), new Identifier("textures/entity/frog/warm_frog.png")));
        builder.put(SpectrumSkullType.GHAST, new Pair<>(new GhastHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GHAST_HEAD)), new Identifier("textures/entity/ghast/ghast.png")));
        builder.put(SpectrumSkullType.GLOW_SQUID, new Pair<>(new SquidHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GLOW_SQUID_HEAD)), new Identifier("textures/entity/squid/glow_squid.png")));
        builder.put(SpectrumSkullType.GOAT, new Pair<>(new GoatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GOAT_HEAD)), new Identifier("textures/entity/goat/goat.png")));
        builder.put(SpectrumSkullType.GUARDIAN, new Pair<>(new GuardianHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GUARDIAN_HEAD)), new Identifier("textures/entity/guardian.png")));
        builder.put(SpectrumSkullType.HOGLIN, new Pair<>(new HoglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.HOGLIN_HEAD)), new Identifier("textures/entity/hoglin/hoglin.png")));
        builder.put(SpectrumSkullType.HORSE, new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.HORSE_HEAD)), new Identifier("textures/entity/horse/horse_chestnut.png")));
        builder.put(SpectrumSkullType.HUSK, new Pair<>(new ZombieHeadModel(modelLoader.getModelPart(SpectrumModelLayers.HUSK_HEAD)), new Identifier("textures/entity/zombie/husk.png")));
        builder.put(SpectrumSkullType.ILLUSIONER, new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ILLUSIONER_HEAD)), new Identifier("textures/entity/illager/illusioner.png")));
        builder.put(SpectrumSkullType.IRON_GOLEM, new Pair<>(new IronGolemHeadModel(modelLoader.getModelPart(SpectrumModelLayers.IRON_GOLEM_HEAD)), new Identifier("textures/entity/iron_golem/iron_golem.png")));
        builder.put(SpectrumSkullType.LLAMA, new Pair<>(new LlamaHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LLAMA_HEAD)), new Identifier("textures/entity/llama/gray.png")));
        builder.put(SpectrumSkullType.MAGMA_CUBE, new Pair<>(new SlimeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MAGMA_CUBE_HEAD)), new Identifier("textures/entity/slime/magmacube.png")));
        builder.put(SpectrumSkullType.MOOSHROOM_BROWN, new Pair<>(new CowHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MOOSHROOM_BROWN_HEAD)), new Identifier("textures/entity/cow/brown_mooshroom.png")));
        builder.put(SpectrumSkullType.MOOSHROOM_RED, new Pair<>(new CowHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MOOSHROOM_RED_HEAD)), new Identifier("textures/entity/cow/red_mooshroom.png")));
        builder.put(SpectrumSkullType.MULE, new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MULE_HEAD)), new Identifier("textures/entity/horse/mule.png")));
        builder.put(SpectrumSkullType.OCELOT, new Pair<>(new CatHeadModel(modelLoader.getModelPart(SpectrumModelLayers.OCELOT_HEAD)), new Identifier("textures/entity/cat/ocelot.png")));
        builder.put(SpectrumSkullType.PANDA, new Pair<>(new PandaHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PANDA_HEAD)), new Identifier("textures/entity/panda/panda.png"))); // pandas have variants
        builder.put(SpectrumSkullType.PARROT_BLUE, new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_BLUE_HEAD)), new Identifier("textures/entity/parrot/parrot_blue.png")));
        builder.put(SpectrumSkullType.PARROT_CYAN, new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_CYAN_HEAD)), new Identifier("textures/entity/parrot/parrot_yellow_blue.png"))); // cyan vs. yellow_blue
        builder.put(SpectrumSkullType.PARROT_GRAY, new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_GRAY_HEAD)), new Identifier("textures/entity/parrot/parrot_grey.png"))); // gray vs.grey
        builder.put(SpectrumSkullType.PARROT_GREEN, new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_GREEN_HEAD)), new Identifier("textures/entity/parrot/parrot_green.png")));
        builder.put(SpectrumSkullType.PARROT_RED, new Pair<>(new ParrotHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PARROT_RED_HEAD)), new Identifier("textures/entity/parrot/parrot_red_blue.png"))); // red vs. red_blue
        builder.put(SpectrumSkullType.PHANTOM, new Pair<>(new PhantomHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PHANTOM_HEAD)), new Identifier("textures/entity/phantom.png")));
        builder.put(SpectrumSkullType.PIG, new Pair<>(new PigHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PIG_HEAD)), new Identifier("textures/entity/pig/pig.png")));
        builder.put(SpectrumSkullType.PILLAGER, new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PILLAGER_HEAD)), new Identifier("textures/entity/illager/pillager.png")));
        builder.put(SpectrumSkullType.POLAR_BEAR, new Pair<>(new BearHeadModel(modelLoader.getModelPart(SpectrumModelLayers.POLAR_BEAR_HEAD)), new Identifier("textures/entity/bear/polarbear.png")));
        builder.put(SpectrumSkullType.PUFFERFISH, new Pair<>(new PufferFishHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PUFFERFISH_HEAD)), new Identifier("textures/entity/fish/pufferfish.png"))); // other fish?
        builder.put(SpectrumSkullType.RABBIT, new Pair<>(new RabbitHeadModel(modelLoader.getModelPart(SpectrumModelLayers.RABBIT_HEAD)), new Identifier("textures/entity/rabbit/brown.png"))); // rabbits have variant
        builder.put(SpectrumSkullType.RAVAGER, new Pair<>(new RavagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.RAVAGER_HEAD)), new Identifier("textures/entity/illager/ravager.png")));
        builder.put(SpectrumSkullType.SALMON, new Pair<>(new SalmonHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SALMON_HEAD)), new Identifier("textures/entity/fish/salmon.png"))); // other fish?
        builder.put(SpectrumSkullType.SHEEP, new Pair<>(new SheepHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHEEP_HEAD)), new Identifier("textures/entity/sheep/sheep.png")));
        builder.put(SpectrumSkullType.SHULKER, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_HEAD)), new Identifier("textures/entity/shulker/shulker.png")));
        builder.put(SpectrumSkullType.SHULKER_BLACK, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_BLACK_HEAD)), new Identifier("textures/entity/shulker/shulker_black.png")));
        builder.put(SpectrumSkullType.SHULKER_BLUE, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_BLUE_HEAD)), new Identifier("textures/entity/shulker/shulker_blue.png")));
        builder.put(SpectrumSkullType.SHULKER_BROWN, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_BROWN_HEAD)), new Identifier("textures/entity/shulker/shulker_brown.png")));
        builder.put(SpectrumSkullType.SHULKER_CYAN, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_CYAN_HEAD)), new Identifier("textures/entity/shulker/shulker_cyan.png")));
        builder.put(SpectrumSkullType.SHULKER_GRAY, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_GRAY_HEAD)), new Identifier("textures/entity/shulker/shulker_gray.png")));
        builder.put(SpectrumSkullType.SHULKER_GREEN, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_GREEN_HEAD)), new Identifier("textures/entity/shulker/shulker_green.png")));
        builder.put(SpectrumSkullType.SHULKER_LIGHT_BLUE, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_LIGHT_BLUE_HEAD)), new Identifier("textures/entity/shulker/shulker_light_blue.png")));
        builder.put(SpectrumSkullType.SHULKER_LIGHT_GRAY, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_LIGHT_GRAY_HEAD)), new Identifier("textures/entity/shulker/shulker_light_gray.png")));
        builder.put(SpectrumSkullType.SHULKER_LIME, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_LIME_HEAD)), new Identifier("textures/entity/shulker/shulker_lime.png")));
        builder.put(SpectrumSkullType.SHULKER_MAGENTA, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_MAGENTA_HEAD)), new Identifier("textures/entity/shulker/shulker_magenta.png")));
        builder.put(SpectrumSkullType.SHULKER_ORANGE, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_ORANGE_HEAD)), new Identifier("textures/entity/shulker/shulker_orange.png")));
        builder.put(SpectrumSkullType.SHULKER_PINK, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_PINK_HEAD)), new Identifier("textures/entity/shulker/shulker_pink.png")));
        builder.put(SpectrumSkullType.SHULKER_PURPLE, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_PURPLE_HEAD)), new Identifier("textures/entity/shulker/shulker_purple.png")));
        builder.put(SpectrumSkullType.SHULKER_RED, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_RED_HEAD)), new Identifier("textures/entity/shulker/shulker_red.png")));
        builder.put(SpectrumSkullType.SHULKER_WHITE, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_WHITE_HEAD)), new Identifier("textures/entity/shulker/shulker_white.png")));
        builder.put(SpectrumSkullType.SHULKER_YELLOW, new Pair<>(new ShulkerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SHULKER_YELLOW_HEAD)), new Identifier("textures/entity/shulker/shulker_yellow.png")));
        builder.put(SpectrumSkullType.SILVERFISH, new Pair<>(new SilverfishHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SILVERFISH_HEAD)), new Identifier("textures/entity/silverfish.png")));
        builder.put(SpectrumSkullType.SKELETON_HORSE, new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SKELETON_HORSE_HEAD)), new Identifier("textures/entity/horse/horse_skeleton.png")));
        builder.put(SpectrumSkullType.SLIME, new Pair<>(new SlimeHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SLIME_HEAD)), new Identifier("textures/entity/slime/slime.png")));
        builder.put(SpectrumSkullType.SNIFFER, new Pair<>(new SnifferHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SNIFFER_HEAD)), new Identifier("textures/entity/sniffer/sniffer.png")));
        builder.put(SpectrumSkullType.SNOW_GOLEM, new Pair<>(new ZombieHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SNOW_GOLEM_HEAD)), new Identifier("textures/entity/snow_golem.png")));
        builder.put(SpectrumSkullType.SPIDER, new Pair<>(new SpiderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SPIDER_HEAD)), new Identifier("textures/entity/spider/spider.png")));
        builder.put(SpectrumSkullType.SQUID, new Pair<>(new SquidHeadModel(modelLoader.getModelPart(SpectrumModelLayers.SQUID_HEAD)), new Identifier("textures/entity/squid/squid.png")));
        builder.put(SpectrumSkullType.STRAY, new Pair<>(new StrayHeadModel(modelLoader.getModelPart(SpectrumModelLayers.STRAY_HEAD)), new Identifier("textures/entity/skeleton/stray.png")));
        builder.put(SpectrumSkullType.STRIDER, new Pair<>(new StriderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.STRIDER_HEAD)), new Identifier("textures/entity/strider/strider.png")));
        builder.put(SpectrumSkullType.TADPOLE, new Pair<>(new TadpoleHeadModel(modelLoader.getModelPart(SpectrumModelLayers.TADPOLE_HEAD)), new Identifier("textures/entity/tadpole/tadpole.png")));
        builder.put(SpectrumSkullType.TROPICAL_FISH, new Pair<>(new TropicalFishHeadModel(modelLoader.getModelPart(SpectrumModelLayers.TROPICAL_FISH_HEAD), modelLoader.getModelPart(SpectrumModelLayers.TROPICAL_FISH_HEAD_PATTERN)), new Identifier("textures/entity/fish/tropical_a.png"))); // oof
        builder.put(SpectrumSkullType.TURTLE, new Pair<>(new TurtleHeadModel(modelLoader.getModelPart(SpectrumModelLayers.TURTLE_HEAD)), new Identifier("textures/entity/turtle/big_sea_turtle.png")));
        builder.put(SpectrumSkullType.VEX, new Pair<>(new VexHeadModel(modelLoader.getModelPart(SpectrumModelLayers.VEX_HEAD)), new Identifier("textures/entity/illager/vex.png")));
        builder.put(SpectrumSkullType.VILLAGER, new Pair<>(new VillagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.VILLAGER_HEAD)), new Identifier("textures/entity/villager/villager.png")));
        builder.put(SpectrumSkullType.VINDICATOR, new Pair<>(new IllagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.VINDICATOR_HEAD)), new Identifier("textures/entity/illager/vindicator.png")));
        builder.put(SpectrumSkullType.WANDERING_TRADER, new Pair<>(new VillagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WANDERING_TRADER_HEAD)), new Identifier("textures/entity/wandering_trader.png")));
        builder.put(SpectrumSkullType.WARDEN, new Pair<>(new WardenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WARDEN_HEAD)), new Identifier("textures/entity/warden/warden.png")));
        builder.put(SpectrumSkullType.WITCH, new Pair<>(new WitchHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WITCH_HEAD)), new Identifier("textures/entity/witch.png")));
        builder.put(SpectrumSkullType.WITHER, new Pair<>(new ZombieHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WITHER_HEAD)), new Identifier("textures/entity/wither/wither.png")));
        builder.put(SpectrumSkullType.WOLF, new Pair<>(new WolfHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WOLF_HEAD)), new Identifier("textures/entity/wolf/wolf.png")));
        builder.put(SpectrumSkullType.ZOGLIN, new Pair<>(new HoglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOGLIN_HEAD)), new Identifier("textures/entity/hoglin/zoglin.png")));
        builder.put(SpectrumSkullType.ZOMBIE_HORSE, new Pair<>(new HorseHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOMBIE_HORSE_HEAD)), new Identifier("textures/entity/horse/horse_zombie.png")));
        builder.put(SpectrumSkullType.ZOMBIE_VILLAGER, new Pair<>(new VillagerHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOMBIE_VILLAGER_HEAD)), new Identifier("textures/entity/zombie_villager/zombie_villager.png")));
        builder.put(SpectrumSkullType.ZOMBIFIED_PIGLIN, new Pair<>(new PiglinHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ZOMBIFIED_PIGLIN_HEAD)), new Identifier("textures/entity/piglin/zombified_piglin.png")));

        // Spectrum
        builder.put(SpectrumSkullType.EGG_LAYING_WOOLY_PIG, new Pair<>(new EggLayingWoolyPigHeadModel(modelLoader.getModelPart(SpectrumModelLayers.EGG_LAYING_WOOLY_PIG_HEAD)), EggLayingWoolyPigEntityRenderer.TEXTURE));
        builder.put(SpectrumSkullType.ERASER, new Pair<>(new SpiderHeadModel(modelLoader.getModelPart(SpectrumModelLayers.ERASER_HEAD)), SpectrumCommon.locate("textures/entity/eraser/eraser_combined.png")));
        builder.put(SpectrumSkullType.KINDLING, new Pair<>(new KindlingHeadModel(modelLoader.getModelPart(SpectrumModelLayers.KINDLING_HEAD)), KindlingVariant.DEFAULT.defaultTexture()));
        builder.put(SpectrumSkullType.LIZARD_BLACK, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BLACK.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_BLUE, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BLUE.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_BROWN, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.BROWN.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_CYAN, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.CYAN.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_GRAY, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.GRAY.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_GREEN, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.GREEN.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_LIGHT_BLUE, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIGHT_BLUE.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_LIGHT_GRAY, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIGHT_GRAY.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_LIME, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.LIME.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_MAGENTA, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.MAGENTA.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_ORANGE, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.ORANGE.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_PINK, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.PINK.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_PURPLE, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.PURPLE.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_RED, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.RED.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_WHITE, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.WHITE.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.LIZARD_YELLOW, new Pair<>(new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD), modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD_FRILLS), InkColors.YELLOW.getColorVec()), LizardHeadModel.HEAD_TEXTURE));
        builder.put(SpectrumSkullType.MONSTROSITY, new Pair<>(new MonstrosityHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MONSTROSITY_HEAD)), MonstrosityEntityRenderer.TEXTURE));
        builder.put(SpectrumSkullType.PRESERVATION_TURRET, new Pair<>(new PreservationTurretHeadModel(modelLoader.getModelPart(SpectrumModelLayers.PRESERVATION_TURRET_HEAD)), PreservationTurretEntityRenderer.TEXTURE));

        return builder.build();
    }
    
    public static SpectrumSkullModel getModel(SkullBlock.SkullType skullType) {
        return MODELS.get(skullType).getLeft();
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
        
        if (skullType == null) {
            skullType = SpectrumSkullType.PIG;
        }
        SpectrumSkullModel model = MODELS.get(skullType).getLeft();

        RenderLayer renderLayer = getRenderLayer(skullType);
        renderSkull(direction, yaw, tickDelta, matrixStack, vertexConsumerProvider, light, model, renderLayer);
    }
    
    public static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SpectrumSkullModel model, RenderLayer renderLayer) {
        matrices.push();
        if (direction == null) {
            matrices.translate(0.5D, 0.0D, 0.5D);
        } else {
            matrices.translate((0.5F - (float) direction.getOffsetX() * 0.25F), 0.25D, (0.5F - (float) direction.getOffsetZ() * 0.25F));
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.setHeadRotation(animationProgress, yaw, 0.0F);
        model.render(matrices, vertexConsumer, vertexConsumers, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }
    
    public static RenderLayer getRenderLayer(SpectrumSkullType type) {
        Identifier identifier = getTextureIdentifier(type);
        return RenderLayer.getEntityCutoutNoCullZOffset(identifier);
    }
    
    protected static Identifier getTextureIdentifier(SpectrumSkullType type) {
        return MODELS.get(type).getRight();
    }

}