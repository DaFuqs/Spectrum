package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import io.netty.buffer.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public record WithMilkComponent() implements TooltipProvider {
	
	public static final Codec<WithMilkComponent> CODEC = Codec.unit(WithMilkComponent::new);
	public static final StreamCodec<ByteBuf, WithMilkComponent> PACKET_CODEC = StreamCodec.unit(new WithMilkComponent());
	
	@Override
	public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag type) {
		tooltip.accept(Component.translatable("item.spectrum.restoration_tea.tooltip_milk"));
	}
	
}
