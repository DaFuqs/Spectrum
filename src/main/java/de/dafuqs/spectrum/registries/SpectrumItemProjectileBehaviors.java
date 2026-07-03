package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.boom.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.magic_items.ampoules.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.*;
import org.jspecify.annotations.Nullable;
import vazkii.botania.common.helper.*;

import java.util.*;
import java.util.function.*;

public class SpectrumItemProjectileBehaviors {
	
	public static void register() {
		registerHarmless();
		if (SpectrumConfig.CONFIG.OmniAcceleratorPvP.get()) {
			registerPvP();
		}
	}
	
	protected static void registerHarmless() {
		// The code for consuming potions is written so it never gets consumed if a non-player drinks it
		// Thank you, witches and wandering traders
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
				Entity hitEntity = hitResult.getEntity();
				;
				if (hitEntity instanceof LivingEntity livingEntity) {
					stack.finishUsingItem(projectile.level(), livingEntity);
					return new ItemStack(Items.GLASS_BOTTLE);
				}
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
				return stack;
			}
		}, Items.POTION);
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
				if (strikeLightning(hitResult.getEntity().level(), hitResult.getEntity().blockPosition())) {
					stack.shrink(1);
				}
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
				if (strikeLightning(projectile.level(), hitResult.getBlockPos())) {
					stack.shrink(1);
				}
				return stack;
			}
			
			private boolean strikeLightning(Level world, BlockPos pos) {
				if (world.canSeeSky(pos.above())) {
					LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
					if (lightningEntity != null) {
						lightningEntity.moveTo(Vec3.atBottomCenterOf(pos));
						world.addFreshEntity(lightningEntity);
						return true;
					}
				}
				return false;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.STORM_STONE.get()).build());
		
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(4F, true), SpectrumItemTags.GEMSTONE_SHARDS);
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(6F, true), Items.POINTED_DRIPSTONE);
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(6F, true), Items.END_ROD);
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(6F, true), Items.BLAZE_ROD);
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(8F, true), () -> ItemPredicate.Builder.item().of(SpectrumItems.STAR_FRAGMENT.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Damaging() {
			
			@Override
			public boolean destroyItemOnHit() {
				return false;
			}
			
			@Override
			public boolean dealDamage(ThrowableItemProjectile projectile, Entity owner, Entity target) {
				return target.hurt(target.damageSources().thrown(projectile, owner), 6F);
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				Level world = projectile.level();
				BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
				if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity && !blockEntity.isRemoved()) {
					ItemStack currentStack = jukeboxBlockEntity.getItem(0);
					if (!currentStack.isEmpty()) {
						jukeboxBlockEntity.popOutTheItem();
					}
					jukeboxBlockEntity.setTheItem(stack.copy());
					stack.shrink(1);
				}
				return stack;
			}
		}, Tags.Items.MUSIC_DISCS);
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity entity = hitResult.getEntity();
				if (!entity.fireImmune()) {
					entity.igniteForSeconds(15);
					if (entity.hurt(entity.damageSources().inFire(), 4.0F)) {
						entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + entity.level().getRandom().nextFloat() * 0.4F);
					}
					stack.shrink(1);
				}
				return stack;
			}
		}, Items.FIRE_CHARGE);
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				IncandescentAmalgamBlock.explode(projectile.level(), BlockPos.containing(hitResult.getLocation()), owner, stack);
				stack.shrink(1);
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				IncandescentAmalgamBlock.explode(projectile.level(), BlockPos.containing(hitResult.getLocation()), owner, stack);
				stack.shrink(1);
				return stack;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumBlocks.INCANDESCENT_AMALGAM.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack accelerator, @Nullable Entity owner, BlockHitResult hitResult) {
				Optional<ItemStack> optionalAcceleratorContentStack = OmniAcceleratorItem.getFirstStack(projectile.level().registryAccess(), accelerator);
				if (optionalAcceleratorContentStack.isPresent() && owner instanceof LivingEntity livingOwner) {
					ItemStack acceleratorContentStack = optionalAcceleratorContentStack.get();
					
					Level world = projectile.level();
					OmniAcceleratorProjectile newProjectile = OmniAcceleratorProjectile.get(optionalAcceleratorContentStack.get());
					Entity newEntity = newProjectile.createProjectile(acceleratorContentStack, livingOwner, world, accelerator);
					
					if (newEntity != null) {
						Vec3 pos = hitResult.getLocation();
						newEntity.setPosRaw(pos.x(), pos.y(), pos.z());
						OmniAcceleratorProjectile.setVelocity(newEntity, projectile, 20, world.getRandom().nextFloat() * 360, 0.0F, 2.0F, 1.0F);
						world.playSound(null, pos.x(), pos.y(), pos.z(), newProjectile.getSoundEffect(), SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
						OmniAcceleratorItem.decrementFirstItem(accelerator);
					}
				}
				return accelerator;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.OMNI_ACCELERATOR.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity target = hitResult.getEntity();
				if (target instanceof LivingEntity livingTarget) {
					livingTarget.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 0));
					livingTarget.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
				}
				stack.shrink(1);
				return stack;
			}
		}, Items.CAKE);
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if (MemoryBlockEntity.manifest((ServerLevel) projectile.level(), hitResult.getEntity().blockPosition(), stack, owner == null ? null : owner.getUUID())) {
					stack.shrink(1);
				}
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				if (MemoryBlockEntity.manifest((ServerLevel) projectile.level(), hitResult.getBlockPos().relative(hitResult.getDirection()), stack, owner == null ? null : owner.getUUID())) {
					stack.shrink(1);
				}
				return stack;
			}
			
		}, () -> ItemPredicate.Builder.item().of(SpectrumBlocks.MEMORY.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				PipeBombItem.prime(stack, projectile.level(), projectile.position(), owner);
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				PipeBombItem.prime(stack, projectile.level(), projectile.position(), owner);
				return stack;
			}
			
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.PIPE_BOMB.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if (projectile.getOwner() instanceof LivingEntity livingOwner && hitResult.getEntity() instanceof LivingEntity livingTarget && ((GlassAmpouleItem) stack.getItem()).trigger(projectile.level(), stack, livingOwner, livingTarget, hitResult.getLocation())) {
					stack.shrink(1);
				}
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				if (projectile.getOwner() instanceof LivingEntity livingOwner && ((GlassAmpouleItem) stack.getItem()).trigger(projectile.level(), stack, livingOwner, null, hitResult.getLocation())) {
					stack.shrink(1);
				}
				return stack;
			}
			
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.AZURITE_GLASS_AMPOULE.get(), SpectrumItems.MALACHITE_GLASS_AMPOULE.get(), SpectrumItems.BLOODSTONE_GLASS_AMPOULE.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if(hitResult.getEntity() instanceof LivingEntity livingEntity) {
					int durationTicks = (int) (10 * Support.logBase(1.05, 1 + stack.getCount()));
					PrimordialFireAttachmentType.addPrimordialFireTicks(livingEntity, durationTicks);
					
					return ItemStack.EMPTY;
				}
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				Level world = projectile.level();
				BlockPos hitPos = hitResult.getBlockPos();
				Direction facing = hitResult.getDirection().getOpposite();
				BlockPos placementPos = hitPos.relative(facing.getOpposite());
				Direction placementDirection = world.isEmptyBlock(placementPos.below()) ? facing : Direction.UP;
				
				if (PrimordialFireBlock.canBePlacedAt(world, placementPos, placementDirection)) {
					BlockState primordialFireState = SpectrumBlocks.PRIMORDIAL_FIRE.get().getStateForPosition(world, placementPos, facing);
					world.setBlock(placementPos, primordialFireState, 11);
					world.gameEvent(owner, GameEvent.BLOCK_PLACE, placementPos);
					
					stack.shrink(1);
				}
				return stack;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.DOOMBLOOM_SEED.get()).build());
	}
	
	protected static void registerPvP() {
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if (hitResult.getEntity() instanceof LivingEntity livingTarget) {
					List<ItemStack> equipment = new ArrayList<>();
					livingTarget.getAllSlots().forEach(equipment::add);
					Collections.shuffle(equipment);
					
					for (ItemStack equip : equipment) {
						if (EnchantmentCanvasItem.tryExchangeEnchantments(stack, equip, livingTarget)) {
							return stack;
						}
					}
				}
				return stack;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.ENCHANTMENT_CANVAS.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if (hitResult.getEntity() instanceof Player target) {
					int playerExperience = target.totalExperience;
					if (playerExperience > 0) {
						KnowledgeGemItem item = (KnowledgeGemItem) stack.getItem();
						long transferableExperiencePerTick = item.getTransferableExperiencePerTick(target.level().registryAccess(), stack);
						int xpToTransfer = (int) Math.min(target.totalExperience, transferableExperiencePerTick * 100);
						int experienceOverflow = ExperienceStorageItem.addStoredExperience(target.level().registryAccess(), stack, xpToTransfer);
						
						target.giveExperiencePoints(-xpToTransfer + experienceOverflow);
						target.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.3F, 0.8F + target.level().getRandom().nextFloat() * 0.4F);
						return stack;
					}
				}
				return stack;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.KNOWLEDGE_GEM.get()).build());
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				RecipeHolder<?> storedRecipe = CraftingTabletItem.getStoredRecipe(projectile.level(), stack);
				if(storedRecipe == null) {
					return stack;
				}
				var recipe = storedRecipe.value();
				if (recipe instanceof CraftingRecipe craftingRecipe && hitResult.getEntity() instanceof ServerPlayer target) {
					CraftingTabletItem.tryCraftRecipe(target, craftingRecipe, projectile.level());
				}
				return stack;
			}
		}, () -> ItemPredicate.Builder.item().of(SpectrumItems.CRAFTING_TABLET.get()).build());
	}
	
}
