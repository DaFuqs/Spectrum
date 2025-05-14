package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class NightdewBlock extends TriStateVineBlock {
	
	public static final MapCodec<NightdewBlock> CODEC = simpleCodec(NightdewBlock::new);

    public static final float BASE_BURGEON_CHANCE = 2000;
    public static final float MAX_BURGEON_CHANCE = 250;
	
	public NightdewBlock(Properties settings) {
        super(settings, 6, 1F, 0.3F, 0.85F);
    }

    @Override
	public MapCodec<? extends NightdewBlock> codec() {
        return CODEC;
    }

    @Override
	public boolean mayPlaceOn(BlockState roof, BlockGetter world, BlockPos pos) {
		return super.mayPlaceOn(roof, world, pos) && roof.is(SpectrumBlockTags.NIGHTDEW_SOILS);
    }

    @Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return SpectrumItems.NIGHTDEW_SPROUT.getDefaultInstance();
    }

    @Override
	public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        var random = world.getRandom();
		
		var sleepingEntities = Math.min(world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(20), LivingEntity::isSleeping).size() / 20F, 1F);
		var dropChance = Mth.clampedLerp(BASE_BURGEON_CHANCE, MAX_BURGEON_CHANCE, sleepingEntities);

        if (random.nextFloat() < 1 / dropChance)
			for (ItemStack rareStack : getRareStacks(state, world, pos, tool, SpectrumLootTables.NIGHTDEW_VINE_RARE_DROP)) {
				popResource(world, pos, rareStack);
            }
    }

    @Override
    boolean hasGrowthActions() {
        return false;
    }
	
	public static List<ItemStack> getRareStacks(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack, ResourceKey<LootTable> lootTableKey) {
		var builder = (new LootParams.Builder(world))
				.withParameter(LootContextParams.BLOCK_STATE, state)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, stack);
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(lootTableKey);
		return lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
    }
}
