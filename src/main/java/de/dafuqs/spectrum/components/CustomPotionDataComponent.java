package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import io.netty.buffer.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.function.*;

public record CustomPotionDataComponent(boolean unidentifiable, int additionalDrinkDuration) implements TooltipProvider {
	
	public static final CustomPotionDataComponent DEFAULT = new CustomPotionDataComponent(false, 0);
	
	public static final Codec<CustomPotionDataComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.fieldOf("unidentifiable").forGetter(c -> c.unidentifiable),
			Codec.INT.fieldOf("additional_drink_duration").forGetter(c -> c.additionalDrinkDuration)
	).apply(i, CustomPotionDataComponent::new));
	
	public static final StreamCodec<ByteBuf, CustomPotionDataComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, c -> c.unidentifiable,
			ByteBufCodecs.INT, c -> c.additionalDrinkDuration,
			CustomPotionDataComponent::new
	);
	
	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type) {
		int additionalDrinkDuration = this.additionalDrinkDuration();
		if (additionalDrinkDuration > 0) {
			tooltip.accept(Component.translatable("item.spectrum.potion.slower_to_drink"));
		} else if (additionalDrinkDuration < 0) {
			tooltip.accept(Component.translatable("item.spectrum.potion.faster_to_drink"));
		}
	}
	
}
