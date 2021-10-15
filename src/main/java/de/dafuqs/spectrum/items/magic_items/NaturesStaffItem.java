package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.sound.NaturesStaffUseSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NaturesStaffItem extends Item {

    public static final Map<Block, BlockState> BLOCK_CONVERSIONS = new HashMap<>() {{
        // BLOCKS
        put(Blocks.DIRT, Blocks.GRASS_BLOCK.getDefaultState());
        put(Blocks.DIRT_PATH, Blocks.DIRT.getDefaultState());
        put(Blocks.ROOTED_DIRT, Blocks.DIRT.getDefaultState());
        put(Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState());
        put(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE.getDefaultState());
        put(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS.getDefaultState());
        put(Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS.getDefaultState());

        // VEGETATION
        put(Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true));
        put(Blocks.DEAD_BUSH, Blocks.ACACIA_SAPLING.getDefaultState());

        // CORALS
        put(Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL.getDefaultState());
        put(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK.getDefaultState());
        put(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_FAN.getDefaultState());
        put(Blocks.DEAD_BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN.getDefaultState());
        put(Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL.getDefaultState());
        put(Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK.getDefaultState());
        put(Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN.getDefaultState());
        put(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN.getDefaultState());
        put(Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL.getDefaultState());
        put(Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK.getDefaultState());
        put(Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_FAN.getDefaultState());
        put(Blocks.DEAD_FIRE_CORAL_WALL_FAN, Blocks.FIRE_CORAL_WALL_FAN.getDefaultState());
        put(Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL.getDefaultState());
        put(Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK.getDefaultState());
        put(Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_FAN.getDefaultState());
        put(Blocks.DEAD_HORN_CORAL_WALL_FAN, Blocks.HORN_CORAL_WALL_FAN.getDefaultState());
        put(Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL.getDefaultState());
        put(Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK.getDefaultState());
        put(Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_FAN.getDefaultState());
        put(Blocks.DEAD_TUBE_CORAL_WALL_FAN, Blocks.TUBE_CORAL_WALL_FAN.getDefaultState());
    }};

    public NaturesStaffItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            startSoundInstance(user);
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Environment(EnvType.CLIENT)
    public void startSoundInstance(PlayerEntity user) {
        SpectrumClient.minecraftClient.getSoundManager().play(new NaturesStaffUseSoundInstance(user));
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        // trigger the items usage action every x ticks
        if(remainingUseTicks % 10 == 0) {
            if(MinecraftClient.getInstance().crosshairTarget.getType() == HitResult.Type.BLOCK) {

                MinecraftClient.getInstance().interactionManager.interactBlock(
                        MinecraftClient.getInstance().player,
                        MinecraftClient.getInstance().world,
                        MinecraftClient.getInstance().player.getActiveHand(),
                        (BlockHitResult) MinecraftClient.getInstance().crosshairTarget
                );

            }
        }
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();

        if(user != null && user.getItemUseTime() > 2) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();

            if (world.isClient) {
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isIn(SpectrumBlockTags.NATURES_STAFF_STACKABLE) || blockState.isOf(Blocks.BAMBOO)) {
                    int i = 0;
                    while(world.getBlockState(context.getBlockPos().up(i)).isOf(blockState.getBlock())) {
                        BoneMealItem.createParticles(world, context.getBlockPos().up(i), 3);
                        i++;
                    }
                    BoneMealItem.createParticles(world, context.getBlockPos().up(i+1), 5);
                    for(int j = 1; world.getBlockState(context.getBlockPos().down(j)).isOf(blockState.getBlock()); j++) {
                        BoneMealItem.createParticles(world, context.getBlockPos().down(j), 3);
                    }
                } else {
                    BoneMealItem.createParticles(world, blockPos, 15);
                }
            } else if(user.getItemUseTime() % 10 == 0) {
                BlockState blockState = world.getBlockState(blockPos);

                // hardcoded as convertible? => convert
                if (BLOCK_CONVERSIONS.containsKey(blockState.getBlock())) {
                    BlockState destinationState = BLOCK_CONVERSIONS.get(blockState.getBlock());
                    if (destinationState.getBlock() instanceof Waterloggable) {
                        if((world.getFluidState(blockPos.north()).isIn(FluidTags.WATER)
                                || world.getFluidState(blockPos.east()).isIn(FluidTags.WATER)
                                || world.getFluidState(blockPos.south()).isIn(FluidTags.WATER)
                                || world.getFluidState(blockPos.west()).isIn(FluidTags.WATER))) {
                            destinationState = destinationState.with(CoralBlock.WATERLOGGED, true);
                        } else {
                            destinationState = destinationState.with(CoralBlock.WATERLOGGED, false);
                        }
                    }
                    world.setBlockState(blockPos, destinationState, 3);
                    world.syncWorldEvent(2005, blockPos, 0);
                    return ActionResult.success(false);
                    // fertilizable? => grow
                } else if (useOnFertilizable(context.getStack(), world, blockPos)) {
                    world.syncWorldEvent(2005, blockPos, 0);
                    return ActionResult.success(false);
                    // blockstate marked as stackable? => stack on top!
                } else if(blockState.isIn(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
                    int i = 0;
                    BlockState state;
                    do {
                        state = world.getBlockState(context.getBlockPos().up(i));
                        i++;
                    } while (state.isOf(blockState.getBlock()));

                    BlockPos targetPos = context.getBlockPos().up(i-1);
                    BlockState targetState = blockState.getBlock().getDefaultState();
                    if(world.getBlockState(targetPos).isAir() && !world.isOutOfHeightLimit(targetPos.getY()) && blockState.getBlock().canPlaceAt(targetState, world, targetPos)) {
                        world.setBlockState(targetPos, targetState);
                        for(int j = 0; j < 20; j++) {
                            ((ServerWorld) world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, targetState), targetPos.getX() + world.getRandom().nextFloat(), targetPos.getY() + world.getRandom().nextFloat(), targetPos.getZ() + world.getRandom().nextFloat(), 1, 0, 0, 0, 0.1);
                        }
                        world.playSound(null, targetPos, targetState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
                        world.syncWorldEvent(2005, targetPos, 0);
                        return ActionResult.success(false);
                    }

                    // random tickable and whitelisted? => tick
                    // without whitelist we would be able to tick budding blocks, ...
                } else if(blockState.hasRandomTicks() && blockState.isIn(SpectrumBlockTags.NATURES_STAFF_TICKABLE)) {
                    if (world instanceof ServerWorld) {
                        blockState.randomTick((ServerWorld) world, blockPos, world.random);
                    }
                    world.syncWorldEvent(2005, blockPos, 0);
                    return ActionResult.success(false);
                } else {
                    BlockPos blockPos2 = blockPos.offset(context.getSide());
                    boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
                    if (bl && useOnGround(context.getStack(), world, blockPos2, context.getSide())) {
                        world.syncWorldEvent(2005, blockPos2, 0);
                        return ActionResult.success(false);
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    /**
     * Near identical copy of BonemealItem.useOnFertilizable
     * just with stack decrement removed
     */
    public static boolean useOnFertilizable(ItemStack stack, World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof Fertilizable) {
            Fertilizable fertilizable = (Fertilizable)blockState.getBlock();
            if (fertilizable.isFertilizable(world, pos, blockState, world.isClient)) {
                if (world instanceof ServerWorld) {
                    if (fertilizable.canGrow(world, world.random, pos, blockState)) {
                        fertilizable.grow((ServerWorld)world, world.random, pos, blockState);
                    }

                }

                return true;
            }
        }

        return false;
    }

    /**
     * Near identical copy of BonemealItem.useOnGround
     * just with stack decrement removed
     */
    public static boolean useOnGround(ItemStack stack, World world, BlockPos blockPos, @Nullable Direction facing) {
        if (world.getBlockState(blockPos).isOf(Blocks.WATER) && world.getFluidState(blockPos).getLevel() == 8) {
            if (!(world instanceof ServerWorld)) {
                return true;
            } else {
                Random random = world.getRandom();

                label80:
                for(int i = 0; i < 128; ++i) {
                    BlockPos blockPos2 = blockPos;
                    BlockState blockState = Blocks.SEAGRASS.getDefaultState();

                    for(int j = 0; j < i / 16; ++j) {
                        blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                        if (world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
                            continue label80;
                        }
                    }

                    Optional<RegistryKey<Biome>> optional = world.getBiomeKey(blockPos2);
                    if (Objects.equals(optional, Optional.of(BiomeKeys.WARM_OCEAN)) || Objects.equals(optional, Optional.of(BiomeKeys.DEEP_WARM_OCEAN))) {
                        if (i == 0 && facing != null && facing.getAxis().isHorizontal()) {
                            blockState = (BlockTags.WALL_CORALS.getRandom(world.random)).getDefaultState().with(DeadCoralWallFanBlock.FACING, facing);
                        } else if (random.nextInt(4) == 0) {
                            blockState = (BlockTags.UNDERWATER_BONEMEALS.getRandom(random)).getDefaultState();
                        }
                    }

                    if (blockState.isIn(BlockTags.WALL_CORALS)) {
                        for(int k = 0; !blockState.canPlaceAt(world, blockPos2) && k < 4; ++k) {
                            blockState = blockState.with(DeadCoralWallFanBlock.FACING, Direction.Type.HORIZONTAL.random(random));
                        }
                    }

                    if (blockState.canPlaceAt(world, blockPos2)) {
                        BlockState blockState2 = world.getBlockState(blockPos2);
                        if (blockState2.isOf(Blocks.WATER) && world.getFluidState(blockPos2).getLevel() == 8) {
                            world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
                        } else if (blockState2.isOf(Blocks.SEAGRASS) && random.nextInt(10) == 0) {
                            ((Fertilizable)Blocks.SEAGRASS).grow((ServerWorld)world, random, blockPos2, blockState2);
                        }
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

}
