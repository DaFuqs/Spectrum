package de.dafuqs.spectrum.mixin;

import com.klikli_dev.modonomicon.book.conditions.*;
import com.llamalad7.mixinextras.sugar.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BookCategoryHasVisibleEntriesCondition.class)
public class FixModonomiconMixin {
	
	// Legend has it that modonomicon is a good mod
	@ModifyArg(method = "fromJson", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;"))
	private static Object[] componentfy(Object[] args, @Local(ordinal = 1) Identifier categoryId) {
		return new String[]{categoryId.toTranslationKey()};
	}
}
