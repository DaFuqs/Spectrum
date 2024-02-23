package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.boom.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.block.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumItemProjectileBehaviors {

	public static void register() {
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
				return strikeLightning(hitResult.getEntity().getWorld(), hitResult.getEntity().getBlockPos());
			}

			@Override
			public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
				return strikeLightning(projectile.getWorld(), hitResult.getBlockPos());
			}

			private boolean strikeLightning(World world, BlockPos pos) {
				if (world.isSkyVisible(pos.up())) {
					LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
					if (lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
						world.spawnEntity(lightningEntity);
						return true;
					}
				}
				return false;
			}
		}, SpectrumItems.STORM_STONE);

		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(4F, true), SpectrumItemTags.GEMSTONE_SHARDS);
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(6F, true), Items.POINTED_DRIPSTONE);
		ItemProjectileBehavior.register(ItemProjectileBehavior.damaging(6F, true), Items.END_ROD);
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Damaging() {

			@Override
			public boolean shouldDestroyItemOnHit() {
				return false;
			}

			@Override
			public boolean dealDamage(ThrownItemEntity projectile, Entity owner, Entity target) {
				return target.damage(target.getDamageSources().thrown(projectile, owner), 6F);
			}

			@Override
			public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				World world = projectile.getWorld();
				BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
				if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity && !blockEntity.isRemoved()) {
					ItemStack currentStack = jukeboxBlockEntity.getStack(0);
					if (!currentStack.isEmpty()) {
						jukeboxBlockEntity.dropRecord();
					}
					jukeboxBlockEntity.setStack(stack.copy());
					stack.setCount(0);
				}
				return true;
			}
		}, ItemTags.MUSIC_DISCS);
		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity target = hitResult.getEntity();
				for (ItemStack equippedStack : target.getItemsEquipped()) {
					if (!equippedStack.isEmpty() && equippedStack.getItem() instanceof ElytraItem) {
						// YEET
						Vec3d vec3d = target.getRotationVector();
						Vec3d vec3d2 = target.getVelocity();
						target.setVelocity(vec3d2.add(
								vec3d.x * 0.1 + (vec3d.x * 1.5 - vec3d2.x) * 0.5,
								vec3d.y * 0.1 + (vec3d.y * 1.5 - vec3d2.y) * 0.5 + 5.0,
								vec3d.z * 0.1 + (vec3d.z * 1.5 - vec3d2.z) * 0.5)
						);
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				World world = projectile.getWorld();
				Vec3d hitPos = hitResult.getPos();
				Direction direction = hitResult.getSide();
				FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(
						world,
						owner,
						hitPos.x + (double) direction.getOffsetX() * 0.15,
						hitPos.y + (double) direction.getOffsetY() * 0.15,
						hitPos.z + (double) direction.getOffsetZ() * 0.15,
						stack
				);
				fireworkRocketEntity.setVelocity(projectile.getVelocity());
				return world.spawnEntity(fireworkRocketEntity);
			}
		}, Items.FIREWORK_ROCKET);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity entity = hitResult.getEntity();
				if (!entity.isFireImmune()) {
					entity.setOnFireFor(15);
					if (entity.damage(entity.getDamageSources().inFire(), 4.0F)) {
						entity.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + entity.getWorld().getRandom().nextFloat() * 0.4F);
					}
					return true;
				}
				return false;
			}
		}, Items.FIRE_CHARGE);

		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				IncandescentAmalgamBlock.explode(projectile.getWorld(), BlockPos.ofFloored(hitResult.getPos()), owner, stack);
				return true;
			}

			@Override
			public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				IncandescentAmalgamBlock.explode(projectile.getWorld(), BlockPos.ofFloored(hitResult.getPos()), owner, stack);
				return true;
			}
		}, SpectrumBlocks.INCANDESCENT_AMALGAM.asItem());

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity target = hitResult.getEntity();
				List<ItemStack> equipment = new ArrayList<>();
				target.getItemsEquipped().forEach(equipment::add);
				Collections.shuffle(equipment);

				Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);

				boolean success = false;
				for (ItemStack equip : equipment) {
					for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
						Pair<Boolean, ItemStack> result = SpectrumEnchantmentHelper.addOrExchangeEnchantment(equip, enchantment.getKey(), enchantment.getValue(), false, false);
						if (success || result.getLeft()) {
							success = true;
						} else {
							break;
						}
					}
				}
				return success;
			}
		}, Items.ENCHANTED_BOOK);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity target = hitResult.getEntity();
				List<ItemStack> equipment = new ArrayList<>();
				target.getItemsEquipped().forEach(equipment::add);
				Collections.shuffle(equipment);

				for (ItemStack equip : equipment) {
					if(EnchantmentCanvasItem.tryExchangeEnchantments(stack, equip, target)) {
						return true;
					}
				}
				return false;
			}
		}, SpectrumItems.ENCHANTMENT_CANVAS);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if(hitResult.getEntity() instanceof PlayerEntity target) {
					int playerExperience = target.totalExperience;
					if(playerExperience > 0) {
						KnowledgeGemItem item = (KnowledgeGemItem) stack.getItem();
						long transferableExperiencePerTick = item.getTransferableExperiencePerTick(stack);
						int xpToTransfer = (int) Math.min(target.totalExperience, transferableExperiencePerTick * 100);
						int experienceOverflow = ExperienceStorageItem.addStoredExperience(stack, xpToTransfer);

						target.addExperience(-xpToTransfer + experienceOverflow);
						target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3F, 0.8F + target.getWorld().getRandom().nextFloat() * 0.4F);
						return false;
					}
				}
				return false;
			}
		}, SpectrumItems.KNOWLEDGE_GEM);

		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				return false;
			}

			@Override
			public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				return false;
			}
		}, SpectrumItems.OMNI_ACCELERATOR);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity
					owner, EntityHitResult hitResult) {
				Recipe<?> recipe = CraftingTabletItem.getStoredRecipe(projectile.getWorld(), stack);
				if (recipe instanceof CraftingRecipe craftingRecipe && hitResult.getEntity() instanceof ServerPlayerEntity target) {
					CraftingTabletItem.tryCraftRecipe(target, craftingRecipe);
					return true;
				}
				return false;
			}
		}, SpectrumItems.CRAFTING_TABLET);
	}

}
