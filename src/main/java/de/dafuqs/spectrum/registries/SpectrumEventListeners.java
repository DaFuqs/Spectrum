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
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.client.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.fabric.api.util.*;
import net.fabricmc.loader.api.*;
import net.minecraft.advancements.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.server.players.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;

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
			if (!world.isClientSide && !player.isSpectator()) {
				
				ItemStack mainHandStack = player.getMainHandItem();
				if (mainHandStack.getItem() instanceof ExchangeStaffItem exchangeStaffItem) {
					BlockState targetBlockState = world.getBlockState(pos);
					if (exchangeStaffItem.canInteractWith(targetBlockState, world, pos, player)) {
						Optional<Block> storedBlock = ExchangeStaffItem.getStoredBlock(player.getMainHandItem());
						
						if (storedBlock.isPresent()
								&& storedBlock.get() != targetBlockState.getBlock()
								&& storedBlock.get().asItem() != Items.AIR
								&& ExchangeStaffItem.exchange(world, pos, player, storedBlock.get(), player.getMainHandItem(), true, direction)) {
							
							return InteractionResult.SUCCESS;
						}
					}
					world.playSound(null, player.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
					return InteractionResult.FAIL;
				}
			}
			return InteractionResult.PASS;
		});
		
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			if (client) {
				SpectrumColorProviders.resetToggleableProviders();
			}
		});
		
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (player instanceof ServerPlayer serverPlayerEntity) {
				ItemStack stack = serverPlayerEntity.getMainHandItem();
				if (stack.getItem() instanceof AoEBreakingTool tool) {
					tool.afterBreakingBlock(stack, pos, serverPlayerEntity);
				}
				
				ItemStack handStack = player.getItemInHand(serverPlayerEntity.getUsedItemHand());
				if (SpectrumEnchantmentHelper.hasEnchantment(player.level().registryAccess(), SpectrumEnchantments.INERTIA, handStack)) {
					InertiaComponent.onInertiaBlockBreak(level, pos, state, serverPlayerEntity, handStack);
				}
				
				SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
			}
		});
		
		EnchantmentEvents.ALLOW_ENCHANTING.register((registryEntry, itemStack, enchantingContext) -> {
			if (registryEntry.is(SpectrumEnchantments.INDESTRUCTIBLE) && itemStack.is(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
				return TriState.FALSE;
			}
			return TriState.DEFAULT;
		});
		
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			ItemStack handStack = player.getItemInHand(hand);
			if (handStack.getItem() instanceof PrioritizedEntityInteraction && entity instanceof LivingEntity livingEntity) {
				return handStack.interactLivingEntity(player, livingEntity, hand);
			}
			return InteractionResult.PASS;
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			ItemStack handStack = player.getItemInHand(hand);
			if (handStack.getItem() instanceof PrioritizedBlockInteraction) {
				return handStack.useOn(new UseOnContext(player, hand, hitResult));
			}
			return InteractionResult.PASS;
		});
		
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (!server.tickRateManager().runsNormally()) {
				return;
			}

			try {
				Pastel.getServerInstance().tick();
			} catch (Exception e) {
				SpectrumCommon.logError("Error in the Pastel Network transmission loop.");
				e.printStackTrace();
			}
			
			PlayerList playerManager = server.getPlayerList();
			for (ServerPlayer player : playerManager.getPlayers()) {
				Level world = player.level();
				if (!player.isCreative() && !player.isSpectator() && world.dimension() == SpectrumDimensions.DIMENSION_KEY && player.getY() > world.getMaxBuildHeight()) {
					player.hurt(player.damageSources().fellOutOfWorld(), 10.0F);
					if (player.isDeadOrDying()) {
						Support.grantAdvancementCriterion(player, "lategame/get_killed_while_out_of_deeper_down_bounds", "get_rekt");
					}
				}
			}
		});
		
		ServerTickEvents.START_WORLD_TICK.register(world -> {
			if (!world.tickRateManager().runsNormally()) {
				return;
			}

			// these would actually be nicer to have as Spawners in ServerWorld
			// to have them run in tickSpawners()
			// but getting them in there would require some ugly mixins
			
			if (world.getGameTime() % 100 == 0) {
				if (TimeHelper.getTimeOfDay(world).isNight()) { // 90 chances in a night
					if (SpectrumCommon.CONFIG.ShootingStarWorlds.contains(world.dimension().location().toString())) {
						ShootingStarSpawner.INSTANCE.tick(world, true, true);
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
			for (Iterator<Block> it = BuiltInRegistries.BLOCK.stream().iterator(); it.hasNext(); ) {
				Block block = it.next();
				if (block instanceof LiquidBlock fluidBlock) {
					fluidLuminance.put(fluidBlock.fluid, fluidBlock.defaultBlockState().getLightEmission());
				}
			}
			
			SpectrumCommon.logInfo("Injecting dynamic recipes into recipe manager...");
			FirestarterIdolBlock.addBlockSmeltingRecipes(server);
		});
		
		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			// If the player wears a Whispy Cirlcet and sleeps
			// they get fully healed and all negative status effects removed
			// When the sleep timer reached 100 the player is fully asleep
			if (entity instanceof ServerPlayer serverPlayerEntity
					&& serverPlayerEntity.getSleepTimer() == 100
					&& SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.WHISPY_CIRCLET)) {
				
				entity.setHealth(entity.getMaxHealth());
				WhispyCircletItem.removeNegativeStatusEffects(entity);
			}
		});
		
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof ServerPlayer serverPlayerEntity && SpectrumTrinketItem.hasEquipped(serverPlayerEntity, SpectrumItems.JEOPARDANT)) {
				SpectrumAdvancementCriteria.JEOPARDANT_KILL.trigger(serverPlayerEntity, killedEntity);
			}
		});
		
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
			var oldInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.level().registryAccess(), SpectrumEnchantments.INEXORABLE, previousStack);
			var newInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.level().registryAccess(), SpectrumEnchantments.INEXORABLE, currentStack);
			
			var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumAttributeTags.INEXORABLE_ARMOR_EFFECTIVE : SpectrumAttributeTags.INEXORABLE_HANDHELD_EFFECTIVE;
			
			//TODO make inexorable use enchantment effects or something
			//TODO also move the enchantment cloaking logic from LivingEntityMixin into here
			if (oldInexorable > 0 && newInexorable <= 0) {
				livingEntity.getActiveEffects()
						.stream()
						.filter(instance -> {
							AtomicBoolean result = new AtomicBoolean(false);
							instance.getEffect().value().createModifiers(instance.getAmplifier(), (attribute, modifier) -> {
								if (attribute.is(effectType))
									result.set(true);
							});
							return result.get();
						})
						.forEach(instance -> instance.getEffect().value().onEffectStarted(livingEntity, instance.getAmplifier()));
			}
			
		});

		EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
			if (entity instanceof Player player && MiscPlayerDataComponent.get(player).isSleeping())
				return InteractionResult.SUCCESS;
			
			return InteractionResult.PASS;
		});

		EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
			if (entity instanceof Player player && MiscPlayerDataComponent.get(player).isSleeping())
				return player.getDirection();
			return sleepingDirection;
		});

		EntitySleepEvents.ALLOW_NEARBY_MONSTERS.register((player, sleepingPos, vanillaResult) -> {
			if (MiscPlayerDataComponent.get(player).isSleeping() || player.hasEffect(SpectrumStatusEffects.SOMNOLENCE))
				return InteractionResult.SUCCESS;
			
			return InteractionResult.PASS;
		});

		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
			if (player.hasEffect(SpectrumStatusEffects.SOMNOLENCE))
				return InteractionResult.SUCCESS;
			
			return InteractionResult.PASS;
		});
		
		CrossbowShootingCallback.register((world, shooter, crossbow, projectile) -> {
			int snipingLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), SpectrumEnchantments.SNIPING, crossbow);
			if (snipingLevel > 0) {
				projectile.setDeltaMovement(projectile.getDeltaMovement().scale(1.25F * snipingLevel));
			}
			
			if (crossbow.getItem() instanceof GlassCrestCrossbowItem && GlassCrestCrossbowItem.isOvercharged(crossbow)) {
				Vec3 particleVelocity = projectile.getDeltaMovement().scale(0.05);
				
				if (GlassCrestCrossbowItem.getOvercharge(crossbow) > 0.99F) {
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world,
							projectile.position(), ParticleTypes.SCRAPE, 5,
							Vec3.ZERO, particleVelocity);
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world,
							projectile.position(), ParticleTypes.WAX_OFF, 5,
							Vec3.ZERO, particleVelocity);
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world,
							projectile.position(), ParticleTypes.WAX_ON, 5,
							Vec3.ZERO, particleVelocity);
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world,
							projectile.position(), ParticleTypes.GLOW, 5,
							Vec3.ZERO, particleVelocity);
					
					if (shooter instanceof ServerPlayer serverPlayerEntity) {
						Support.grantAdvancementCriterion(serverPlayerEntity,
								SpectrumCommon.locate("lategame/shoot_fully_overcharged_crossbow"),
								"shot_fully_overcharged_crossbow");
					}
					if (projectile instanceof AbstractArrow persistentProjectileEntity) {
						persistentProjectileEntity.setBaseDamage(persistentProjectileEntity.getBaseDamage() * 1.5);
					}
				}
				
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world,
						projectile.position(), ParticleTypes.FIREWORK, 10,
						Vec3.ZERO, particleVelocity);
				
				GlassCrestCrossbowItem.unOvercharge(crossbow);
			}
		});
		
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
			if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
				return true;
			}
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(entity);
			if (optionalTrinketComponent.isPresent()) {
				List<Tuple<SlotReference, ItemStack>> totems = optionalTrinketComponent.get().getEquipped(SpectrumItems.TOTEM_PENDANT);
				for (Tuple<SlotReference, ItemStack> pair : totems) {
					ItemStack totemStack = pair.getB();
					
					if (totemStack.getCount() > 0) {
						// increase stat
						if (entity instanceof ServerPlayer serverPlayerEntity) {
							serverPlayerEntity.awardStat(Stats.ITEM_USED.get(SpectrumItems.TOTEM_PENDANT));
							CriteriaTriggers.USED_TOTEM.trigger(serverPlayerEntity, totemStack);
						}
						
						// consume pendant
						totemStack.shrink(1);
						
						// Heal and add effects
						entity.setHealth(1.0F);
						entity.removeAllEffects();
						entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
						entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
						entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
						entity.level().broadcastEntityEvent(entity, EntityEvent.TALISMAN_ACTIVATE);
						
						return false;
					}
				}
			}
			return true;
		});
		
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof ServerPlayer player) {
				if (entity.level().getLevelData().isHardcore() || HardcoreDeathComponent.isInHardcore(player)) {
					HardcoreDeathComponent.addHardcoreDeath(player.serverLevel(), player.getGameProfile());
				}
				evaluateAndDropPlayerHead(player, damageSource);
			}
		});
		
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			// If the player is damaged by lava and wears an ashen circlet:
			// prevent damage and grant fire resistance
			if (source.is(DamageTypes.LAVA)) {
				Optional<ItemStack> ashenCircletStack = SpectrumTrinketItem.getFirstEquipped(entity, SpectrumItems.ASHEN_CIRCLET);
				if (ashenCircletStack.isPresent()) {
					if (AshenCircletItem.getCooldownTicks(ashenCircletStack.get(), entity.level()) == 0) {
						AshenCircletItem.grantFireResistance(ashenCircletStack.get(), entity);
						return false;
					}
				}
			} else if (source.is(DamageTypeTags.IS_FIRE) && SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.ASHEN_CIRCLET)) {
				return false;
			}
			
			return true;
		});
		
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			private final ResourceLocation id = SpectrumCommon.locate("server_data_cache_clearer");
			
			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				CompactingChestBlockEntity.clearCache();
				SpectrumCommon.CACHED_ITEM_TAG_MAP.clear();
				
				if (SpectrumCommon.minecraftServer != null) {
					FirestarterIdolBlock.addBlockSmeltingRecipes(SpectrumCommon.minecraftServer);
				}
				
				if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
					UnlockToastManager.clear();
				}
			}
			
			@Override
			public ResourceLocation getFabricId() {
				return id;
			}
		});
	}
	
	private static void evaluateAndDropPlayerHead(ServerPlayer player, DamageSource source) {
		if (!player.isSpectator()) {
			// TODO: Can we evaluate a SpectrumLootPoolModifiers.treasureHunter() here instead?
			// code reuse is always nice
			ServerLevel serverWorld = player.serverLevel();
			
			boolean shouldDropHead = source.is(SpectrumDamageTypeTags.ALWAYS_DROPS_MOB_HEAD);
			if (!shouldDropHead && source.getEntity() instanceof LivingEntity livingAttacker) {
				int damageSourceTreasureHunt = SpectrumEnchantmentHelper.getEquipmentLevel(
						serverWorld.registryAccess(),
						SpectrumEnchantments.TREASURE_HUNTER,
						livingAttacker);
				
				shouldDropHead = damageSourceTreasureHunt > 0 && serverWorld.getRandom().nextFloat() < 0.2 * damageSourceTreasureHunt;
			}
			
			if (shouldDropHead) {
				ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);
				headItemStack.set(DataComponents.PROFILE, new ResolvableProfile(player.getGameProfile()));
				
				ItemEntity headEntity = new ItemEntity(serverWorld, player.getX(), player.getY(), player.getZ(), headItemStack);
				serverWorld.addFreshEntity(headEntity);
			}
		}
	}
	
	public static int getFluidLuminance(Fluid fluid) {
		return fluidLuminance.getOrDefault(fluid, 0);
	}
	
}
