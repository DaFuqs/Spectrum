package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.misc.SpectrumBlockCloaker;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {

    @Shadow private ServerPlayerEntity owner;

    @Inject(at=@At("RETURN"), method= "grantCriterion(Lnet/minecraft/advancement/Advancement;Ljava/lang/String;)Z")
    public void grantCriterion(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        AdvancementProgress advancementProgress = ((PlayerAdvancementTracker)(Object) this).getProgress(advancement);
        if(advancementProgress.isDone()) {
            boolean triggersRevelation = SpectrumBlockCloaker.doesAdvancementTriggerRevelation(advancement.getId());
            if(triggersRevelation) {
                Support.grantAdvancementCriterion(owner, "trigger_revelation", "have_revelation");
            }
        }
    }



}
