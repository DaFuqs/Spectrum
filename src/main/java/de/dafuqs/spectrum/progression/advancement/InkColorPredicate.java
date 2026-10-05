package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;

import javax.annotation.*;
import java.util.*;

public record InkColorPredicate(Optional<InkColor> color) {
	
	public static final Codec<InkColorPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			InkColor.CODEC.optionalFieldOf("color").forGetter(InkColorPredicate::color)
	).apply(instance, InkColorPredicate::new));
	
	public static final InkColorPredicate ANY = new InkColorPredicate(Optional.empty());
	
	public boolean test(@Nullable InkColor color) {
		if (this == ANY || color == null) {
			return true;
		}
		return this.color.isPresent() && this.color.get().equals(color);
	}
	
	public static class Builder {
		
		@Nullable
		private InkColor color;
		
		private Builder() {
			this.color = null;
		}
		
		public static InkColorPredicate.Builder create() {
			return new InkColorPredicate.Builder();
		}
		
		public InkColorPredicate.Builder color(InkColor color) {
			this.color = color;
			return this;
		}
		
		public InkColorPredicate build() {
			return new InkColorPredicate(Optional.ofNullable(this.color));
		}
	}
}
