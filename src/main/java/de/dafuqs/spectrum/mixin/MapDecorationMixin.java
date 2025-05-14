package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.injectors.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.level.saveddata.maps.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;
import java.util.function.*;

@Mixin(MapDecoration.class)
public class MapDecorationMixin implements MapDecorationInjector {
	
	@Unique
	private byte scale = 0;
	
	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function5;)Lnet/minecraft/network/codec/StreamCodec;"))
	private static StreamCodec<RegistryFriendlyByteBuf, MapDecoration> wrapCodec(
			StreamCodec<? super RegistryFriendlyByteBuf, Holder<MapDecorationType>> codec1, Function<MapDecoration, Holder<MapDecorationType>> from1,
			StreamCodec<? super RegistryFriendlyByteBuf, Byte> codec2, Function<MapDecoration, Byte> from2,
			StreamCodec<? super RegistryFriendlyByteBuf, Byte> codec3, Function<MapDecoration, Byte> from3,
			StreamCodec<? super RegistryFriendlyByteBuf, Byte> codec4, Function<MapDecoration, Byte> from4,
			StreamCodec<? super RegistryFriendlyByteBuf, Optional<Component>> codec5, Function<MapDecoration, Optional<Component>> from5,
			Function5<Holder<MapDecorationType>, Byte, Byte, Byte, Optional<Component>, MapDecoration> _to,
			Operation<StreamCodec<RegistryFriendlyByteBuf, MapDecoration>> original
	) {
		StreamCodec<RegistryFriendlyByteBuf, MapDecoration> codec = original.call(codec1, from1, codec2, from2, codec3, from3, codec4, from4, codec5, from5, _to);
		return new StreamCodec<>() {
			@Override
			public void encode(RegistryFriendlyByteBuf buf, MapDecoration value) {
				codec.encode(buf, value);
				buf.writeByte(value.spectrum$getScale());
			}
			
			@Override
			public MapDecoration decode(RegistryFriendlyByteBuf buf) {
				MapDecoration decoration = codec.decode(buf);
				decoration.spectrum$setScale(buf.readByte());
				return decoration;
			}
		};
	}
	
	@Override
	public void spectrum$setScale(byte scale) {
		this.scale = scale;
	}
	
	@Override
	public byte spectrum$getScale() {
		return this.scale;
	}
	
}
