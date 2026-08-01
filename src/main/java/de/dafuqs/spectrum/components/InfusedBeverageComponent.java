package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import io.netty.buffer.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.function.*;

public record InfusedBeverageComponent(String variant, int color) implements TooltipProvider {
	
	public static final InfusedBeverageComponent DEFAULT = new InfusedBeverageComponent("unknown", 0xfff4c6cb);
	
	public static final Codec<InfusedBeverageComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("variant", "unknown").forGetter(InfusedBeverageComponent::variant),
			SpectrumColorHelper.CODEC.optionalFieldOf("color", 0xfff4c6cb).forGetter(InfusedBeverageComponent::color)
	).apply(i, InfusedBeverageComponent::new));
	
	public static final StreamCodec<ByteBuf, InfusedBeverageComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, InfusedBeverageComponent::variant,
			ByteBufCodecs.VAR_INT, InfusedBeverageComponent::color,
			InfusedBeverageComponent::new
	);
	
	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type) {
		tooltip.accept(Component.translatable("item.spectrum.infused_beverage.tooltip.variant." + variant).withStyle(ChatFormatting.YELLOW));
	}
	
}
