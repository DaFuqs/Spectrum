package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class MalachiteBidentItem extends TridentItem implements Preenchanted {
	
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	
	public MalachiteBidentItem(Settings settings) {
		super(settings);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 12.0, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -2.6, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
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
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
			return TypedActionResult.fail(itemStack);
		} else if (!requiresRainForRiptide()) {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		} else if (EnchantmentHelper.getRiptide(itemStack) > 0 && !user.isTouchingWaterOrRain()) {
			return TypedActionResult.fail(itemStack);
		} else {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		}
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
			if (useTime >= 10) {
				playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
				
				int riptideLevel = getRiptideLevel(stack);
				if (riptideLevel > 0 && (playerEntity.isTouchingWaterOrRain() || !requiresRainForRiptide())) {
					riptide(world, playerEntity, riptideLevel);
				} else if (!world.isClient) {
					stack.damage(1, playerEntity, (p) -> {
						p.sendToolBreakStatus(user.getActiveHand());
					});
					throwBident(stack, (ServerWorld) world, playerEntity);
				}
			}
		}
	}
	
	public int getRiptideLevel(ItemStack stack) {
		return Math.max(EnchantmentHelper.getRiptide(stack), getBuiltinRiptideLevel());
	}
	
	protected void riptide(World world, PlayerEntity playerEntity, int riptideLevel) {
		yeetPlayer(playerEntity, (float) riptideLevel);
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
	
	protected void yeetPlayer(PlayerEntity playerEntity, float riptideLevel) {
		float f = playerEntity.getYaw();
		float g = playerEntity.getPitch();
		float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
		float k = -MathHelper.sin(g * 0.017453292F);
		float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
		float m = MathHelper.sqrt(h * h + k * k + l * l);
		float n = 3.0F * ((1.0F + riptideLevel) / 4.0F);
		h *= n / m;
		k *= n / m;
		l *= n / m;
		playerEntity.addVelocity(h, k, l);
	}
	
	protected void throwBident(ItemStack stack, ServerWorld world, PlayerEntity playerEntity) {
		boolean mirrorImage = isThrownAsMirrorImage(stack, world, playerEntity);
		
		BidentEntity bidentEntity = mirrorImage ? new BidentMirrorImageEntity(world) : new BidentEntity(world);
		bidentEntity.setStack(stack);
		bidentEntity.setOwner(playerEntity);
		bidentEntity.updatePosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
		bidentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, getThrowSpeed(), 1.0F);
		if (!mirrorImage && playerEntity.getAbilities().creativeMode) {
			bidentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
		
		world.spawnEntity(bidentEntity);
		SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THROW;
		if (mirrorImage) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, bidentEntity.getPos(), SpectrumParticleTypes.MIRROR_IMAGE, 8, Vec3d.ZERO, new Vec3d(0.2, 0.2, 0.2));
			bidentEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
			soundEvent = SpectrumSoundEvents.BIDENT_MIRROR_IMAGE_THROWN;
		} else if (playerEntity.getAbilities().creativeMode) {
			bidentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
		
		world.playSoundFromEntity(null, bidentEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if (!playerEntity.getAbilities().creativeMode && !mirrorImage) {
			playerEntity.getInventory().removeOne(stack);
		}
	}
	
	public float getThrowSpeed() {
		return 2.5F;
	}
	
	public int getBuiltinRiptideLevel() {
		return 0;
	}
	
	public boolean requiresRainForRiptide() {
		return false;
	}
	
	public boolean isThrownAsMirrorImage(ItemStack stack, ServerWorld world, PlayerEntity player) {
		return false;
	}
	
}
