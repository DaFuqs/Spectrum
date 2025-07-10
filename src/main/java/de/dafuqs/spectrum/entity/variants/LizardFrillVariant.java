package de.dafuqs.spectrum.entity.variants;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;

public class LizardFrillVariant {
	
	public static final Codec<LizardFrillVariant> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter((variant) -> variant.texture)
	).apply(instance, LizardFrillVariant::new));
	public static final Codec<Holder<LizardFrillVariant>> CODEC = RegistryFileCodec.create(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, DIRECT_CODEC);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, LizardFrillVariant> DIRECT_STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, LizardFrillVariant::getTextureLocation,
			LizardFrillVariant::new);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<LizardFrillVariant>> STREAM_CODEC = ByteBufCodecs.holder(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, DIRECT_STREAM_CODEC);
	
	public static final ResourceKey<LizardFrillVariant> SIMPLE = createKey("simple");
	
	private static ResourceKey<LizardFrillVariant> createKey(String name) {
		return ResourceKey.create(SpectrumRegistryKeys.LIZARD_FRILL_VARIANT, ResourceLocation.withDefaultNamespace(name));
	}
	
	private final ResourceLocation texture;
	
	LizardFrillVariant(ResourceLocation texture) {
		this.texture = texture.withPath((string) -> "textures/" + string + ".png");
	}
	
	public ResourceLocation getTextureLocation() {
		return texture;
	}
	
}
