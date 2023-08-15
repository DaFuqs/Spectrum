package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class JadeiteFlowerBlock extends SpectrumFacingBlock {
    
    public JadeiteFlowerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState());
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var facing = state.get(FACING);
        var root = pos.offset(facing.getOpposite());
        var supportBlock = world.getBlockState(root);
        return (facing.getAxis().isVertical() && supportBlock.isOf(SpectrumBlocks.JADEITE_LOTUS_STEM)) || supportBlock.isSideSolid(world, root, facing, SideShapeType.CENTER);
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return SpectrumItems.JADEITE_LOTUS_BULB.getDefaultStack();
    }
    
    @Override
	@SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 1);
        }
    
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
	@SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var handStack = player.getStackInHand(hand);
        var random = world.getRandom();

        if (handStack.isIn(ConventionalItemTags.SHEARS)) {
            int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, handStack);
            int itemCount = random.nextInt(fortuneLevel * 3 + 2) + 1;
            player.getInventory().offerOrDrop(new ItemStack(SpectrumItems.JADEITE_PETALS, itemCount));
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
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        }
    }
    
}
