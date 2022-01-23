package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ShootingStarItem extends BlockItem {
	
	private final ShootingStarBlock.Type type;
	
	public ShootingStarItem(ShootingStarBlock block, Settings settings) {
		super(block, settings);
		this.type = block.shootingStarType;
	}
	
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(context.getPlayer().isSneaking()) {
			// place as block
			return super.useOnBlock(context);
		} else {
			// place as entity
			World world = context.getWorld();
			
			if(!world.isClient) {
				ItemStack itemStack = context.getStack();
				Vec3d hitPos = context.getHitPos();
				PlayerEntity user = context.getPlayer();
				
				ShootingStarEntity shootingStarEntity = new ShootingStarEntity(context.getWorld(), hitPos.x, hitPos.y, hitPos.z);
				shootingStarEntity.setShootingStarType(this.type);
				shootingStarEntity.setYaw(user.getYaw());
				if (!world.isSpaceEmpty(shootingStarEntity, shootingStarEntity.getBoundingBox())) {
					return ActionResult.FAIL;
				} else {
					world.spawnEntity(shootingStarEntity);
					world.emitGameEvent(user, GameEvent.ENTITY_PLACE, context.getBlockPos());
					if (!user.getAbilities().creativeMode) {
						itemStack.decrement(1);
					}
					
					user.incrementStat(Stats.USED.getOrCreateStat(this));
				}
			}
			
			return ActionResult.success(world.isClient);
		}
	}
	
}
