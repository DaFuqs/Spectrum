package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.api.energy.color.*;
import org.jetbrains.annotations.*;

public class ColorPredicate {
	
	public static final ColorPredicate ANY;
	
	static {
		ANY = new ColorPredicate(null);
	}
	
	@Nullable
	private final InkColor color;
	
	public ColorPredicate(@Nullable InkColor color) {
		this.color = color;
	}
	
	public static ColorPredicate fromJson(@Nullable JsonElement json) {
		if (json != null && !json.isJsonNull() && json instanceof JsonPrimitive) {
			InkColor color = InkColor.ofIdString(json.getAsString()).orElseThrow();
			return new ColorPredicate(color);
		} else {
			return ANY;
		}
	}
	
	public boolean test(InkColor color) {
		if (this == ANY || color == null) {
			return true;
		}
		return this.color == color;
	}
	
	public JsonElement toJson() {
		if (this == ANY) {
			return JsonNull.INSTANCE;
		} else {
			JsonObject jsonObject = new JsonObject();
			if (this.color != null) {
				jsonObject.addProperty("color", this.color.getID().toString());
			}
			return jsonObject;
		}
	}
	
	public static class Builder {
		@Nullable
		private InkColor color;
		
		private Builder() {
			this.color = null;
		}
		
		public static ColorPredicate.Builder create() {
			return new ColorPredicate.Builder();
		}
		
		public ColorPredicate.Builder color(InkColor color) {
			this.color = color;
			return this;
		}
		
		public ColorPredicate build() {
			return new ColorPredicate(this.color);
		}
	}
}
