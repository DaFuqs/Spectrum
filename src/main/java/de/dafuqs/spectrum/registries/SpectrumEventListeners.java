package de.dafuqs.spectrum.registries;

import com.google.common.collect.*;
import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.entity.spawners.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.registries.client.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
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
				} else if (mainHandStack.getItem() instanceof TuningStampItem tuningStampItem) {
					if (mainHandStack.getOrCreateNbt().contains(TuningStampItem.DATA))
						tuningStampItem.clearData(Optional.of(player), mainHandStack);
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
				SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
			}
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
					fluidLuminance.put(fluidBlock.getFluidState(fluidBlock.getDefaultState()).getFluid(), fluidBlock.getDefaultState().getLuminance());
				}
			}
			
			SpectrumCommon.logInfo("Injecting dynamic recipes into recipe manager...");
			FirestarterIdolBlock.addBlockSmeltingRecipes(server);
			injectEnchantmentUpgradeRecipes(server);
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
		ServerLivingEntityEvents.MOB_CONVERSION.register((previous, converted, keepEquipment) -> {
			if (EverpromiseRibbonComponent.hasRibbon(previous)) {
				EverpromiseRibbonComponent.attachRibbon(converted);
			}
		});
		
		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
			var oldInexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, previousStack);
			var newInexorable = EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, currentStack);
			
			var effectType = equipmentSlot == EquipmentSlot.CHEST ? SpectrumAttributeTags.INEXORABLE_ARMOR_EFFECTIVE : SpectrumAttributeTags.INEXORABLE_HANDHELD_EFFECTIVE;
			
			if (oldInexorable > 0 && newInexorable <= 0) {
				livingEntity.getStatusEffects()
						.stream()
						.filter(instance -> {
							var statusEffect = instance.getEffectType();
							var attributes = statusEffect.getAttributeModifiers().keySet();
							return attributes.stream()
									.anyMatch(attribute -> {
										var attributeRegistryOptional = Registries.ATTRIBUTE.getEntryList(effectType);
										
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
		
		ModifyItemAttributeModifiersCallback.EVENT.register((stack, slot, attributeModifiers) -> {
			if (slot == EquipmentSlot.MAINHAND) {
				int tightGripLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.TIGHT_GRIP, stack);
				if (tightGripLevel > 0) {
					float attackSpeedBonus = tightGripLevel * SpectrumCommon.CONFIG.TightGripAttackSpeedBonusPercentPerLevel;
					EntityAttributeModifier mod = new EntityAttributeModifier(UUID.fromString("b09d9b57-eefb-4499-9150-5d8d3e644a40"), "Tight Grip modifier", attackSpeedBonus, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
					attributeModifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, mod);
				}
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
			if (entity instanceof PlayerEntity player) {
				if (entity.getWorld().getLevelProperties().isHardcore() || HardcoreDeathComponent.isInHardcore(player)) {
					HardcoreDeathComponent.addHardcoreDeath(player.getGameProfile());
				}
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
					injectEnchantmentUpgradeRecipes(SpectrumCommon.minecraftServer);
					FirestarterIdolBlock.addBlockSmeltingRecipes(SpectrumCommon.minecraftServer);
				}
			}
			
			@Override
			public Identifier getFabricId() {
				return id;
			}
		});
	}
	
	// It could have been so much easier and performant, but KubeJS overrides the ENTIRE recipe manager
	// and cancels all sorts of functions at HEAD unconditionally, so Spectrum cannot mixin into it
	public static void injectEnchantmentUpgradeRecipes(MinecraftServer minecraftServer) {
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
	
	public static int getFluidLuminance(Fluid fluid) {
		return fluidLuminance.getOrDefault(fluid, 0);
	}
	
}
