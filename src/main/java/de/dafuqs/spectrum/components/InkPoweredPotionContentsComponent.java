package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;

import java.util.*;

public record InkPoweredPotionContentsComponent(List<InkPoweredMobEffectInstance> effects) implements Iterable<MobEffectInstance> {
	
	public static final InkPoweredPotionContentsComponent DEFAULT = new InkPoweredPotionContentsComponent(List.of());
	
	public static final Codec<InkPoweredPotionContentsComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			InkPoweredMobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(InkPoweredPotionContentsComponent::effects)
	).apply(i, InkPoweredPotionContentsComponent::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, InkPoweredPotionContentsComponent> PACKET_CODEC = StreamCodec.composite(
			InkPoweredMobEffectInstance.PACKET_CODEC.apply(ByteBufCodecs.list()), InkPoweredPotionContentsComponent::effects,
			InkPoweredPotionContentsComponent::new
	);
	
	public static List<InkPoweredMobEffectInstance> getEffects(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.INK_POWERED_POTION_CONTENTS, InkPoweredPotionContentsComponent.DEFAULT).effects();
	}
	
	public static void setEffects(ItemStack stack, List<InkPoweredMobEffectInstance> effects) {
		stack.set(SpectrumDataComponentTypes.INK_POWERED_POTION_CONTENTS, new InkPoweredPotionContentsComponent(effects));
	}
	
	public OptionalInt getColor() {
		return getColor(this.effects());
	}
	
	public static OptionalInt getColor(List<InkPoweredMobEffectInstance> effects) {
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		
		for (InkPoweredMobEffectInstance instance : effects) {
			if (instance.getStatusEffectInstance().isVisible()) {
				int i1 = instance.getColor();
				int j1 = instance.getStatusEffectInstance().getAmplifier() + 1;
				i += j1 * FastColor.ARGB32.red(i1);
				j += j1 * FastColor.ARGB32.green(i1);
				k += j1 * FastColor.ARGB32.blue(i1);
				l += j1;
			}
		}
		
		return l == 0 ? OptionalInt.empty() : OptionalInt.of(FastColor.ARGB32.color(i / l, j / l, k / l));
	}
	
	@Override
	public Iterator<MobEffectInstance> iterator() {
		return this.effects.stream()
				.map(InkPoweredMobEffectInstance::getStatusEffectInstance)
				.iterator();
	}
	
	public int size() {
		return this.effects.size();
	}
	
	@Deprecated
	public List<MobEffectInstance> getVanillaEffects() {
		return effects.stream().map(InkPoweredMobEffectInstance::getStatusEffectInstance).toList();
	}
	
	public static void clearEffects(ItemStack itemStack) {
		itemStack.remove(SpectrumDataComponentTypes.INK_POWERED_POTION_CONTENTS);
	}
	
}
