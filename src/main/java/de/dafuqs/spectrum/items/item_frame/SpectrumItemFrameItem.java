package de.dafuqs.spectrum.items.item_frame;

import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;

public abstract class SpectrumItemFrameItem extends ItemFrameItem {
	
	public SpectrumItemFrameItem(EntityType<? extends HangingEntity> entityType, Item.Properties settings) {
		super(entityType, settings);
	}
	
	public abstract ItemFrame getItemFrameEntity(Level world, BlockPos blockPos, Direction direction);
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos blockPos = context.getClickedPos();
		Direction direction = context.getClickedFace();
		BlockPos blockPos2 = blockPos.relative(direction);
		Player playerEntity = context.getPlayer();
		ItemStack itemStack = context.getItemInHand();
		if (playerEntity != null && !this.mayPlace(playerEntity, direction, itemStack, blockPos2)) {
			return InteractionResult.FAIL;
		} else {
			Level world = context.getLevel();
			ItemFrame invisibleItemFrameEntity = getItemFrameEntity(world, blockPos2, direction);
			
			var nbtComponent = itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
			if (!nbtComponent.isEmpty()) {
				EntityType.updateCustomEntityTag(world, playerEntity, invisibleItemFrameEntity, nbtComponent);
			}
			
			if (invisibleItemFrameEntity.survives()) {
				if (!world.isClientSide) {
					invisibleItemFrameEntity.playPlacementSound();
					world.gameEvent(playerEntity, GameEvent.ENTITY_PLACE, blockPos);
					world.addFreshEntity(invisibleItemFrameEntity);
				}
				
				itemStack.shrink(1);
				return InteractionResult.sidedSuccess(world.isClientSide);
			} else {
				return InteractionResult.CONSUME;
			}
		}
	}
	
}
