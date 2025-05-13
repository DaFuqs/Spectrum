package de.dafuqs.spectrum.registries;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.entity.spawners.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.client.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.fabric.api.util.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.resource.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class SpectrumEventListeners {
	
	/**
	 * Caches the luminance states from fluids as int
	 * for blocks that react to the light level of fluids
	 * like the fusion shrine lighting up with lava or liquid crystal
	 */
	public static final HashMap<Fluid, Integer> fluidLuminance = new HashMap<>();
	
	public static void register() {
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (!world.isClient && !player.isSpectator()) {
				
				ItemStack mainHandStack = player.getMainHandStack();
				if (mainHandStack.getItem() instanceof ExchangeStaffItem exchangeStaffItem) {
					BlockState targetBlockState = world.getBlockState(pos);
					if (exchangeStaffItem.canInteractWith(targetBlockState, world, pos, player)) {
						Optional<Block> storedBlock = ExchangeStaffItem.getStoredBlock(player.getMainHandStack());
						
						if (storedBlock.isPresent()
								&& storedBlock.get() != targetBlockState.getBlock()
								&& storedBlock.get().asItem() != Items.AIR
								&& ExchangeStaffItem.exchange(world, pos, player, storedBlock.get(), player.getMainHandStack(), true, direction)) {
							
							return ActionResult.SUCCESS;
						}
					}
					world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
					return ActionResult.FAIL;
				}
			}
			return ActionResult.PASS;
		});
		
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			if (client) {
				SpectrumColorProviders.resetToggleableProviders();
			}
		});
		
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				ItemStack stack = player.getStackInHand(serverPlayerEntity.getActiveHand());
				if (SpectrumEnchantmentHelper.hasEnchantment(player.getWorld().getRegistryManager(), SpectrumEnchantments.INERTIA, stack)) {
					InertiaComponent inertia = stack.getOrDefault(SpectrumDataComponentTypes.INERTIA, InertiaComponent.DEFAULT);
					long inertiaAmount = state.isOf(inertia.lastMined()) ? inertia.count() + 1 : 1;
					stack.set(SpectrumDataComponentTypes.INERTIA, new InertiaComponent(state.getBlock(), inertiaAmount));
					
					SpectrumAdvancementCriteria.INERTIA_USED.trigger(serverPlayerEntity, state, inertiaAmount);
				}
				
				SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
			}
		});
		
		EnchantmentEvents.ALLOW_ENCHANTING.register((registryEntry, itemStack, enchantingContext) -> {
			if (registryEntry.matchesKey(SpectrumEnchantments.INDESTRUCTIBLE) && itemStack.isIn(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
				return TriState.FALSE;
			}
			return TriState.DEFAULT;
		});
		
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			ItemStack handStack = player.getStackInHand(hand);
			if (handStack.getItem() instanceof PrioritizedEntityInteraction && entity instanceof LivingEntity livingEntity) {
				return handStack.useOnEntity(player, livingEntity, hand);
			}
			return ActionResult.PASS;
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			ItemStack handStack = player.getStackInHand(hand);
			if (handStack.getItem() instanceof PrioritizedBlockInteraction) {
				return handStack.useOnBlock(new ItemUsageContext(player, hand, hitResult));
			}
			return ActionResult.PASS;
		});
		
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (!server.getTickManager().shouldTick()) {
				return;
			}

			try {
				Pastel.getServerInstance().tick();
			} catch (Exception e) {
				SpectrumCommon.logError("Error in the Pastel Network transmission loop.");
				e.printStackTrace();
			}
			
			PlayerManager playerManager = server.getPlayerManager();
			for (ServerPlayerEntity player : playerManager.getPlayerList()) {
				World world = player.getWorld();
				if (!player.isCreative() && !player.isSpectator() && world.getRegistryKey() == SpectrumDimensions.DIMENSION_KEY && player.getY() > world.getTopY()) {
					player.damage(player.getDamageSources().outOfWorld(), 10.0F);
					if (player.isDead()) {
						Support.grantAdvancementCriterion(player, "lategame/get_killed_while_out_of_deeper_down_bounds", "get_rekt");
					}
				}
			}
		});
		
		ServerTickEvents.START_WORLD_TICK.register(world -> {
			if (!world.getTickManager().shouldTick()) {
				return;
			}

			// these would actually be nicer to have as Spawners in ServerWorld
			// to have them run in tickSpawners()
			// but getting them in there would require some ugly mixins
			
			if (world.getTime() % 100 == 0) {
				if (TimeHelper.getTimeOfDay(world).isNight()) { // 90 chances in a night
					if (SpectrumCommon.CONFIG.ShootingStarWorlds.contains(world.getRegistryKey().getValue().toString())) {
						ShootingStarSpawner.INSTANCE.spawn(world, true, true);
					}
				}
				
				/* TODO: Monstrosity
				if (world.getRegistryKey() == SpectrumDimensions.DIMENSION_KEY) {
					MonstrositySpawner.INSTANCE.spawn(world, true, true);
				}*/
			}
		});
		
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			SpectrumCommon.logInfo("Querying fluid luminance...");
			for (Iterator<Block> it = Registries.BLOCK.stream().iterator(); it.hasNext(); ) {
				Block block = it.next();
				if (block instanceof FluidBlock fluidBlock) {
					fluidLuminance.put(fluidBlock.fluid, fluidBlock.getDefaultState().getLuminance());
				}
			}
			
			SpectrumCommon.logInfo("Injecting dynamic recipes into recipe manager...");
			FirestarterIdolBlock.addBlockSmeltingRecipes(server);
			//injectEnchantmentUpgradeRecipes(server);
		});
		
		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			// If the player wears a Whispy Cirlcet and sleeps
			// they get fully healed and all negative status effects removed
			// When the sleep timer reached 100 the player is fully asleep
			if (entity instanceof ServerPlayerEntity serverPlayerEntity
					&& serverPlayerEntity.getSleepTimer() == 100
					&& SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.WHISPY_CIRCLET)) {
				
				entity.setHealth(entity.getMaxHealth());
				WhispyCircletItem.removeNegativeStatusEffects(entity);
			}
		});
		
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof ServerPlayerEntity serverPlayerEntity && SpectrumTrinketItem.hasEquipped(serverPlayerEntity, SpectrumItems.JEOPARDANT)) {
				SpectrumAdvancementCriteria.JEOPARDANT_KILL.trigger(serverPlayerEntity, killedEntity);
			}
		});
		
		// CCA 1.21 supports mob conversion by default, but for now we have to persist this component ourselves
		// TODO do we need this now?
		ServerLivingEntityEvents.MOB_CONVERSION.register((previous, converted, keepEquipment) -> {
			/*if (EverpromiseRibbonComponent.hasRibbon(previous)) {
				EverpromiseRibbonComponent.attachRibbon(converted);
			}*/
		});
		
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
			var oldInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager(), SpectrumEnchantments.INEXORABLE, previousStack);
			var newInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager(), SpectrumEnchantments.INEXORABLE, currentStack);
			
			var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumAttributeTags.INEXORABLE_ARMOR_EFFECTIVE : SpectrumAttributeTags.INEXORABLE_HANDHELD_EFFECTIVE;
			
			//TODO make inexorable use enchantment effects or something
			//TODO also move the enchantment cloaking logic from LivingEntityMixin into here
			if (oldInexorable > 0 && newInexorable <= 0) {
				livingEntity.getStatusEffects()
						.stream()
						.filter(instance -> {
							AtomicBoolean result = new AtomicBoolean(false);
							instance.getEffectType().value().forEachAttributeModifier(instance.amplifier, (attribute, modifier) -> {
								if (attribute.isIn(effectType))
									result.set(true);
							});
							return result.get();
						})
						.forEach(instance -> instance.getEffectType().value().onApplied(livingEntity, instance.getAmplifier()));
			}
			
		});

		EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
			if (entity instanceof PlayerEntity player && MiscPlayerDataComponent.get(player).isSleeping())
				return ActionResult.SUCCESS;

			return ActionResult.PASS;
		});

		EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
			if (entity instanceof PlayerEntity player && MiscPlayerDataComponent.get(player).isSleeping())
				return player.getHorizontalFacing();
			return sleepingDirection;
		});

		EntitySleepEvents.ALLOW_NEARBY_MONSTERS.register((player, sleepingPos, vanillaResult) -> {
			if (MiscPlayerDataComponent.get(player).isSleeping() || player.hasStatusEffect(SpectrumStatusEffects.SOMNOLENCE))
				return ActionResult.SUCCESS;

			return ActionResult.PASS;
		});

		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
			if (player.hasStatusEffect(SpectrumStatusEffects.SOMNOLENCE))
				return ActionResult.SUCCESS;

			return ActionResult.PASS;
		});
		
		CrossbowShootingCallback.register((world, shooter, crossbow, projectile) -> {
			int snipingLevel = SpectrumEnchantmentHelper.getLevel(world.getRegistryManager(), SpectrumEnchantments.SNIPING, crossbow);
			if (snipingLevel > 0) {
				projectile.setVelocity(projectile.getVelocity().multiply(1.25F * snipingLevel)); // TODO: is this a sensible value?
			}
			
			if (crossbow.getItem() instanceof GlassCrestCrossbowItem && GlassCrestCrossbowItem.isOvercharged(crossbow)) {
				Vec3d particleVelocity = projectile.getVelocity().multiply(0.05);
				
				if (GlassCrestCrossbowItem.getOvercharge(crossbow) > 0.99F) {
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							projectile.getPos(), ParticleTypes.SCRAPE, 5,
							Vec3d.ZERO, particleVelocity);
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							projectile.getPos(), ParticleTypes.WAX_OFF, 5,
							Vec3d.ZERO, particleVelocity);
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							projectile.getPos(), ParticleTypes.WAX_ON, 5,
							Vec3d.ZERO, particleVelocity);
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
							projectile.getPos(), ParticleTypes.GLOW, 5,
							Vec3d.ZERO, particleVelocity);
					
					if (shooter instanceof ServerPlayerEntity serverPlayerEntity) {
						Support.grantAdvancementCriterion(serverPlayerEntity,
								SpectrumCommon.locate("lategame/shoot_fully_overcharged_crossbow"),
								"shot_fully_overcharged_crossbow");
					}
					if (projectile instanceof PersistentProjectileEntity persistentProjectileEntity) {
						persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 1.5);
					}
				}
				
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerWorld) world,
						projectile.getPos(), ParticleTypes.FIREWORK, 10,
						Vec3d.ZERO, particleVelocity);
				
				GlassCrestCrossbowItem.unOvercharge(crossbow);
			}
		});
		
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
			if (damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
				return true;
			}
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(entity);
			if (optionalTrinketComponent.isPresent()) {
				List<Pair<SlotReference, ItemStack>> totems = optionalTrinketComponent.get().getEquipped(SpectrumItems.TOTEM_PENDANT);
				for (Pair<SlotReference, ItemStack> pair : totems) {
					ItemStack totemStack = pair.getRight();
					
					if (totemStack.getCount() > 0) {
						// increase stat
						if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
							serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
							Criteria.USED_TOTEM.trigger(serverPlayerEntity, totemStack);
						}
						
						// consume pendant
						totemStack.decrement(1);
						
						// Heal and add effects
						entity.setHealth(1.0F);
						entity.clearStatusEffects();
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
						entity.getWorld().sendEntityStatus(entity, EntityStatuses.USE_TOTEM_OF_UNDYING);
						
						return false;
					}
				}
			}
			return true;
		});
		
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof ServerPlayerEntity player) {
				if (entity.getWorld().getLevelProperties().isHardcore() || HardcoreDeathComponent.isInHardcore(player)) {
					HardcoreDeathComponent.addHardcoreDeath(player.getServerWorld(), player.getGameProfile());
				}
				evaluateAndDropPlayerHead(player, damageSource);
			}
		});
		
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			// If the player is damaged by lava and wears an ashen circlet:
			// prevent damage and grant fire resistance
			if (source.isOf(DamageTypes.LAVA)) {
				Optional<ItemStack> ashenCircletStack = SpectrumTrinketItem.getFirstEquipped(entity, SpectrumItems.ASHEN_CIRCLET);
				if (ashenCircletStack.isPresent()) {
					if (AshenCircletItem.getCooldownTicks(ashenCircletStack.get(), entity.getWorld()) == 0) {
						AshenCircletItem.grantFireResistance(ashenCircletStack.get(), entity);
						return false;
					}
				}
			} else if (source.isIn(DamageTypeTags.IS_FIRE) && SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.ASHEN_CIRCLET)) {
				return false;
			}
			
			return true;
		});
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			private final Identifier id = SpectrumCommon.locate("cache_clearer");
			
			@Override
			public void reload(ResourceManager manager) {
				CompactingChestBlockEntity.clearCache();
				SpectrumCommon.CACHED_ITEM_TAG_MAP.clear();
				
				if (SpectrumCommon.minecraftServer != null) {
					//injectEnchantmentUpgradeRecipes(SpectrumCommon.minecraftServer);
					FirestarterIdolBlock.addBlockSmeltingRecipes(SpectrumCommon.minecraftServer);
				}
			}
			
			@Override
			public Identifier getFabricId() {
				return id;
			}
		});
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			private final Identifier id = SpectrumCommon.locate("cache_clearer_client");
			
			@Override
			public void reload(ResourceManager manager) {
				UnlockToastManager.clear();
			}
			
			@Override
			public Identifier getFabricId() {
				return id;
			}
		});
	}
	
	private static void evaluateAndDropPlayerHead(ServerPlayerEntity player, DamageSource source) {
		if (!player.isSpectator()) {
			// TODO: Can we evaluate a SpectrumLootPoolModifiers.treasureHunter() here instead?
			// code reuse is always nice
			ServerWorld serverWorld = player.getServerWorld();
			
			boolean shouldDropHead = source.isIn(SpectrumDamageTypeTags.ALWAYS_DROPS_MOB_HEAD);
			if (!shouldDropHead && source.getAttacker() instanceof LivingEntity livingAttacker) {
				int damageSourceTreasureHunt = SpectrumEnchantmentHelper.getEquipmentLevel(
						serverWorld.getRegistryManager(),
						SpectrumEnchantments.TREASURE_HUNTER,
						livingAttacker);
				
				shouldDropHead = damageSourceTreasureHunt > 0 && serverWorld.getRandom().nextFloat() < 0.2 * damageSourceTreasureHunt;
			}
			
			if (shouldDropHead) {
				ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);
				headItemStack.set(DataComponentTypes.PROFILE, new ProfileComponent(player.getGameProfile()));
				
				ItemEntity headEntity = new ItemEntity(serverWorld, player.getX(), player.getY(), player.getZ(), headItemStack);
				serverWorld.spawnEntity(headEntity);
			}
		}
	}
	
	public static int getFluidLuminance(Fluid fluid) {
		return fluidLuminance.getOrDefault(fluid, 0);
	}
	
}
