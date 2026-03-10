package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class MalachiteBidentItem extends TridentItem implements Preenchanted, ExpandedStatTooltip, ArmorPiercingItem {
	
	private final boolean hasActiveAbilities;
	private final float armorPierce, protPierce;
	
	public MalachiteBidentItem(Item.Properties settings, double attackSpeed, double damage, float armorPierce, float protPierce, boolean hasActiveAbilities) {
		super(settings.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.build()));
		this.armorPierce = armorPierce;
		this.protPierce = protPierce;
		this.hasActiveAbilities = hasActiveAbilities;
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.IMPALING, 6);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack handStack = user.getItemInHand(hand);
		
		if (hasActiveAbilities && user.isShiftKeyDown()) {
			boolean newActivated = !ActivatableItem.isActivated(handStack);
			if(newActivated) {
				ActivatableItem.setActivated(handStack, true);
				user.displayClientMessage(Component.translatable("item.spectrum.bident.tooltip.enabled"), true);
			} else {
				ActivatableItem.setActivated(handStack, false);
				user.displayClientMessage(Component.translatable("item.spectrum.bident.tooltip.disabled"), true);
			}
			return InteractionResultHolder.consume(user.getItemInHand(hand));
		}
		
		if (handStack.getDamageValue() >= handStack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(handStack);
		}
		user.startUsingItem(hand);
		return InteractionResultHolder.consume(handStack);
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof Player player) {
			int useTime = this.getUseDuration(stack, user) - remainingUseTicks;
			if (useTime >= 10) {
				player.awardStat(Stats.ITEM_USED.get(this));
				
				if (canStartRiptide(player, stack)) {
					riptide(world, player, stack, getRiptideLevel(world.registryAccess(), stack));
				} else if (!world.isClientSide) {
					stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(user.getUsedItemHand()));
					throwBident(stack, (ServerLevel) world, player);
				}
			}
		}
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
		return SpectrumToolTiers.MALACHITE.getRepairIngredient().test(ingredient) || super.isValidRepairItem(stack, ingredient);
	}
	
	public int getRiptideLevel(HolderLookup.Provider lookup, ItemStack stack) {
		return SpectrumEnchantmentHelper.getLevel(lookup, Enchantments.RIPTIDE, stack);
	}
	
	protected void riptide(Level world, Player playerEntity, ItemStack stack, int riptideLevel) {
		yeetPlayer(playerEntity, (float) riptideLevel);
		playerEntity.startAutoSpinAttack(20, (float) playerEntity.getAttributeValue(Attributes.ATTACK_DAMAGE), stack);
		if (playerEntity.onGround()) {
			playerEntity.move(MoverType.SELF, new Vec3(0.0, 1.2, 0.0));
		}
		
		SoundEvent soundEvent;
		if (riptideLevel >= 3) {
			soundEvent = SoundEvents.TRIDENT_RIPTIDE_3.value();
		} else if (riptideLevel == 2) {
			soundEvent = SoundEvents.TRIDENT_RIPTIDE_2.value();
		} else {
			soundEvent = SoundEvents.TRIDENT_RIPTIDE_1.value();
		}
		
		world.playSound(null, playerEntity, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
	}
	
	protected void yeetPlayer(Player playerEntity, float riptideLevel) {
		float f = playerEntity.getYRot();
		float g = playerEntity.getXRot();
		float h = -Mth.sin(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
		float k = -Mth.sin(g * 0.017453292F);
		float l = Mth.cos(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
		float m = Mth.sqrt(h * h + k * k + l * l);
		float n = 3.0F * ((1.0F + riptideLevel) / 4.0F);
		h *= n / m;
		k *= n / m;
		l *= n / m;
		playerEntity.push(h, k, l);
	}
	
	protected void throwBident(ItemStack stack, ServerLevel world, Player playerEntity) {
		boolean mirrorImage = isThrownAsMirrorImage(stack, world, playerEntity);
		
		BidentBaseEntity bidentBaseEntity = mirrorImage ? new BidentMirrorImageEntity(world) : new BidentEntity(world);
		bidentBaseEntity.setPickupItemStack(stack);
		bidentBaseEntity.setOwner(playerEntity);
		bidentBaseEntity.absMoveTo(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
		bidentBaseEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, getThrowSpeed(stack), 1.0F);
		if (!mirrorImage && playerEntity.getAbilities().instabuild) {
			bidentBaseEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		}
		
		world.addFreshEntity(bidentBaseEntity);
		var soundEvent = SoundEvents.TRIDENT_THROW.value();
		if (mirrorImage) {
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, bidentBaseEntity.position(), SpectrumParticleTypes.MIRROR_IMAGE, 8, Vec3.ZERO, new Vec3(0.2, 0.2, 0.2));
			bidentBaseEntity.pickup = AbstractArrow.Pickup.DISALLOWED;
			soundEvent = SpectrumSoundEvents.BIDENT_MIRROR_IMAGE_THROWN;
		} else if (playerEntity.getAbilities().instabuild) {
			bidentBaseEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		}
		
		world.playSound(null, bidentBaseEntity, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
		if (!playerEntity.getAbilities().instabuild && !mirrorImage) {
			playerEntity.getInventory().removeItem(stack);
		}
	}
	
	public boolean canBeDisabled() {
		return false;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if(hasActiveAbilities) {
			if (ActivatableItem.isActivated(stack)) {
				tooltip.add(Component.translatable("item.spectrum.bident.tooltip.enabled").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			} else {
				tooltip.add(Component.translatable("item.spectrum.bident.tooltip.disabled").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
			}
		}
	}
	
	public float getThrowSpeed(ItemStack stack) {
		return 3F;
	}
	
	public boolean canStartRiptide(Player player, ItemStack stack) {
		return getRiptideLevel(player.level().registryAccess(), stack) > 0 && player.isInWaterOrRain();
	}
	
	public boolean isThrownAsMirrorImage(ItemStack stack, ServerLevel world, Player player) {
		return false;
	}
	
	@Override
	public float getDefenseMultiplier(LivingEntity target, ItemStack stack) {
		return 1 - armorPierce;
	}
	
	@Override
	public float getToughnessMultiplier(LivingEntity target, ItemStack stack) {
		return 1;
	}
	
	@Override
	public float getProtReduction(LivingEntity target, ItemStack stack) {
		return protPierce;
	}
	
	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		var composition = new DamageComposition();
		var source = composition.getPlayerOrEntity(attacker);
		SpectrumDamageTypes.wrapWithStackTracking(source, stack);
		composition.add(source, damage);
		return composition;
	}
	
	@Override
	public void expandTooltip(ItemStack stack, @Nullable Player player, Consumer<Component> tooltip, TooltipContext context) {
		if (Screen.hasShiftDown()) {
			tooltip.accept(Component.translatable("item.spectrum.bident.postToolTip.ap", armorPierce * 100).withStyle(ChatFormatting.DARK_GREEN));
			
			if (protPierce > 0) {
				tooltip.accept(Component.translatable("item.spectrum.bident.postToolTip.pp", protPierce * 100).withStyle(ChatFormatting.DARK_GREEN));
			}
			if (canBeDisabled()) {
				tooltip.accept(Component.translatable("item.spectrum.bident.postToolTip.disable").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
			}
		} else {
			tooltip.accept(Component.translatable("spectrum.tooltip.press_shift_for_more").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
		}
	}
	
	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.SHARPNESS) || enchantment.is(Enchantments.SMITE) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) || enchantment.is(Enchantments.LOOTING);
	}
	
}
