package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NightdewBlock extends TriStateVineBlock {

    public static final Identifier BURGEON_LOOT_TABLE = SpectrumCommon.locate("gameplay/nightdew_vine_rare_drop");
    public static final float BASE_BURGEON_CHANCE = 10000;
    public static final float MAX_BURGEON_CHANCE = 1000;


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
            for (ItemStack rareStack : getRareStacks(state, world, pos, tool, BURGEON_LOOT_TABLE)) {
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
