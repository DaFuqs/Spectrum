package de.dafuqs.spectrum.cca;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;
import org.ladysnake.cca.api.v3.component.*;

import java.util.*;

public class HardcoreDeathComponent implements Component {
	
	public static final ComponentKey<HardcoreDeathComponent> HARDCORE_DEATHS_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("hardcore_deaths"), HardcoreDeathComponent.class);
	
	private final static List<UUID> playersThatDiedInHardcore = new ArrayList<>();
	
	@Override
	public void writeToNbt(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
		ListTag uuidList = new ListTag();
		for (UUID playerThatDiedInHardcore : playersThatDiedInHardcore) {
			uuidList.add(NbtUtils.createUUID(playerThatDiedInHardcore));
		}
		tag.put("HardcoreDeaths", uuidList);
	}
	
	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
		playersThatDiedInHardcore.clear();
		ListTag uuidList = tag.getList("HardcoreDeaths", Tag.TAG_INT_ARRAY);
		for (Tag listEntry : uuidList) {
			playersThatDiedInHardcore.add(NbtUtils.loadUUID(listEntry));
		}
	}
	
	public static boolean isInHardcore(Player player) {
		return player.hasEffect(SpectrumStatusEffects.DIVINITY);
	}
	
	public static void addHardcoreDeath(ServerLevel world, GameProfile profile) {
		addHardcoreDeath(world, profile.getId());
	}
	
	public static void removeHardcoreDeath(GameProfile profile) {
		removeHardcoreDeath(profile.getId());
	}
	
	public static boolean hasHardcoreDeath(GameProfile profile) {
		return hasHardcoreDeath(profile.getId());
	}
	
	protected static void addHardcoreDeath(ServerLevel world, UUID uuid) {
		if (!playersThatDiedInHardcore.contains(uuid)) {
			playersThatDiedInHardcore.add(uuid);
		}
		world.getServer().getPlayerList().getPlayer(uuid).setGameMode(GameType.SPECTATOR);
	}
	
	protected static boolean hasHardcoreDeath(UUID uuid) {
		return playersThatDiedInHardcore.contains(uuid);
	}
	
	protected static void removeHardcoreDeath(UUID uuid) {
		playersThatDiedInHardcore.remove(uuid);
	}
	
}
