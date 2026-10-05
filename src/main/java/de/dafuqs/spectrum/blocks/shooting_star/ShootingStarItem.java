package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class ShootingStarItem extends BlockItem implements ShootingStar {
	
	private final Variant shootingStarType;
	
	public ShootingStarItem(ShootingStarBlock block, Properties settings) {
		super(block, settings);
		this.shootingStarType = block.shootingStarType;
	}
	
	public static ItemStack getWithRemainingHits(ShootingStarItem shootingStarItem, int remainingHits, boolean hardened) {
		return getWithRemainingHits(shootingStarItem.getDefaultInstance(), remainingHits, hardened);
	}
	
	public static ItemStack getWithRemainingHits(ItemStack stack, int remainingHits, boolean hardened) {
		ShootingStarComponent component = new ShootingStarComponent(remainingHits, hardened);
		stack.set(SpectrumDataComponentTypes.SHOOTING_STAR, component);
		return stack;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer().isShiftKeyDown()) {
			// place as block
			return super.useOn(context);
		} else {
			// place as entity
			Level world = context.getLevel();
			
			if (!world.isClientSide()) {
				ItemStack itemStack = context.getItemInHand();
				Vec3 hitPos = context.getClickLocation();
				Player user = context.getPlayer();
				
				ShootingStarEntity shootingStarEntity = getEntityForStack(context.getLevel(), hitPos, itemStack);
				shootingStarEntity.setYRot(user.getYRot());
				if (!world.noCollision(shootingStarEntity, shootingStarEntity.getBoundingBox())) {
					return InteractionResult.FAIL;
				} else {
					world.addFreshEntity(shootingStarEntity);
					world.gameEvent(user, GameEvent.ENTITY_PLACE, context.getClickedPos());
					if (!user.getAbilities().instabuild) {
						itemStack.shrink(1);
					}
					
					user.awardStat(Stats.ITEM_USED.get(this));
				}
			}
			
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
	}
	
	public ShootingStarEntity getEntityForStack(Level world, Vec3 pos, ItemStack stack) {
		return new ShootingStarEntity(world, pos.x, pos.y, pos.z, this.shootingStarType, true, getRemainingHits(stack), isHardened(stack));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		if (isHardened(stack)) {
			tooltip.add(Component.translatable("item.spectrum.shooting_star.tooltip.hardened").withStyle(ChatFormatting.GRAY));
		}
	}
	
	public Variant getShootingStarType() {
		return this.shootingStarType;
	}
	
	public static boolean isHardened(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.SHOOTING_STAR, ShootingStarComponent.DEFAULT).hardened();
	}
	
	public static int getRemainingHits(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.SHOOTING_STAR, ShootingStarComponent.DEFAULT).remainingHits();
	}
	
	public static void setHardened(ItemStack stack) {
		ShootingStarComponent component = stack.getOrDefault(SpectrumDataComponentTypes.SHOOTING_STAR, ShootingStarComponent.DEFAULT);
		component = new ShootingStarComponent(component.remainingHits(), true);
		stack.set(SpectrumDataComponentTypes.SHOOTING_STAR, component);
	}
	
}
