package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import de.dafuqs.spectrum.items.Preenchanted;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.UUID;

/**
 * A sword with additional reach
 */
public class GreatswordItem extends SwordItem implements Preenchanted {

	protected static final UUID REACH_MODIFIER_ID = UUID.fromString("c81a7152-313c-452f-b15e-fcf51322ccc0");

	// shadowing SwordItem's properties in a way, since those are private final
	private final float attackDamage;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public GreatswordItem(ToolMaterial material, int attackDamage, float attackSpeed, float extraReach, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);

		this.attackDamage = (float) attackDamage + material.getAttackDamage();
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_MODIFIER_ID, "Weapon modifier", extraReach, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.SWEEPING, 4);
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

}
