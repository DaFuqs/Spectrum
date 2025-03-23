package de.dafuqs.spectrum.compat.botania;

import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.botania.api.item.*;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.handler.*;
import vazkii.botania.common.helper.*;
import vazkii.botania.network.*;
import vazkii.botania.network.clientbound.*;
import vazkii.botania.xplat.*;

import java.util.*;

public class BlackestLotusItem extends Item implements ManaDissolvable {
	
	public BlackestLotusItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onDissolveTick(ManaPool manaPool, ItemEntity itemEntity) {
		if (manaPool.isFull() || manaPool.getCurrentMana() == 0) {
			return;
		}
		
		BlockPos pos = manaPool.getManaReceiverPos();
		if (!itemEntity.getWorld().isClient) {
			manaPool.receiveMana(manaPool.getMaxMana());
			EntityHelper.shrinkItem(itemEntity);
			XplatAbstractions.INSTANCE.sendToTracking(itemEntity, new BotaniaEffectPacket(EffectType.BLACK_LOTUS_DISSOLVE, pos.getX(), pos.getY() + 0.5, pos.getZ()));
		}
		
		itemEntity.playSound(BotaniaSounds.blackLotus, 1F, 0.25F);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		tooltip.add(Text.translatable("item.spectrum.blackest_lotus.tooltip").formatted(Formatting.GRAY));
	}
	
}
