package de.dafuqs.spectrum.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonObject;

import de.dafuqs.spectrum.helpers.NbtHelper;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

@Mixin(AdvancementDisplay.class)
public class AdvancementDisplayMixin {
	@Inject(
		method = "iconFromJson(Lcom/google/gson/JsonObject;)Lnet/minecraft/item/ItemStack;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V",
			ordinal = 0),
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true)
	private static void iconFromJson(JsonObject json, CallbackInfoReturnable<ItemStack> info, Item item) {
		ItemStack itemStack = new ItemStack(item);
		Optional<NbtCompound> nbt = NbtHelper.getNbtCompound(json.get("nbt"));
		if (nbt.isPresent()) itemStack.setNbt(nbt.get());
		info.setReturnValue(itemStack);
	}
}
