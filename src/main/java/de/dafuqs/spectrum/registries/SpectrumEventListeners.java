package de.dafuqs.spectrum.registries;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.arrowhead.api.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
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
import de.dafuqs.spectrum.mob_effect.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.advancements.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.loading.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.*;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.*;
import net.neoforged.neoforge.event.level.block.*;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.event.tick.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.items.wrapper.*;
import net.neoforged.neoforgespi.locating.*;
import org.jspecify.annotations.*;
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
	public static void registerCauldronFluids(RegisterCauldronFluidContentEvent event) {
		event.register(SpectrumBlocks.LIQUID_CRYSTAL_CAULDRON.get(), SpectrumFluids.LIQUID_CRYSTAL.get(), FluidType.BUCKET_VOLUME, null);
		event.register(SpectrumBlocks.DRAGONROT_CAULDRON.get(), SpectrumFluids.DRAGONROT.get(), FluidType.BUCKET_VOLUME, null);
		event.register(SpectrumBlocks.MIDNIGHT_SOLUTION_CAULDRON.get(), SpectrumFluids.MIDNIGHT_SOLUTION.get(), FluidType.BUCKET_VOLUME, null);
	}
	
	@SubscribeEvent
	public static InteractionResult exchangeBlock(PlayerInteractEvent.LeftClickBlock event) {
		Level world = event.getLevel();
		BlockPos pos = event.getPos();
		Player player = event.getEntity();
		Direction direction = event.getFace();
		
		if (!world.isClientSide() && !player.isSpectator()) {
			
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
	}
	
	@SubscribeEvent
	public static void playerInteraction(PlayerInteractEvent.EntityInteractSpecific event) {
		if (event.getLevel().isClientSide()) return;
		
		Entity target = event.getTarget();
		Component entityCustomName = target.getCustomName();
		if (entityCustomName == null || !(target instanceof Cat cat)) return;
		
		Player player = event.getEntity();
		InteractionHand hand = event.getHand();
		ItemStack itemStack = player.getItemInHand(hand);
		
		String customName = target.getCustomName().getString().toUpperCase(Locale.ROOT);
		boolean howMany = customName.equals("AAA") || customName.equals("AAA ❣");
		if (player instanceof ServerPlayer serverPlayerEntity) {
			if (itemStack.is(SpectrumItems.STRATINE_GEM) && cat.hasEffect(MobEffects.LEVITATION) && howMany) {
				Support.grantAdvancementCriterion(serverPlayerEntity, ResourceLocation.fromNamespaceAndPath("spectrum", "midgame/become_enlightened"), "confirmed");
				cat.removeEffect(MobEffects.LEVITATION);
				cat.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 1));
			}
		}
	}
	
	@SubscribeEvent
	public static void canCropGrow(CropGrowEvent.Pre event) {
		if (event.getLevel().getBlockState(event.getPos().below()).is(SpectrumBlocks.TILLED_SHALE_CLAY)) {
			event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
		}
	}
	
	@SubscribeEvent
	public static void resetColorProviders(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			SpectrumColorProviders.resetToggleableProviders();
		}
	}
	
	@SubscribeEvent
	public static void fatalSlumberKill(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance.is(SpectrumMobEffects.FATAL_SLUMBER)) {
			LivingEntity entity = event.getEntity();
			
			if (entity.level().isClientSide())
				return;
			
			if (entity.isSpectator() || entity instanceof Player player && player.getAbilities().instabuild)
				return;
			
			float damage = Float.MAX_VALUE;
			if (SleepMobEffect.isImmuneish(entity)) {
				if (entity instanceof Player)
					damage = entity.getHealth() * 0.95F;
				else
					damage = entity.getMaxHealth() * 0.3F;
			}
			
			entity.hurt(SpectrumDamageTypes.sleep(entity.level(), null), damage);
			if (entity.isAlive() && entity instanceof ServerPlayer serverPlayerEntity && !serverPlayerEntity.isCreative()) {
				Support.grantAdvancementCriterion(serverPlayerEntity, "lategame/survive_fatal_slumber", "survived_fatal_slumber");
			}
		}
	}
	
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
				if (SpectrumConfig.spawnsShootingStars(serverLevel.dimension().location())) {
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
	public static void levelTickPost(LevelTickEvent.Post event) {
		for(LivingEntity entity : FATAL_SLUMBER_CURES) {
			entity.addEffect(new MobEffectInstance(SpectrumMobEffects.ETERNAL_SLUMBER, 6000));
		}
		FATAL_SLUMBER_CURES.clear();
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
		
		var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumEntityAttributeKeys.INEXORABLE_ARMOR_EFFECTIVE : SpectrumEntityAttributeKeys.INEXORABLE_HANDHELD_EFFECTIVE;
		
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
				((FrenzyMobEffect) frenzy.getEffect().value()).onKill(livinSource, frenzy.getAmplifier());
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
				killedEntity.removeEffectsCuredBy(EffectCures.PROTECTED_BY_TOTEM);
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
	
	
	private static final List<LivingEntity> FATAL_SLUMBER_CURES = new ArrayList<>();
	
	@SubscribeEvent
	private static void mobEffectRemoval(MobEffectEvent.Remove event) {
		if(event.getCure() == null && event.getEffect().is(SpectrumMobEffects.FATAL_SLUMBER)) {
			FATAL_SLUMBER_CURES.add(event.getEntity());
		}
	}
	
	@SubscribeEvent
	private static void onRightClickBlock(UseItemOnBlockEvent event) {
		ItemStack handStack = event.getItemStack();
		if(!handStack.is(Items.GLASS_BOTTLE)) {
			return;
		}
		
		Level level = event.getLevel();
		BlockPos blockPos = event.getPos();
		Player user = event.getPlayer();
		BlockState blockState = level.getBlockState(blockPos);
		
		if (blockState.is(SpectrumBlocks.FADING) && SpectrumConfig.CONFIG.CanBottleUpFading.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FADING)) {
			event.cancelWithResult(bottleUpDecay(level, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_FADING.get()));
		} else if (blockState.is(SpectrumBlocks.FAILING) && SpectrumConfig.CONFIG.CanBottleUpFailing.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FAILING)) {
			event.cancelWithResult(bottleUpDecay(level, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_FAILING.get()));
		} else if (blockState.is(SpectrumBlocks.RUIN) && SpectrumConfig.CONFIG.CanBottleUpRuin.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_RUIN)) {
			event.cancelWithResult(bottleUpDecay(level, user, handStack, blockPos, blockState, SpectrumItems.BOTTLE_OF_RUIN.get()));
		} else if (blockState.is(SpectrumBlocks.FORFEITURE) && SpectrumConfig.CONFIG.CanBottleUpForfeiture.get() && AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FORFEITURE)) {
		}
	}
	
	private static ItemInteractionResult bottleUpDecay(Level world, Player user, @Local ItemStack handStack, @Local BlockPos blockPos, BlockState blockState, Item item) {
		if(!world.isClientSide) {
			blockState.getBlock().playerWillDestroy(world, blockPos, blockState, user);
			world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			blockState.spawnAfterBreak((ServerLevel) world, blockPos, handStack, false);
		}
		world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
		user.awardStat(Stats.ITEM_USED.get(handStack.getItem()));
		ItemStack result = item.getDefaultInstance();
		
		handStack.consume(1, user);
		if (!user.getInventory().add(result)) {
			user.drop(result, false);
		}
		
		return ItemInteractionResult.sidedSuccess(world.isClientSide);
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
                if (world.isClientSide()) { // it is split here so the particles spawn immediately, without network lag
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
		ServerPlayer player = event.getEntity();
		Player.BedSleepingProblem problem = event.getProblem();
		
		if (problem != Player.BedSleepingProblem.NOT_POSSIBLE_NOW && MiscPlayerDataAttachmentType.get(player).isSleeping()) {
			event.setProblem(null);
		} else if (player.hasEffect(SpectrumMobEffects.SOMNOLENCE) && (problem == Player.BedSleepingProblem.NOT_POSSIBLE_NOW || problem == Player.BedSleepingProblem.NOT_SAFE)) {
			event.setProblem(null);
		}
	}
	
	@SubscribeEvent
	private static void canPlayerContinueSleeping(CanContinueSleepingEvent event) {
		LivingEntity entity = event.getEntity();
		Player.BedSleepingProblem problem = event.getProblem();
		
		if (entity.hasEffect(SpectrumMobEffects.SOMNOLENCE) && (problem == Player.BedSleepingProblem.NOT_POSSIBLE_NOW || problem == Player.BedSleepingProblem.NOT_SAFE)) {
			event.setContinueSleeping(true);
			return;
		}
		
		if(entity instanceof ServerPlayer serverPlayerEntity) {
			MiscPlayerDataAttachmentType attachmentType = MiscPlayerDataAttachmentType.get(serverPlayerEntity);
			if(attachmentType.isSleeping()) {
				event.setContinueSleeping(true);
				
			}
		}
	}
	
	@SubscribeEvent
	private static void onReloadResources(AddReloadListenerEvent event) {
		event.addListener(new ResourceManagerReloadListener() {
			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				AutoCraftingMode.invalidateCache();
				VariantHelper.invalidateCaches();
				FilterConfigurable.invalidateCache();
				
				if (SpectrumCommon.minecraftServer != null) {
					FirestarterIdolBlock.addBlockSmeltingRecipes(SpectrumCommon.minecraftServer);
				}
				
				if (FMLLoader.getDist() == Dist.CLIENT) {
					UnlockToastManager.clear();
				}
			}
			
			@Override
			public String getName() {
				return SpectrumCommon.MOD_ID + ":resources_cleanup";
			}
		});
	}
	
	
	public static void register() {
		CrossbowShootingCallback.register((world, shooter, crossbow, projectile) -> {
			crossbow = shooter.getItemInHand(shooter.getUsedItemHand()); // TODO: fix this in Arrowhead
			int snipingLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), SpectrumEnchantmentKeys.SNIPING, crossbow);
			if (snipingLevel > 0) {
				projectile.setDeltaMovement(projectile.getDeltaMovement().scale(1 + 0.25F * snipingLevel));
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
	private static void applySetHealthDamage(LivingIncomingDamageEvent event) {
		float amount = event.getAmount();
		DamageSource source = event.getSource();
		LivingEntity target = event.getEntity();
		
		// SetHealth damage does exactly that
		if (amount > 0 && source.is(SpectrumDamageTypeTags.USES_SET_HEALTH)) {
			float h = target.getHealth();
			target.setHealth(h - amount);
			target.getCombatTracker().recordDamage(source, amount);
			if (target.isDeadOrDying()) {
				var deathSound = ((LivingEntityAccessor) target).invokeGetDeathSound();
				if (deathSound != null) {
					target.makeSound(deathSound);
				}
				target.die(source);
			}
			event.setCanceled(true);
			return;
		}
		
		// If this entity is hit with a SplitDamageItem, damage() gets called recursively for each type of damage dealt
		if (!SpectrumDamageTypes.recursiveDamageFlag && amount > 0 && source.getDirectEntity() instanceof LivingEntity livingSource) {
			if (source.getWeaponItem().getItem() instanceof SplitDamageItem splitDamageItem) {
				SpectrumDamageTypes.recursiveDamageFlag = true;
				SplitDamageItem.DamageComposition composition = splitDamageItem.getDamageComposition(livingSource, target, source.getWeaponItem(), amount);
				
				for (Tuple<DamageSource, Float> entry : composition.get()) {
					int invulnerableTimeStore = target.invulnerableTime;
					target.invulnerableTime = 0;
					target.hurt(entry.getA(), entry.getB());
					target.invulnerableTime = invulnerableTimeStore;
				}
				
				SpectrumDamageTypes.recursiveDamageFlag = false;
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	private static void addMobEffect(MobEffectEvent.Applicable event) {
		LivingEntity entity = event.getEntity();
		if(entity.level().isClientSide()) {
			return;
		}
		
		if (entity.hasEffect(SpectrumMobEffects.IMMUNITY)) {
			event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			return;
		}
		
		MobEffectInstance effect = event.getEffectInstance();
		Holder<MobEffect> effectType = effect.getEffect();
		
		if (AetherGracedNectarGlovesItem.testEffectFor(entity, effectType)) {
			int cost = (effect.getAmplifier() + 1) * AetherGracedNectarGlovesItem.HARMFUL_EFFECT_COST;
			
			if (AetherGracedNectarGlovesItem.tryBlockEffect(entity, cost)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
				return;
			}
		}
		
		if (MobEffectHelper.canBeExtended(effect.getEffect())) {
			MobEffectInstance effectProlongingInstance = entity.getEffect(SpectrumMobEffects.EFFECT_PROLONGING);
			if (effectProlongingInstance != null) {
				effect.spectrum$setDuration(MobEffectHelper.getExtendedDuration(effect.getDuration(), effectProlongingInstance.getAmplifier()));
			}
		}
		
		// if it is a stacking effect, stack it
		MobEffectInstance existingInstance = entity.getEffect(effectType);
		if (existingInstance != null && effectType.is(SpectrumMobEffectTags.STACKING)) {
			SpectrumMobEffects.effectsAreGettingStacked = true;
			
			int newAmplifier = 1 + existingInstance.getAmplifier() + effect.getAmplifier();
			effect.spectrum$setAmplifier(newAmplifier);
			SpectrumMobEffects.effectsAreGettingStacked = false;
		}
		
		float resistanceModifier = Mth.clamp(SleepMobEffect.getSleepResistance(effect, entity), 0.1F, 10F);
		if (effectType.is(SpectrumMobEffects.ETERNAL_SLUMBER)) {
			if (SleepMobEffect.isImmuneish(entity)) {
				effect.spectrum$setDuration(Math.round(effect.getDuration() / resistanceModifier));
			} else if (!entity.getType().is(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
				effect.spectrum$setDuration(MobEffectInstance.INFINITE_DURATION);
			}
		} else if (effectType.is(SpectrumMobEffects.FATAL_SLUMBER)) {
			if (SleepMobEffect.isImmuneish(entity) && entity.getType().is(Tags.EntityTypes.BOSSES)) {
				effect.spectrum$setDuration(20 * 60);
			} else {
				effect.spectrum$setDuration(Math.max(Math.round(effect.getDuration() * resistanceModifier * 3), 20 * 10));
			}
		}
	}
	
	@SubscribeEvent
	private static void shieldBlock(LivingShieldBlockEvent event) {
		var entity = event.getEntity();
		var activeStack = entity.getUseItem();
		var useTime = entity.getTicksUsingItem();
		
		if (!(activeStack.getItem() instanceof ParryingSwordItem parryingSword))
			return;
		
		if (entity instanceof Player player && parryingSword.canBluffParry(activeStack, entity, useTime)) {
			var comp = MiscPlayerDataAttachmentType.get(player);
			comp.setParryTicks(15);
			
			if (parryingSword.canPerfectParry(activeStack, entity, useTime))
				comp.markForPerfectCounter();
		}
		
		event.setBlockedDamage(event.getBlockedDamage() * parryingSword.getBlockingMultiplier(event.getDamageSource(), activeStack, entity, useTime));
	}
	
	@SubscribeEvent
	private static void onLivingDamagePre(LivingDamageEvent.Pre event) {
		Entity sourceEntity = event.getSource().getEntity();
		
		if(sourceEntity instanceof LivingEntity livingSource && SpectrumCurioItem.hasEquipped(livingSource, SpectrumItems.JEOPARDANT.get())) {
			double jeopardantMod = JeopardantItem.getAttackModifierForWearer(livingSource);
			float newDamage = (float) (event.getNewDamage() * (1+ jeopardantMod));
			event.setNewDamage(newDamage);
		}
		
		if (sourceEntity instanceof LivingEntity livingAttacker) {
			LivingEntity hurtEntity = event.getEntity();
			float newDamage = event.getNewDamage();
			if (newDamage != 0F && hurtEntity.getHealth() == hurtEntity.getMaxHealth()) {
				ItemStack mainHandStack = livingAttacker.getMainHandItem();
				int level = SpectrumEnchantmentHelper.getLevel(livingAttacker.level().registryAccess(), SpectrumEnchantmentKeys.FIRST_STRIKE, mainHandStack);
				if (level > 0) {
					float additionalFirstStrikeDamage = SpectrumConfig.CONFIG.FirstStrikeDamagePerLevel.get().floatValue() * level;
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
				float disarmingChance = disarmingLevel * (hurtEntity instanceof Player ? SpectrumConfig.CONFIG.DisarmingChancePerLevelPlayers.get().floatValue() : SpectrumConfig.CONFIG.DisarmingChancePerLevelMobs.get().floatValue());
				if(level.getRandom().nextFloat() < disarmingChance) {
					disarmEntity(hurtEntity);
				}
			}
		}
		
		// Gemstone Armor
		if (!level.isClientSide()) {
			if (hurtEntity instanceof Mob thisMobEntity) {
				for (ItemStack armorItemStack : thisMobEntity.getArmorSlots()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisMobEntity, event.getNewDamage());
					}
				}
			} else if (hurtEntity instanceof ServerPlayer thisPlayerEntity) {
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorSlots()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisPlayerEntity, event.getNewDamage());
					}
				}
			}
		}
	}
	
	private static void disarmEntity(LivingEntity entity) {
		// since endermen save their carried block as blockState, not in hand
		// we have to use custom logic for them
		if (entity instanceof EnderMan enderman) {
			BlockState carriedBlockState = enderman.getCarriedBlock();
			if (carriedBlockState != null) {
				Item item = carriedBlockState.getBlock().asItem();
				if (item != Items.AIR) {
					enderman.spawnAtLocation(item.getDefaultInstance());
					enderman.setCarriedBlock(null);
				}
			}
			return;
		}
		
		// choose a random slot and drop its content
		List<EquipmentSlot> slots = new ArrayList<>(List.of(EquipmentSlot.values()));
		Collections.shuffle(slots);
		for (EquipmentSlot slot : slots) {
			ItemStack slotStack = entity.getItemBySlot(slot);
			if (slotStack.isEmpty()) {
				continue;
			}
			
			// set to cannot drop? Skip that slot
			if (entity instanceof Mob mobEntity && ((MobEntityAccessor) mobEntity).invokeGetEquipmentDropChance(slot) <= 0) {
				continue;
			}
			
			entity.spawnAtLocation(slotStack);
			entity.setItemSlot(slot, ItemStack.EMPTY);
			entity.level().playSound(null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.NEUTRAL, 1.0F, 1.0F);
			break;
		}
	}
	
	@SubscribeEvent
	private static void playerWakeUp(PlayerWakeUpEvent event) {
		Player player = event.getEntity();
		
		if(player.isAddedToLevel()) {
			MiscPlayerDataAttachmentType.get(player).resetSleepingState(false);
			player.removeEffect(SpectrumMobEffects.SOMNOLENCE);
		}
	}
	
	@SubscribeEvent
	private static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
		if (event.getCurrentGameMode() == GameType.SPECTATOR && event.getNewGameMode() != GameType.SPECTATOR
				&& event.getEntity() instanceof ServerPlayer serverPlayer && HardcoreDeathAttachmentType.hasHardcoreDeath(serverPlayer)) {
			HardcoreDeathAttachmentType.clearHardcoreDeath(serverPlayer);
		}
	}
	
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
		if (!world.isClientSide()) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof LivingEntity livingEntity) {
				boolean protect = false;
				
				MobEffectInstance reboundInstance = livingEntity.getEffect(SpectrumMobEffects.PROJECTILE_REBOUND);
 				if (reboundInstance != null && entity.level().getRandom().nextFloat() < SpectrumMobEffects.PROJECTILE_REBOUND_CHANCE_PER_LEVEL * (reboundInstance.getAmplifier() + 1)) {
					protect = true;
				}
				
				if(!protect && SpectrumCurioItem.hasEquipped(livingEntity, SpectrumItems.PUFF_CIRCLET.get())) {
					AzureDikeAttachmentType azureDikeAttachment = livingEntity.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
					if (azureDikeAttachment.getCurrentCharges() > 0) {
						azureDikeAttachment.absorbDamage(livingEntity, PuffCircletItem.PROJECTILE_DEFLECTION_COST);
						protect = true;
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
	private static void onBlockDrops(BlockDropsEvent event) {
		if(ResonanceProcessor.preventNextXPDrop && EnchantmentHelper.hasTag(event.getTool(), SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			ResonanceProcessor.preventNextXPDrop = false;
			event.setDroppedExperience(0);
		}
	}
	
	@SubscribeEvent
	private static void onBlockBreak(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		Level level = event.getPlayer().level();
		BlockState state = event.getState();
		if (player instanceof ServerPlayer serverPlayerEntity) {
			ItemStack handStack = player.getItemInHand(serverPlayerEntity.getUsedItemHand());
			if (SpectrumEnchantmentHelper.hasEnchantment(player.level().registryAccess(), SpectrumEnchantmentKeys.INERTIA, handStack)) {
				InertiaComponent.onInertiaBlockBreak(level, pos, state, serverPlayerEntity, handStack);
			}
			
			SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, pos, player.getMainHandItem());
		}
		
		ItemStack miningStack =  player.getMainHandItem();
		if(miningStack.getItem() instanceof AoEBreakingTool aoeBreakingTool) {
			aoeBreakingTool.afterBreakingBlock(event.getLevel(), event.getPos(), player, miningStack);
		}
	}
	
	@SubscribeEvent
	private static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
		if(!event.isCanceled()) {
			event.getLevel().gameEvent(SpectrumGameEvents.BLOCK_CHANGED, event.getPos(), GameEvent.Context.of(event.getLevel().getBlockState(event.getPos())));
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
		long time = event.getLevel().dayTime();
		TimeHelper.TimeOfDay timeOfDay = TimeHelper.getTimeOfDay(time);
		if (timeOfDay.isDay()) {
			event.setTimeAddition((time - time % 24000) + 13000L);
		}
	}
	
	@SubscribeEvent
	private static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
		for(EntityType<? extends LivingEntity> et : event.getTypes()) {
			event.add(et, SpectrumEntityAttributes.LOOT_CHANCE_MULTIPLIER);
		}
	}
	
	@SubscribeEvent
	private static void tickGravity(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity.isNoGravity()) {
			return;
		}
		Level level = entity.level();
		
		if(entity instanceof Player player && !player.getAbilities().flying) {
			float appliedGravity = applyGravityBasedOnInventory(player, new InvWrapper(player.getInventory()));
			Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
			if(curiosInventory.isPresent()) {
				appliedGravity += applyGravityBasedOnInventory(player, curiosInventory.get().getEquippedCurios());
			}
			
			// taking flight
			if(level.getGameTime() % 20 == 0 && player instanceof ServerPlayer serverPlayer) {
				if (appliedGravity > 0.081) {
					Support.grantAdvancementCriterion(serverPlayer, "lategame/carry_too_many_low_gravity_blocks", "gravity");
					// unable to jump a full block
				} else if (appliedGravity < -0.025) {
					Support.grantAdvancementCriterion(serverPlayer, "midgame/carry_too_many_heavy_gravity_blocks", "gravity");
				}
			}
			
			// if falling very slowly => reset fall distance / damage
			if (appliedGravity > 0.48) {
				entity.fallDistance = 0;
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
			float appliedGravity = applyGravityBasedOnInventory(horse, new InvWrapper(horse.getInventory()));
			
			// when the animal is sent flying trigger a hidden advancement
			if (appliedGravity > 0.081 && level.getGameTime() % 20 == 0) {
				Player owner = PlayerOwned.getPlayerIfOnline(level, horse.getOwnerUUID());
				if (owner != null) {
					Support.grantAdvancementCriterion((ServerPlayer) owner, "lategame/put_too_many_low_gravity_blocks_into_animal", "gravity");
				}
				
				// take damage when at height heights
				// otherwise the animal would just be floating forever
				if (horse.position().y() > level.getMaxBuildHeight() + 200) {
					horse.hurt(horse.damageSources().fellOutOfWorld(), 10);
				}
			}
			
			// if falling very slowly => reset fall distance / damage
			if (entity.getDeltaMovement().y > -0.4) {
				entity.fallDistance = 0;
			}
		}
	}
	
	/**
	 * This one is for LivingEntities, like players
	 * Makes entities lighter / heavier, depending on the gravity effect of the item stack
	 *
	 * @return The additional Y Velocity that was applied
	 */
	public static float applyGravityBasedOnInventory(LivingEntity entity, IItemHandler itemHandler) {
		if (!entity.isPushable() || entity.isNoGravity() || entity.isSpectator()) {
			return 0;
		}
		
		float appliedGravityThisTick = 0F;
		for(int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			appliedGravityThisTick += stack.getOrDefault(SpectrumDataComponentTypes.GRAVITABLE, 0F) * stack.getCount();
			if(stack.getItem() instanceof GravityRingItem gravityRingItem) {
				appliedGravityThisTick += gravityRingItem.getGravityMod(stack);
			}
		}
		
		// Limit the max push per tick
		// that limit is still mighty high
		appliedGravityThisTick = Math.clamp(appliedGravityThisTick, -0.18F, 0.18F);
		
		if(appliedGravityThisTick != 0) {
			entity.push(0, appliedGravityThisTick, 0);
		}
		
		// if falling very slowly => reset fall distance / damage
		if (appliedGravityThisTick > 0 && entity.getDeltaMovement().y > -0.4) {
			entity.fallDistance /= 2;
		}
		
		return appliedGravityThisTick;
	}
	
	@SubscribeEvent
	private static void modifyBreakSpeed(PlayerEvent.BreakSpeed event) {
		Player player = event.getEntity();
		ItemStack handStack = player.getItemInHand(player.getUsedItemHand());
		Level level = player.level();
		RegistryAccess drm = level.registryAccess();
		BlockState state = event.getState();
		
		// INEXORABLE GAMING
		int inexorableLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.INEXORABLE, handStack);
		if (inexorableLevel > 0) {
			float original = handStack.getDestroySpeed(state);
			event.setNewSpeed(Math.max(original, event.getNewSpeed()));
			return;
		}
		
		// RAZING GAMING
		float defaultDestroyTime = state.getBlock().defaultDestroyTime();
		int razingLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.RAZING, handStack);
		if (defaultDestroyTime > 0 && razingLevel > 0) {
			Tool tool = handStack.get(DataComponents.TOOL);
			if(tool != null && tool.isCorrectForDrops(state)) {
				double razingMultiplier = (razingLevel + 1) * defaultDestroyTime / 16F;
				razingMultiplier = Math.max(1, razingMultiplier);
				event.setNewSpeed((float) (event.getOriginalSpeed() * razingMultiplier));
			}
		}
		
		// INERTIA GAMING
		// inertia mining speed calculation logic is capped
		// Higher values would do weird stuff with the formula
		int inertiaLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.INERTIA, handStack);
		if (inertiaLevel > 0) {
			inertiaLevel = Math.min(4, inertiaLevel);
			var inertia = handStack.getOrDefault(SpectrumDataComponentTypes.INERTIA, InertiaComponent.DEFAULT);
			if (state.is(inertia.lastMined())) {
				double additionalSpeedMultiplier = 0.5 + 2.0 * Math.log(inertia.count()) / Math.log((6 - inertiaLevel) * (6 - inertiaLevel) + 1);
				event.setNewSpeed(event.getNewSpeed() * (float) additionalSpeedMultiplier);
			} else {
				event.setNewSpeed(event.getNewSpeed() / 4);
			}
		}
	}
	
	@SubscribeEvent
	private static void addPackFinders(AddPackFindersEvent event) {
		if (event.getPackType() == PackType.CLIENT_RESOURCES) {
			IModFile modFile = ModList.get().getModFileById(SpectrumCommon.MOD_ID).getFile();
			
			event.addRepositorySource(packConsumer -> {
				packConsumer.accept(
					Pack.readMetaAndCreate(
						new PackLocationInfo("spectrum_alternate", Component.literal("Alternate Spectrum Textures"), PackSource.BUILT_IN, Optional.empty()),
						new PathPackResources.PathResourcesSupplier(modFile.findResource("resourcepacks", "spectrum_alternate")),
						PackType.CLIENT_RESOURCES,
						new PackSelectionConfig(false, Pack.Position.TOP, false)
					)
				);
				
				packConsumer.accept(
					Pack.readMetaAndCreate(
						new PackLocationInfo("spectrum_programmer_art", Component.literal("Spectrum Programmer Art"), PackSource.BUILT_IN, Optional.empty()),
						new PathPackResources.PathResourcesSupplier(modFile.findResource("resourcepacks", "spectrum_programmer_art")),
						PackType.CLIENT_RESOURCES,
						new PackSelectionConfig(false, Pack.Position.TOP, false)
					)
				);
			});
		}
	}
	
}
