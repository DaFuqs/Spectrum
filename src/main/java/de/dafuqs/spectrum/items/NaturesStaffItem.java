package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.mixin.FluidTagsAccessor;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.sound.NaturesStaffUseSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class NaturesStaffItem extends Item {

    public HashMap<Block, BlockState> blockConversions = new HashMap<>();

    public NaturesStaffItem(Settings settings) {
        super(settings);
        // BLOCKS
        blockConversions.put(Blocks.DIRT, Blocks.GRASS_BLOCK.getDefaultState());
        blockConversions.put(Blocks.DIRT_PATH, Blocks.DIRT.getDefaultState());
        blockConversions.put(Blocks.ROOTED_DIRT, Blocks.DIRT.getDefaultState());
        blockConversions.put(Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState());

        // VEGETATION
        blockConversions.put(Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES.getDefaultState());
        blockConversions.put(Blocks.DEAD_BUSH, Blocks.ACACIA_SAPLING.getDefaultState());

        // CORALS
        blockConversions.put(Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL.getDefaultState());
        blockConversions.put(Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK.getDefaultState());
        blockConversions.put(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL.getDefaultState());
        blockConversions.put(Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK.getDefaultState());
        blockConversions.put(Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL.getDefaultState());
        blockConversions.put(Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK.getDefaultState());
        blockConversions.put(Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_FIRE_CORAL_WALL_FAN, Blocks.FIRE_CORAL_WALL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL.getDefaultState());
        blockConversions.put(Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK.getDefaultState());
        blockConversions.put(Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_HORN_CORAL_WALL_FAN, Blocks.HORN_CORAL_WALL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL.getDefaultState());
        blockConversions.put(Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK.getDefaultState());
        blockConversions.put(Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_FAN.getDefaultState());
        blockConversions.put(Blocks.DEAD_TUBE_CORAL_WALL_FAN, Blocks.TUBE_CORAL_WALL_FAN.getDefaultState());
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
            if(MinecraftClient.getInstance().crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
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
                float randomX = world.getRandom().nextFloat() * 0.4F - 0.2F;
                float randomZ = world.getRandom().nextFloat() * 0.4F - 0.2F;
                BlockPos particleBlockPos = blockPos.offset(context.getSide());
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, particleBlockPos.getX(), particleBlockPos.getY(), particleBlockPos.getZ(), randomX, 0, randomZ);
            } else if(user.getItemUseTime() % 10 == 0) {
                BlockState blockState = world.getBlockState(blockPos);

                // hardcoded as convertible? => convert
                if (blockConversions.containsKey(blockState.getBlock())) {
                    BlockState destinationState = blockConversions.get(blockState.getBlock());
                    if (destinationState instanceof Waterloggable) {
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
                } else if (BoneMealItem.useOnFertilizable(context.getStack(), world, blockPos)) {
                    world.syncWorldEvent(2005, blockPos, 0);
                    return ActionResult.success(false);
                    // random tickable and whitelisted? => tick
                    // without whitelist we would be able to tick budding blocks, ...
                } else if (blockState.hasRandomTicks() && blockState.isIn(SpectrumBlockTags.NATURES_STAFF_TICKABLE)) {
                    if (world instanceof ServerWorld) {
                        blockState.randomTick((ServerWorld) world, blockPos, world.random);
                    }
                    world.syncWorldEvent(2005, blockPos, 0);
                    return ActionResult.success(false);
                } else {
                    BlockPos blockPos2 = blockPos.offset(context.getSide());
                    boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
                    if (bl && BoneMealItem.useOnGround(context.getStack(), world, blockPos2, context.getSide())) {
                        world.syncWorldEvent(2005, blockPos2, 0);
                        return ActionResult.success(false);
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

}
