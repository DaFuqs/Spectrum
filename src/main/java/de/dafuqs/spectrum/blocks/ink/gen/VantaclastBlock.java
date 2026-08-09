package de.dafuqs.spectrum.blocks.ink.gen;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import javax.annotation.*;
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
		tooltip.add(Component.translatable("block.spectrum.vantaclast.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new VantaclastBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkGeneratorTicker(level, blockEntityType, SpectrumBlockEntities.VANTACLAST.get());
	}
	
}
