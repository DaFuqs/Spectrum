package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.LeavesBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NephriteBlossomLeavesBlock extends LeavesBlock implements Fertilizable {

    public static final IntProperty AGE = Properties.AGE_3;
    public static final BooleanProperty COLLAPSING = BooleanProperty.of("collapsing");

    public NephriteBlossomLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == 2) {
            var handStack = player.getStackInHand(hand);
            var fortune = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, handStack) / 2;

            var peach = new ItemStack(SpectrumItems.GLASS_PEACH, world.getRandom().nextInt(fortune) + 1);

            if (handStack.isEmpty()) {
                player.setStackInHand(hand, peach);
            }
            else {
                player.giveItemStack(peach);
            }

            world.setBlockState(pos, state.with(AGE, 0));
            player.playSound(SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1, 1 + player.getRandom().nextFloat() * 0.25F);
            return ActionResult.success(world.isClient());
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var age = state.get(AGE);
        var leafSum = 0;

        if (state.get(PERSISTENT)) {
            super.randomTick(state, world, pos, random);
            return;
        }

        for (BlockPos iPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            var leafState = world.getBlockState(iPos);

            if (leafState.isOf(this)) {
                leafSum += (leafState.get(AGE).byteValue() + 1) * 3;
            }
        }

        leafSum = Math.max(leafSum, 0) + 1;

        if (random.nextInt(leafSum + 1) != 0) {
            super.randomTick(state, world, pos, random);
            return;
        }

        if (age == 2) {
            var drop = new ItemStack(SpectrumItems.GLASS_PEACH);
            var dropPos = pos.mutableCopy();
            while(world.getBlockState(dropPos).isOf(this) && pos.getY() - dropPos.getY() < 32) {
                dropPos.move(0, -1, 0);
            }
            var entity = new ItemEntity(world, dropPos.getX() + 0.5, dropPos.getY() + 0.15, dropPos.getZ() + 0.5, drop);
            world.spawnEntity(entity);
            world.setBlockState(pos, state.with(AGE, 0));
        }
        else {
            world.setBlockState(pos, state.with(AGE, age + 1));
        }

        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.appendProperties(builder);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) != 2;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var age = state.get(AGE) + 1;

        if(age > 2)
            return;

        world.setBlockState(pos, state.with(AGE, age));
    }
}
