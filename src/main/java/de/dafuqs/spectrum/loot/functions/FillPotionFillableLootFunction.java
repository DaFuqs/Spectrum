package de.dafuqs.spectrum.loot.functions;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.*;

import java.util.*;

public class FillPotionFillableLootFunction extends LootItemConditionalFunction {
	
	public static final MapCodec<FillPotionFillableLootFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(
			InkPoweredPotionTemplate.CODEC.forGetter(c -> c.template)
	).apply(i, FillPotionFillableLootFunction::new));
	
	public record InkPoweredPotionTemplate(
			boolean ambient, boolean showParticles, NumberProvider duration,
			List<Holder<MobEffect>> statusEffects, int color, NumberProvider amplifier,
			List<InkColor> inkColors, NumberProvider inkCost, boolean unidentifiable, boolean incurable
	) {
		
		public static final MapCodec<InkPoweredPotionTemplate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.BOOL.optionalFieldOf("ambient", false).forGetter(InkPoweredPotionTemplate::ambient),
				Codec.BOOL.optionalFieldOf("show_particles", false).forGetter(InkPoweredPotionTemplate::showParticles),
				NumberProviders.CODEC.fieldOf("duration").forGetter(InkPoweredPotionTemplate::duration),
				CodecHelper.singleOrList(BuiltInRegistries.MOB_EFFECT.holderByNameCodec()).fieldOf("status_effect").forGetter(InkPoweredPotionTemplate::statusEffects),
				Codec.INT.optionalFieldOf("color", -1).forGetter(InkPoweredPotionTemplate::color),
				NumberProviders.CODEC.fieldOf("amplifier").forGetter(InkPoweredPotionTemplate::amplifier),
				CodecHelper.singleOrList(InkColor.CODEC).fieldOf("ink_color").forGetter(InkPoweredPotionTemplate::inkColors),
				NumberProviders.CODEC.fieldOf("ink_cost").forGetter(InkPoweredPotionTemplate::inkCost),
				Codec.BOOL.optionalFieldOf("unidentifiable", false).forGetter(InkPoweredPotionTemplate::unidentifiable),
				Codec.BOOL.optionalFieldOf("incurable", false).forGetter(InkPoweredPotionTemplate::incurable)
		).apply(i, InkPoweredPotionTemplate::new));
		
		public InkPoweredStatusEffectInstance get(LootContext context) {
			Holder<MobEffect> statusEffect = this.statusEffects.get(context.getRandom().nextInt(this.statusEffects.size()));
			MobEffectInstance statusEffectInstance = new MobEffectInstance(statusEffect, this.duration.getInt(context), this.amplifier.getInt(context), ambient, showParticles, true);
			InkColor inkColor = this.inkColors.get(context.getRandom().nextInt(this.inkColors.size()));
			int cost = this.inkCost.getInt(context);
			return new InkPoweredStatusEffectInstance(statusEffectInstance, new InkCost(inkColor, cost), this.color, this.unidentifiable, this.incurable);
		}
		
	}
	
	private final InkPoweredPotionTemplate template;
	
	FillPotionFillableLootFunction(List<LootItemCondition> conditions, InkPoweredPotionTemplate template) {
		super(conditions);
		this.template = template;
	}
	
	@Override
	public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
		return SpectrumLootFunctionTypes.FILL_POTION_FILLABLE;
	}
	
	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		if (this.template == null)
			return stack;

		if (!(stack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable))
			return stack;

		if (inkPoweredPotionFillable.isFull(stack))
			return stack;
		
		InkPoweredStatusEffectInstance effect = template.get(context);
		inkPoweredPotionFillable.addOrUpgradeEffects(stack, List.of(effect));
		
		return stack;
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(InkPoweredPotionTemplate template) {
		return simpleBuilder((conditions) -> new FillPotionFillableLootFunction(conditions, template));
	}
	
}
