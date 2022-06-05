package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
	
	@Shadow
	private ServerPlayerEntity owner;
	
	@Inject(at = @At("RETURN"), method = "grantCriterion(Lnet/minecraft/advancement/Advancement;Ljava/lang/String;)Z")
	public void triggerAdvancementCriteria(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
		AdvancementProgress advancementProgress = ((PlayerAdvancementTracker) (Object) this).getProgress(advancement);
		if (advancementProgress.isDone()) {
			
			SpectrumAdvancementCriteria.ADVANCEMENT_GOTTEN.trigger(owner, advancement);
			
			List<Cloakable> revealedBlocks = BlockCloakManager.getRevelationsForAdvancement(advancement.getId());
			for (Cloakable block : revealedBlocks) {
				SpectrumAdvancementCriteria.HAD_REVELATION.trigger(owner, block);
			}
		}
	}
	
}
