package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class JadeiteFlowerBlock extends SpectrumFacingBlock implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public JadeiteFlowerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var facing = state.get(FACING);
        var root = pos.offset(facing.getOpposite());
        var supportBlock = world.getBlockState(root);
        return (facing.getAxis().isVertical() && supportBlock.isOf(SpectrumBlocks.JADEITE_LOTUS_STEM)) || supportBlock.isSideSolid(world, root, facing, SideShapeType.CENTER);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        world.createAndScheduleBlockTick(pos, this, 1);
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var amount = random.nextInt(18) + 9;
        for (int i = 0; i < amount; i++) {
            var xOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            var yOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            var zOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            world.addImportantParticle(ParticleTypes.END_ROD, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var handStack = player.getStackInHand(hand);
        var random = world.getRandom();

        if (handStack.isIn(ConventionalItemTags.SHEARS)) {
            int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, handStack);
            int itemCount = random.nextInt(fortuneLevel * 3 + 2) + 1;
    
            for (int i = itemCount; i > 0; i--) {
                ItemScatterer.spawn(world, pos.getX() + random.nextFloat() * 0.6 + 0.2, pos.getY() + random.nextFloat() * 0.6 + 0.2, pos.getZ() + random.nextFloat() * 0.6 + 0.2, new ItemStack(SpectrumItems.MOONSTRUCK_PETALS));
            }
    
            world.breakBlock(pos, false);
    
            var amount = random.nextInt(32) + 16;
            for (int i = 0; i < amount; i++) {
                var xOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 2F), -9F, 9F) + 0.5F;
                var yOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 2F), -9F, 9F) + 0.5F;
                var zOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 2F), -9F, 9F) + 0.5F;
                world.addImportantParticle(ParticleTypes.END_ROD, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, random.nextFloat() * 0.15 - 0.075, random.nextFloat() * 0.15 - 0.075, random.nextFloat() * 0.15 - 0.075);
            }

            player.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.BLOCKS, 1, 0.9F + random.nextFloat() * 0.2F);
            player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.BLOCKS, 0.75F, 1F + random.nextFloat() * 0.2F);
            handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));

            return ActionResult.success(world.isClient());
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        var amount = random.nextInt(18) + 9;
        for (int i = 0; i < amount; i++) {
            var xOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            var yOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            var zOffset = MathHelper.clamp(MathHelper.nextGaussian(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
            world.addImportantParticle(ParticleTypes.END_ROD, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025);
        }
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        super.appendProperties(builder);
    }
    
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
    
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canPlaceAt(state, world, pos))
            world.breakBlock(pos, true);
    }
}
