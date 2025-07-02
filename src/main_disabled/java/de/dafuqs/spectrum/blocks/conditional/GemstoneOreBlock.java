package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.state.*;

public class GemstoneOreBlock extends CloakedOreBlock {

	public final MapCodec<GemstoneOreBlock> codec;
	private final GemstoneColor gemstoneColor;
	
	public GemstoneOreBlock(IntProvider experienceDropped, Properties settings, GemstoneColor gemstoneColor, ResourceLocation cloakAdvancementIdentifier, BlockState cloakBlockState) {
		super(experienceDropped, settings, cloakAdvancementIdentifier, cloakBlockState);
		this.gemstoneColor = gemstoneColor;
		this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
				IntProvider.codec(0, 10).fieldOf("experience").forGetter(b -> ((ExperienceDroppingBlockAccessor) b).getXpRange()),
				propertiesCodec(),
				SpectrumRegistries.GEMSTONE_COLOR.byNameCodec().fieldOf("color").forGetter(b -> b.gemstoneColor),
				ResourceLocation.CODEC.fieldOf("advancement").forGetter(CloakedOreBlock::getCloakAdvancementIdentifier),
				BlockState.CODEC.fieldOf("cloak").forGetter(b -> b.getBlockStateCloaks().get(b.defaultBlockState()))
		).apply(instance, GemstoneOreBlock::new));
	}

	@Override
	public MapCodec<? extends GemstoneOreBlock> codec() {
		return codec;
	}

}
