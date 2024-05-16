package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MalachiteBidentItem extends TridentItem implements Preenchanted, ExtendedEnchantable, ExpandedStatTooltip, ArmorPiercingItem {
	
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	private final float armorPierce, protPierce;
	public static final String THROW_EFFECTS_DISABLED = "disabled";
	
	public MalachiteBidentItem(Settings settings, double attackSpeed, double damage, float armorPierce, float protPierce) {
		super(settings);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", damage, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
		this.armorPierce = armorPierce;
		this.protPierce = protPierce;
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
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack handStack = user.getStackInHand(hand);
		if (handStack.getDamage() >= handStack.getMaxDamage() - 1) {
			return TypedActionResult.fail(handStack);
		}
		user.setCurrentHand(hand);
		return TypedActionResult.consume(handStack);
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity player) {
			int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
			if (useTime >= 10) {
				player.incrementStat(Stats.USED.getOrCreateStat(this));

				if (canStartRiptide(player, stack)) {
					riptide(world, player, getRiptideLevel(stack));
				} else if (!world.isClient) {
					stack.damage(1, player, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
					throwBident(stack, (ServerWorld) world, player);
				}
			}
		}
	}
	
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return SpectrumToolMaterials.ToolMaterial.MALACHITE.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
	}
	
	public int getRiptideLevel(ItemStack stack) {
		return EnchantmentHelper.getRiptide(stack);
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
		
		BidentBaseEntity bidentBaseEntity = mirrorImage ? new BidentMirrorImageEntity(world) : new BidentEntity(world);
		bidentBaseEntity.setStack(stack);
		bidentBaseEntity.setOwner(playerEntity);
		bidentBaseEntity.updatePosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
		bidentBaseEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, getThrowSpeed(stack), 1.0F);
		if (!mirrorImage && playerEntity.getAbilities().creativeMode) {
			bidentBaseEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
		
		world.spawnEntity(bidentBaseEntity);
		SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THROW;
		if (mirrorImage) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, bidentBaseEntity.getPos(), SpectrumParticleTypes.MIRROR_IMAGE, 8, Vec3d.ZERO, new Vec3d(0.2, 0.2, 0.2));
			bidentBaseEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
			soundEvent = SpectrumSoundEvents.BIDENT_MIRROR_IMAGE_THROWN;
		} else if (playerEntity.getAbilities().creativeMode) {
			bidentBaseEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
		
		world.playSoundFromEntity(null, bidentBaseEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
		if (!playerEntity.getAbilities().creativeMode && !mirrorImage) {
			playerEntity.getInventory().removeOne(stack);
		}
	}

	public void markDisabled(ItemStack stack, boolean disabled) {
		stack.getOrCreateNbt().putBoolean(THROW_EFFECTS_DISABLED, disabled);
	}

	public boolean isDisabled(ItemStack stack) {
		return stack.getOrCreateNbt().getBoolean(THROW_EFFECTS_DISABLED);
	}

	public boolean canBeDisabled() {
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (isDisabled(stack))
			tooltip.add(Text.translatable("item.spectrum.bident.toolTip.disabled").formatted(Formatting.RED, Formatting.ITALIC));
	}

	@Override
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (canBeDisabled() && clickType == ClickType.RIGHT) {
			markDisabled(stack, !isDisabled(stack));
			return true;
		}
		return false;
	}

	public float getThrowSpeed(ItemStack stack) {
		return 3F;
	}
	
	public boolean canStartRiptide(PlayerEntity player, ItemStack stack) {
		return getRiptideLevel(stack) > 0 && player.isTouchingWaterOrRain();
	}
	
	public boolean isThrownAsMirrorImage(ItemStack stack, ServerWorld world, PlayerEntity player) {
		return false;
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.SHARPNESS || enchantment == Enchantments.SMITE || enchantment == Enchantments.BANE_OF_ARTHROPODS || enchantment == Enchantments.LOOTING || enchantment == SpectrumEnchantments.CLOVERS_FAVOR;
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
	public void expandTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context) {
		if (Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("item.spectrum.bident.postToolTip.ap", armorPierce * 100).formatted(Formatting.DARK_GREEN));

			if (protPierce > 0) {
				tooltip.add(Text.translatable("item.spectrum.bident.postToolTip.pp", protPierce * 100).formatted(Formatting.DARK_GREEN));
			}
			if (canBeDisabled()) {
				tooltip.add(Text.translatable("item.spectrum.bident.postToolTip.disable").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
			}
		}
		else {
			tooltip.add(Text.translatable("spectrum.tooltip.press_shift_for_more").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
		}
	}
}
