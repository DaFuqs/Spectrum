package de.dafuqs.spectrum.compat.botania;

import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import vazkii.botania.api.item.*;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.handler.*;
import vazkii.botania.common.helper.*;
import vazkii.botania.network.*;
import vazkii.botania.network.clientbound.*;
import vazkii.botania.xplat.*;

import java.util.*;

public class LeastBlackLotusItem extends Item implements ManaDissolvable {
	
	public LeastBlackLotusItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void onDissolveTick(ManaPool manaPool, ItemEntity itemEntity) {
		if (manaPool.isFull() || manaPool.getCurrentMana() == 0) {
			return;
		}
		
		BlockPos pos = manaPool.getManaReceiverPos();
		if (!itemEntity.level().isClientSide) {
			manaPool.receiveMana(1);
			EntityHelper.shrinkItem(itemEntity);
			XplatAbstractions.INSTANCE.sendToTracking(itemEntity, new BotaniaEffectPacket(EffectType.BLACK_LOTUS_DISSOLVE, pos.getX(), pos.getY() + 0.5, pos.getZ()));
		}
		
		itemEntity.playSound(BotaniaSounds.blackLotus, 1F, 0.25F);
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		tooltip.add(Component.translatable("item.spectrum.least_black_lotus.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
