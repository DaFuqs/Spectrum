package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class SpectrumEnchantmentHelper {
	
	public static Tuple<Boolean, ItemStack> addOrUpgradeEnchantment(HolderLookup.Provider registryLookup, ItemStack stack, ResourceKey<Enchantment> enchantmentKey, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		return getEntry(registryLookup, enchantmentKey)
				.map(entry -> addOrUpgradeEnchantment(stack, entry, level, forceEvenIfNotApplicable, allowEnchantmentConflicts))
				.orElse(new Tuple<>(false, stack));
	}
	
	public static Optional<ItemStack> addOrUpgradeEnchantmentOpt(HolderLookup.Provider registryLookup, ItemStack stack, ResourceKey<Enchantment> enchantmentKey, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		Tuple<Boolean, ItemStack> result = addOrUpgradeEnchantment(registryLookup, stack, enchantmentKey, level, forceEvenIfNotApplicable, allowEnchantmentConflicts);
		return result.getA() ? Optional.empty() : Optional.of(result.getB());
	}
	
	/**
	 * Adds an enchantment to an ItemStack. If the stack already has that enchantment, it gets upgraded instead
	 *
	 * @param stack                     the stack that receives the enchantments
	 * @param enchantment               the enchantment to add
	 * @param level                     the level of the enchantment
	 * @param forceEvenIfNotApplicable  add enchantments to the item, even if the item does usually not support that enchantment
	 * @param allowEnchantmentConflicts add enchantments to the item, even if there are enchantment conflicts
	 * @return the enchanted stack and a boolean if the enchanting was successful
	 */
	public static Tuple<Boolean, ItemStack> addOrUpgradeEnchantment(ItemStack stack, Holder<Enchantment> enchantment, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		boolean isAcceptable = stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE) || forceEvenIfNotApplicable;
		boolean isConflicting = !allowEnchantmentConflicts && !EnchantmentHelper.isEnchantmentCompatible(stack.getEnchantments().keySet(), enchantment);
		boolean isEnchantedBook = stack.is(Items.ENCHANTED_BOOK) || SpectrumEnchantmentHelper.isEnchantableBook(stack);
		
		// Can this enchant even go on that tool?
		if (!isAcceptable && !isEnchantedBook) {
			return new Tuple<>(false, stack);
		}
		
		// Are there any conflicting enchantments?
		if (isConflicting) {
			return new Tuple<>(false, stack);
		}
		
		// Convert enchantable books into enchanted books
		if (isEnchantedBook && !stack.is(Items.ENCHANTED_BOOK)) {
			ItemStack enchantedBookStack = new ItemStack(Items.ENCHANTED_BOOK, stack.getCount());
			enchantedBookStack.applyComponentsAndValidate(stack.getComponentsPatch());
			stack = enchantedBookStack;
		}
		
		// Is the existing enchantment the same or better than the new one?
		var builder = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(stack));
		if (level <= builder.getLevel(enchantment)) {
			return new Tuple<>(false, stack);
		}
		
		// Add the enchantment
		builder.set(enchantment, level);
		EnchantmentHelper.setEnchantments(stack, builder.toImmutable());
		return new Tuple<>(true, stack);
	}
	
	/**
	 * Checks if a stack can be used as the source to create an enchanted book
	 *
	 * @param stack The stack to check
	 * @return true if it is a book that can be turned into an enchanted book by enchanting
	 */
	public static boolean isEnchantableBook(@NotNull ItemStack stack) {
		return stack.is(SpectrumItemTags.ENCHANTABLE_BOOKS) || stack.getItem() instanceof BookItem;
	}
	
	public static ItemEnchantments collectHighestEnchantments(List<ItemStack> itemStacks) {
		var builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (ItemStack itemStack : itemStacks) {
			for (var entry : EnchantmentHelper.getEnchantmentsForCrafting(itemStack).entrySet()) {
				builder.upgrade(entry.getKey(), entry.getIntValue());
			}
		}
		return builder.toImmutable();
	}
	
	public static boolean canCombineAny(ItemStack existingStack, ItemStack newStack) {
		var existingEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(existingStack).keySet();
		var newEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(newStack).keySet();
		return existingEnchantments.isEmpty()
				|| newEnchantments.stream().anyMatch(newEnchantment ->
				EnchantmentHelper.isEnchantmentCompatible(existingEnchantments, newEnchantment));
	}
	
	@SafeVarargs
	public static Tuple<ItemStack, Integer> removeEnchantments(HolderLookup.Provider registryLookup, @NotNull ItemStack itemStack, ResourceKey<Enchantment>... enchantmentKeys) {
		if (!EnchantmentHelper.hasAnyEnchantments(itemStack)) {
			return new Tuple<>(itemStack, 0);
		}
		
		var wrapper = getRegistry(registryLookup).orElse(null);
		if (wrapper == null) {
			return new Tuple<>(itemStack, 0);
		}
		
		return removeEnchantments(itemStack, Arrays.stream(enchantmentKeys)
				.map(key -> wrapper.get(key).orElse(null))
				.filter(Objects::nonNull)
				.toList());
	}
	
	@SafeVarargs
	public static Tuple<ItemStack, Integer> removeEnchantments(@NotNull ItemStack itemStack, Holder<Enchantment>... enchantments) {
		return removeEnchantments(itemStack, Arrays.stream(enchantments).toList());
	}
	
	/**
	 * Removes the enchantments on a stack of items / enchanted book
	 *
	 * @param itemStack    the stack
	 * @param enchantments the enchantments to remove
	 * @return The resulting stack & the count of enchants that were removed
	 */
	public static <T extends Holder<Enchantment>> Tuple<ItemStack, Integer> removeEnchantments(@NotNull ItemStack itemStack, List<T> enchantments) {
		var removals = new AtomicInteger(0);
		var builder = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack));
		enchantments.forEach(enchantment -> {
			if (builder.keySet().contains(enchantment)) {
				builder.set(enchantment, 0);
				removals.getAndIncrement();
			}
		});
		
		var component = builder.toImmutable();
		if (itemStack.is(Items.ENCHANTED_BOOK) && component.isEmpty()) {
			itemStack = new ItemStack(Items.BOOK, itemStack.getCount());
		}
		EnchantmentHelper.setEnchantments(itemStack, builder.toImmutable());
		
		return new Tuple<>(itemStack, removals.get());
	}
	
	public static ItemStack getEnchantedStack(HolderLookup.Provider lookup, Item item, Map<ResourceKey<Enchantment>, Integer> enchantments) {
		HolderLookup<Enchantment> wrapper = lookup.lookupOrThrow(Registries.ENCHANTMENT);
		ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		
		for (Map.Entry<ResourceKey<Enchantment>, Integer> e : enchantments.entrySet()) {
			builder.upgrade(wrapper.getOrThrow(e.getKey()), e.getValue());
		}
		ItemStack stack = item.getDefaultInstance();
		stack.set(DataComponents.ENCHANTMENTS, builder.toImmutable());
		
		return stack;
	}
	
	public static int getLevel(HolderLookup.Provider registryLookup, ResourceKey<Enchantment> enchantment, ItemStack stack) {
		return getRegistry(registryLookup)
				.flatMap(impl -> impl.get(enchantment))
				.map(entry -> EnchantmentHelper.getItemEnchantmentLevel(entry, stack))
				.orElse(0);
	}
	
	public static boolean hasEnchantment(HolderLookup.Provider registryLookup, ResourceKey<Enchantment> enchantment, ItemStack stack) {
		return getLevel(registryLookup, enchantment, stack) > 0;
	}
	
	public static Optional<HolderLookup.RegistryLookup<Enchantment>> getRegistry(HolderLookup.Provider registryLookup) {
		return registryLookup.lookup(Registries.ENCHANTMENT);
	}
	
	public static Optional<Holder<Enchantment>> getEntry(HolderLookup.Provider lookup, ResourceKey<Enchantment> key) {
		return getRegistry(lookup).flatMap(impl -> impl.get(key));
	}
	
	public static int getEquipmentLevel(HolderLookup.Provider lookup, ResourceKey<Enchantment> key, LivingEntity entity) {
		return getEntry(lookup, key).map(e -> EnchantmentHelper.getEnchantmentLevel(e, entity)).orElse(0);
	}
	
}
