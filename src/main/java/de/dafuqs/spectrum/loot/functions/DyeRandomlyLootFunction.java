package de.dafuqs.spectrum.loot.functions;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.core.component.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.*;

public class DyeRandomlyLootFunction extends LootItemConditionalFunction {
	
	public static final MapCodec<DyeRandomlyLootFunction> CODEC = RecordCodecBuilder.mapCodec((instance) -> commonFields(instance).and(instance.group(
			SpectrumColorHelper.CODEC.listOf().fieldOf("colors").forGetter((function) -> function.colors),
			Codec.BOOL.optionalFieldOf("show_in_tooltip", false).forGetter((function) -> function.showInTooltip))
	).apply(instance, DyeRandomlyLootFunction::new));
	
	final List<Integer> colors;
	final boolean showInTooltip;
	
	DyeRandomlyLootFunction(List<LootItemCondition> conditions, List<Integer> colors, boolean showInTooltip) {
		super(conditions);
		this.colors = colors;
		this.showInTooltip = showInTooltip;
	}
	
	@Override
	public LootItemFunctionType<DyeRandomlyLootFunction> getType() {
		return SpectrumLootFunctionTypes.DYE_RANDOMLY;
	}
	
	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		stack.get(DataComponents.DYED_COLOR);
		if (stack.is(ItemTags.DYEABLE)) {
			RandomSource random = context.getRandom();
			int color = this.colors.isEmpty() ? SpectrumColorHelper.getRandomColor(random.nextInt()) : this.colors.get(random.nextInt(this.colors.size()));
			
			DyedItemColor component = new DyedItemColor(color, showInTooltip);
			stack.set(DataComponents.DYED_COLOR, component);
		}
		
		return stack;
	}
	
}
