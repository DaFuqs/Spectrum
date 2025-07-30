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
	
	private final ResourceLocation defaultTexture;
	private final ResourceLocation blinkingTexture;
	private final ResourceLocation angryTexture;
	private final ResourceLocation clippedTexture;
	private final ResourceLocation blinkingClippedTexture;
	private final ResourceLocation angryClippedTexture;
	private final ResourceKey<LootTable> clippingLootTable;
	
	KindlingVariant(ResourceLocation defaultTexture, ResourceLocation blinkingTexture, ResourceLocation angryTexture, ResourceLocation clippedTexture,
					ResourceLocation blinkingClippedTexture, ResourceLocation angryClippedTexture, ResourceKey<LootTable> clippingLootTable) {
		this.defaultTexture = defaultTexture.withPath((string) -> "textures/" + string + ".png");
		this.blinkingTexture = blinkingTexture.withPath((string) -> "textures/" + string + ".png");
		this.angryTexture = angryTexture.withPath((string) -> "textures/" + string + ".png");
		this.clippedTexture = clippedTexture.withPath((string) -> "textures/" + string + ".png");
		this.blinkingClippedTexture = blinkingClippedTexture.withPath((string) -> "textures/" + string + ".png");
		this.angryClippedTexture = angryClippedTexture.withPath((string) -> "textures/" + string + ".png");
		this.clippingLootTable = clippingLootTable;
	}
	
	public ResourceLocation getDefaultTexture() {
		return defaultTexture;
	}
	
	public ResourceLocation getBlinkingTexture() {
		return blinkingTexture;
	}
	
	public ResourceLocation getAngryTexture() {
		return angryTexture;
	}
	
	public ResourceLocation getClippedTexture() {
		return clippedTexture;
	}
	
	public ResourceLocation getBlinkingClippedTexture() {
		return blinkingClippedTexture;
	}
	
	public ResourceLocation getAngryClippedTexture() {
		return angryClippedTexture;
	}
	
	public ResourceKey<LootTable> getClippingLootTable() {
		return clippingLootTable;
	}
	
}
