package de.dafuqs.spectrum.compat.modonomicon.unlock_conditions;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.conditions.context.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class NotCondition extends BookCondition {
	
	protected BookCondition child;
	
	protected List<Text> tooltips;
	
	public NotCondition(Text component, BookCondition child) {
		super(component);
		if (child == null)
			throw new IllegalArgumentException("NotCondition must have exactly one child.");
		this.child = child;
	}
	
	public static NotCondition fromJson(JsonObject json) {
		BookCondition child;
		var j = JsonHelper.getObject(json, "child");
		if (!j.isJsonObject()) {
			throw new JsonSyntaxException("Condition children must be an array of JsonObjects.");
		}
		child = BookCondition.fromJson(j.getAsJsonObject());
		var tooltip = tooltipFromJson(json);
		return new NotCondition(tooltip, child);
	}
	
	public static NotCondition fromNetwork(PacketByteBuf buffer) {
		var tooltip = buffer.readBoolean() ? buffer.readText() : null;
		return new NotCondition(tooltip, BookCondition.fromNetwork(buffer));
	}
	
	@Override
	public Identifier getType() {
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
	public void toNetwork(PacketByteBuf buffer) {
		buffer.writeBoolean(this.tooltip != null);
		if (this.tooltip != null) {
			buffer.writeText(this.tooltip);
		}
		
		BookCondition.toNetwork(this.child, buffer);
	}
	
	@Override
	public boolean test(BookConditionContext context, PlayerEntity player) {
		return !this.child.test(context, player);
	}
	
	@Override
	public boolean testOnLoad() {
		return child.testOnLoad();
	}
	
	@Override
	public List<Text> getTooltip(PlayerEntity player, BookConditionContext context) {
		if (this.tooltips == null) {
			this.tooltips = new ArrayList<>();
			if (this.tooltip != null)
				this.tooltips.add(this.tooltip);
			this.tooltips.addAll(this.child.getTooltip(player, context));
		}
		
		return this.tooltips != null ? this.tooltips : List.of();
	}
	
}
