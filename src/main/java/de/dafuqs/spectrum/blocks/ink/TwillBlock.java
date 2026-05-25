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

import javax.annotation.Nullable;
import java.util.*;

public class TwillBlock extends InkGeneratorBlock {
	
	public static final MapCodec<TwillBlock> CODEC = simpleCodec(TwillBlock::new);
	
	public TwillBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends TwillBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.twill.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TwillBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createInkGeneratorTicker(level, blockEntityType, SpectrumBlockEntities.TWILL.get());
	}
	
}
