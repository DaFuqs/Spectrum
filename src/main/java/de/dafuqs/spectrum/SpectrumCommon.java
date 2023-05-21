package de.dafuqs.spectrum;

import com.google.common.collect.*;
import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.mob_blocks.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.color.*;
import de.dafuqs.spectrum.spells.*;
import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.loader.api.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.resource.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.slf4j.*;

import java.util.*;

public class SpectrumCommon implements ModInitializer {
	
	public static final String MOD_ID = "spectrum";
	
	private static final Logger LOGGER = LoggerFactory.getLogger("Spectrum");
	public static SpectrumConfig CONFIG;
	public static RegistryKey<World> DEEPER_DOWN = RegistryKey.of(Registry.WORLD_KEY, new Identifier(MOD_ID, "deeper_down"));
	
	public static MinecraftServer minecraftServer;
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
		logInfo("Registering Status Effects...");
		SpectrumStatusEffects.register();
		SpectrumStatusEffectTags.register();
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
		logInfo("Setting up server side Mod Compat...");
		SpectrumModCompat.register();
		logInfo("Registering Block Entities...");
		SpectrumBlockEntities.register();
		
		// Worldgen
		logInfo("Registering Worldgen Features...");
		SpectrumFeatures.register();
		logInfo("Registering Configured and Placed Features...");
		SpectrumConfiguredFeatures.register();
		logInfo("Registering Structure Types...");
		SpectrumStructureTypes.register();
		
		// Dimension
		logInfo("Registering Dimension...");
		DDDimension.register();
		
		// Recipes
		logInfo("Registering Recipe Types...");
		SpectrumRecipeTypes.registerSerializer();
		logInfo("Registering Loot Conditions & Functions...");
		SpectrumLootConditionTypes.register();
		SpectrumLootFunctionTypes.register();
		
		// GUI
		logInfo("Registering Containers...");
		SpectrumScreenHandlerIDs.register();
		logInfo("Registering Screen Handler Types...");
		SpectrumScreenHandlerTypes.register();
		
		
		// Default enchantments for some items
		logInfo("Registering Default Item Stack Damage Immunities...");
		SpectrumItemStackDamageImmunities.registerDefaultItemStackImmunities();
		logInfo("Registering Enchantment Drops...");
		SpectrumLootPoolModifiers.setup();
		logInfo("Registering Type Specific Predicates...");
		SpectrumTypeSpecificPredicates.register();
		
		logInfo("Registering Items to Fuel Registry...");
		SpectrumItems.registerFuelRegistry();
		logInfo("Registering Enchantments...");
		SpectrumEnchantments.register();
		
		logInfo("Registering Entities...");
		SpectrumTrackedDataHandlerRegistry.register();
		SpectrumEntityTypes.register();
		
		logInfo("Registering Commands...");
		SpectrumCommands.register();
		
		logInfo("Registering Client To ServerPackage Receivers...");
		SpectrumC2SPacketReceiver.registerC2SReceivers();
		
