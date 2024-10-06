package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.*;
import com.jamieswhiteshirt.reachentityattributes.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DragonTalonItem extends MalachiteBidentItem implements MergeableItem, SlotReservingItem, ExtendedEnchantable, TranstargetItem, SlotBackgroundEffectProvider {
	
	protected static final UUID REACH_MODIFIER_ID = UUID.fromString("3b9a13c8-a9a7-4545-8c32-e60baf25823e");
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers, phantomModifiers;
	
	
	public DragonTalonItem(ToolMaterial toolMaterial, double damage, double extraReach, Settings settings) {
		super(settings, 0, 0, 0, 0);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", damage + toolMaterial.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -2.0, EntityAttributeModifier.Operation.ADDITION));
		builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(REACH_MODIFIER_ID, "Tool modifier", extraReach, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
		
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> phantom = ImmutableMultimap.builder();
		this.phantomModifiers = phantom.build();
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		var nbt = stack.getNbt();
		if (nbt == null || slot != EquipmentSlot.MAINHAND)
			return super.getAttributeModifiers(slot);
		
		return this.isReservingSlot(stack) || nbt.getBoolean("cooldown") ? phantomModifiers : attributeModifiers;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.dragon_talon.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.dragon_talon.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.dragon_talon.tooltip3").formatted(Formatting.GRAY));
	}
	
	@Override
	public float getThrowSpeed(ItemStack stack) {
		return 3.5F;
	}
	
	@Override
	protected void throwBident(ItemStack stack, ServerWorld world, PlayerEntity playerEntity) {
		var needleEntity = new DragonTalonEntity(world);
		needleEntity.setStack(stack);
		needleEntity.setOwner(playerEntity);
		needleEntity.updatePosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
		needleEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, getThrowSpeed(stack), 1.0F);
		needleEntity.velocityDirty = true;
		needleEntity.velocityModified = true;
		needleEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
		
		world.spawnEntity(needleEntity);
		SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THROW;
		
		world.playSoundFromEntity(null, needleEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
		var nbt = stack.getOrCreateNbt();
		markReserved(stack, true);
		nbt.putUuid("lastNeedle", needleEntity.getUuid());
	}
	
	@Override
	public ItemStack getResult(ServerPlayerEntity player, ItemStack firstHalf, ItemStack secondHalf) {
		var durability = Math.max(firstHalf.getDamage(), secondHalf.getDamage());
		var result = new ItemStack(SpectrumItems.DRACONIC_TWINSWORD);
		result.setNbt(firstHalf.getNbt());
		
		var nbt = result.getOrCreateNbt();
		nbt.remove("pairSignature");
		nbt.remove("lastNeedle");
		nbt.remove("cooldown");
		nbt.remove(SlotReservingItem.NBT_STRING);
		
		if (isReservingSlot(firstHalf) || isReservingSlot(secondHalf)) {
			durability += player.getAbilities().creativeMode ? 0 : 500;
			player.getItemCooldownManager().set(result.getItem(), 400);
		}
		result.setDamage(durability);
		
		return result;
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		var hand = user.getActiveHand();
		if (hand == Hand.MAIN_HAND)
			return;
		
		if (!isReservingSlot(stack)) {
			super.onStoppedUsing(user.getStackInHand(Hand.OFF_HAND), world, user, remainingUseTicks);
			return;
		}
		
		var nbt = stack.getOrCreateNbt();
		
		if (world.isClient() || !nbt.containsUuid("lastNeedle"))
			return;
		
		ServerWorld serverWorld = (ServerWorld) world;
		
		var entity = serverWorld.getEntity(nbt.getUuid("lastNeedle"));
		
		if (entity instanceof DragonTalonEntity needle) {
			needle.recall();
		}
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) && !isReservingSlot(stack);
	}

	@Override
	public boolean canMerge(ServerPlayerEntity player, ItemStack parent, ItemStack other) {
		if (player.getItemCooldownManager().isCoolingDown(parent.getItem()))
			return false;
		return (parent.getItem() == other.getItem() && verify(parent, other));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (hand == Hand.MAIN_HAND)
			return TypedActionResult.fail(user.getStackInHand(hand));
		return super.use(world, user, hand);
	}

	@Override
	public void playSound(ServerPlayerEntity player) {
		player.playSound(SpectrumSoundEvents.METALLIC_UNSHEATHE, SoundCategory.PLAYERS, 0.5F, 0.8F + player.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public boolean isReservingSlot(ItemStack stack) {
		@Nullable NbtCompound nbt = stack.getNbt();
		if (nbt == null) {
			return false;
		}
		return nbt.getBoolean(SlotReservingItem.NBT_STRING);
	}
	
	@Override
	public void markReserved(ItemStack stack, boolean reserved) {
		stack.getOrCreateNbt().putBoolean(SlotReservingItem.NBT_STRING, reserved);
	}
	
	public static ItemStack findThrownStack(PlayerEntity player, UUID id) {
		var inventory = player.getInventory();
		for (int i = 0; i < inventory.size(); i++) {
			var stack = inventory.getStack(i);
			var nbt = stack.getNbt();
			if (nbt != null && nbt.containsUuid("lastNeedle") && nbt.getUuid("lastNeedle").equals(id)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (entity instanceof PlayerEntity player) {
			var nbt = stack.getOrCreateNbt();
			if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
				if (!nbt.getBoolean("cooldown")) {
					nbt.putBoolean("cooldown", true);
				}
			} else if (nbt.contains("cooldown")) {
				nbt.remove("cooldown");
			}
		}
	}
	
	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		var composition = new DamageComposition();
		composition.add(SpectrumDamageTypes.evisceration(attacker.getWorld(), attacker), damage);
		return composition;
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.CHANNELING || enchantment == Enchantments.PIERCING || enchantment == SpectrumEnchantments.INERTIA;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of();
	}
	
	@Override
	public EnchantmentTarget getRealTarget() {
		return EnchantmentTarget.WEAPON;
	}
	
	@Override
	public float getDefenseMultiplier(LivingEntity target, ItemStack stack) {
		return 1F;
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		return SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return InkColors.YELLOW_COLOR;
	}
	
	@Override
	public void expandTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context) {
	}
}
