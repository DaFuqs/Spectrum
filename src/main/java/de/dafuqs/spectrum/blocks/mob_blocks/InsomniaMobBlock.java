package de.dafuqs.spectrum.blocks.mob_blocks;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InsomniaMobBlock extends MobBlock {
	
	public final int additionalTicksSinceLastRest;
	
	public InsomniaMobBlock(Settings settings, ParticleEffect particleEffect, int additionalTicksSinceLastRest) {
		super(settings, particleEffect);
		this.additionalTicksSinceLastRest = additionalTicksSinceLastRest;
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// spawn phantoms regardless of gamerule
		// makes phantom drops accessible even with gamerule disabled
		if (entity instanceof ServerPlayerEntity serverPlayerEntity /*&& !world.getGameRules().getBoolean(GameRules.DO_INSOMNIA)*/) {
			Random random = world.random;
			
			// play a phantom sound
			world.playSound(null, blockPos, SoundEvents.ENTITY_PHANTOM_AMBIENT, SoundCategory.BLOCKS, 1.0F, 0.8F + random.nextFloat() * 0.4F);
			
			// cause insomnia
			int currentStatValue = serverPlayerEntity.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
			int newValue = MathHelper.clamp(currentStatValue, 0, 2147483647 - additionalTicksSinceLastRest) + this.additionalTicksSinceLastRest; // prevent overflows
			serverPlayerEntity.getStatHandler().setStat(serverPlayerEntity, Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST), newValue);
			
			// if sky visible & night: immediately spawn phantom
			if (world.isSkyVisible(blockPos.up()) && TimeHelper.getTimeOfDay(world).isNight()) {
				PhantomEntity phantomEntity = EntityType.PHANTOM.create(world);
				if (phantomEntity != null) {
					phantomEntity.refreshPositionAndAngles(blockPos.up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21)), 0.0F, 0.0F);
					phantomEntity.initialize(world, world.getLocalDifficulty(blockPos), SpawnReason.MOB_SUMMONED, null, null);
					
					int phantomSize = Math.min(64, newValue / 24000);
					phantomEntity.setPhantomSize(phantomSize);
					
					world.spawnEntityAndPassengers(phantomEntity);
				}
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public int getCooldownTicks() {
		return 200;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.insomnia_mob_block.tooltip"));
	}
	
}
