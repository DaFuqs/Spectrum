package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import org.jetbrains.annotations.*;

public class SpectrumRegistry<T> extends MappedRegistry<T> {
	
	public SpectrumRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
		super(key, lifecycle);
	}
	
	@Override
	public Codec<T> byNameCodec() {
		return this.referenceHolderWithLifecycle().flatComapMap(Holder.Reference::value, value -> this.safeCastToReference(this.wrapAsHolder(value)));
	}
	
	@Override
	public Codec<Holder<T>> holderByNameCodec() {
		return this.referenceHolderWithLifecycle().flatComapMap(entry -> entry, this::safeCastToReference);
	}
	
	protected Codec<Holder.Reference<T>> referenceHolderWithLifecycle() {
		return CodecHelper.SPECTRUM_DEFAULTED_IDENTIFIER.comapFlatMap(
				id -> this.getHolder(id).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + this.key() + ": " + id)),
				entry -> entry.key().location());
	}
	
	protected DataResult<Holder.Reference<T>> safeCastToReference(Holder<T> entry) {
		return entry instanceof Holder.Reference<T> reference
				? DataResult.success(reference)
				: DataResult.error(() -> "Unregistered holder in " + this.key() + ": " + entry);
	}
	
	@Nullable
	public T get(@Nullable String id) {
		return id == null ? null : get(SpectrumCommon.ofSpectrumDefaulted(id));
	}
	
}