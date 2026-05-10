package de.dafuqs.spectrum.blocks;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.inventories.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class BedrockAnvilBlock extends AnvilBlock {
	
	public static final MapCodec<BedrockAnvilBlock> CODEC = simpleCodec(BedrockAnvilBlock::new);
	
	private static final Component TITLE = Component.translatable("container.spectrum.bedrock_anvil");
	
	public BedrockAnvilBlock(Properties settings) {
		super(settings);
	}

//	@Override
//	public MapCodec<? extends BedrockAnvilBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}
	
	// Heavier => More damage
	@Override
	protected void falling(FallingBlockEntity entity) {
		entity.setHurtsEntities(3.0F, 64);
	}

	@Override
	public @Nullable MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
		return new SimpleMenuProvider((syncId, inventory, player) -> new BedrockAnvilScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, pos)), TITLE);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("container.spectrum.bedrock_anvil.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("container.spectrum.bedrock_anvil.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("container.spectrum.bedrock_anvil.tooltip3").withStyle(ChatFormatting.GRAY));
	}
	
}
