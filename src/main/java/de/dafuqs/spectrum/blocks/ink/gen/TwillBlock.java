package de.dafuqs.spectrum.blocks.ink.gen;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.ink.*;
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
import org.jspecify.annotations.Nullable;

import javax.annotation.*;
import java.util.*;

public class TwillBlock extends BaseInkBlock {
	
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
	public static final MapCodec<TwillBlock> CODEC = simpleCodec(TwillBlock::new);
	
	public TwillBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends TwillBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.twill.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.spectrum.twill.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TwillBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkBlockTicker(level, blockEntityType, SpectrumBlockEntities.TWILL.get());
	}
	
}
