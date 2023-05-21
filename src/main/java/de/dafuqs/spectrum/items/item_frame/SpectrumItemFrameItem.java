package de.dafuqs.spectrum.items.item_frame;

import net.minecraft.entity.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public abstract class SpectrumItemFrameItem extends ItemFrameItem {
	
	public SpectrumItemFrameItem(EntityType<? extends AbstractDecorationEntity> entityType, Item.Settings settings) {
		super(entityType, settings);
	}
	
	public abstract ItemFrameEntity getItemFrameEntity(World world, BlockPos blockPos, Direction direction);
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos blockPos = context.getBlockPos();
		Direction direction = context.getSide();
		BlockPos blockPos2 = blockPos.offset(direction);
		PlayerEntity playerEntity = context.getPlayer();
		ItemStack itemStack = context.getStack();
		if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
			return ActionResult.FAIL;
		} else {
			World world = context.getWorld();
			ItemFrameEntity invisibleItemFrameEntity = getItemFrameEntity(world, blockPos2, direction);
			
			NbtCompound nbtCompound = itemStack.getNbt();
			if (nbtCompound != null) {
				EntityType.loadFromEntityNbt(world, playerEntity, invisibleItemFrameEntity, nbtCompound);
			}
			
			if (invisibleItemFrameEntity.canStayAttached()) {
				if (!world.isClient) {
					invisibleItemFrameEntity.onPlace();
					world.emitGameEvent(playerEntity, GameEvent.ENTITY_PLACE, blockPos);
					world.spawnEntity(invisibleItemFrameEntity);
				}
				
				itemStack.decrement(1);
				return ActionResult.success(world.isClient);
			} else {
				return ActionResult.CONSUME;
			}
		}
	}
	
}
