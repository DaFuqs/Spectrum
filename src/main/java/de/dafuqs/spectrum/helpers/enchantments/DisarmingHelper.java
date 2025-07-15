package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class DisarmingHelper {
	
	public static void disarmEntity(LivingEntity livingEntity) {
		// since endermen save their carried block as blockState, not in hand
		// we have to use custom logic for them
		if (livingEntity instanceof EnderMan endermanEntity) {
			BlockState carriedBlockState = endermanEntity.getCarriedBlock();
			if (carriedBlockState != null) {
				Item item = carriedBlockState.getBlock().asItem();
				if (item != null) {
					endermanEntity.spawnAtLocation(item.getDefaultInstance());
					endermanEntity.setCarriedBlock(null);
				}
			}
			return;
		}
		
		// choose a random slot and drop its content
		List<EquipmentSlot> slots = new ArrayList<>(List.of(EquipmentSlot.values()));
		Collections.shuffle(slots);
		for (EquipmentSlot slot : slots) {
			ItemStack slotStack = livingEntity.getItemBySlot(slot);
			if (slotStack.isEmpty()) {
				continue;
			}
			
			// set to cannot drop? Skip that slot
			if (livingEntity instanceof Mob mobEntity && ((MobEntityAccessor) mobEntity).invokeGetEquipmentDropChance(slot) <= 0) {
				continue;
			}
			
			livingEntity.spawnAtLocation(slotStack);
			livingEntity.setItemSlot(slot, ItemStack.EMPTY);
			livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.NEUTRAL, 1.0F, 1.0F);
			break;
		}
	}
	
}
