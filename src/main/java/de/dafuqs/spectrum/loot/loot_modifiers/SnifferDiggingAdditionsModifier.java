package de.dafuqs.spectrum.loot.loot_modifiers;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.loot.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SnifferDiggingAdditionsModifier extends LootModifier {
	
	public static final MapCodec<SnifferDiggingAdditionsModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
			LootModifier.codecStart(i).and(i.group(
							ResourceLocation.CODEC.listOf().fieldOf("targets").forGetter(m -> m.targets),
									IntProvider.POSITIVE_CODEC.fieldOf("count").forGetter(m -> m.count),
									Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance),
									Codec.BOOL.fieldOf("replace").forGetter(m -> m.replace)
							)
					).apply(i, SnifferDiggingAdditionsModifier::new));
	
	private final List<ResourceLocation> targets;
	private final IntProvider count;
	private final float chance;
	private final boolean replace;
	
	protected SnifferDiggingAdditionsModifier(LootItemCondition[] conditionsIn, List<ResourceLocation> targets, IntProvider count, float chance, boolean replace) {
		super(conditionsIn);
		this.targets = targets;
		this.count = count;
		this.chance = chance;
		this.replace = replace;
	}
	
	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> original, LootContext lootContext) {
		ResourceLocation id = lootContext.getQueriedLootTableId();
		RandomSource random = lootContext.getRandom();
		Item item = random.nextFloat() < 0.25F ? SpectrumBlocks.WEEPING_GALA_SPRIG.asItem() : SpectrumItems.NIGHTDEW_SPROUT.get();
		
		if (!targets.contains(id) || random.nextFloat() > chance)
			return original;
		
		if (replace) {
			original.clear();
		}
		
		original.add(new ItemStack(item, count.sample(random)));
		return original;
	}
	
	@Override
	public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
	
}