		logInfo("Registering Data Loaders...");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(NaturesStaffConversionDataLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ResonanceDropsDataLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(EntityFishingDataLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(CrystalApothecarySimulationsDataLoader.INSTANCE);
		
		logInfo("Adding to Fabric's Registries...");
		SpectrumFlammableBlocks.register();
		SpectrumStrippableBlocks.register();
		SpectrumCompostableBlocks.register();
		
		logInfo("Registering Game Events...");
		SpectrumGameEvents.register();
		SpectrumPositionSources.register();
		
		logInfo("Registering Spell Effects...");
		InkSpellEffects.register();

		logInfo("Initializing Item Groups...");
		SpectrumItemGroups.ITEM_GROUP_GENERAL.initialize();
		SpectrumItemGroups.ITEM_GROUP_BLOCKS.initialize();

		logInfo("Registering Special Recipes...");
		SpectrumCustomRecipeSerializers.registerRecipeSerializers();

		logInfo("Registering Dispenser Behaviors...");
		SpectrumDispenserBehaviors.register();

		logInfo("Registering Resource Conditions...");
		SpectrumResourceConditions.register();

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (!world.isClient && !player.isSpectator()) {
				ItemStack mainHandStack = player.getMainHandStack();
				if (mainHandStack.isOf(SpectrumItems.EXCHANGING_STAFF)) {
					Optional<Block> blockTarget = ExchangeStaffItem.getBlockTarget(player.getMainHandStack());
					blockTarget.ifPresent(block -> ExchangeStaffItem.exchange(world, pos, player, block, player.getMainHandStack(), true));
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
		
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
			}
		});

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			SpectrumCommon.logInfo("Fetching server instance...");
			SpectrumCommon.minecraftServer = server;

			logInfo("Registering MultiBlocks...");
			SpectrumMultiblocks.register();
		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			Pastel.clearServerInstance();
			SpectrumCommon.minecraftServer = server;
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			Pastel.getServerInstance().tick();
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (world.getTime() % 100 == 0) {
				long timeOfDay = world.getTimeOfDay() % 24000;
				if (timeOfDay > 13000 && timeOfDay < 22000) { // 90 chances in a night
					if (SpectrumCommon.CONFIG.ShootingStarWorlds.contains(world.getRegistryKey().getValue().toString())) {
						ShootingStarEntity.doShootingStarSpawnsForPlayers(world);
					}
				}
				
				if (world.getRegistryKey().equals(DDDimension.DIMENSION_KEY)) {
					MonstrosityEntity.checkForSpawn(world);
				}
			}
		});
		
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			SpectrumCommon.logInfo("Querying fluid luminance...");
			for (Iterator<Block> it = Registry.BLOCK.stream().iterator(); it.hasNext(); ) {
				Block block = it.next();
				if (block instanceof FluidBlock fluidBlock) {
					fluidLuminance.put(fluidBlock.getFluidState(fluidBlock.getDefaultState()).getFluid(), fluidBlock.getDefaultState().getLuminance());
				}
			}
			
			SpectrumCommon.logInfo("Injecting additional recipes...");
			FirestarterMobBlock.addBlockSmeltingRecipes(server.getRecipeManager());
			injectEnchantmentUpgradeRecipes(server);
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

		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
			var oldInexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, previousStack);
			var newInexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, currentStack);

			var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumMiscTags.INEXORABLE_ARMOR_EFFECTIVE : SpectrumMiscTags.INEXORABLE_HANDHELD_EFFECTIVE;

			if (oldInexorable > 0 && newInexorable <= 0) {
				livingEntity.getStatusEffects()
						.stream()
						.filter(instance -> {
							var statusEffect = instance.getEffectType();
							var attributes = statusEffect.getAttributeModifiers().keySet();
							return attributes.stream()
									.anyMatch(attribute -> {
										var attributeRegistryOptional = Registry.ATTRIBUTE.getEntryList(effectType);

										return attributeRegistryOptional.map(registryEntries -> registryEntries
												.stream()
												.map(RegistryEntry::value)
												.anyMatch(entityAttribute -> {

													if (!statusEffect.getAttributeModifiers().containsKey(entityAttribute))
														return false;

													var value = statusEffect.getAttributeModifiers().get(entityAttribute).getValue();
													return value < 0;

												})).orElse(false);

									});
						})
						.forEach(instance -> instance.getEffectType().onApplied(livingEntity, livingEntity.getAttributes(), instance.getAmplifier()));
			}

		});

        CrossbowShootingCallback.register((world, shooter, hand, crossbow, projectile, projectileEntity) -> {
            if (crossbow.getItem() instanceof GlassCrestCrossbowItem && GlassCrestCrossbowItem.isOvercharged(crossbow)) {
				if (!world.isClient) { // only fired on the client, but making sure mods aren't doing anything weird
					Vec3d particleVelocity = projectileEntity.getVelocity().multiply(0.05);

					if (GlassCrestCrossbowItem.getOvercharge(crossbow) > 0.99F) {
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
								projectileEntity.getPos(), ParticleTypes.SCRAPE, 5,
								Vec3d.ZERO, particleVelocity);
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
								projectileEntity.getPos(), ParticleTypes.WAX_OFF, 5,
								Vec3d.ZERO, particleVelocity);
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
								projectileEntity.getPos(), ParticleTypes.WAX_ON, 5,
								Vec3d.ZERO, particleVelocity);
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
								projectileEntity.getPos(), ParticleTypes.GLOW, 5,
								Vec3d.ZERO, particleVelocity);

						if (shooter instanceof ServerPlayerEntity serverPlayerEntity) {
							Support.grantAdvancementCriterion(serverPlayerEntity,
									SpectrumCommon.locate("lategame/shoot_fully_overcharged_crossbow"),
									"shot_fully_overcharged_crossbow");
						}
						if (projectileEntity instanceof PersistentProjectileEntity persistentProjectileEntity) {
							persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 1.5);
						}
					}

					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							projectileEntity.getPos(), ParticleTypes.FIREWORK, 10,
							Vec3d.ZERO, particleVelocity);

					GlassCrestCrossbowItem.unOvercharge(crossbow);
				}
			}
        });

        logInfo("Registering RecipeCache reload listener");
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            private final Identifier id = SpectrumCommon.locate("compacting_cache_clearer");

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

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.storage, SpectrumBlockEntities.BOTTOMLESS_BUNDLE);
		FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, SpectrumBlockEntities.FUSION_SHRINE);
		FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, SpectrumBlockEntities.TITRATION_BARREL);

		// Builtin Resource Packs
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(SpectrumCommon.MOD_ID);
		if (modContainer.isPresent()) {
			ResourceManagerHelper.registerBuiltinResourcePack(locate("jinc"), modContainer.get(), "Alternate Spectrum textures", ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(locate("spectrum_programmer_art"), modContainer.get(), "Spectrum's Programmer Art", ResourcePackActivationType.NORMAL);
		}
		;

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
	}
	
}
