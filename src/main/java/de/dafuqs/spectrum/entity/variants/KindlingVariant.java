package de.dafuqs.spectrum.entity.variants;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.storage.loot.*;

public class KindlingVariant {
	
	public static final Codec<KindlingVariant> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ResourceLocation.CODEC.fieldOf("default_texture").forGetter((variant) -> variant.defaultTexture),
			ResourceLocation.CODEC.fieldOf("blinking_texture").forGetter((variant) -> variant.blinkingTexture),
			ResourceLocation.CODEC.fieldOf("angry_texture").forGetter((variant) -> variant.angryTexture),
			ResourceLocation.CODEC.fieldOf("clipped_texture").forGetter((variant) -> variant.clippedTexture),
			ResourceLocation.CODEC.fieldOf("blinking_clipped_texture").forGetter((variant) -> variant.blinkingClippedTexture),
			ResourceLocation.CODEC.fieldOf("angry_clipped_texture").forGetter((variant) -> variant.angryClippedTexture),
			ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("clipping_loot_table").forGetter((variant) -> variant.clippingLootTable)
	).apply(instance, KindlingVariant::new));
	public static final Codec<Holder<KindlingVariant>> CODEC = RegistryFileCodec.create(SpectrumRegistryKeys.KINDLING_VARIANT, DIRECT_CODEC);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, KindlingVariant> DIRECT_STREAM_CODEC = PacketCodecHelper.tuple(
			ResourceLocation.STREAM_CODEC, KindlingVariant::getDefaultTexture,
			ResourceLocation.STREAM_CODEC, KindlingVariant::getBlinkingTexture,
			ResourceLocation.STREAM_CODEC, KindlingVariant::getAngryTexture,
			ResourceLocation.STREAM_CODEC, KindlingVariant::getClippedTexture,
			ResourceLocation.STREAM_CODEC, KindlingVariant::getBlinkingClippedTexture,
			ResourceLocation.STREAM_CODEC, KindlingVariant::getAngryClippedTexture,
			ResourceKey.streamCodec(Registries.LOOT_TABLE), KindlingVariant::getClippingLootTable,
			KindlingVariant::new);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<KindlingVariant>> STREAM_CODEC = ByteBufCodecs.holder(SpectrumRegistryKeys.KINDLING_VARIANT, DIRECT_STREAM_CODEC);
	
	public static final ResourceKey<KindlingVariant> DEFAULT = createKey("default");
	
	private static ResourceKey<KindlingVariant> createKey(String name) {
		return ResourceKey.create(SpectrumRegistryKeys.KINDLING_VARIANT, SpectrumCommon.locate(name));
	}
	
	private final ResourceLocation defaultTexture, defaultTextureFull;
	private final ResourceLocation blinkingTexture, blinkingTextureFull;
	private final ResourceLocation angryTexture, angryTextureFull;
	private final ResourceLocation clippedTexture, clippedTextureFull;
	private final ResourceLocation blinkingClippedTexture, blinkingClippedTextureFull;
	private final ResourceLocation angryClippedTexture, angryClippedTextureFull;
	private final ResourceKey<LootTable> clippingLootTable;
	
	KindlingVariant(ResourceLocation defaultTexture, ResourceLocation blinkingTexture, ResourceLocation angryTexture, ResourceLocation clippedTexture,
					ResourceLocation blinkingClippedTexture, ResourceLocation angryClippedTexture, ResourceKey<LootTable> clippingLootTable) {
		this.defaultTexture = defaultTexture;
		this.blinkingTexture = blinkingTexture;
		this.angryTexture = angryTexture;
		this.clippedTexture = clippedTexture;
		this.blinkingClippedTexture = blinkingClippedTexture;
		this.angryClippedTexture = angryClippedTexture;
		
		this.defaultTextureFull = fullTextureId(defaultTexture);
		this.blinkingTextureFull = fullTextureId(blinkingTexture);
		this.angryTextureFull = fullTextureId(angryTexture);
		this.clippedTextureFull = fullTextureId(clippedTexture);
		this.blinkingClippedTextureFull = fullTextureId(blinkingClippedTexture);
		this.angryClippedTextureFull = fullTextureId(angryClippedTexture);
		
		this.clippingLootTable = clippingLootTable;
	}
	
	private static ResourceLocation fullTextureId(ResourceLocation texture) {
		return texture.withPath((s) -> "textures/" + s + ".png");
	}
	
	public ResourceLocation getDefaultTexture() {
		return defaultTextureFull;
	}
	
	public ResourceLocation getBlinkingTexture() {
		return blinkingTextureFull;
	}
	
	public ResourceLocation getAngryTexture() {
		return angryTextureFull;
	}
	
	public ResourceLocation getClippedTexture() {
		return clippedTextureFull;
	}
	
	public ResourceLocation getBlinkingClippedTexture() {
		return blinkingClippedTextureFull;
	}
	
	public ResourceLocation getAngryClippedTexture() {
		return angryClippedTextureFull;
	}
	
	public ResourceKey<LootTable> getClippingLootTable() {
		return clippingLootTable;
	}
	
}
