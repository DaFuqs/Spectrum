package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlock;
import de.dafuqs.spectrum.config.SpectrumConfig;
import de.dafuqs.spectrum.dimension.DeeperDownDimension;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.items.magic_items.BottomlessBundleItem;
import de.dafuqs.spectrum.items.magic_items.ExchangeStaffItem;
import de.dafuqs.spectrum.items.magic_items.RadianceStaffItem;
import de.dafuqs.spectrum.loot.EnchantmentDrops;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import de.dafuqs.spectrum.networking.SpectrumC2SPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import de.dafuqs.spectrum.sound.SpectrumBlockSoundGroups;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import de.dafuqs.spectrum.worldgen.SpectrumFeatures;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class SpectrumCommon implements ModInitializer {

	public static final String MOD_ID = "spectrum";

	public static SpectrumConfig CONFIG;
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static MinecraftServer minecraftServer;
	/**
	 * Caches the luminance states from fluids as int
	 * for blocks that react to the light level of fluids
	 * like the fusion shrine lighting up with lava or liquid crystal
	 */
	public static HashMap<Fluid, Integer> fluidLuminance = new HashMap<>();
	/**
	 * Like waterlogged, but for liquid crystal!
	 */
	public static final BooleanProperty LIQUID_CRYSTAL_LOGGED = BooleanProperty.of("liquidcrystallogged");

	public static void log(Level logLevel, String message) {
		LOGGER.log(logLevel, "[Spectrum] " + message);
	}

	@Override
	public void onInitialize() {
		log(Level.INFO, "Starting Common Startup");

		//Set up config
		log(Level.INFO, "Loading config file...");
		AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
		log(Level.INFO, "Finished loading config file.");

		// Register ALL the stuff
		log(Level.INFO, "Registering Advancement Criteria...");
		SpectrumAdvancementCriteria.register();
		log(Level.INFO, "Registering Particle Types...");
		SpectrumParticleTypes.register();
		log(Level.INFO, "Registering Sound Events...");
		SpectrumSoundEvents.register();
		log(Level.INFO, "Registering BlockSound Groups...");
		SpectrumBlockSoundGroups.register();
		log(Level.INFO, "Registering Fluid Tags...");
		SpectrumFluidTags.register();
		log(Level.INFO, "Fetching Block Tags...");
		SpectrumBlockTags.getReferences();
		log(Level.INFO, "Registering Fluids...");
		SpectrumFluids.register();
		log(Level.INFO, "Registering Blocks...");
		SpectrumBlocks.register();
		log(Level.INFO, "Registering Items...");
		SpectrumItems.register();
		
		// Tags
		log(Level.INFO, "Fetching Item Tags...");
		SpectrumItemTags.getReferences();
		log(Level.INFO, "Registering Block Entities...");
		SpectrumBlockEntityRegistry.register();
		
		// Worldgen
		log(Level.INFO, "Registering Worldgen Features...");
		SpectrumFeatures.register();
		log(Level.INFO, "Registering Configured and Placed Features...");
		SpectrumConfiguredFeatures.register();

		// Dimension
		log(Level.INFO, "Registering Dimension...");
		DeeperDownDimension.setup();

		// Recipes
		log(Level.INFO, "Registering Recipe Types...");
		SpectrumRecipeTypes.registerSerializer();
		log(Level.INFO, "Registering Loot Conditions...");
		SpectrumLootConditionTypes.register();

		// GUI
		log(Level.INFO, "Registering Containers...");
		SpectrumContainers.register();
		log(Level.INFO, "Registering Screen Handler Types...");
		SpectrumScreenHandlerTypes.register();

		// Default enchantments for some items
		log(Level.INFO, "Registering Default Item Stack Damage Immunities...");
		SpectrumItemStackDamageImmunities.registerDefaultItemStackImmunities();
		log(Level.INFO, "Registering Enchantment Drops...");
		EnchantmentDrops.setup();

		log(Level.INFO, "Registering Items to Fuel Registry...");
		SpectrumItems.registerFuelRegistry();
		log(Level.INFO, "Registering Enchantments...");
		SpectrumEnchantments.register();
		log(Level.INFO, "Registering Default Enchantments...");
		SpectrumDefaultEnchantments.registerDefaultEnchantments();
		log(Level.INFO, "Registering Entity Types...");
		SpectrumEntityTypes.register();
		log(Level.INFO, "Registering Commands...");
		SpectrumCommands.register();

		log(Level.INFO, "Registering Client To ServerPackage Receivers...");
		SpectrumC2SPackets.registerC2SReceivers();

		log(Level.INFO, "Registering Block Cloaker...");
		BlockCloakManager.setupCloaks();
		log(Level.INFO, "Registering MultiBlocks...");
		SpectrumMultiblocks.register();
		log(Level.INFO, "Registering Flammable Blocks...");
		SpectrumFlammableBlocks.register();
		log(Level.INFO, "Registering Compostable Blocks...");
		SpectrumComposting.register();
		log(Level.INFO, "Registering Game Events...");
		SpectrumGameEvents.register();

		log(Level.INFO, "Initializing Item Groups...");
		SpectrumItemGroups.ITEM_GROUP_GENERAL.initialize();
		SpectrumItemGroups.ITEM_GROUP_BLOCKS.initialize();

		log(Level.INFO, "Registering Block / Item Color Registries...");
		ColorRegistry.registerColorRegistries();
		
		log(Level.INFO, "Registering Dispenser Behaviors..");
		DispenserBlock.registerBehavior(SpectrumItems.BOTTOMLESS_BUNDLE, new BottomlessBundleItem.BottomlessBundlePlacementDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.COLORFUL_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.FIERY_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.GEMSTONE_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.PRISTINE_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if(!world.isClient && !player.isSpectator()) {
				if (player.getMainHandStack().isOf(SpectrumItems.EXCHANGE_STAFF)) {
					Optional<Block> blockTarget = ExchangeStaffItem.getBlockTarget(player.getMainHandStack());
					if (blockTarget.isPresent()) {
						ExchangeStaffItem.exchange(world, pos, player, blockTarget.get(), player.getMainHandStack(), true);
					}
					return ActionResult.CONSUME;
				} else if (player.getMainHandStack().isOf(SpectrumItems.LIGHT_STAFF)) {
					if(!world.getBlockState(pos).isOf(SpectrumBlocks.WAND_LIGHT_BLOCK)) { // those get destroyed instead
						BlockPos targetPos = pos.offset(direction);
						if (RadianceStaffItem.placeLight(world, targetPos, player, player.getMainHandStack())) {
							RadianceStaffItem.playSoundAndParticles(world, targetPos, player, world.random.nextInt(5), world.random.nextInt(5));
						} else {
							RadianceStaffItem.playDenySound(world, player);
						}
						return ActionResult.CONSUME;
					}
				}
			}
			return ActionResult.PASS;
		});

		ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
			SpectrumCommon.minecraftServer = minecraftServer;

			for (Iterator<Block> it = Registry.BLOCK.stream().iterator(); it.hasNext(); ) {
				Block block = it.next();
				if(block instanceof FluidBlock fluidBlock) {
					fluidLuminance.put(fluidBlock.getFluidState(fluidBlock.getDefaultState()).getFluid(), fluidBlock.getDefaultState().getLuminance());
				}
			}
		});
		
		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			// TODO: Whispy Circlet
		});

		log(Level.INFO, "Common startup completed!");
	}

}
