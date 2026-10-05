package de.dafuqs.spectrum.loot.modifiers;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.loot.*;

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
		
		int treasureHunterLevel = 0;
		if (damageSource.is(SpectrumDamageTypeTags.ALWAYS_DROPS_MOB_HEAD)) {
			treasureHunterLevel = Integer.MAX_VALUE;
		} else if(SpectrumConfig.CONFIG.ChargedCreepersDropSpectrumMobHeads.get() && damageSource.getEntity() instanceof Creeper creeper && creeper.isPowered()) {
			treasureHunterLevel = Integer.MAX_VALUE;
		} else {
			ItemStack damageSourceWeapon = damageSource.getWeaponItem();
			if (damageSourceWeapon != null) {
				treasureHunterLevel = SpectrumEnchantmentHelper.getLevel(killed.registryAccess(), SpectrumEnchantmentKeys.TREASURE_HUNTER, damageSourceWeapon);
			}
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
				if(random.nextFloat() > SpectrumEntityAttributes.modifyLootChance(e.chance(), player) * treasureHunterLevel) {
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