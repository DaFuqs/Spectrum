package de.dafuqs.spectrum.registries;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.entity.spawners.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
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
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.loading.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.*;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.event.tick.*;
import org.jetbrains.annotations.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

import java.util.*;
import java.util.concurrent.atomic.*;

@EventBusSubscriber(modid = SpectrumCommon.MOD_ID)
public class SpectrumEventListeners {
	
	
	/**
	 * Caches the luminance states from fluids as int
	 * for blocks that react to the light level of fluids
	 * like the fusion shrine lighting up with lava or liquid crystal
	 */
	public static final HashMap<Fluid, Integer> fluidLuminance = new HashMap<>();
	
	
	 // I'm putting all event listeners here, they can be moved later so nbd
	
	@SubscribeEvent
	public InteractionResult exchangeBlock(PlayerInteractEvent.LeftClickBlock event) {
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
	public void resetColorProviders(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			SpectrumColorProviders.resetToggleableProviders();
		}
	}
	
	@SubscribeEvent
	public void handleInertia(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		Level level = event.getPlayer().level();
		BlockState state = event.getState();
		if (player instanceof ServerPlayer serverPlayerEntity) {
			ItemStack handStack = player.getItemInHand(serverPlayerEntity.getUsedItemHand());
			if (SpectrumEnchantmentHelper.hasEnchantment(player.level().registryAccess(), SpectrumEnchantments.INERTIA, handStack)) {
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
	public InteractionResult triggerPrioritizedEntityInteraction(PlayerInteractEvent.EntityInteract event){
		
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
	public InteractionResult triggerPrioritizedBlockInteraction(PlayerInteractEvent.RightClickBlock event) {
		
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
	public void triggerWhispyCirclet(PlayerWakeUpEvent event) {
		Player player = event.getEntity();
		
		if(player.getSleepTimer() == 100 && SpectrumTrinketItem.hasEquipped(player, SpectrumItems.WHISPY_CIRCLET.asItem())) {
			player.setHealth(player.getMaxHealth());
			WhispyCircletItem.removeNegativeStatusEffects(player);
		}
		
	}
	
	@SubscribeEvent
	public void triggerJeopardantKillCriterion(LivingDeathEvent event) {
		Entity player = event.getSource().getEntity();
		LivingEntity target = event.getEntity();
		
		if(player instanceof ServerPlayer && SpectrumTrinketItem.hasEquipped((LivingEntity) player, SpectrumItems.JEOPARDANT.asItem())) {
			SpectrumAdvancementCriteria.JEOPARDANT_KILL.trigger((ServerPlayer) player, target);
		}
	}
	
	@SubscribeEvent
	public void tickSpawners(LevelTickEvent.Pre event) {
		Level level =  event.getLevel();
		ServerLevel world = level.getServer().getLevel(level.dimension());
		
		if(!world.tickRateManager().runsNormally()) {
			return;
		}
		
		if (world.getGameTime() % 100 == 0 && !world.isClientSide) {
			if (TimeHelper.getTimeOfDay(world).isNight()) { // 90 chances in a night
				if (SpectrumCommon.CONFIG.ShootingStarWorlds.contains(world.dimension().location().toString())) {
					ShootingStarSpawner.INSTANCE.tick((ServerLevel) world, true, true);
				}
			}
				
			/* TODO: Monstrosity
			if (world.getRegistryKey() == SpectrumDimensions.DIMENSION_KEY) {
				MonstrositySpawner.INSTANCE.spawn(world, true, true);
			}*/
		}
		
	}
	
	@SubscribeEvent
	public void updateFluidLuminance(ServerStartedEvent event) {
		SpectrumCommon.logInfo("Querying fluid luminance...");
		for (Iterator<Block> it = BuiltInRegistries.BLOCK.stream().iterator(); it.hasNext(); ) {
			Block block = it.next();
			if (block instanceof LiquidBlock fluidBlock) {
				fluidLuminance.put(fluidBlock.fluid, fluidBlock.defaultBlockState().getLightEmission());
			}
		}
	}
	
	@SubscribeEvent
	public void injectDynamicRecipe(ServerStartedEvent event) {
		MinecraftServer server = event.getServer();
		
		SpectrumCommon.logInfo("Injecting dynamic recipes into recipe manager...");
		FirestarterIdolBlock.addBlockSmeltingRecipes(server);
	}
	
	@SubscribeEvent
	public void tickPastelNetwork(ServerTickEvent.Post event) {
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
	public void damagePlayersOutOfBoundsInDD(PlayerTickEvent.Post event) {
		if(event.getEntity() instanceof ServerPlayer player) {
			Level world = player.level();
			if (!player.isCreative() && !player.isSpectator() && world.dimension() == SpectrumDimensions.DIMENSION_KEY && player.getY() > world.getMaxBuildHeight()) {
				player.hurt(player.damageSources().fellOutOfWorld(), 10.0F);
				if (player.isDeadOrDying()) {
					Support.grantAdvancementCriterion(player, "lategame/get_killed_while_out_of_deeper_down_bounds", "get_rekt");
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEquipmentChange(LivingEquipmentChangeEvent event) {
		var livingEntity = event.getEntity();
		var oldEquipment = event.getFrom();
		var newEquipment = event.getTo();
		var equipmentSlot = event.getSlot();
		
		var oldInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.level().registryAccess(), SpectrumEnchantments.INEXORABLE, oldEquipment);
		var newInexorable = SpectrumEnchantmentHelper.getLevel(livingEntity.level().registryAccess(), SpectrumEnchantments.INEXORABLE, newEquipment);
		
		var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumAttributeTags.INEXORABLE_ARMOR_EFFECTIVE : SpectrumAttributeTags.INEXORABLE_HANDHELD_EFFECTIVE;
		
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
	public void onLivingDeath(LivingDeathEvent event) {
		var killedEntity = event.getEntity();
		var damageSource = event.getSource();
		
		if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return;
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
		
		if (event.getEntity() instanceof ServerPlayer player) {
			if (player.level().getLevelData().isHardcore() || HardcoreDeathAttachmentType.isInHardcore(player)) {
				HardcoreDeathAttachmentType.setHardcoreDeath(player);
			}
			evaluateAndDropPlayerHead(player, event.getSource());
		}
	}
	
	@SubscribeEvent
	private static void preventFireDamage(LivingIncomingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		DamageSource source = event.getSource();
		
		// If the player is damaged by lava and wears an ashen circlet:
		// prevent damage and grant fire resistance
		if (source.is(DamageTypes.LAVA)) {
			Optional<ItemStack> ashenCircletStack = SpectrumTrinketItem.getFirstEquipped(entity, SpectrumItems.ASHEN_CIRCLET.get());
			if (ashenCircletStack.isPresent()) {
				if (AshenCircletItem.getCooldownTicks(ashenCircletStack.get(), entity.level()) == 0) {
					AshenCircletItem.grantFireResistance(ashenCircletStack.get(), entity);
					event.setCanceled(true);
				}
			}
		} else if (source.is(DamageTypeTags.IS_FIRE) && SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.ASHEN_CIRCLET.get())) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	private static void canPlayerSleep(CanPlayerSleepEvent event) {
		var player = event.getEntity();
		var reason = event.getProblem();
		
		if (reason != Player.BedSleepingProblem.NOT_POSSIBLE_NOW && MiscPlayerDataAttachmentType.get(player).isSleeping()) {
			event.setProblem(null);
		} else if ((reason == Player.BedSleepingProblem.NOT_POSSIBLE_NOW || reason == Player.BedSleepingProblem.NOT_SAFE) && player.hasEffect(SpectrumStatusEffects.SOMNOLENCE)) {
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
