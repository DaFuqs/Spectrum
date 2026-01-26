package de.dafuqs.spectrum.loot.loot_modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.progression.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.*;

import java.util.*;

// TODO: migrate the remaining entries to the json
// TODO: test advancement trigger
public class TreasureHunterModifier extends LootModifier {
	
	public record Entry(EntityPredicate predicate, ItemStack headStack, float chance) {
		
		public static final MapCodec<TreasureHunterModifier.Entry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				EntityPredicate.CODEC.fieldOf("entity").forGetter(m -> m.predicate),
				ItemStack.CODEC.fieldOf("head_stack").forGetter(m -> m.headStack),
				Codec.FLOAT.fieldOf("drop_chance_per_level").forGetter(m -> m.chance)
		).apply(instance, TreasureHunterModifier.Entry::new));
		
	}
	
	public static final MapCodec<TreasureHunterModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
			LootModifier.codecStart(i).and(
					Entry.CODEC.codec().listOf().fieldOf("entries").forGetter(o -> o.entries)
			).apply(i, TreasureHunterModifier::new));
	
	protected List<TreasureHunterModifier.Entry> entries;
	
	protected TreasureHunterModifier(LootItemCondition[] conditionsIn, List<TreasureHunterModifier.Entry> entries) {
		super(conditionsIn);
	}
	
	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> original, LootContext lootContext) {
		Entity killed = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
		Player player = lootContext.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
		DamageSource damageSource = lootContext.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
		
		if (damageSource == null) {
			return original;
		}
		
		ItemStack damageSourceWeapon = damageSource.getWeaponItem();
		if (damageSourceWeapon == null) {
			return original;
		}
		
		ServerLevel serverLevel = (ServerLevel) killed.level();
		Vec3 pos = killed.position();
		RandomSource random = lootContext.getRandom();
		for(Entry e : this.entries) {
			if(e.predicate.matches(serverLevel, pos, killed)) {
				if(random.nextFloat() > e.chance()) {
					continue;
				}
				
				original.add(e.headStack);
				if (player instanceof ServerPlayer sp) {
					SpectrumAdvancementCriteria.LOOT_FUNCTION_TRIGGER.trigger(sp, SpectrumCommon.locate("mob_head"));
					SpectrumAdvancementCriteria.LOOT_FUNCTION_TRIGGER.trigger(sp, BuiltInRegistries.ITEM.getKey(e.headStack.getItem()));
				}
			}
		}
		
		return original;
	}
	
	@Override
	public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
	
}