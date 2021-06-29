package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.entity.PigmentEntityTypes;
import de.dafuqs.pigment.entity.entity.InvisibleItemFrameEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class InvisibleItemFrameItem extends ItemFrameItem {

    public InvisibleItemFrameItem(EntityType<? extends AbstractDecorationEntity> entityType, Settings settings) {
        super(entityType, settings);
    }

    protected boolean canPlaceOn(PlayerEntity player, Direction side, ItemStack stack, BlockPos pos) {
        return !player.world.isOutOfHeightLimit(pos) && player.canPlaceOn(pos, side, stack);
    }

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
            InvisibleItemFrameEntity invisibleItemFrameEntity = new InvisibleItemFrameEntity(PigmentEntityTypes.INVISIBLE_ITEM_FRAME, world, blockPos2, direction);

            NbtCompound nbtCompound = itemStack.getTag();
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
