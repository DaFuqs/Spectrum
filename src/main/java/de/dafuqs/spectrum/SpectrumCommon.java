package de.dafuqs.spectrum;

import com.google.common.collect.ImmutableMap;
import de.dafuqs.spectrum.blocks.chests.CompactingChestBlockEntity;
import de.dafuqs.spectrum.blocks.mob_blocks.FirestarterMobBlock;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlock;
import de.dafuqs.spectrum.config.SpectrumConfig;
import de.dafuqs.spectrum.deeper_down.DDDimension;
import de.dafuqs.spectrum.enchantments.ResonanceEnchantment;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.items.magic_items.BottomlessBundleItem;
import de.dafuqs.spectrum.items.magic_items.ExchangeStaffItem;
import de.dafuqs.spectrum.items.magic_items.RadianceStaffItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import de.dafuqs.spectrum.loot.SpectrumLootConditionTypes;
import de.dafuqs.spectrum.loot.SpectrumLootPoolModifiers;
import de.dafuqs.spectrum.mixin.accessors.RecipeManagerAccessor;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketReceiver;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipeSerializer;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import de.dafuqs.spectrum.spells.InkSpellEffects;
import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import de.dafuqs.spectrum.worldgen.SpectrumFeatures;
import de.dafuqs.spectrum.worldgen.structure_features.SpectrumStructureFeatures;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SpectrumCommon implements ModInitializer {
	
	public static final String MOD_ID = "spectrum";
	/**
	 * Like waterlogged, but for liquid crystal!
	 */
	public static final BooleanProperty LIQUID_CRYSTAL_LOGGED = BooleanProperty.of("liquidcrystallogged");
	private static final Logger LOGGER = LoggerFactory.getLogger("Spectrum");
	public static SpectrumConfig CONFIG;
	public static RegistryKey<World> DEEPER_DOWN = RegistryKey.of(Registry.WORLD_KEY, new Identifier(MOD_ID, "deeper_down"));
	public static MinecraftServer minecraftServer;
	private static boolean serverLoadEventFired = false;
	/**
	 * Caches the luminance states from fluids as int
	 * for blocks that react to the light level of fluids
	 * like the fusion shrine lighting up with lava or liquid crystal
	 */
	public static HashMap<Fluid, Integer> fluidLuminance = new HashMap<>();
	
	public static void logInfo(String message) {
		LOGGER.info("[Spectrum] " + message);
	}
	
	public static void logWarning(String message) {
		LOGGER.warn("[Spectrum] " + message);
	}
	
	public static void logError(String message) {
		LOGGER.error("[Spectrum] " + message);
	}

	public static Identifier locate(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	@Override
	public void onInitialize() {
		logInfo("Starting Common Startup");
		
		//Set up config
		logInfo("Loading config file...");
		AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
		logInfo("Finished loading config file.");
		
		// Register internals
		InkColors.register();
		
		logInfo("Registering Banner Patterns...");
		SpectrumBannerPatterns.register();
		
		logInfo("Registering Block / Item Color Registries...");
		ColorRegistry.registerColorRegistries();
		
		// Register ALL the stuff
		logInfo("Registering Advancement Criteria...");
		SpectrumAdvancementCriteria.register();
		logInfo("Registering Particle Types...");
		SpectrumParticleTypes.register();
		logInfo("Registering Sound Events...");
		SpectrumSoundEvents.register();
		logInfo("Registering Music...");
		SpectrumMusicType.register();
		logInfo("Registering BlockSound Groups...");
		SpectrumBlockSoundGroups.register();
		logInfo("Registering Fluids...");
		SpectrumFluids.register();
		logInfo("Registering Blocks...");
		SpectrumBlocks.register();
		logInfo("Registering Items...");
		SpectrumPotions.register();
		SpectrumItems.register();
		//logInfo("Registering SAR Components...");
		//SpectrumComponents.register();
		logInfo("Registering Block Entities...");
		SpectrumBlockEntities.register();
		
		// Worldgen
		logInfo("Registering Structure Features...");
		SpectrumStructureFeatures.register();
		logInfo("Registering Worldgen Features...");
		SpectrumFeatures.register();
		logInfo("Registering Configured and Placed Features...");
		SpectrumConfiguredFeatures.register();
		
		// Dimension
		logInfo("Registering Dimension...");
		DDDimension.register();
		
		// Recipes
		logInfo("Registering Recipe Types...");
		SpectrumRecipeTypes.registerSerializer();
		logInfo("Registering Loot Conditions...");
		SpectrumLootConditionTypes.register();
		
		// GUI
		logInfo("Registering Containers...");
		SpectrumContainers.register();
		logInfo("Registering Screen Handler Types...");
		SpectrumScreenHandlerTypes.register();
		
		// Status Effects
		logInfo("Registering Status Effects...");
		SpectrumStatusEffects.register();
		
		// Default enchantments for some items
		logInfo("Registering Default Item Stack Damage Immunities...");
		SpectrumItemStackDamageImmunities.registerDefaultItemStackImmunities();
		logInfo("Registering Enchantment Drops...");
		SpectrumLootPoolModifiers.setup();
		
		logInfo("Registering Items to Fuel Registry...");
		SpectrumItems.registerFuelRegistry();
		logInfo("Registering Enchantments...");
		SpectrumEnchantments.register();
		logInfo("Registering Entity Types...");
		SpectrumEntityTypes.register();
		logInfo("Registering Commands...");
		SpectrumCommands.register();
		
		logInfo("Registering Client To ServerPackage Receivers...");
		SpectrumC2SPacketReceiver.registerC2SReceivers();
		
		logInfo("Registering Data Loaders...");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ResonanceEnchantment.ResonanceDropsDataLoader.INSTANCE);
		
		logInfo("Registering MultiBlocks...");
		SpectrumMultiblocks.register();
		logInfo("Registering Flammable Blocks...");
		SpectrumFlammableBlocks.register();
		logInfo("Registering Compostable Blocks...");
		SpectrumComposting.register();
		logInfo("Registering Game Events...");
		SpectrumGameEvents.register();
		
		logInfo("Registering Spell Effects...");
		InkSpellEffects.register();
		
		logInfo("Initializing Item Groups...");
		SpectrumItemGroups.ITEM_GROUP_GENERAL.initialize();
		SpectrumItemGroups.ITEM_GROUP_BLOCKS.initialize();
		
		logInfo("Registering Special Recipes...");
		SpectrumCustomRecipeSerializers.registerRecipeSerializers();
		
		logInfo("Registering Dispenser Behaviors...");
		DispenserBlock.registerBehavior(SpectrumItems.BOTTOMLESS_BUNDLE, new BottomlessBundleItem.BottomlessBundlePlacementDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.COLORFUL_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.FIERY_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.GEMSTONE_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		DispenserBlock.registerBehavior(SpectrumBlocks.PRISTINE_SHOOTING_STAR.asItem(), new ShootingStarBlock.ShootingStarBlockDispenserBehavior());
		
		logInfo("Registering Resource Conditions...");
		SpectrumResourceConditions.register();
		
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (!world.isClient && !player.isSpectator()) {
				ItemStack mainHandStack = player.getMainHandStack();
				if (mainHandStack.isOf(SpectrumItems.EXCHANGE_STAFF)) {
					Optional<Block> blockTarget = ExchangeStaffItem.getBlockTarget(player.getMainHandStack());
					blockTarget.ifPresent(block -> ExchangeStaffItem.exchange(world, pos, player, block, player.getMainHandStack(), true));
					return ActionResult.CONSUME;
				} else if (mainHandStack.isOf(SpectrumItems.RADIANCE_STAFF)) {
					if (!world.getBlockState(pos).isOf(SpectrumBlocks.WAND_LIGHT_BLOCK)) { // those get destroyed instead
						BlockPos targetPos = pos.offset(direction);
						if (((RadianceStaffItem) mainHandStack.getItem()).placeLight(world, targetPos, (ServerPlayerEntity) player)) {
							RadianceStaffItem.playSoundAndParticles(world, targetPos, (ServerPlayerEntity) player, world.random.nextInt(5), world.random.nextInt(5));
						} else {
							RadianceStaffItem.playDenySound(world, player);
						}
						return ActionResult.CONSUME;
					}
				}
			}
			return ActionResult.PASS;
		});
		
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			SpectrumCommon.logInfo("Fetching server instance...");
			SpectrumCommon.minecraftServer = server;
		});
		
		ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
			SpectrumCommon.minecraftServer = minecraftServer;
			if(!serverLoadEventFired) {
				SpectrumCommon.logInfo("Querying fluid luminance...");
				for (Iterator<Block> it = Registry.BLOCK.stream().iterator(); it.hasNext(); ) {
					Block block = it.next();
					if (block instanceof FluidBlock fluidBlock) {
						fluidLuminance.put(fluidBlock.getFluidState(fluidBlock.getDefaultState()).getFluid(), fluidBlock.getDefaultState().getLuminance());
					}
				}
				
				SpectrumCommon.logInfo("Injecting additional recipes...");
				FirestarterMobBlock.addBlockSmeltingRecipes(minecraftServer.getRecipeManager());
				injectEnchantmentUpgradeRecipes(minecraftServer);
				
				serverLoadEventFired = true;
			}
		});
		
		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			// If the player wears a Whispy Cirlcet and sleeps
			// it gets fully healed and all negative status effects removed
			
			// When the sleep timer reached 100 the player is fully asleep
			if (entity instanceof ServerPlayerEntity serverPlayerEntity
					&& serverPlayerEntity.getSleepTimer() == 100
					&& SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.WHISPY_CIRCLET)) {
				
				entity.setHealth(entity.getMaxHealth());
				WhispyCircletItem.removeNegativeStatusEffects(entity);
			}
		});
		
		logInfo("Registering RecipeCache reload listener");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			private final Identifier id = new Identifier(SpectrumCommon.MOD_ID, "compacting_cache_clearer");
			
			@Override
			public void reload(ResourceManager manager) {
				CompactingChestBlockEntity.clearCache();
				
				if (minecraftServer != null) {
					injectEnchantmentUpgradeRecipes(minecraftServer);
					FirestarterMobBlock.addBlockSmeltingRecipes(minecraftServer.getRecipeManager());
				}
			}
			
			@Override
			public Identifier getFabricId() {
				return id;
			}
		});
		
		logInfo("Common startup completed!");
	}
	
	// It could have been so much easier and performant, but KubeJS overrides the ENTIRE recipe manager
	// and cancels all sorts of functions at HEAD unconditionally, so Spectrum can not mixin into it
	public void injectEnchantmentUpgradeRecipes(MinecraftServer minecraftServer) {
		if (!EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.isEmpty()) {
			ImmutableMap<Identifier, Recipe<?>> collectedRecipes = EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.stream().collect(ImmutableMap.toImmutableMap(EnchantmentUpgradeRecipe::getId, enchantmentUpgradeRecipe -> enchantmentUpgradeRecipe));
			Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes = ((RecipeManagerAccessor) minecraftServer.getRecipeManager()).getRecipes();
			
			ArrayList<Recipe<?>> newList = new ArrayList<>();
			for (Map<Identifier, Recipe<?>> r : recipes.values()) {
				newList.addAll(r.values());
			}
			for (Recipe<?> recipe : collectedRecipes.values()) {
				if (!newList.contains(recipe)) {
					newList.add(recipe);
				}
			}
			
			minecraftServer.getRecipeManager().setRecipes(newList);
		}
		EnchantmentUpgradeRecipeSerializer.enchantmentUpgradeRecipesToInject.clear();
	}
	
}
