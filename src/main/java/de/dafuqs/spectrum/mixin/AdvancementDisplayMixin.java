package de.dafuqs.spectrum.mixin;

import com.google.gson.*;
import de.dafuqs.spectrum.helpers.NbtHelper;
import net.minecraft.advancement.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

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
