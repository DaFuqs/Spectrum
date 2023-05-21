package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.inventories.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockAnvilBlock extends AnvilBlock {
	
	private static final Text TITLE = Text.translatable("container.spectrum.bedrock_anvil");
	
	public BedrockAnvilBlock(Settings settings) {
		super(settings);
	}
	
	// Heavier => More damage
	@Override
	protected void configureFallingBlockEntity(FallingBlockEntity entity) {
		entity.setHurtEntities(3.0F, 64);
	}
	
	@Override
	@Nullable
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new BedrockAnvilScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("container.spectrum.bedrock_anvil.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("container.spectrum.bedrock_anvil.tooltip2").formatted(Formatting.GRAY));
	}
	
}
