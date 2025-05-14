package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InsomniaIdolBlock extends IdolBlock {
	
	public final int additionalTicksSinceLastRest;
	
	public InsomniaIdolBlock(Properties settings, ParticleOptions particleEffect, int additionalTicksSinceLastRest) {
		super(settings, particleEffect);
		this.additionalTicksSinceLastRest = additionalTicksSinceLastRest;
	}

	@Override
	public MapCodec<? extends InsomniaIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// spawn phantoms regardless of gamerule
		// makes phantom drops accessible even with gamerule disabled
		if (entity instanceof ServerPlayer serverPlayerEntity /*&& !world.getGameRules().getBoolean(GameRules.DO_INSOMNIA)*/) {
			RandomSource random = world.random;
			
			// play a phantom sound
			world.playSound(null, blockPos, SoundEvents.PHANTOM_AMBIENT, SoundSource.BLOCKS, 1.0F, 0.8F + random.nextFloat() * 0.4F);
			
			// cause insomnia
			int currentStatValue = serverPlayerEntity.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
			int newValue = Mth.clamp(currentStatValue, 0, 2147483647 - additionalTicksSinceLastRest) + this.additionalTicksSinceLastRest; // prevent overflows
			serverPlayerEntity.getStats().setValue(serverPlayerEntity, Stats.CUSTOM.get(Stats.TIME_SINCE_REST), newValue);
			
			// if sky visible & night: immediately spawn phantom
			if (world.canSeeSky(blockPos.above()) && TimeHelper.getTimeOfDay(world).isNight()) {
				Phantom phantomEntity = EntityType.PHANTOM.create(world);
				if (phantomEntity != null) {
					phantomEntity.moveTo(blockPos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21)), 0.0F, 0.0F);
					phantomEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
					
					int phantomSize = Math.min(64, newValue / 24000);
					phantomEntity.setPhantomSize(phantomSize);
					
					world.addFreshEntityWithPassengers(phantomEntity);
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
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.insomnia_idol.tooltip"));
	}
	
}
