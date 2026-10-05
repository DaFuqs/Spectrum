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

public record BeverageComponent(long daysAged, int alcoholPercent, float thickness) implements TooltipProvider {
	
	public static final BeverageComponent DEFAULT = new BeverageComponent(0, 0, 0);
	
	public static final Codec<BeverageComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.LONG.optionalFieldOf("days_aged", 0L).forGetter(BeverageComponent::daysAged),
			Codec.INT.optionalFieldOf("alcohol_percent", 0).forGetter(BeverageComponent::alcoholPercent),
			Codec.FLOAT.optionalFieldOf("thickness", 0f).forGetter(BeverageComponent::thickness)
	).apply(i, BeverageComponent::new));
	
	public static final StreamCodec<ByteBuf, BeverageComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG, BeverageComponent::daysAged,
			ByteBufCodecs.VAR_INT, BeverageComponent::alcoholPercent,
			ByteBufCodecs.FLOAT, BeverageComponent::thickness,
			BeverageComponent::new
	);
	
	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type) {
		if (daysAged > 365) {
			long ageInDays = daysAged % 365;
			long ageInYears = Math.floorDiv(daysAged, 365);
			if (ageInDays == 0)
				tooltip.accept(Component.translatable("item.spectrum.infused_beverage.tooltip.age_years", ageInYears, alcoholPercent).withStyle(ChatFormatting.GRAY));
			else
				tooltip.accept(Component.translatable("item.spectrum.infused_beverage.tooltip.age_composite", ageInYears, ageInDays, alcoholPercent).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.accept(Component.translatable("item.spectrum.infused_beverage.tooltip.age", daysAged, alcoholPercent).withStyle(ChatFormatting.GRAY));
		}
	}
	
}
