package de.dafuqs.spectrum.api.ink.color;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import io.netty.buffer.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import org.joml.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class InkColor {
	
	public static final Codec<InkColor> CODEC = CodecHelper.SPECTRUM_DEFAULTED_IDENTIFIER.comapFlatMap(
			id -> ofId(id).map(DataResult::success).orElse(DataResult.error(() -> "Not a valid ink color: " + id)),
			InkColor::getID
	);
	
	public static final StreamCodec<ByteBuf, InkColor> PACKET_CODEC = ResourceLocation.STREAM_CODEC.map(
			id -> ofId(id).orElseThrow(),
			InkColor::getID
	);
	
	protected static final Map<DyeColor, InkColor> DYE_TO_COLOR = new HashMap<>();
	
	protected final Optional<DyeColor> dyeColor;
	protected final int colorInt;
	protected final Vector3f colorVec;
	protected final int textColor;
	protected final Vector3f textColorVec;
	
	protected final ResourceLocation requiredAdvancement;
	
	public InkColor(DyeColor dyeColor, int color, ResourceLocation requiredAdvancement) {
		this(Optional.of(dyeColor), color, color, requiredAdvancement);
	}
	
	public InkColor(DyeColor dyeColor, int color, int textColor, ResourceLocation requiredAdvancement) {
		this(Optional.of(dyeColor), color, textColor, requiredAdvancement);
	}
	
	public InkColor(Optional<DyeColor> dyeColor, int color, int textColor, ResourceLocation requiredAdvancement) {
		this.dyeColor = dyeColor;
		this.colorInt = color;
		this.colorVec = SpectrumColorHelper.colorIntToVec(color);
		this.textColor = textColor;
		this.textColorVec = SpectrumColorHelper.colorIntToVec(textColor);
		this.requiredAdvancement = requiredAdvancement;
		
		dyeColor.ifPresent(value -> DYE_TO_COLOR.put(value, this));
	}
	
	public static @Nullable InkColor ofDyeColor(DyeColor dyeColor) {
		return DYE_TO_COLOR.get(dyeColor);
	}
	
	public static Optional<InkColor> ofId(ResourceLocation id) {
		return SpectrumRegistries.INK_COLOR.getOptional(id);
	}
	
	public static Optional<InkColor> ofIdString(String idString) {
		return SpectrumRegistries.INK_COLOR.getOptional(ResourceLocation.parse(idString));
	}
	
	public Optional<DyeColor> getDyeColor() {
		return this.dyeColor;
	}
	
	@Override
	public String toString() {
		return this.getID().toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InkColor that = (InkColor) o;
		return this.dyeColor.equals(that.dyeColor);
	}
	
	// hash lookup go wheeeeee!
	@Override
	public int hashCode() {
		return colorInt;
	}
	
	public MutableComponent getName() {
		return Component.translatable(this.getID().toLanguageKey("ink", "name"));
	}
	
	public MutableComponent getColoredName() {
		return getName().setStyle(Style.EMPTY.withColor(textColor));
	}
	
	public MutableComponent getColoredInkName() {
		return Component.translatable("ink.suffix", getName()).setStyle(Style.EMPTY.withColor(textColor));
	}
	
	public Vector3f getColorVec() {
		return this.colorVec;
	}
	
	public int getColorInt() {
		return this.colorInt;
	}
	
	public int getTextColorInt() {
		return this.textColor;
	}
	
	public Vector3f getTextColorVec() {
		return this.textColorVec;
	}
	
	public ResourceLocation getRequiredAdvancement() {
		return requiredAdvancement;
	}
	
	public ResourceLocation getID() {
		return SpectrumRegistries.INK_COLOR.getKey(this);
	}
	
	public boolean isIn(TagKey<InkColor> tag) {
		return SpectrumRegistries.INK_COLOR.wrapAsHolder(this).is(tag);
	}
	
}
