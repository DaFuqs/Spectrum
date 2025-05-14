package de.dafuqs.spectrum.compat.modonomicon.unlock_conditions;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.conditions.context.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;

import java.util.*;

public class NotCondition extends BookCondition {
	
	protected BookCondition child;
	
	protected List<Component> tooltips;
	
	public NotCondition(Component component, BookCondition child) {
		super(component);
		if (child == null)
			throw new IllegalArgumentException("NotCondition must have exactly one child.");
		this.child = child;
	}
	
	public static NotCondition fromJson(ResourceLocation conditionParentId, JsonObject json, HolderLookup.Provider provider) {
		BookCondition child;
		var j = GsonHelper.getAsJsonObject(json, "child");
		if (!j.isJsonObject()) {
			throw new JsonSyntaxException("Condition children must be an array of JsonObjects.");
		}
		child = BookCondition.fromJson(conditionParentId, j.getAsJsonObject(), provider);
		var tooltip = tooltipFromJson(json, provider);
		return new NotCondition(tooltip, child);
	}
	
	public static NotCondition fromNetwork(RegistryFriendlyByteBuf buffer) {
		var tooltip = buffer.readBoolean() ? ComponentSerialization.STREAM_CODEC.decode(buffer) : null;
		return new NotCondition(tooltip, BookCondition.fromNetwork(buffer));
	}
	
	@Override
	public ResourceLocation getType() {
		return ModonomiconCompat.NOT;
	}
	
	@Override
	public boolean requiresMultiPassUnlockTest() {
		return this.child.requiresMultiPassUnlockTest();
	}
	
	public BookCondition child() {
		return this.child;
	}
	
	@Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
		buffer.writeBoolean(this.tooltip != null);
		if (this.tooltip != null) {
			ComponentSerialization.STREAM_CODEC.encode(buffer, this.tooltip);
		}
		
		BookCondition.toNetwork(this.child, buffer);
	}
	
	@Override
	public boolean test(BookConditionContext context, Player player) {
		return !this.child.test(context, player);
	}
	
	@Override
	public boolean testOnLoad() {
		return child.testOnLoad();
	}
	
	@Override
	public List<Component> getTooltip(Player player, BookConditionContext context) {
		if (this.tooltips == null) {
			this.tooltips = new ArrayList<>();
			if (this.tooltip != null)
				this.tooltips.add(this.tooltip);
			this.tooltips.addAll(this.child.getTooltip(player, context));
		}
		
		return this.tooltips != null ? this.tooltips : List.of();
	}
	
}
