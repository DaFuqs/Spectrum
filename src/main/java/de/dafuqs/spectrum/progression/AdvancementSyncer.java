package de.dafuqs.spectrum.progression;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.server.players.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.saveddata.*;

import java.util.*;

public class AdvancementSyncer extends SavedData {
	
	public enum Option implements StringRepresentable {
		ALL_PERSISTENT("all_persistent", true, false),
		SPECTRUM_PERSISTENT("spectrum_persistent", true, true),
		ALL_ONCE("all_once", false, false),
		SPECTRUM_ONCE("spectrum_once", false, true);
		
		private final String name;
		private final boolean persistent;
		private final boolean spectrumOnly;
		
		Option(String name, boolean persistent, boolean spectrumOnly) {
			this.name = name;
			this.persistent = persistent;
			this.spectrumOnly = spectrumOnly;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
	
	public record Relationship(List<UUID> players, Option option) {
		
	}
	
	protected final List<Relationship> relationships = new ArrayList<>();
	
	public static AdvancementSyncer load(CompoundTag compoundTag, HolderLookup.Provider lookupProvider) {
		AdvancementSyncer advancementSyncer = new AdvancementSyncer();
		
		ListTag relationships = compoundTag.getList("relationships", CompoundTag.TAG_COMPOUND);

		for(int i = 0; i < relationships.size(); ++i) {
			CompoundTag entry = relationships.getCompound(i);
			List<UUID> players = new ArrayList<>();
			for (Tag tag : entry.getList("player_uuids", CompoundTag.TAG_INT_ARRAY)) {
				players.add(NbtUtils.loadUUID(tag));
			}
			Option option = Option.valueOf(entry.getString("option"));
			advancementSyncer.relationships.add(new  Relationship(players, option));
		}
		
		return advancementSyncer;
	}
	
	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) {
		ListTag relationshipsTag = new ListTag();
		for(Relationship relationship : this.relationships) {
			CompoundTag t = new CompoundTag();
			t.putString("option", relationship.option().getSerializedName());
			
			ListTag listtag = new ListTag();
			for (UUID uuid : relationship.players()) {
				listtag.add(NbtUtils.createUUID(uuid));
			}
			t.put("player_uuids", listtag);
			relationshipsTag.add(t);
		}
		
		tag.put("relationships", relationshipsTag);
		return tag;
	}
	
	public static AdvancementSyncer getInstance(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(AdvancementSyncer::new, AdvancementSyncer::load), "advancement_syncer");
	}
	
	public void sync(List<ServerPlayer> players, Option option) {
		syncAdvancements(players, option.spectrumOnly);
		
		if(option.persistent) {
			this.relationships.add(new Relationship(players.stream().map(Entity::getUUID).toList(), option));
			this.setDirty();
		}
	}
	
	public List<Relationship> getSyncs(ServerPlayer player) {
		List<Relationship> results = new ArrayList<>();
		for(Relationship relationship : this.relationships) {
			if(!relationship.players().contains(player.getUUID())) {
				continue;
			}
			results.add(relationship);
		}
		return results;
	}
	
	public int clearSyncs(ServerPlayer player) {
		int i = 0;
		
		for(Relationship relationship : this.relationships) {
			if(!relationship.players().contains(player.getUUID())) {
				continue;
			}
			
			if(relationship.players().size() <= 2) {
				relationships.remove(relationship);
			} else {
				relationship.players().remove(player.getUUID());
			}
			i++;
		}
		
		if(i > 0) {
			this.setDirty();
		}
		return i;
	}
	
	public void onAdvancementEarn(ServerPlayer player, AdvancementHolder advancement, String criterionKey) {
		MinecraftServer server = player.getServer();
		for(Relationship relationship : this.relationships) {
			if(!relationship.players().contains(player.getUUID())) {
				continue;
			}
			
			List<ServerPlayer> relationshipPlayers = new  ArrayList<>();
			relationshipPlayers.add(player);
			for(UUID uuid : relationship.players()) {
				ServerPlayer onlinePlayer = server.getPlayerList().getPlayer(uuid);
				if(onlinePlayer != null) {
					onlinePlayer.getAdvancements().award(advancement, criterionKey);
				}
			}
		}
	}
	
	public void onPlayerJoin(ServerPlayer player) {
		MinecraftServer server = player.getServer();
		PlayerList playerList = server.getPlayerList();
		if(playerList.getPlayers().size() < 2) {
			return;
		}
		
		for(Relationship relationship : this.relationships) {
			if(!relationship.players().contains(player.getUUID())) {
				continue;
			}
			
			List<ServerPlayer> relationshipPlayers = new  ArrayList<>();
			relationshipPlayers.add(player);
			for(UUID uuid : relationship.players()) {
				ServerPlayer onlinePlayer = playerList.getPlayer(uuid);
				if(onlinePlayer != null) {
					relationshipPlayers.add(onlinePlayer);
				}
			}
			
			syncAdvancements(relationshipPlayers, relationship.option.spectrumOnly);
		}
	}
	
	private void syncAdvancements(List<ServerPlayer> players, boolean spectrumOnly) {
		String namespaceToSync = spectrumOnly ? SpectrumCommon.MOD_ID : "all";
		for(ServerPlayer source : players) {
			for (ServerPlayer target : players) {
				if(source == target) {
					continue;
				}
				AdvancementUtils.forPlayer(source).withNamespace(namespaceToSync).syncTo(target, false);
			}
		}
	}
	
}
