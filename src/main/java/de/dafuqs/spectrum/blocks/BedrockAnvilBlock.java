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
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.*;

import java.util.*;

public class BedrockAnvilBlock extends AnvilBlock {
	
	public static final MapCodec<BedrockAnvilBlock> CODEC = simpleCodec(BedrockAnvilBlock::new);
	
	private static final VoxelShape X_BASE = Block.box(2.0, 0.0, 3.0, 14.0, 3.0, 13.0);
	private static final VoxelShape X_LEG1 = Block.box(3.0, 3.0, 4.0, 13.0, 4.0, 12.0);
	private static final VoxelShape X_LEG2 = Block.box(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);
	private static final VoxelShape X_TOP = Block.box(1.0, 7.0, 2.0, 15.0, 13.0, 14.0);
	
	private static final VoxelShape Z_BASE = Block.box(3.0, 0.0, 2.0, 13.0, 3.0, 14.0);
	private static final VoxelShape Z_LEG1 = Block.box(4.0, 2.0, 3.0, 12.0, 4.0, 13.0);
	private static final VoxelShape Z_LEG2 = Block.box(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);
	private static final VoxelShape Z_TOP = Block.box(2.0, 7.0, 1.0, 14.0, 13.0, 15.0);
	
	private static final VoxelShape X_AXIS_AABB = Shapes.or(X_BASE, X_LEG1, X_LEG2, X_TOP);
	private static final VoxelShape Z_AXIS_AABB = Shapes.or(Z_BASE, Z_LEG1, Z_LEG2, Z_TOP);
	
	private static final Component TITLE = Component.translatable("container.spectrum.bedrock_anvil");
	
	public BedrockAnvilBlock(Properties settings) {
		super(settings);
	}

//	@Override
//	public MapCodec<? extends BedrockAnvilBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
	}
	
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
