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
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
				if (strikeLightning(hitResult.getEntity().getWorld(), hitResult.getEntity().getBlockPos())) {
					stack.decrement(1);
				}
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
				if (strikeLightning(projectile.getWorld(), hitResult.getBlockPos())) {
					stack.decrement(1);
				}
				return stack;
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
			public boolean destroyItemOnHit() {
				return false;
			}

			@Override
			public boolean dealDamage(ThrownItemEntity projectile, Entity owner, Entity target) {
				return target.damage(target.getDamageSources().thrown(projectile, owner), 6F);
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				World world = projectile.getWorld();
				BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
				if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity && !blockEntity.isRemoved()) {
					ItemStack currentStack = jukeboxBlockEntity.getStack(0);
					if (!currentStack.isEmpty()) {
						jukeboxBlockEntity.dropRecord();
					}
					jukeboxBlockEntity.setStack(stack.copy());
					stack.decrement(1);
				}
				return stack;
			}
		}, ItemTags.MUSIC_DISCS);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity entity = hitResult.getEntity();
				if (!entity.isFireImmune()) {
					entity.setOnFireFor(15);
					if (entity.damage(entity.getDamageSources().inFire(), 4.0F)) {
						entity.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + entity.getWorld().getRandom().nextFloat() * 0.4F);
					}
					stack.decrement(1);
				}
				return stack;
			}
		}, Items.FIRE_CHARGE);

		ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				IncandescentAmalgamBlock.explode(projectile.getWorld(), BlockPos.ofFloored(hitResult.getPos()), owner, stack);
				stack.decrement(1);
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				IncandescentAmalgamBlock.explode(projectile.getWorld(), BlockPos.ofFloored(hitResult.getPos()), owner, stack);
				stack.decrement(1);
				return stack;
			}
		}, SpectrumBlocks.INCANDESCENT_AMALGAM.asItem());

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity target = hitResult.getEntity();
				List<ItemStack> equipment = new ArrayList<>();
				target.getItemsEquipped().forEach(equipment::add);
				Collections.shuffle(equipment);
				
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
				
				boolean success = false;
				for (ItemStack equip : equipment) {
					for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
						Pair<Boolean, ItemStack> result = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(equip, enchantment.getKey(), enchantment.getValue(), false, false);
						if (success || result.getLeft()) {
							success = true;
						} else {
							break;
						}
					}
				}
				if (success) {
					stack.decrement(1);
				}
				return stack;
			}
		}, Items.ENCHANTED_BOOK);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Entity target = hitResult.getEntity();
				List<ItemStack> equipment = new ArrayList<>();
				target.getItemsEquipped().forEach(equipment::add);
				Collections.shuffle(equipment);
				
				for (ItemStack equip : equipment) {
					if (EnchantmentCanvasItem.tryExchangeEnchantments(stack, equip, target)) {
						return stack;
					}
				}
				return stack;
			}
		}, SpectrumItems.ENCHANTMENT_CANVAS);

		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				if (hitResult.getEntity() instanceof PlayerEntity target) {
					int playerExperience = target.totalExperience;
					if (playerExperience > 0) {
						KnowledgeGemItem item = (KnowledgeGemItem) stack.getItem();
						long transferableExperiencePerTick = item.getTransferableExperiencePerTick(stack);
						int xpToTransfer = (int) Math.min(target.totalExperience, transferableExperiencePerTick * 100);
						int experienceOverflow = ExperienceStorageItem.addStoredExperience(stack, xpToTransfer);
						
						target.addExperience(-xpToTransfer + experienceOverflow);
						target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3F, 0.8F + target.getWorld().getRandom().nextFloat() * 0.4F);
						return stack;
					}
				}
				return stack;
			}
		}, SpectrumItems.KNOWLEDGE_GEM);
		
		ItemProjectileBehavior.register(new ItemProjectileBehavior.Default() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				Recipe<?> recipe = CraftingTabletItem.getStoredRecipe(projectile.getWorld(), stack);
				if (recipe instanceof CraftingRecipe craftingRecipe && hitResult.getEntity() instanceof ServerPlayerEntity target) {
					CraftingTabletItem.tryCraftRecipe(target, craftingRecipe);
				}
				return stack;
			}
		}, SpectrumItems.CRAFTING_TABLET);
		
		/*ItemProjectileBehavior.register(new ItemProjectileBehavior() {
			@Override
			public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult) {
				return stack;
			}
			
			@Override
			public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult) {
				OmniAcceleratorProjectile projectile = OmniAcceleratorProjectile.get(stack);
				World world = projectile.getWorld();
				if(projectile.fireProjectile(stack, owner, world)) {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), projectile.getSoundEffect(), SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
					if(!player.isCreative()) {
						OmniAcceleratorItem.decrementFirstItem(stack);
					}
				}
				
				return stack;
			}
		}, SpectrumItems.OMNI_ACCELERATOR);*/ // TODO
	}

}
