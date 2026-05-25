package de.dafuqs.spectrum.blocks.ink;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VantaclastBlock extends InkGeneratorBlock {
	
	public static final MapCodec<VantaclastBlock> CODEC = simpleCodec(VantaclastBlock::new);
	
	public VantaclastBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends VantaclastBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.vantaclast.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new VantaclastBlockEntity(pos, state);
	}
	
}
