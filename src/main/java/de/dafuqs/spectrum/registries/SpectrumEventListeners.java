package de.dafuqs.spectrum.registries;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.entity.spawners.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.mob_effect.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.loading.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.event.tick.*;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.items.wrapper.*;
import org.jetbrains.annotations.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;
import java.util.concurrent.atomic.*;

@EventBusSubscriber(modid = SpectrumCommon.MOD_ID)
public class SpectrumEventListeners {
	
	@SubscribeEvent
	public static void modifyComponents(ModifyDefaultComponentsEvent event) {
		event.modify(Items.NETHER_STAR, builder -> builder.set(SpectrumDataComponentTypes.DAMAGE_IMMUNE.get(), List.of(DamageTypeTags.IS_EXPLOSION, DamageTypeTags.IS_FIRE)));
	}
	
	@SubscribeEvent
	public static InteractionResult exchangeBlock(PlayerInteractEvent.LeftClickBlock event) {
		Level world = event.getLevel();
		BlockPos pos = event.getPos();
		Player player = event.getEntity();
		Direction direction = event.getFace();
		
		if (!world.isClientSide && !player.isSpectator()) {
			
			ItemStack mainHandStack = player.getMainHandItem();
			if (mainHandStack.getItem() instanceof ExchangeStaffItem exchangeStaffItem) {
				BlockState targetBlockState = world.getBlockState(pos);
				if (exchangeStaffItem.canInteractWith(targetBlockState, world, pos, player)) {
					Optional<Block> storedBlock = ExchangeStaffItem.getStoredBlock(player.getMainHandItem());
					
					if (storedBlock.isPresent()
							&& storedBlock.get() != targetBlockState.getBlock()
							&& storedBlock.get().asItem() != Items.AIR
							&& ExchangeStaffItem.exchange(world, pos, player, storedBlock.get(), player.getMainHandItem(),
							true, direction)) {
						return InteractionResult.SUCCESS;
					}
				}
				world.playSound(null, player.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
				return InteractionResult.FAIL;
			}
		}
		
		return InteractionResult.PASS;
	}
	
	@SubscribeEvent
	public static void resetColorProviders(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			SpectrumColorProviders.resetToggleableProviders();
		}
	}
	
	@SubscribeEvent
	public static void handleInertia(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		Level level = event.getPlayer().level();
		BlockState state = event.getState();
		if (player instanceof ServerPlayer serverPlayerEntity) {
			ItemStack handStack = player.getItemInHand(serverPlayerEntity.getUsedItemHand());
			if (SpectrumEnchantmentHelper.hasEnchantment(player.level().registryAccess(), SpectrumEnchantmentKeys.INERTIA, handStack)) {
				InertiaComponent.onInertiaBlockBreak(level, pos, state, serverPlayerEntity, handStack);
			}
			
			SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
		}
	}
	
	//Curious: I'm like 90% sure this isn't needed anymore since Enchantments are stored as components now so I'm just commenting both instances of this
	//Curious TODO: Look into whether enchantments still need this event check for the Indestructible enchant
//	@SubscribeEvent
//	public TriState preventIndestructible(GetEnchantmentLevelEvent.) {
//		RegistryE
//		if (registryEntry.is(SpectrumEnchantments.INDESTRUCTIBLE) && itemStack.is(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED)) {
//			return TriState.FALSE;
//		}
//		return TriState.DEFAULT;
//	}
	
	@SubscribeEvent
	private static void entityTick(EntityTickEvent.Post event) {
		var entity = event.getEntity();
		
		if (entity instanceof LivingEntity living) {
			PrimordialFireAttachmentType.tick(living);
			
			if (living.level().isClientSide())
				return;
			AzureDikeAttachmentType azureDikeAttachment = living.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
			azureDikeAttachment.serverTick(living);
		}
	}
	
	@SubscribeEvent
	public static InteractionResult triggerPrioritizedEntityInteraction(PlayerInteractEvent.EntityInteract event){
		
		Player player = event.getEntity();
		Entity entity = event.getTarget();
		InteractionHand hand = event.getHand();
		ItemStack handStack = player.getItemInHand(hand);
		
		if (handStack.getItem() instanceof PrioritizedEntityInteraction && entity instanceof LivingEntity livingEntity) {
			return handStack.interactLivingEntity(player, livingEntity, hand);
		}
		return InteractionResult.PASS;
		
	}
	
