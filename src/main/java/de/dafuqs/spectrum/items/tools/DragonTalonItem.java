package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.spectrum.api.energy.color.InkColors;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import de.dafuqs.spectrum.entity.entity.DragonTalonEntity;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.registries.SpectrumDamageTypes;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DragonTalonItem extends MalachiteBidentItem implements MergeableItem, SlotReservingItem, ExtendedEnchantable, TranstargetItem, SlotBackgroundEffectProvider {

    protected static final UUID REACH_MODIFIER_ID = UUID.fromString("3b9a13c8-a9a7-4545-8c32-e60baf25823e");
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers, phantomModifiers;


    public DragonTalonItem(ToolMaterial toolMaterial, double damage, double extraReach, Settings settings) {
        super(settings, 0, 0, 0, 0);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", damage + toolMaterial.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -0.8, EntityAttributeModifier.Operation.ADDITION));
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
        var nbt = stack.getOrCreateNbt();
        if (slot != EquipmentSlot.MAINHAND)
            return super.getAttributeModifiers(slot);
        return this.isReservingSlot(stack) || nbt.getBoolean("cooldown") ? phantomModifiers : attributeModifiers;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.spectrum.dragon_needle.tooltip").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.spectrum.dragon_needle.tooltip2").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.spectrum.dragon_needle.tooltip3").formatted(Formatting.GRAY));
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
            durability  += player.getAbilities().creativeMode ? 0 : 500;
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
    public SoundProvider getMergeSound() {
        return (player -> player.playSound(SpectrumSoundEvents.METALLIC_UNSHEATHE, SoundCategory.PLAYERS, 0.3F, 0.4F + player.getRandom().nextFloat() * 0.2F));
    }

    @Override
    public boolean isReservingSlot(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean(SlotReservingItem.NBT_STRING);
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
            }
            else if(nbt.contains("cooldown")) {
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
        return ColorHelper.colorVecToRGB(InkColors.YELLOW.getColor());
    }

    @Override
    public void expandTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context) {}
}
