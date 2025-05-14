package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.food.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public record PairedFoodComponent(Item item, boolean consumeAndApplyRequiredStack, FoodProperties bonusFoodComponent) {
	
	//TODO what is bonusFoodComponent used for?
	public static final Codec<PairedFoodComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(PairedFoodComponent::item),
			Codec.BOOL.optionalFieldOf("consume_and_apply_required_stack", true).forGetter(PairedFoodComponent::consumeAndApplyRequiredStack),
			FoodProperties.DIRECT_CODEC.optionalFieldOf("bonus_food_component", new FoodProperties.Builder().build()).forGetter(PairedFoodComponent::bonusFoodComponent)
	).apply(instance, PairedFoodComponent::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, PairedFoodComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.ITEM), PairedFoodComponent::item,
			ByteBufCodecs.BOOL, PairedFoodComponent::consumeAndApplyRequiredStack,
			FoodProperties.DIRECT_STREAM_CODEC, PairedFoodComponent::bonusFoodComponent,
			PairedFoodComponent::new
	);
	
	public void tryEatFood(Level world, LivingEntity livingEntity, ItemStack eatenStack) {
		if (!(livingEntity instanceof Player player)) {
			return;
		}
		
		// does the entity have a matching stack in their inv?
		int requiredSlotStack = -1;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (player.getInventory().getItem(i).is(this.item)) {
				requiredSlotStack = i;
				break;
			}
		}
		if (requiredSlotStack == -1) {
			return;
		}
		ItemStack foundRequiredStack = player.getInventory().getItem(requiredSlotStack);
		
		// should the required stack be consumed, too?
		if (consumeAndApplyRequiredStack) {
			FoodProperties component = foundRequiredStack.get(DataComponents.FOOD);
			if (component != null) {
				player.eat(world, foundRequiredStack, component);
			}
		}
		
		if (player instanceof ServerPlayer serverPlayer) {
			SpectrumAdvancementCriteria.CONDITIONAL_FOOD_EATEN.trigger(serverPlayer, eatenStack, foundRequiredStack);
		}
	}
	
}
