package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class MalachiteBidentItem extends TridentItem implements Preenchanted {

	public MalachiteBidentItem(Settings settings) {
		super(settings);
	}

	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.IMPALING, 6);
	}

	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
			if (useTime >= 10) {
				int riptideLevel = EnchantmentHelper.getRiptide(stack);
				if (riptideLevel <= 0 || playerEntity.isTouchingWaterOrRain()) {
					if (!world.isClient) {
						stack.damage(1, playerEntity, (p) -> {
							p.sendToolBreakStatus(user.getActiveHand());
						});
						if (riptideLevel == 0) {
							throwBident(stack, world, playerEntity, (float) riptideLevel);
						}
					}

					playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
					if (riptideLevel > 0) {
						riptide(world, playerEntity, riptideLevel);
					}

				}
			}
		}
	}

	private static void riptide(World world, PlayerEntity playerEntity, int riptideLevel) {
		float f = playerEntity.getYaw();
		float g = playerEntity.getPitch();
		float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
		float k = -MathHelper.sin(g * 0.017453292F);
		float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
		float m = MathHelper.sqrt(h * h + k * k + l * l);
		float n = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
		h *= n / m;
		k *= n / m;
		l *= n / m;
		playerEntity.addVelocity(h, k, l);
		playerEntity.useRiptide(20);
		if (playerEntity.isOnGround()) {
			playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.2, 0.0));
		}

		SoundEvent soundEvent;
		if (riptideLevel >= 3) {
			soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
		} else if (riptideLevel == 2) {
			soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
		} else {
			soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
		}

		world.playSoundFromEntity(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}

	private static void throwBident(ItemStack stack, World world, PlayerEntity playerEntity, float j) {
		BidentEntity bidentEntity = new BidentEntity(world);
		bidentEntity.setStack(stack);
		bidentEntity.setOwner(playerEntity);
		bidentEntity.updatePosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
		bidentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F + j * 0.5F, 1.0F);
		if (playerEntity.getAbilities().creativeMode) {
			bidentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}

		world.spawnEntity(bidentEntity);
		world.playSoundFromEntity(null, bidentEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if (!playerEntity.getAbilities().creativeMode) {
			playerEntity.getInventory().removeOne(stack);
		}
	}

}
