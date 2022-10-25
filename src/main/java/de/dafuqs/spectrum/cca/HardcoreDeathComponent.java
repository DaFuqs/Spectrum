package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HardcoreDeathComponent implements Component, LevelComponentInitializer, AutoSyncedComponent {
	
	public static final ComponentKey<HardcoreDeathComponent> HARDCORE_DEATHS_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(SpectrumCommon.MOD_ID, "hardcore_deaths"), HardcoreDeathComponent.class);
	
	private final static List<UUID> playersThatDiedInHardcore = new ArrayList<>();
	
	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(HARDCORE_DEATHS_COMPONENT , e -> new HardcoreDeathComponent());
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		NbtList uuidList = new NbtList();
		for(UUID playerThatDiedInHardcore : playersThatDiedInHardcore) {
			uuidList.add(NbtHelper.fromUuid(playerThatDiedInHardcore));
		}
		tag.put("HardcoreDeaths", uuidList);
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		playersThatDiedInHardcore.clear();
		NbtList uuidList = tag.getList("HardcoreDeaths", NbtElement.INT_ARRAY_TYPE);
		for(NbtElement listEntry : uuidList) {
			playersThatDiedInHardcore.add(NbtHelper.toUuid(listEntry));
		}
	}
	
	public static void addHardcoreDeath(UUID uuid) {
		if(!playersThatDiedInHardcore.contains(uuid)) {
			playersThatDiedInHardcore.add(uuid);
			HardcoreDeathComponent.HARDCORE_DEATHS_COMPONENT.sync(SpectrumCommon.minecraftServer);
		}
		SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(uuid).changeGameMode(GameMode.SPECTATOR);
	}
	
	public static boolean hasHardcoreDeath(UUID uuid) {
		return playersThatDiedInHardcore.contains(uuid);
	}
	
	public static void removeHardcoreDeath(UUID uuid) {
		playersThatDiedInHardcore.remove(uuid);
		HardcoreDeathComponent.HARDCORE_DEATHS_COMPONENT.sync(SpectrumCommon.minecraftServer);
	}

	public static boolean isInHardcore(PlayerEntity player) {
		return player.hasStatusEffect(SpectrumStatusEffects.DIVINITY);
	}
	
	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		NbtCompound tag = new NbtCompound();
		NbtList uuidList = new NbtList();
		if(hasHardcoreDeath(recipient.getUuid())) {
			uuidList.add(NbtHelper.fromUuid(recipient.getUuid()));
		}
		tag.put("HardcoreDeaths", uuidList);
		buf.writeNbt(tag);
	}
	
}
