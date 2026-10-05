package de.dafuqs.spectrum.compat.modonomicon.unlock_conditions;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.conditions.context.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.*;

import java.util.*;

public class EnchantmentRegisteredCondition extends BookCondition {
	
	protected static final String TOOLTIP = "book.condition.tooltip." + SpectrumCommon.MOD_ID + ".enchantment_registered";
	
	protected ResourceKey<Enchantment> enchantmentKey;
	
	public EnchantmentRegisteredCondition(@Nullable Component tooltip, ResourceKey<Enchantment> enchantmentKey) {
		super(tooltip);
		this.enchantmentKey = enchantmentKey;
	}
	
	public static EnchantmentRegisteredCondition fromJson(ResourceLocation conditionParentId, JsonObject json, HolderLookup.Provider provider) {
		ResourceLocation enchantmentID = ResourceLocation.parse(GsonHelper.getAsString(json, "enchantment_id"));
		Component tooltip = tooltipFromJson(json, provider);
		return new EnchantmentRegisteredCondition(tooltip, ResourceKey.create(Registries.ENCHANTMENT, enchantmentID));
	}
	
	public static EnchantmentRegisteredCondition fromNetwork(RegistryFriendlyByteBuf buffer) {
		var tooltip = buffer.readBoolean() ? ComponentSerialization.STREAM_CODEC.decode(buffer) : null;
		var entryId = buffer.readResourceLocation();
		return new EnchantmentRegisteredCondition(tooltip, ResourceKey.create(Registries.ENCHANTMENT, entryId));
	}
	
	@Override
	public ResourceLocation getType() {
		return ModonomiconCompat.ENCHANTMENT_REGISTERED;
	}
	
	@Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
		buffer.writeBoolean(this.tooltip != null);
		if (this.tooltip != null) {
			ComponentSerialization.STREAM_CODEC.encode(buffer, this.tooltip);
		}
		buffer.writeResourceLocation(this.enchantmentKey.location());
	}
	
	@Override
	public boolean test(BookConditionContext context, Player player) {
		return SpectrumEnchantmentHelper.getEntry(player.level().registryAccess(), this.enchantmentKey).isPresent();
	}
	
	@Override
	public List<Component> getTooltip(Player player, BookConditionContext context) {
		if (this.tooltip == null && context instanceof BookConditionEntryContext entryContext) {
			this.tooltip = Component.translatable(TOOLTIP, Component.translatable(entryContext.getBook().getEntry(this.enchantmentKey.location()).getName()));
		}
		return super.getTooltip(player, context);
	}
}
