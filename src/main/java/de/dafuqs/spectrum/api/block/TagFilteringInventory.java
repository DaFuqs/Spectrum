package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import org.apache.commons.lang3.*;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public interface TagFilteringInventory {
	
	List<ItemVariant> getItemFilters();
	default Object2BooleanMap<TagKey<Item>> getFilteredTags() { return Object2BooleanMaps.emptyMap(); }
	default boolean onlyDenyListTags() { return true; }
	default void setOnlyDenyListTags(boolean onlyDenyListTags) {  }
	
	default boolean acceptsItem(Item item) {
		if (item == null || item.equals(Items.AIR)) { return false; }
		
		if (!this.getFilteredTags().isEmpty()) {
			int returnValue = 0;
			// Latest takes precedence. 1 for if it's allowed, -1 for if it's denied.
			for(TagKey<Item> tag : this.getFilteredTags().keySet()) {
				if(item.getRegistryEntry().isIn(tag)) { returnValue = this.getFilteredTags().getBoolean(tag) ? 1 : -1; }
			}
			if(returnValue != 0) { return returnValue == 1; }
			// If we only have denyList tags, treat not being in any as am implicit c:everything.
			if(this.onlyDenyListTags()) { return true; }
		}
		
		boolean allAir = true;
		for (ItemVariant filterItem: this.getItemFilters()) {
			if (filterItem.getItem().equals(item)) { return true; }
			if (!filterItem.getItem().equals(Items.AIR)) { allAir = false; }
		}
		return allAir;
	}
	
	// Run on NBT read.
	default void clearFilters() {
		this.getFilteredTags().clear();
		this.setOnlyDenyListTags(true);
	}
	
	default boolean addTagFilteringItem(ItemVariant itemVariant) {
		ItemStack stack = itemVariant.toStack();
		if (!stack.hasCustomName() || !stack.isIn(SpectrumItemTags.TAG_FILTERING_ITEMS)) {
			this.setOnlyDenyListTags(false);
			return false;
		}
		String name = StringUtils.trim(stack.getName().getString());
		if (StringUtils.equalsAnyIgnoreCase(name, "*", "any", "all", "everything", "c:*", "c:any", "c:all", "c:everything")) {
			this.setOnlyDenyListTags(true);
			return true;
		}
		
		boolean allow = !name.startsWith("!");
		Identifier identifier = Identifier.tryParse(StringUtils.remove(allow ? name : name.substring(1), '#'));
		if(identifier == null) { return false; }
		
		// Copied from PastelNodeBlockEntity. This entire section could potentially be a candidate to move into its own function.
		TagKey<Item> tag = SpectrumCommon.CACHED_ITEM_TAG_MAP.computeIfAbsent(identifier, tagId -> Registries.ITEM.streamTags()
				.filter(t -> t.id().equals(tagId))
				.findFirst()
				.orElse(null));
		
		if(tag == null) { return false; }
		if(allow) { this.setOnlyDenyListTags(false); }
		return this.getFilteredTags().put(tag, allow);
	}
	
	// Call on change.
	default void updateTagFilteringItems() {
		this.clearFilters();
		this.getItemFilters().forEach(this::addTagFilteringItem);
	}
}
