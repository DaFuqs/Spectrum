package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.injectors.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(MobEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceInjector {
	
	@Shadow
	private int duration;
	@Shadow
	private int amplifier;
	@Unique
	private boolean spectrum$severe;
	
	@ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;", remap = false))
	private static Codec<MobEffectInstance> wrapCodec(Codec<MobEffectInstance> original) {
		return original.mapResult(new Codec.ResultFunction<>() {
			@Override
			public <T> DataResult<Pair<MobEffectInstance, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<MobEffectInstance, T>> result) {
				return result.map(pair -> {
					ops.get(input, "severe").flatMap(ops::getBooleanValue).ifSuccess(v -> pair.getFirst().spectrum$setSevere(v));
					return pair;
				});
			}
			
			@Override
			public <T> DataResult<T> coApply(DynamicOps<T> ops, MobEffectInstance inst, DataResult<T> result) {
				return result.map(output -> ops.set(output, "severe", ops.createBoolean(inst.spectrum$isSevere())));
			}
		});
	}
	
	@Inject(method = "update", at = @At("RETURN"))
	private void readSevere(MobEffectInstance that, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) LocalBooleanRef changed) {
		if (spectrum$severe != that.spectrum$isSevere()) {
			spectrum$setSevere(that.spectrum$isSevere());
			changed.set(true);
		}
	}
	
	@Override
	public boolean spectrum$isSevere() {
		return spectrum$severe;
	}
	
	@Override
	public void spectrum$setSevere(boolean severe) {
		this.spectrum$severe = severe;
	}
	
	@Override
	public void spectrum$setDuration(int newDuration) {
		this.duration = newDuration;
	}
	
	@Override
	public void spectrum$setAmplifier(int newAmplifier) {
		this.amplifier = newAmplifier;
	}
	
	@ModifyReturnValue(method = "describeDuration()Ljava/lang/String;", at = @At("RETURN"))
	private String describeDuration(String original) {
		if (this.spectrum$severe) {
			original = original + Component.translatable("item.spectrum.potion.tooltip.severe").getString();
		}
		return original;
	}
	
}
