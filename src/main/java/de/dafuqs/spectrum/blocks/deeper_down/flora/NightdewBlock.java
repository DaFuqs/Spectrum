package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class NightdewBlock extends TriStateVineBlock {

    public static final float BASE_BURGEON_CHANCE = 2000;
    public static final float MAX_BURGEON_CHANCE = 250;


    public NightdewBlock(Settings settings) {
        super(settings, 6, 1F, 0.3F, 0.85F);
    }

    @Override
    public boolean canPlantOnTop(BlockState roof, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(roof, world, pos) && roof.isIn(SpectrumBlockTags.NIGHTDEW_SOILS);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return SpectrumItems.NIGHTDEW_SPROUT.getDefaultStack();
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        var random = world.getRandom();

        var sleepingEntities = Math.min(world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(20), LivingEntity::isSleeping).size() / 20F, 1F);
        var dropChance = MathHelper.clampedLerp(BASE_BURGEON_CHANCE, MAX_BURGEON_CHANCE, sleepingEntities);

        if (random.nextFloat() < 1 / dropChance)
			for (ItemStack rareStack : getRareStacks(state, world, pos, tool, SpectrumLootTables.NIGHTDEW_VINE_RARE_DROP)) {
                dropStack(world, pos, rareStack);
            }
    }

    @Override
    boolean hasGrowthActions() {
        return false;
    }

    public static List<ItemStack> getRareStacks(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, Identifier lootTableIdentifier) {
        var builder = (new LootContextParameterSet.Builder(world))
                .add(LootContextParameters.BLOCK_STATE, state)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                .add(LootContextParameters.TOOL, stack);

        LootTable lootTable = world.getServer().getLootManager().getLootTable(lootTableIdentifier);
        return lootTable.generateLoot(builder.build(LootContextTypes.BLOCK));
    }
}
