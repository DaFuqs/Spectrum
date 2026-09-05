package de.dafuqs.spectrum.api.recipe;

import com.google.gson.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import io.netty.buffer.*;
import net.minecraft.commands.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.*;

import java.util.*;

/**
 * Effects that are played when crafting with the fusion shrine or similar
 */
public interface WorldEffect {
	
	record CraftingWorldEffects(WorldEffect start, List<WorldEffect> during, WorldEffect finish) {
		
		public static final CraftingWorldEffects EMPTY = new CraftingWorldEffects(WorldEffect.NOTHING, List.of(), WorldEffect.NOTHING);
		public static final CraftingWorldEffects FUSION_SHRINE_DEFAULT = new CraftingWorldEffects(WorldEffect.NOTHING, List.of(), SpectrumWorldEffects.SINGLE_VISUAL_EXPLOSION_ON_SHRINE);
		
		public static final Codec<CraftingWorldEffects> CODEC = RecordCodecBuilder.create(i -> i.group(
				WorldEffect.CODEC.fieldOf("start").forGetter(recipe -> recipe.start),
				WorldEffect.CODEC.listOf().optionalFieldOf("during", List.of()).forGetter(recipe -> recipe.during),
				WorldEffect.CODEC.fieldOf("finish").forGetter(recipe -> recipe.finish)
		).apply(i, CraftingWorldEffects::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, CraftingWorldEffects> STREAM_CODEC = PacketCodecHelper.tuple(
				WorldEffect.STREAM_CODEC, recipe -> recipe.start,
				WorldEffect.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.during,
				WorldEffect.STREAM_CODEC, recipe -> recipe.finish,
				CraftingWorldEffects::new
		);
	}
	
	Codec<WorldEffect> CODEC = Codec.STRING.xmap(
			WorldEffect::fromString,
			effect -> effect instanceof CommandRecipeWorldEffect command
					? command.command
					: String.valueOf(SpectrumRegistries.WORLD_EFFECT.getKey(effect)));
	
	StreamCodec<ByteBuf, WorldEffect> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
			WorldEffect::fromString,
			effect -> effect instanceof CommandRecipeWorldEffect command
					? command.command
					: String.valueOf(SpectrumRegistries.WORLD_EFFECT.getKey(effect)));
	
	WorldEffect NOTHING = register("nothing", new WorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
		}
	});
	
	static WorldEffect register(String id, WorldEffect effect) {
		Registry.register(SpectrumRegistries.WORLD_EFFECT, SpectrumCommon.locate(id), effect);
		return effect;
	}
	
	static WorldEffect fromString(String string) {
		if (string.isBlank()) {
			return NOTHING;
		}
		if (string.startsWith("/")) {
			return new CommandRecipeWorldEffect(string);
		}
		
		WorldEffect effect = SpectrumRegistries.WORLD_EFFECT.get(SpectrumCommon.ofSpectrumDefaulted(string));
		if (effect == null) {
			SpectrumCommon.logError("Unknown world effect '" + string + "'. Will be ignored.");
			return NOTHING;
		}
		return effect;
	}
	
	/**
	 * True for all effects that should just play once.
	 * Otherwise, it will be triggered each tick of the recipe
	 */
	boolean isOneTimeEffect();
	
	void trigger(ServerLevel world, BlockPos pos);
	
	abstract class EveryTickRecipeWorldEffect implements WorldEffect {
		
		public EveryTickRecipeWorldEffect() {
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return false;
		}
		
	}
	
	abstract class SingleTimeRecipeWorldEffect implements WorldEffect {
		
		public SingleTimeRecipeWorldEffect() {
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return true;
		}
		
	}
	
	class CommandRecipeWorldEffect implements WorldEffect, CommandSource {
		
		protected final String command;
		
		public CommandRecipeWorldEffect(String command) {
			this.command = command;
		}
		
		public static CommandRecipeWorldEffect fromJson(JsonObject json) {
			return new CommandRecipeWorldEffect(json.getAsString());
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return false;
		}
		
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			MinecraftServer minecraftServer = world.getServer();
			CommandSourceStack serverCommandSource = new CommandSourceStack(this, Vec3.atCenterOf(pos), Vec2.ZERO, world, 2, "WorldEffect", world.getBlockState(pos).getBlock().getName(), minecraftServer, null);
			minecraftServer.getCommands().performPrefixedCommand(serverCommandSource, command);
		}
		
		@Override
		public void sendSystemMessage(Component message) {
		}
		
		@Override
		public boolean acceptsSuccess() {
			return false;
		}
		
		@Override
		public boolean acceptsFailure() {
			return false;
		}
		
		@Override
		public boolean shouldInformAdmins() {
			return false;
		}
	}
	
}
