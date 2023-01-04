package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import de.dafuqs.spectrum.items.Preenchanted;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.function.Predicate;

public class MalachiteCrossbowItem extends CrossbowItem implements Preenchanted, ArrowheadCrossbow {
	
	public static final Predicate<ItemStack> PROJECTILES = (stack) -> stack.isIn(ItemTags.ARROWS) || stack.isIn(SpectrumItemTags.GLASS_ARROWS);
	
	public MalachiteCrossbowItem(Settings settings) {
        super(settings);
    }
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.PIERCING, 5);
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
	
	public static ItemStack getFirstProjectile(ItemStack crossbow) {
		NbtCompound nbtCompound = crossbow.getNbt();
		if (nbtCompound != null && nbtCompound.contains("ChargedProjectiles", 9)) {
			NbtList nbtList = nbtCompound.getList("ChargedProjectiles", 10);
			if (nbtList != null && nbtList.size() > 0) {
				NbtCompound nbtCompound2 = nbtList.getCompound(0);
				return ItemStack.fromNbt(nbtCompound2);
			}
		}
		return ItemStack.EMPTY;
	}
	
	public Predicate<ItemStack> getProjectiles() {
		return PROJECTILES;
	}
	
	@Override
	public float getProjectileVelocityModifier() {
		return 1.25F;
	}
	
	@Override
	public float getPullTimeModifier() {
		return 1.0F;
	}
	
	@Override
	public float getDivergenceMod() {
		return 0.75F;
	}
	
	
}
