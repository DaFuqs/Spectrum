package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.dafuqs.spectrum.energy.color.CMYKColor;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class ColorPredicate {

	public static final ColorPredicate ANY;
	@Nullable
	private final CMYKColor color;

	public ColorPredicate(@Nullable CMYKColor color) {
		this.color = color;
	}

	public boolean test(CMYKColor color) {
		if (this == ANY || color == null) {
			return true;
		}
		return this.color == color;
	}

	public static ColorPredicate fromJson(@Nullable JsonElement json) {
		if (json != null && !json.isJsonNull() && json instanceof JsonPrimitive) {
			String colorString = json.getAsString();
			CMYKColor color = CMYKColor.of(colorString.toUpperCase(Locale.ROOT));
			return new ColorPredicate(color);
		} else {
			return ANY;
		}
	}

	public JsonElement toJson() {
		if (this == ANY) {
			return JsonNull.INSTANCE;
		} else {
			JsonObject jsonObject = new JsonObject();
			if (this.color != null) {
				jsonObject.addProperty("color", this.color.toString());
			}
			return jsonObject;
		}
	}

	static {
		ANY = new ColorPredicate(null);
	}

	public static class Builder {
		@Nullable
		private CMYKColor color;

		private Builder() {
			this.color = null;
		}

		public static ColorPredicate.Builder create() {
			return new ColorPredicate.Builder();
		}

		public ColorPredicate.Builder color(CMYKColor color) {
			this.color = color;
			return this;
		}

		public ColorPredicate build() {
			return new ColorPredicate(this.color);
		}
	}
}
