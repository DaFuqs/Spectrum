package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.mojang.authlib.*;
import com.mojang.authlib.minecraft.*;
import de.dafuqs.spectrum.render.*;
import net.minecraft.*;
import net.minecraft.client.resources.*;
import net.minecraft.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;
import java.util.concurrent.*;

@Mixin(SkinManager.class)
public class SkinManagerMixin {
	
	@ModifyVariable(method = "registerTextures(Ljava/util/UUID;Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;)Ljava/util/concurrent/CompletableFuture;", at = @At("STORE"), ordinal = 1)
	private CompletableFuture<ResourceLocation> spectrum$injectCustomCapes(CompletableFuture<ResourceLocation> capeFutureOriginal, UUID uuid) {
		Optional<WorthinessChecker.CapeType> capeType = WorthinessChecker.getCapeType(uuid);
		if (capeType.isPresent()) {
			return CompletableFuture.completedFuture(capeType.get().capePath);
		}
		return capeFutureOriginal;
	}
	
}