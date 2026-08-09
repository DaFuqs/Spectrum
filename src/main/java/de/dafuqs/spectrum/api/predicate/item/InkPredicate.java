package de.dafuqs.spectrum.api.predicate.item;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.advancement.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;

import java.util.*;

public record InkPredicate(LongRange totalRange, Map<InkColor, LongRange> colorRanges) implements SingleComponentItemPredicate<InkStorageComponent> {
		
		public static final Codec<InkPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LongRange.CODEC.optionalFieldOf("total", LongRange.ANY).forGetter(InkPredicate::totalRange),
				CodecHelper.registryMap(SpectrumRegistries.INK_COLOR, LongRange.CODEC).fieldOf("colors").forGetter(InkPredicate::colorRanges)
		).apply(instance, InkPredicate::new));
		
		public InkPredicate(LongRange totalRange, Map<InkColor, LongRange> colorRanges) {
			this.totalRange = totalRange;
			this.colorRanges = colorRanges;
		}
		
		@Override
		public DataComponentType<InkStorageComponent> componentType() {
			return SpectrumDataComponentTypes.INK_STORAGE.get();
		}
		
		public boolean matches(ItemStack stack, InkStorageComponent value) {
			Map<InkColor, Long> storedEnergy = value.storedEnergy();
			
			long total = 0L;
			for(long i : storedEnergy.values()) {
				total += i;
			}
			if(!totalRange.test(total)) {
				return false;
			}
			
			Map<InkColor, LongRange> colorRanges = colorRanges();
			return !colorRanges.isEmpty() && colorRanges.entrySet().stream().allMatch(entry -> {
				LongRange required = colorRanges.get(entry.getKey());
				Long stored = storedEnergy.getOrDefault(entry.getKey(), 0L);
				return required.test(stored);
			});
		}
	}