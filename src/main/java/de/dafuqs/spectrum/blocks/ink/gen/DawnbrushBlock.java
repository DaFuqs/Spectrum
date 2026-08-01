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

public class DawnbrushBlock extends InkGeneratorBlock {
	
	public static final MapCodec<DawnbrushBlock> CODEC = BlockBehaviour.simpleCodec(DawnbrushBlock::new);
	
	public DawnbrushBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends DawnbrushBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.dawnbrush.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DawnbrushBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkGeneratorTicker(level, blockEntityType, SpectrumBlockEntities.DAWNBRUSH.get());
	}

}
