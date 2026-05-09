package de.dafuqs.spectrum.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import javax.annotation.*;

import java.util.*;

public class TreasureHunterLootModifier extends LootModifier {
	
	public record Entry(EntityPredicate predicate, ItemStack headStack, float chance) {
		
		public static final MapCodec<TreasureHunterLootModifier.Entry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				EntityPredicate.CODEC.fieldOf("entity").forGetter(m -> m.predicate),
				ItemStack.CODEC.fieldOf("head_stack").forGetter(m -> m.headStack),
				Codec.FLOAT.fieldOf("drop_chance_per_level").forGetter(m -> m.chance)
		).apply(instance, TreasureHunterLootModifier.Entry::new));
		
	}
	
	public static final MapCodec<TreasureHunterLootModifier> CODEC = RecordCodecBuilder.mapCodec(i ->
			LootModifier.codecStart(i).and(
					Entry.CODEC.codec().listOf().fieldOf("entries").forGetter(o -> o.entries)
			).apply(i, TreasureHunterLootModifier::new));
	
	protected List<TreasureHunterLootModifier.Entry> entries;
	
	protected TreasureHunterLootModifier(LootItemCondition[] conditionsIn, List<TreasureHunterLootModifier.Entry> entries) {
		super(conditionsIn);
		this.entries = entries;
	}
	
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> original, LootContext lootContext) {
		Entity killed = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (killed == null) {
			return original;
		}
		
		DamageSource damageSource = lootContext.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
		if (damageSource == null) {
			return original;
		}
		
		ItemStack damageSourceWeapon = damageSource.getWeaponItem();
		int treasureHunterLevel;
		if(damageSource.is(SpectrumDamageTypeTags.ALWAYS_DROPS_MOB_HEAD)) {
			treasureHunterLevel = Integer.MAX_VALUE;
		} else if(damageSourceWeapon != null) {
			treasureHunterLevel = SpectrumEnchantmentHelper.getLevel(killed.registryAccess(), SpectrumEnchantmentKeys.TREASURE_HUNTER, damageSourceWeapon);
		} else {
			return original;
		}
		
		if(treasureHunterLevel <= 0) {
			return original;
		}
		
		Player player = lootContext.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
		ServerLevel serverLevel = (ServerLevel) killed.level();
		Vec3 pos = killed.position();
		RandomSource random = lootContext.getRandom();
		for(Entry e : this.entries) {
			if(e.predicate.matches(serverLevel, pos, killed)) {
				if(random.nextFloat() > e.chance() * treasureHunterLevel) {
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
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
	
}