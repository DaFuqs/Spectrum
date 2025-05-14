package de.dafuqs.spectrum.blocks.conditional.amaranth;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AmaranthBushelBlock extends FlowerBlock implements RevelationAware {

	public static final MapCodec<AmaranthBushelBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects),
			propertiesCodec()
	).apply(instance, AmaranthBushelBlock::new));
	
	public AmaranthBushelBlock(Holder<MobEffect> stewEffect, float effectLengthInSeconds, BlockBehaviour.Properties settings) {
		this(makeEffectList(stewEffect, effectLengthInSeconds), settings);
	}
	
	public AmaranthBushelBlock(SuspiciousStewEffects stewEffects, BlockBehaviour.Properties settings) {
		super(stewEffects, settings);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends AmaranthBushelBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_AMARANTH;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.defaultBlockState(), Blocks.FERN.defaultBlockState());
		return map;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.FERN.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.amaranthBushelBlockColorProvider != null && SpectrumColorProviders.amaranthBushelItemColorProvider != null) {
			SpectrumColorProviders.amaranthBushelBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.amaranthBushelItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.amaranthBushelBlockColorProvider != null && SpectrumColorProviders.amaranthBushelItemColorProvider != null) {
			SpectrumColorProviders.amaranthBushelBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.amaranthBushelItemColorProvider.setShouldApply(true);
		}
	}

}
