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
import vazkii.botania.network.clientbound.*;
import vazkii.botania.xplat.*;

import java.util.*;

public class BlackestLotusItem extends Item implements ManaDissolvable {
	
	private static final int MANA = 1000000;
	
	public BlackestLotusItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void onDissolveTick(ManaPool pool, ItemEntity itemEntity) {
		if (pool.isFull() || pool.getCurrentMana() == 0) {
			return;
		}
		
		BlockPos pos = pool.getManaReceiverPos();
		if (!itemEntity.level().isClientSide()) {
			pool.receiveMana(MANA);
			EntityHelper.shrinkItem(itemEntity);
			XplatAbstractions.INSTANCE.sendToTracking(itemEntity, new BlackLotusDissolveEffectPacket(pos));
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
		
		tooltip.add(Component.translatable("item.spectrum.blackest_lotus.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
