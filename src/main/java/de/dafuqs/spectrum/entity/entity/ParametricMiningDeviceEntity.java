package de.dafuqs.spectrum.entity.entity;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;

public class ParametricMiningDeviceEntity extends ThrownItemEntity {
    
    private static final ItemStack MINING_STACK;
    
    public ParametricMiningDeviceEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public ParametricMiningDeviceEntity(World world, LivingEntity owner) {
        super(SpectrumEntityTypes.PARAMETRIC_MINING_DEVICE_ENTITY, owner, world);
    }
    
    public ParametricMiningDeviceEntity(World world, double x, double y, double z) {
        super(SpectrumEntityTypes.PARAMETRIC_MINING_DEVICE_ENTITY, x, y, z, world);
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        processExplosion(blockHitResult.getBlockPos());
    }
    
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        var target = entityHitResult.getEntity();
        target.damage(SpectrumDamageSources.incandescence(getOwner()), 20);
        super.onEntityHit(entityHitResult);
    }

    private void processExplosion(BlockPos center) {
        if (world.isClient())
            return;

        world.sendEntityStatus(this, (byte) 1);

        var explosion = new Explosion(world, getOwner(), center.getX(), center.getY(), center.getZ(), 10);
        var blastRadius = BlockPos.iterateOutwards(center, 5, 5, 5);

        ObjectArrayList<Pair<ItemStack, BlockPos>> drops = new ObjectArrayList<>();
        for (BlockPos pos : blastRadius) {
            processBlock(center, pos, drops, explosion);
        }

        if (!drops.isEmpty())
            world.sendEntityStatus(this, (byte) 2);

        for (Pair<ItemStack, BlockPos> stackPosPair : drops) {
            Block.dropStack(world, stackPosPair.getSecond(), stackPosPair.getFirst());
        }

        playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 0.8F + random.nextFloat() * 0.4F);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void handleStatus(byte status) {
        var pos = getPos();

        if (status == 1) {
            for (int i = 0; i < 20; i++) {
                var particle = random.nextBoolean() ? SpectrumParticleTypes.PRIMORDIAL_SMOKE : SpectrumParticleTypes.PRIMORDIAL_FLAME;
                world.addImportantParticle(particle, true, pos.getX(), pos.getY(), pos.getZ(), random.nextFloat() * 0.25 - 0.125, random.nextFloat() * 0.25 - 0.125, random.nextFloat() * 0.25 - 0.125);
            }
        }
        else if (status == 2) {
            var particles = 15 + random.nextInt(16);
            for (int i = 0; i < particles; i++) {
                var r = random.nextDouble() * 4;
                var orientation = Orientation.create(random.nextDouble() * Math.PI * 2, random.nextDouble() * Math.PI * 2);
                var particle = orientation.toVector(r).add(pos);
                world.addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, particle.getX(), particle.getY(), particle.getZ(), 0, 0, 0);
            }
        }
    }

    private void processBlock(BlockPos center, BlockPos pos, ObjectArrayList<Pair<ItemStack, BlockPos>> drops, Explosion explosion) {
        if (Math.pow(pos.getX() - center.getX(), 2) + Math.pow(pos.getY() - center.getY(), 2) + Math.pow(pos.getZ() - center.getZ(), 2) < 4 * 4) {

            var state = world.getBlockState(pos);
            var block = state.getBlock();
            var blockEntity = state.hasBlockEntity() ? this.world.getBlockEntity(pos) : null;

            if (state.getBlock().getBlastResistance() <= 9) {
                if (random.nextFloat() < 0.15F) {
                    playSound(block.getSoundGroup(state).getBreakSound(), 2F, 0.8F + random.nextFloat() * 0.5F);
                }

                if (block.shouldDropItemsOnExplosion(explosion)) {
                    LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).random(this.world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, MINING_STACK).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, getOwner());
                    builder.parameter(LootContextParameters.EXPLOSION_RADIUS, 10F);
                    state.onStacksDropped((ServerWorld) world, pos, MINING_STACK, true);
                    state.getDroppedStacks(builder).forEach((stack) -> {
                        tryMergeStack(drops, stack, pos.toImmutable());
                    });
                }

                this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                block.onDestroyedByExplosion(this.world, pos, explosion);
            }
        }
    }

    private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        int i = stacks.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = stacks.get(j);
            ItemStack itemStack = pair.getFirst();
            if (ItemEntity.canMerge(itemStack, stack)) {
                ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
                stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        stacks.add(Pair.of(stack, pos));
    }

    @Override
    protected Item getDefaultItem() {
        return SpectrumBlocks.PARAMETRIC_MINING_DEVICE.asItem();
    }

    static {
        MINING_STACK = new ItemStack(SpectrumItems.BEDROCK_PICKAXE);
        MINING_STACK.addEnchantment(Enchantments.SILK_TOUCH, 1);
        MINING_STACK.addEnchantment(Enchantments.FORTUNE, 3);
    }
}