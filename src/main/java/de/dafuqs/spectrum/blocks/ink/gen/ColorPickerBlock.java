package de.dafuqs.spectrum.blocks.ink.gen;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ColorPickerBlock extends BaseInkBlock {
	
	public static final MapCodec<ColorPickerBlock> CODEC = simpleCodec(ColorPickerBlock::new);
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public ColorPickerBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends ColorPickerBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
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
	
	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkBlockTicker(level, blockEntityType, SpectrumBlockEntities.COLOR_PICKER.get());
	}
	
}
