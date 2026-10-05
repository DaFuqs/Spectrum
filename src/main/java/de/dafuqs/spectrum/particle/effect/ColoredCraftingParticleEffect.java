package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import org.joml.*;

public record ColoredCraftingParticleEffect(Vector3f color) implements ParticleOptions {
	
	public static final ColoredCraftingParticleEffect BLACK = new ColoredCraftingParticleEffect(InkColors.BLACK_COLOR);
	public static final ColoredCraftingParticleEffect BLUE = new ColoredCraftingParticleEffect(InkColors.BLUE_COLOR);
	public static final ColoredCraftingParticleEffect BROWN = new ColoredCraftingParticleEffect(InkColors.BROWN_COLOR);
	public static final ColoredCraftingParticleEffect CYAN = new ColoredCraftingParticleEffect(InkColors.CYAN_COLOR);
	public static final ColoredCraftingParticleEffect GRAY = new ColoredCraftingParticleEffect(InkColors.GRAY_COLOR);
	public static final ColoredCraftingParticleEffect GREEN = new ColoredCraftingParticleEffect(InkColors.GREEN_COLOR);
	public static final ColoredCraftingParticleEffect LIGHT_BLUE = new ColoredCraftingParticleEffect(InkColors.LIGHT_BLUE_COLOR);
	public static final ColoredCraftingParticleEffect LIGHT_GRAY = new ColoredCraftingParticleEffect(InkColors.LIGHT_GRAY_COLOR);
	public static final ColoredCraftingParticleEffect LIME = new ColoredCraftingParticleEffect(InkColors.LIME_COLOR);
	public static final ColoredCraftingParticleEffect MAGENTA = new ColoredCraftingParticleEffect(InkColors.MAGENTA_COLOR);
	public static final ColoredCraftingParticleEffect ORANGE = new ColoredCraftingParticleEffect(InkColors.ORANGE_COLOR);
	public static final ColoredCraftingParticleEffect PINK = new ColoredCraftingParticleEffect(InkColors.PINK_COLOR);
	public static final ColoredCraftingParticleEffect PURPLE = new ColoredCraftingParticleEffect(InkColors.PURPLE_COLOR);
	public static final ColoredCraftingParticleEffect RED = new ColoredCraftingParticleEffect(InkColors.RED_COLOR);
	public static final ColoredCraftingParticleEffect WHITE = new ColoredCraftingParticleEffect(InkColors.WHITE_COLOR);
	public static final ColoredCraftingParticleEffect YELLOW = new ColoredCraftingParticleEffect(InkColors.YELLOW_COLOR);
	
	public static final MapCodec<ColoredCraftingParticleEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((effect) -> effect.color)
	).apply(instance, ColoredCraftingParticleEffect::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, ColoredCraftingParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VECTOR3F, (effect) -> effect.color,
			ColoredCraftingParticleEffect::new
	);
	
	public ColoredCraftingParticleEffect(int color) {
		this(SpectrumColorHelper.colorIntToVec(color));
	}
	
	public ParticleType<ColoredCraftingParticleEffect> getType() {
		return SpectrumParticleTypes.COLORED_CRAFTING;
	}
	
	public static ParticleOptions of(int color) {
		return new ColoredCraftingParticleEffect(color);
	}
	
}
