package de.dafuqs.spectrum.blocks.conditional.resonant_lily;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class ResonantLilyBlock extends FlowerBlock implements RevelationAware {
	
	public static final ResourceLocation ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("midgame/collect_resonant_lily");

	public static final MapCodec<ResonantLilyBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects),
			propertiesCodec()
	).apply(instance, ResonantLilyBlock::new));
	
	public ResonantLilyBlock(Holder<MobEffect> stewEffect, float effectLengthInSeconds, BlockBehaviour.Properties settings) {
		this(makeEffectList(stewEffect, effectLengthInSeconds), settings);
	}
	
	public ResonantLilyBlock(SuspiciousStewEffects stewEffects, BlockBehaviour.Properties settings) {
		super(stewEffects, settings);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends ResonantLilyBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.defaultBlockState(), Blocks.WHITE_TULIP.defaultBlockState());
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Items.WHITE_TULIP);
	}
	
}
