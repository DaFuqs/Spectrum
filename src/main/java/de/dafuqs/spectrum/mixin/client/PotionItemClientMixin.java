package de.dafuqs.spectrum.mixin.client;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({PotionItem.class, LingeringPotionItem.class, TippedArrowItem.class})
public class PotionItemClientMixin {
	
	@Inject(method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V", at = @At("HEAD"), cancellable = true)
	private void spectrum$makePotionUnidentifiable(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("spectrum_unidentifiable", NbtElement.BYTE_TYPE) && nbtCompound.getBoolean("spectrum_unidentifiable")) {
			tooltip.add(new TranslatableText("item.spectrum.potion.tooltip.unidentifiable"));
			ci.cancel();
		}
	}
	
}
