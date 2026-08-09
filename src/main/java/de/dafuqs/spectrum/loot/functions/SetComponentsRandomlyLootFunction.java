package de.dafuqs.spectrum.loot.functions;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.*;

public class SetComponentsRandomlyLootFunction extends LootItemConditionalFunction {
	
	public static final MapCodec<SetComponentsRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(
			DataComponentPatch.CODEC.listOf().fieldOf("options").forGetter(c -> c.options)
	).apply(i, SetComponentsRandomlyLootFunction::new));
	
	private final List<DataComponentPatch> options;
	
	SetComponentsRandomlyLootFunction(List<LootItemCondition> conditions, List<DataComponentPatch> options) {
		super(conditions);
		this.options = options;
	}
	
	@Override
	public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
		return SpectrumLootFunctionTypes.SET_COMPONENTS_RANDOMLY;
	}
	
	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		if (options.isEmpty()) return stack;
		
		var changes = options.get(context.getRandom().nextInt(options.size()));
		stack.applyComponentsAndValidate(changes);
		
		return stack;
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(List<DataComponentPatch> options) {
		return simpleBuilder(conditions -> new SetComponentsRandomlyLootFunction(conditions, options));
	}
	
	public static class Builder extends LootItemConditionalFunction.Builder<SetComponentsRandomlyLootFunction.Builder> {
		private final List<DataComponentPatch> options = new ArrayList<>();
		
		@Override
		protected SetComponentsRandomlyLootFunction.Builder getThis() {
			return this;
		}
		
		public SetComponentsRandomlyLootFunction.Builder add(DataComponentPatch changes) {
			options.add(changes);
			return this;
		}
		
		@Override
		public LootItemFunction build() {
			return new SetComponentsRandomlyLootFunction(this.getConditions(), options);
		}
	}
	
}
