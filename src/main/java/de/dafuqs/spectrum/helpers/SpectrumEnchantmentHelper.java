package de.dafuqs.spectrum.helpers;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class SpectrumEnchantmentHelper {
	
	public static Pair<Boolean, ItemStack> addOrUpgradeEnchantment(RegistryWrapper.WrapperLookup registryLookup, ItemStack stack, RegistryKey<Enchantment> enchantmentKey, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		return getEntry(registryLookup, enchantmentKey)
				.map(entry -> addOrUpgradeEnchantment(stack, entry, level, forceEvenIfNotApplicable, allowEnchantmentConflicts))
				.orElse(new Pair<>(false, stack));
	}
	
	public static Optional<ItemStack> addOrUpgradeEnchantmentOpt(RegistryWrapper.WrapperLookup registryLookup, ItemStack stack, RegistryKey<Enchantment> enchantmentKey, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		Pair<Boolean, ItemStack> result = addOrUpgradeEnchantment(registryLookup, stack, enchantmentKey, level, forceEvenIfNotApplicable, allowEnchantmentConflicts);
		return result.getLeft() ? Optional.empty() : Optional.of(result.getRight());
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
	public static Pair<Boolean, ItemStack> addOrUpgradeEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		boolean isAcceptable = stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE) || forceEvenIfNotApplicable;
		boolean isConflicting = !allowEnchantmentConflicts && !EnchantmentHelper.isCompatible(stack.getEnchantments().getEnchantments(), enchantment);
		boolean isEnchantedBook = stack.isOf(Items.ENCHANTED_BOOK) || SpectrumEnchantmentHelper.isEnchantableBook(stack);
		
		// Can this enchant even go on that tool?
		if (!isAcceptable && !isEnchantedBook) {
			return new Pair<>(false, stack);
		}
		
		// Are there any conflicting enchantments?
		if (isConflicting) {
			return new Pair<>(false, stack);
		}
		
		// Convert enchantable books into enchanted books
		if (isEnchantedBook && !stack.isOf(Items.ENCHANTED_BOOK)) {
			ItemStack enchantedBookStack = new ItemStack(Items.ENCHANTED_BOOK, stack.getCount());
			enchantedBookStack.applyChanges(stack.getComponentChanges());
			stack = enchantedBookStack;
		}
		
		// Is the existing enchantment the same or better than the new one?
		var builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(stack));
		if (level <= builder.getLevel(enchantment)) {
			return new Pair<>(false, stack);
		}
		
		// Add the enchantment
		builder.set(enchantment, level);
		EnchantmentHelper.set(stack, builder.build());
		return new Pair<>(true, stack);
	}
	
	/**
	 * Checks if a stack can be used as the source to create an enchanted book
	 *
	 * @param stack The stack to check
	 * @return true if it is a book that can be turned into an enchanted book by enchanting
	 */
	public static boolean isEnchantableBook(@NotNull ItemStack stack) {
		return stack.isIn(SpectrumItemTags.ENCHANTABLE_BOOKS) || stack.getItem() instanceof BookItem;
	}
	
	public static ItemEnchantmentsComponent collectHighestEnchantments(List<ItemStack> itemStacks) {
		var builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
		for (ItemStack itemStack : itemStacks) {
			for (var entry : EnchantmentHelper.getEnchantments(itemStack).getEnchantmentEntries()) {
				builder.add(entry.getKey(), entry.getIntValue());
			}
		}
		return builder.build();
	}
	
	public static boolean canCombineAny(ItemStack existingStack, ItemStack newStack) {
		var existingEnchantments = EnchantmentHelper.getEnchantments(existingStack).getEnchantments();
		var newEnchantments = EnchantmentHelper.getEnchantments(newStack).getEnchantments();
		return existingEnchantments.isEmpty()
				|| newEnchantments.stream().anyMatch(newEnchantment ->
				EnchantmentHelper.isCompatible(existingEnchantments, newEnchantment));
	}
	
	@SafeVarargs
	public static Pair<ItemStack, Integer> removeEnchantments(RegistryWrapper.WrapperLookup registryLookup, @NotNull ItemStack itemStack, RegistryKey<Enchantment>... enchantmentKeys) {
		if (!EnchantmentHelper.hasEnchantments(itemStack)) {
			return new Pair<>(itemStack, 0);
		}
		
		var wrapper = getRegistry(registryLookup).orElse(null);
		if (wrapper == null) {
			return new Pair<>(itemStack, 0);
		}
		
		return removeEnchantments(itemStack, Arrays.stream(enchantmentKeys)
				.map(key -> wrapper.getOptional(key).orElse(null))
				.filter(Objects::nonNull)
				.toList());
	}
	
	@SafeVarargs
	public static Pair<ItemStack, Integer> removeEnchantments(@NotNull ItemStack itemStack, RegistryEntry<Enchantment>... enchantments) {
		return removeEnchantments(itemStack, Arrays.stream(enchantments).toList());
	}
	
	/**
	 * Removes the enchantments on a stack of items / enchanted book
	 *
	 * @param itemStack    the stack
	 * @param enchantments the enchantments to remove
	 * @return The resulting stack & the count of enchants that were removed
	 */
	public static <T extends RegistryEntry<Enchantment>> Pair<ItemStack, Integer> removeEnchantments(@NotNull ItemStack itemStack, List<T> enchantments) {
		var removals = new AtomicInteger(0);
		var builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(itemStack));
		enchantments.forEach(enchantment -> {
			if (builder.getEnchantments().contains(enchantment)) {
				builder.set(enchantment, 0);
				removals.getAndIncrement();
			}
		});
		
		var component = builder.build();
		if (itemStack.isOf(Items.ENCHANTED_BOOK) && component.isEmpty()) {
			itemStack = new ItemStack(Items.BOOK, itemStack.getCount());
		}
		EnchantmentHelper.set(itemStack, builder.build());
		
		return new Pair<>(itemStack, removals.get());
	}
	
	public static ItemStack getEnchantedStack(RegistryWrapper.WrapperLookup lookup, Item item, Map<RegistryKey<Enchantment>, Integer> enchantments) {
		RegistryWrapper<Enchantment> wrapper = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
		
		for (Map.Entry<RegistryKey<Enchantment>, Integer> e : enchantments.entrySet()) {
			builder.add(wrapper.getOrThrow(e.getKey()), e.getValue());
		}
		ItemStack stack = item.getDefaultStack();
		stack.set(DataComponentTypes.ENCHANTMENTS, builder.build());
		
		return stack;
	}
	
	public static int getLevel(RegistryWrapper.WrapperLookup registryLookup, RegistryKey<Enchantment> enchantment, ItemStack stack) {
		return getRegistry(registryLookup)
				.flatMap(impl -> impl.getOptional(enchantment))
				.map(entry -> EnchantmentHelper.getLevel(entry, stack))
				.orElse(0);
	}
	
	public static boolean hasEnchantment(RegistryWrapper.WrapperLookup registryLookup, RegistryKey<Enchantment> enchantment, ItemStack stack) {
		return getLevel(registryLookup, enchantment, stack) > 0;
	}
	
	public static Optional<RegistryWrapper.Impl<Enchantment>> getRegistry(RegistryWrapper.WrapperLookup registryLookup) {
		return registryLookup.getOptionalWrapper(RegistryKeys.ENCHANTMENT);
	}
	
	public static Optional<RegistryEntry<Enchantment>> getEntry(RegistryWrapper.WrapperLookup lookup, RegistryKey<Enchantment> key) {
		return getRegistry(lookup).flatMap(impl -> impl.getOptional(key));
	}
	
	public static int getEquipmentLevel(RegistryWrapper.WrapperLookup lookup, RegistryKey<Enchantment> key, LivingEntity entity) {
		return getEntry(lookup, key).map(e -> EnchantmentHelper.getEquipmentLevel(e, entity)).orElse(0);
	}
	
	public static boolean canEntityUse(Entity entity, String idAsString) {
		if (entity instanceof PlayerEntity playerEntity) {
			if (idAsString.contains("razing") || idAsString.contains("resonance") || idAsString.contains("voiding")) {
				idAsString += "_usage";
			}
			return AdvancementHelper.hasAdvancement(playerEntity, SpectrumCommon.locate("unlocks/enchantments/" + idAsString.substring(idAsString.indexOf(':') + 1)));
		}
		else {
			return false;
		}
	}
	
}
