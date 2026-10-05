package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import io.netty.buffer.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.function.*;

public record JadeWineComponent(float bloominess, boolean sweetened) implements TooltipProvider {
	
	public static final JadeWineComponent DEFAULT = new JadeWineComponent(0, false);
	
	public static final Codec<JadeWineComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.optionalFieldOf("bloominess", 0f).forGetter(JadeWineComponent::bloominess),
			Codec.BOOL.optionalFieldOf("sweetened", false).forGetter(JadeWineComponent::sweetened)
	).apply(i, JadeWineComponent::new));
	
	public static final StreamCodec<ByteBuf, JadeWineComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, JadeWineComponent::bloominess,
			ByteBufCodecs.BOOL, JadeWineComponent::sweetened,
			JadeWineComponent::new
	);
	
	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type) {
		if (sweetened)
			tooltip.accept(Component.translatable("item.spectrum.jade_wine.tooltip.bloominess_sweetened", bloominess).withStyle(ChatFormatting.GRAY));
		else
			tooltip.accept(Component.translatable("item.spectrum.jade_wine.tooltip.bloominess", bloominess).withStyle(ChatFormatting.GRAY));
	}
	
}
