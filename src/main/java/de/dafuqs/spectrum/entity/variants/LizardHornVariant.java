package de.dafuqs.spectrum.entity.variants;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;

public class LizardHornVariant {
	
	public static final Codec<LizardHornVariant> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter((variant) -> variant.texture)
	).apply(instance, LizardHornVariant::new));
	public static final Codec<Holder<LizardHornVariant>> CODEC = RegistryFileCodec.create(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, DIRECT_CODEC);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, LizardHornVariant> DIRECT_STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, LizardHornVariant::getTextureLocation,
			LizardHornVariant::new);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<LizardHornVariant>> STREAM_CODEC = ByteBufCodecs.holder(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, DIRECT_STREAM_CODEC);
	
	public static final ResourceKey<LizardHornVariant> HORNY = createKey("horny");
	
	private static ResourceKey<LizardHornVariant> createKey(String name) {
		return ResourceKey.create(SpectrumRegistryKeys.LIZARD_HORN_VARIANT, SpectrumCommon.locate(name));
	}
	
	private final ResourceLocation texture, textureFull;
	
	LizardHornVariant(ResourceLocation texture) {
		this.texture = texture;
		this.textureFull = texture.withPath((string) -> "textures/" + string + ".png");
	}
	
	public ResourceLocation getTextureLocation() {
		return textureFull;
	}
	
}
