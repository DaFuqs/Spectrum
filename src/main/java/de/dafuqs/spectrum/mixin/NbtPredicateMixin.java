package de.dafuqs.spectrum.mixin;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;

import de.dafuqs.spectrum.helpers.NbtHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;

@Mixin(NbtPredicate.class)
public class NbtPredicateMixin {
	@Inject(
		method = "fromJson(Lcom/google/gson/JsonElement;)Lnet/minecraft/predicate/NbtPredicate;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/JsonHelper;asString(Lcom/google/gson/JsonElement;Ljava/lang/String;)Ljava/lang/String;",
			ordinal = 0),
		cancellable = true)
	private static void fromJson(@Nullable JsonElement json, CallbackInfoReturnable<NbtPredicate> info) {
		Optional<NbtCompound> nbt = NbtHelper.getNbtCompound(json);
		info.setReturnValue(new NbtPredicate(nbt.get()));
	}
}