	@SubscribeEvent
	public static InteractionResult triggerPrioritizedBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		InteractionHand hand = event.getHand();
		BlockHitResult hitResult = event.getHitVec();
		ItemStack handStack = player.getItemInHand(hand);
		
		if (handStack.getItem() instanceof PrioritizedBlockInteraction) {
			return handStack.useOn(new UseOnContext(player, hand, hitResult));
		}
		return InteractionResult.PASS;
		
	}
	
	//Curious: I'm basically checking the sleep timer when the player wakes up here
	//Curious: Miraculously this works due to a single line in the Player class which is awesome
	@SubscribeEvent
	public static void triggerWhispyCirclet(PlayerWakeUpEvent event) {
		Player player = event.getEntity();
		
		if(player.getSleepTimer() == 100 && SpectrumCurioItem.hasEquipped(player, SpectrumItems.WHISPY_CIRCLET.asItem())) {
			player.setHealth(player.getMaxHealth());
			MobEffectHelper.clearEffects(player, WhispyCircletItem.EFFECT_CLEAR_PREDICATE);
		}
		
	}
	
	@SubscribeEvent
	public static void tickSpawners(LevelTickEvent.Pre event) {
		Level level =  event.getLevel();
		if(!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		
		if(!serverLevel.tickRateManager().runsNormally()) {
			return;
		}
		
		if (serverLevel.getGameTime() % 100 == 0) {
			if (TimeHelper.getTimeOfDay(serverLevel).isNight()) { // 90 chances in a night
				if (SpectrumConfig.CONFIG.ShootingStarDimensions.get().contains(serverLevel.dimension().location().toString())) {
					ShootingStarSpawner.INSTANCE.tick(serverLevel, true, true);
				}
			}
				
			/* TODO: Monstrosity
			if (world.getRegistryKey() == SpectrumDimensions.DIMENSION_KEY) {
				MonstrositySpawner.INSTANCE.spawn(world, true, true);
			}*/
		}
	}
	
	@SubscribeEvent
	public static void injectDynamicRecipe(ServerStartedEvent event) {
		MinecraftServer server = event.getServer();
		
		SpectrumCommon.logInfo("Injecting dynamic recipes into recipe manager...");
		FirestarterIdolBlock.addBlockSmeltingRecipes(server);
	}
	
	@SubscribeEvent
	public static void tickPastelNetwork(ServerTickEvent.Post event) {
		MinecraftServer server = event.getServer();
		
		if (!server.tickRateManager().runsNormally()) {
			return;
		}
		
		try {
			Pastel.getServerInstance().tick();
		} catch (Exception e) {
			SpectrumCommon.logError("Error in the Pastel Network transmission loop.");
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public static void damagePlayersOutOfBoundsInDD(PlayerTickEvent.Post event) {
		if(event.getEntity() instanceof ServerPlayer player) {
			Level world = player.level();
			if (!player.isCreative() && !player.isSpectator() && world.dimension() == SpectrumDimensionKeys.DIMENSION_KEY && player.getY() > world.getMaxBuildHeight()) {
				player.hurt(player.damageSources().fellOutOfWorld(), 10.0F);
				if (player.isDeadOrDying()) {
					Support.grantAdvancementCriterion(player, "lategame/get_killed_while_out_of_deeper_down_bounds", "get_rekt");
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		var livingEntity = event.getEntity();
		var oldEquipment = event.getFrom();
		var newEquipment = event.getTo();
		var equipmentSlot = event.getSlot();
		
		var oldInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.level().registryAccess(), SpectrumEnchantmentKeys.INEXORABLE, oldEquipment);
		var newInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.level().registryAccess(), SpectrumEnchantmentKeys.INEXORABLE, newEquipment);
		
		var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumAttributeKeys.INEXORABLE_ARMOR_EFFECTIVE : SpectrumAttributeKeys.INEXORABLE_HANDHELD_EFFECTIVE;
		
		//TODO make inexorable use enchantment effects or something
		//TODO also move the enchantment cloaking logic from LivingEntityMixin into here
		if (oldInexorable > 0 && newInexorable <= 0) {
			livingEntity.getActiveEffects()
					.stream()
					.filter(instance -> {
						AtomicBoolean result = new AtomicBoolean(false);
						instance.getEffect()
								.value()
								.createModifiers(
										instance.getAmplifier(), (attribute, modifier) -> {
											if (attribute.is(effectType))
												result.set(true);
										}
								);
						return result.get();
					})
					.forEach(instance -> instance.getEffect()
							.value()
							.onEffectStarted(livingEntity, instance.getAmplifier()));
		}
	}
	
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Entity sourceEntity = event.getSource().getEntity();
		LivingEntity killedEntity = event.getEntity();
		
		if(sourceEntity instanceof LivingEntity livinSource) {
			LastKillAttachmentType.rememberKillTick(livinSource, livinSource.level().getGameTime());
			
			MobEffectInstance frenzy = livinSource.getEffect(SpectrumMobEffects.FRENZY);
			if (frenzy != null) {
				((FrenzyMobEffect) frenzy.getEffect()).onKill(livinSource, frenzy.getAmplifier());
			}
		}
		
		if (event.getSource().getEntity() instanceof ServerPlayer player && SpectrumCurioItem.hasEquipped(player, SpectrumItems.JEOPARDANT.get())) {
			SpectrumAdvancementCriteria.JEOPARDANT_KILL.trigger(player, event.getEntity());
		}
		
		Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(killedEntity);
		if (curiosInventory.isPresent()) {
			Optional<SlotResult> firstTotemPendant = curiosInventory.get().findFirstCurio(SpectrumItems.TOTEM_PENDANT.get());
			if (firstTotemPendant.isPresent()) {
				ItemStack totemStack = firstTotemPendant.get().stack();
				
				// increase stat
				if (killedEntity instanceof ServerPlayer serverPlayerEntity) {
					serverPlayerEntity.awardStat(Stats.ITEM_USED.get(SpectrumItems.TOTEM_PENDANT.get()));
					CriteriaTriggers.USED_TOTEM.trigger(serverPlayerEntity, totemStack);
				}
				
				// consume pendant
				totemStack.shrink(1);
				
				// Heal and add effects
				killedEntity.setHealth(1.0F);
				killedEntity.removeAllEffects();
				killedEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
				killedEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
				killedEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
				killedEntity.level().broadcastEntityEvent(killedEntity, EntityEvent.TALISMAN_ACTIVATE);
				
				event.setCanceled(true);
			}
		}
		
		if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer serverPlayer) {
			if (serverPlayer.level().getLevelData().isHardcore() || HardcoreDeathAttachmentType.isInHardcore(serverPlayer)) {
				HardcoreDeathAttachmentType.setHardcoreDeath(serverPlayer);
			}
			evaluateAndDropPlayerHead(serverPlayer, event.getSource());
		}
	}
	
	@SubscribeEvent
	private static void onIncomingDamage(LivingIncomingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		DamageSource source = event.getSource();
		
		// If the player is damaged by fire / lava and wears an ashen circlet:
		// prevent damage and grant fire resistance
		if (source.is(DamageTypeTags.IS_FIRE)) {
			Optional<ItemStack> ashenCircletStack = SpectrumCurioItem.getFirstEquipped(entity, SpectrumItems.ASHEN_CIRCLET.get());
			if (ashenCircletStack.isPresent()) {
				if (source.is(DamageTypes.LAVA)) {
					if (AshenCircletItem.getCooldownTicks(ashenCircletStack.get(), entity.level()) == 0) {
						AshenCircletItem.grantFireResistance(ashenCircletStack.get(), entity);
						event.setCanceled(true);
					}
				} else {
					event.setCanceled(true);
				}
			}
		}
		
		@Nullable MobEffectInstance vulnerability = entity.getEffect(SpectrumMobEffects.VULNERABILITY);
		if (vulnerability != null) {
			float vulnerabilityDamageMultiplier = 1 + (SpectrumMobEffects.VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL * vulnerability.getAmplifier() + 1);
			event.setAmount(event.getAmount() * vulnerabilityDamageMultiplier);
		}
		
		if (source.is(DamageTypes.FALL)) {
			// check if this entity is protected by puff circlet
			AzureDikeAttachmentType azureDikeAttachment = entity.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
			float cost = Math.min(event.getAmount(), PuffCircletItem.FALL_DAMAGE_NEGATING_COST);
			// check if damage reduction is applicable to this entity
			if (azureDikeAttachment.getCurrentCharges() >= cost && SpectrumCurioItem.hasEquipped(entity, SpectrumItems.PUFF_CIRCLET.get())) {
				azureDikeAttachment.absorbDamage(entity, cost);
				
				Vec3 velocity = entity.getDeltaMovement();
				entity.setDeltaMovement(velocity.x(), 0.5, velocity.z());
				Level world = entity.level();
				if (world.isClientSide) { // it is split here so the particles spawn immediately, without network lag
					ParticleHelper.playParticleWithPatternAndVelocityClient(entity.level(), entity.position(), ColoredCraftingParticleEffect.WHITE, VectorPattern.EIGHT, 0.4);
					ParticleHelper.playParticleWithPatternAndVelocityClient(entity.level(), entity.position(), ColoredCraftingParticleEffect.BLUE, VectorPattern.EIGHT_OFFSET, 0.5);
				} else if (entity instanceof ServerPlayer serverPlayerEntity) {
					PlayParticleWithPatternAndVelocityPayload.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerLevel) entity.level(), entity.position(), ColoredCraftingParticleEffect.WHITE, VectorPattern.EIGHT, 0.4);
					PlayParticleWithPatternAndVelocityPayload.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerLevel) entity.level(), entity.position(), ColoredCraftingParticleEffect.BLUE, VectorPattern.EIGHT_OFFSET, 0.5);
				}
				entity.level().playSound(null, entity.blockPosition(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundSource.PLAYERS, 1.0F, 1.0F);
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	private static void onIncomingDamage(LivingDamageEvent.Pre event) {
		DamageSource source = event.getSource();
		if (!source.is(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			LivingEntity entity = event.getEntity();
			AzureDikeAttachmentType azureDikeAttachment = entity.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
			event.setNewDamage(azureDikeAttachment.absorbDamage(entity, event.getNewDamage()));
		}
	}
	
	@SubscribeEvent
	private static void canPlayerSleep(CanPlayerSleepEvent event) {
		var player = event.getEntity();
		var reason = event.getProblem();
		
		if (reason != Player.BedSleepingProblem.NOT_POSSIBLE_NOW && MiscPlayerDataAttachmentType.get(player).isSleeping()) {
			event.setProblem(null);
		} else if ((reason == Player.BedSleepingProblem.NOT_POSSIBLE_NOW || reason == Player.BedSleepingProblem.NOT_SAFE) && player.hasEffect(SpectrumMobEffects.SOMNOLENCE)) {
			event.setProblem(null);
		}
	}
	
	@SubscribeEvent
	private static void onReloadResources(AddReloadListenerEvent event) {
		event.addListener(new ResourceManagerReloadListener() {
			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				AutoCraftingMode.clearCache();
				SpectrumCommon.CACHED_ITEM_TAG_MAP.clear();
				
				if (SpectrumCommon.minecraftServer != null) {
					FirestarterIdolBlock.addBlockSmeltingRecipes(SpectrumCommon.minecraftServer);
				}
				
				if (FMLLoader.getDist() == Dist.CLIENT) {
					UnlockToastManager.clear();
				}
			}
			
			@Override
			public @NotNull String getName() {
				return SpectrumCommon.MOD_ID + ":resoruces_cleanup";
			}
		});
	}
	
	
	public static void register() {
		CrossbowShootingCallback.register((world, shooter, crossbow, projectile) -> {
			crossbow = shooter.getItemInHand(shooter.getUsedItemHand()); // TODO: fix this in Arrowhead
			int snipingLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), SpectrumEnchantmentKeys.SNIPING, crossbow);
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
						Support.grantAdvancementCriterion(serverPlayerEntity, SpectrumCommon.locate("lategame/shoot_fully_overcharged_crossbow"), "shot_fully_overcharged_crossbow");
					}
					if (projectile instanceof AbstractArrow persistentProjectileEntity) {
						persistentProjectileEntity.setBaseDamage(persistentProjectileEntity.getBaseDamage() * 1.5);
					}
				}
				
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, projectile.position(), ParticleTypes.FIREWORK, 10, Vec3.ZERO, particleVelocity);
				GlassCrestCrossbowItem.unOvercharge(crossbow);
			}
		});
	}
	
	private static void evaluateAndDropPlayerHead(ServerPlayer player, DamageSource source) {
		if (!player.isSpectator()) {
			ServerLevel serverWorld = player.serverLevel();
			
			boolean shouldDropHead = source.is(SpectrumDamageTypeTags.ALWAYS_DROPS_MOB_HEAD);
			if (!shouldDropHead && source.getEntity() instanceof LivingEntity livingAttacker) {
				int damageSourceTreasureHunt = SpectrumEnchantmentHelper.getEquipmentLevel(
						serverWorld.registryAccess(),
						SpectrumEnchantmentKeys.TREASURE_HUNTER,
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
	
	@SubscribeEvent
	private static void dropHealingWhenScarred(LivingHealEvent event) {
		if(event.getEntity().hasEffect(SpectrumMobEffects.SCARRED)) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	private static void preventFallFlyingWhenScarred(PlayerFlyableFallEvent event) {
		if(event.getEntity().hasEffect(SpectrumMobEffects.SCARRED)) {
			event.setMultiplier(0.05F);
		}
	}
	
	@SubscribeEvent
	private static void triggerTakeoffBeltJumpCriterion(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayerEntity) {
			SpectrumAdvancementCriteria.TAKE_OFF_BELT_JUMP.trigger(serverPlayerEntity);
		}
	}
	
	@SubscribeEvent
	private static void modifyArmorDamage(ArmorHurtEvent event) {
		DamageSource source = event.getDamageSource();
		if (source.is(SpectrumDamageTypeTags.DOES_NOT_DAMAGE_ARMOR)) {
			event.setCanceled(true);
		} else if (source.is(SpectrumDamageTypeTags.INCREASED_ARMOR_DAMAGE)) {
			for(EquipmentSlot slot : EquipmentSlot.values()) {
				event.setNewDamage(slot, event.getNewDamage(slot) * 10);
			}
		}
	}
	
	@SubscribeEvent
	private static void applyImprovedCriticalDamage(CriticalHitEvent event) {
		Player player = event.getEntity();
		Entity target = event.getTarget();
		
		int improvedCriticalLevel = SpectrumEnchantmentHelper.getLevel(player.level().registryAccess(), SpectrumEnchantmentKeys.IMPROVED_CRITICAL, event.getEntity().getMainHandItem());
		if(improvedCriticalLevel > 0) {
			event.setDamageMultiplier(event.getDamageMultiplier() + ImprovedCriticalHelper.getAdditionalCritDamageMultiplier(improvedCriticalLevel));
		}
		
		MiscPlayerDataAttachmentType miscPlayerDataAttachmentType = MiscPlayerDataAttachmentType.get(player);
		if (NectarLanceItem.sleepCrits(player, target) || miscPlayerDataAttachmentType.isParrying() || miscPlayerDataAttachmentType.isLunging()) {
			if (miscPlayerDataAttachmentType.isParrying()) {
				miscPlayerDataAttachmentType.setParryTicks(0);
			}
			
			if (miscPlayerDataAttachmentType.consumePerfectCounter())
				event.setDamageMultiplier(event.getDamageMultiplier() + 0.5F);
			
			if (!event.isCriticalHit()) {
				event.setCriticalHit(true);
				event.setDamageMultiplier(event.getDamageMultiplier() + 0.5F);
			}
		}
	}
	
	@SubscribeEvent
	private static void onLivingDamagePre(LivingDamageEvent.Pre event) {
		LivingEntity hurtEntity = event.getEntity();
		Entity sourceEntity = event.getSource().getEntity();
		float newDamage = event.getNewDamage();
		
		if (sourceEntity instanceof LivingEntity livingAttacker) {
			if (newDamage != 0F && hurtEntity.getHealth() == hurtEntity.getMaxHealth()) {
				ItemStack mainHandStack = livingAttacker.getMainHandItem();
				int level = SpectrumEnchantmentHelper.getLevel(livingAttacker.level().registryAccess(), SpectrumEnchantmentKeys.FIRST_STRIKE, mainHandStack);
				if (level > 0) {
					float additionalFirstStrikeDamage = SpectrumConfig.CONFIG.FirstStrikeDamagePerLevel.get() * level;
					event.setNewDamage(newDamage + additionalFirstStrikeDamage);
				}
			}
		}
	}
	
	@SubscribeEvent
	private static void lovingDamagePost(LivingDamageEvent.Post event) {
		LivingEntity hurtEntity = event.getEntity();
		
		// remember the hit
		if (hurtEntity instanceof Player hurtPlayer) {
			MiscPlayerDataAttachmentType.get(hurtPlayer).notifyHit();
		}
		
		DamageSource source = event.getSource();
		Entity sourceEntity = source.getEntity();
		Level level = hurtEntity.level();
		
		// Gleaming Pin
		if(hurtEntity instanceof ServerPlayer hurtServerPlayer) {
			MiscPlayerDataAttachmentType miscAttachment = MiscPlayerDataAttachmentType.get(hurtServerPlayer);
			Optional<ItemStack> gleamingPinStack = SpectrumCurioItem.getFirstEquipped(hurtEntity, SpectrumItems.GLEAMING_PIN.get());
			if (gleamingPinStack.isPresent() && level.getGameTime() - miscAttachment.getLastGleamingPinTriggerTick() > GleamingPinItem.COOLDOWN_TICKS) {
				GleamingPinItem.doGleamingPinEffect(hurtServerPlayer, (ServerLevel) level, gleamingPinStack.get());
				miscAttachment.setLastGleamingPinTriggerTick(level.getGameTime());
			}
		}
		
		// Disarming
		if (!source.is(DamageTypes.THORNS) && sourceEntity instanceof LivingEntity livingSource) {
			int disarmingLevel = SpectrumEnchantmentHelper.getLevel(level.registryAccess(), SpectrumEnchantmentKeys.DISARMING, livingSource.getMainHandItem());
			if (disarmingLevel > 0) {
				float disarmingChance = disarmingLevel * (livingSource instanceof Player ? SpectrumConfig.CONFIG.DisarmingChancePerLevelPlayers.get() : SpectrumConfig.CONFIG.DisarmingChancePerLevelMobs.get());
				if(level.getRandom().nextFloat() < disarmingChance) {
					disarmEntity(hurtEntity);
				}
			}
		}
	}
	
	private static void disarmEntity(LivingEntity livingEntity) {
		// since endermen save their carried block as blockState, not in hand
		// we have to use custom logic for them
		if (livingEntity instanceof EnderMan endermanEntity) {
			BlockState carriedBlockState = endermanEntity.getCarriedBlock();
			if (carriedBlockState != null) {
				Item item = carriedBlockState.getBlock().asItem();
				if (item != null) {
					endermanEntity.spawnAtLocation(item.getDefaultInstance());
					endermanEntity.setCarriedBlock(null);
				}
			}
			return;
		}
		
		// choose a random slot and drop its content
		List<EquipmentSlot> slots = new ArrayList<>(List.of(EquipmentSlot.values()));
		Collections.shuffle(slots);
		for (EquipmentSlot slot : slots) {
			ItemStack slotStack = livingEntity.getItemBySlot(slot);
			if (slotStack.isEmpty()) {
				continue;
			}
			
			// set to cannot drop? Skip that slot
			if (livingEntity instanceof Mob mobEntity && ((MobEntityAccessor) mobEntity).invokeGetEquipmentDropChance(slot) <= 0) {
				continue;
			}
			
			livingEntity.spawnAtLocation(slotStack);
			livingEntity.setItemSlot(slot, ItemStack.EMPTY);
			livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.NEUTRAL, 1.0F, 1.0F);
			break;
		}
	}
	
	@SubscribeEvent
	private static void playerWakeUp(PlayerWakeUpEvent event) {
		Player player = event.getEntity();
		
		MiscPlayerDataAttachmentType.get(player).resetSleepingState(false);
		player.removeEffect(SpectrumMobEffects.SOMNOLENCE);
	}
	
	@SubscribeEvent
	private static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
		if (event.getCurrentGameMode() == GameType.SPECTATOR && event.getNewGameMode() != GameType.SPECTATOR
				&& event.getEntity() instanceof ServerPlayer serverPlayer && HardcoreDeathAttachmentType.hasHardcoreDeath(serverPlayer)) {
			HardcoreDeathAttachmentType.clearHardcoreDeath(serverPlayer);
		}
	}
	
	// TODO: continue testing here
	@SubscribeEvent
	private static void onProjectileImpact(ProjectileImpactEvent event) {
		// if the target has a Puff circlet equipped
		// protect it from this projectile
		Projectile projectile = event.getProjectile();
		HitResult hitResult = event.getRayTraceResult();
		
		if(projectile.getType().is(SpectrumEntityTypeTags.UNDEFLECTABLE)) {
			return;
		}
		
		if(!(hitResult instanceof EntityHitResult entityHitResult)) {
			return;
		}
		
		Level world = projectile.level();
		if (!world.isClientSide) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof LivingEntity livingEntity) {
				boolean protect = false;
				
				MobEffectInstance reboundInstance = livingEntity.getEffect(SpectrumMobEffects.PROJECTILE_REBOUND);
				if (reboundInstance != null && entity.level().getRandom().nextFloat() < SpectrumMobEffects.PROJECTILE_REBOUND_CHANCE_PER_LEVEL * (reboundInstance.getAmplifier() + 1)) {
					protect = true;
				} else {
					if (SpectrumCurioItem.hasEquipped(livingEntity, SpectrumItems.PUFF_CIRCLET.get())) {
						AzureDikeAttachmentType azureDikeAttachment = livingEntity.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
						if (azureDikeAttachment.getCurrentCharges() > 0) {
							azureDikeAttachment.absorbDamage(livingEntity, PuffCircletItem.PROJECTILE_DEFLECTION_COST);
							protect = true;
						}
					}
				}
				
				if (protect) {
					projectile.shoot(0, 0, 0, 0, 0);
					
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, projectile.position(),
							ColoredCraftingParticleEffect.WHITE, 6,
							new Vec3(0, 0, 0),
							new Vec3(projectile.getX() - livingEntity.position().x, projectile.getY() - livingEntity.position().y, projectile.getZ() - livingEntity.position().z));
					PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, projectile.position(),
							ColoredCraftingParticleEffect.BLUE, 6,
							new Vec3(0, 0, 0),
							new Vec3(projectile.getX() - livingEntity.position().x, projectile.getY() - livingEntity.position().y, projectile.getZ() - livingEntity.position().z));
					
					world.playSound(null, projectile.blockPosition(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundSource.PLAYERS, 1.0F, 1.0F);
					livingEntity.hurtTime = Math.max(livingEntity.hurtTime, 1);
					event.setCanceled(true);
				}
				
			}
		}
	}
	
	@SubscribeEvent
	private static void onEntityJoinLevel(EntityJoinLevelEvent event) {
		event.getEntity().gameEvent(SpectrumGameEvents.ENTITY_SPAWNED);
	}
	
	@SubscribeEvent
	private static void onBlockBreak(BlockEvent.BreakEvent event) {
		if(!event.isCanceled()) {
			event.getLevel().gameEvent(SpectrumGameEvents.BLOCK_CHANGED, event.getPos(), GameEvent.Context.of(event.getState()));
		}
	}
	
	@SubscribeEvent
	private static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
		if(!event.isCanceled()) {
			event.getLevel().gameEvent(SpectrumGameEvents.BLOCK_CHANGED, event.getPos(), GameEvent.Context.of(event.getPlacedBlock()));
		}
	}
	
	@SubscribeEvent
	private static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
		// if carrying puff circlet: no trampling
		if (!event.isCanceled() && event.getEntity() instanceof LivingEntity livingEntity) {
			if (SpectrumCurioItem.hasEquipped(livingEntity, SpectrumItems.PUFF_CIRCLET.get())) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	private static void onSleepFinished(SleepFinishedTimeEvent event) {
		LevelAccessor l = event.getLevel();
		var time = TimeHelper.getTimeOfDay(l.dayTime());
		if (time.isDay()) {
			event.setTimeAddition(-11000L);
		}
	}
	
	@SubscribeEvent
	private static void onDropExperience(LivingExperienceDropEvent event) {
		if(!event.isCanceled()) {
			Player attackingPlayer = event.getAttackingPlayer();
			float exuberanceMod = ExuberanceHelper.getExuberanceMod(attackingPlayer);
			if(exuberanceMod > 0) {
				event.setDroppedExperience((int) (exuberanceMod * event.getDroppedExperience()));
			}
		}
	}
	
	@SubscribeEvent
	private static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
		for(EntityType<? extends LivingEntity> et : event.getTypes()) {
			event.add(et, SpectrumEntityAttributes.MENTAL_PRESENCE);
		}
	}
	
	@SubscribeEvent
	private static void onMobEffectAdded(MobEffectEvent.Applicable event) {
		LivingEntity entity = event.getEntity();
		if (entity.hasEffect(SpectrumMobEffects.IMMUNITY)) {
			event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
		}
		event.setResult(MobEffectEvent.Applicable.Result.DEFAULT);
	}
	
	@SubscribeEvent
	private static void tickGravity(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity.isNoGravity()) {
			return;
		}
		Level level = entity.level();
		
		if(entity instanceof Player player && !player.getAbilities().flying) {
			float appliedGravity = applyGravityBasedOnInventory(player, player.getInventory());
			
			// taking flight
			if(level.getGameTime() % 20 == 0 && player instanceof ServerPlayer serverPlayer) {
				if (appliedGravity > 0.081) {
					Support.grantAdvancementCriterion(serverPlayer, "lategame/carry_too_many_low_gravity_blocks", "gravity");
					// unable to jump a full block
				} else if (appliedGravity < -0.025) {
					Support.grantAdvancementCriterion(serverPlayer, "midgame/carry_too_many_heavy_gravity_blocks", "gravity");
				}
			}
		}
		
		if(level.isClientSide()) {
			return;
		}
		
		// Since an ItemEntity is much lighter than a player, we can x10 the gravity effect
		// This is not affected by item entity stack count to make it more predictable
		if(entity instanceof ItemEntity itemEntity) {
			ItemStack stack = itemEntity.getItem();
			if (stack.has(SpectrumDataComponentTypes.GRAVITABLE)) {
				float gravity = stack.get(SpectrumDataComponentTypes.GRAVITABLE);
				if(gravity == 0) {
					itemEntity.setNoGravity(true);
				} else {
					itemEntity.push(0, gravity * 10, 0);
					if (itemEntity.position().y() > level.getMaxBuildHeight() + 200) {
						itemEntity.discard();
					}
				}
			}
		}
		
		if(entity instanceof AbstractChestedHorse horse && horse.hasChest()) {
			float appliedGravity = applyGravityBasedOnInventory(horse, horse.getInventory());
				
			// when the animal is sent flying trigger a hidden advancement
			if (appliedGravity > 0.081 && level.getGameTime() % 20 == 0) {
				Player owner = PlayerOwned.getPlayerEntityIfOnline(horse.getOwnerUUID());
				if (owner != null) {
					Support.grantAdvancementCriterion((ServerPlayer) owner, "lategame/put_too_many_low_gravity_blocks_into_animal", "gravity");
				}
				
				// take damage when at height heights
				// otherwise the animal would just be floating forever
				if (horse.position().y() > level.getMaxBuildHeight() + 200) {
					horse.hurt(horse.damageSources().fellOutOfWorld(), 10);
				}
			}
		}
	}
	
	/**
	 * This one is for LivingEntities, like players
	 * Makes entities lighter / heavier, depending on the gravity effect of the item stack
	 *
	 * @return The additional Y Velocity that was applied
	 */
	public static float applyGravityBasedOnInventory(LivingEntity entity, Container inventory) {
		if (!entity.isPushable() || entity.isNoGravity() || entity.isSpectator()) {
			return 0;
		}
		
		float appliedGravityThisTick = 0F;
		for(int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			appliedGravityThisTick += stack.getOrDefault(SpectrumDataComponentTypes.GRAVITABLE, 0F) * stack.getCount();
		}
		
		if(appliedGravityThisTick != 0) {
			entity.push(0, appliedGravityThisTick, 0);
		}
		
		// if falling very slowly => reset fall distance / damage
		if (appliedGravityThisTick > 0 && entity.getDeltaMovement().y > -0.4) {
			entity.fallDistance = 0;
		}
		
		return appliedGravityThisTick;
	}
	
}
