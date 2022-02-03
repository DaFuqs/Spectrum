package de.dafuqs.spectrum.mixin;

import com.google.common.collect.ImmutableMap;
import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.progression.advancement.HasAdvancementCriterion;
import net.minecraft.advancement.*;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(Advancement.Task.class)
public class AdvancementProgressMixin {
	
	@Shadow @Nullable private Identifier parentId;
	
	@Shadow private Map<String, AdvancementCriterion> criteria;
	
	@Mutable
	@Shadow
	private String[][] requirements;
	
	@Inject(method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/advancement/AdvancementDisplay;Lnet/minecraft/advancement/AdvancementRewards;Ljava/util/Map;[[Ljava/lang/String;)V", at = @At(value = "TAIL"))
	private void spectrumAdvancementInjectRequireParent(Identifier parentId, AdvancementDisplay display, AdvancementRewards rewards, Map criteria, String[][] requirements, CallbackInfo ci) {
		if(this.parentId != null && this.parentId.getNamespace().equals("spectrum")) {
			if (!(criteria instanceof ImmutableMap)) {
				this.criteria.put("gotten_previous_advancement", new AdvancementCriterion(new HasAdvancementCriterion().conditionsFromAdvancementIdentifier(EntityPredicate.Extended.EMPTY, this.parentId)));
				
				String[][] newReq = new String[this.requirements.length + 1][];
				for (int i = 0; i < this.requirements.length; i++) {
					newReq[i] = this.requirements[0];
				}
				newReq[this.requirements.length] = new String[]{ "gotten_previous_advancement" };
				
				this.requirements = newReq;
			}
		}
	}
	
}
