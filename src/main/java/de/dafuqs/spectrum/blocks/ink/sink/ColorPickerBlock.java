package de.dafuqs.spectrum.blocks.ink.sink;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColorPickerBlock extends BaseInkBlock {
	
	public static final MapCodec<ColorPickerBlock> CODEC = simpleCodec(ColorPickerBlock::new);
	
	public ColorPickerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends ColorPickerBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.color_picker.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ColorPickerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? null : Support.checkType(type, SpectrumBlockEntities.COLOR_PICKER.get(), ColorPickerBlockEntity::serverTick);
	}
	
}
