package de.dafuqs.spectrum.loot.functions;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.LootContext.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.*;

public class GrantAdvancementLootFunction extends LootItemConditionalFunction {
	
	public static final MapCodec<GrantAdvancementLootFunction> CODEC = RecordCodecBuilder.mapCodec((instance) -> commonFields(instance).and(instance.group(
			EntityTarget.CODEC.fieldOf("entity").forGetter((function) -> function.entity),
			ResourceLocation.CODEC.listOf().fieldOf("tags").forGetter((function) -> function.ids)
	)).apply(instance, GrantAdvancementLootFunction::new));
	
	private final LootContext.EntityTarget entity;
	private final List<ResourceLocation> ids;
	
	public GrantAdvancementLootFunction(List<LootItemCondition> conditions, LootContext.EntityTarget entity, List<ResourceLocation> ids) {
		super(conditions);
		this.entity = entity;
		this.ids = ids;
	}
	
	public LootItemFunctionType<GrantAdvancementLootFunction> getType() {
		return SpectrumLootFunctionTypes.GRANT_ADVANCEMENT;
	}
	
	public Set<LootContextParam<?>> getReferencedContextParams() {
		return ImmutableSet.of(this.entity.getParam());
	}
	
	public ItemStack run(ItemStack stack, LootContext context) {
		Entity entity = context.getParamOrNull(this.entity.getParam());
		if (entity instanceof ServerPlayer player) {
			for (ResourceLocation id : this.ids) {
				SpectrumAdvancementCriteria.LOOT_FUNCTION_TRIGGER.trigger(player, id);
			}
		}
		return stack;
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(LootContext.EntityTarget target, List<ResourceLocation> ids) {
		return simpleBuilder((conditions) -> new GrantAdvancementLootFunction(conditions, target, ids));
	}
}