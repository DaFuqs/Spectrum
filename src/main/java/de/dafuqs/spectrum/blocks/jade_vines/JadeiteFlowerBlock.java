package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.blocks.decoration.SpectrumFacingBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

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

            var roll = random.nextInt(EnchantmentHelper.getLevel(Enchantments.FORTUNE, handStack) * 3 + 2)  + 1;

            for (int i = roll; i > 0; i--) {
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

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canPlaceAt(state, world, pos))
            world.breakBlock(pos, true);
    }
}
