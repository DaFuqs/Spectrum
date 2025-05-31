package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class CreativeInkStorage implements InkStorage {
	
	private static final Map<InkColor, Long> STORAGE = new HashMap<>() {{
		for (InkColor inkColor : InkColors.all()) {
			put(inkColor, Long.MAX_VALUE);
		}
	}};

	protected static final ObjectSet<StorageView<InkColor>> VIEWS;

	static {
		var list = new ObjectArraySet<CreativeInkView>();
		for (InkColor inkColor : InkColors.all()) {
			list.add(new CreativeInkView(inkColor));
		}
		VIEWS = ObjectSets.unmodifiable(list);
	}

	record CreativeInkView(InkColor color) implements StorageView<InkColor> {

		@Override
		public long extract(InkColor resource, long maxAmount, TransactionContext transaction) {
			return maxAmount;
		}

		@Override
		public boolean isResourceBlank() {
			return color.isBlank();
		}

		@Override
		public InkColor getResource() {
			return color;
		}

		@Override
		public long getAmount() {
			return Long.MAX_VALUE;
		}

		@Override
		public long getCapacity() {
			return Long.MAX_VALUE;
		}
	}
	
	public CreativeInkStorage() {
		super();
	}
	
	public static CreativeInkStorage fromNbt(@NotNull NbtCompound ignored) {
		return new CreativeInkStorage();
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return true;
	}
	
	@Override
	public long addEnergy(InkColor color, long amount, boolean simulate) {
		return 0;
	}
	
	@Override
	public long drainEnergy(InkColor color, long requestedAmount, boolean simulate) {
		return requestedAmount;
	}
	
	@Override
	public long getVersion() {
		return 1;
	}

	@Override
	public boolean requestEnergy(InkColor color, long requestedAmount) {
		return true;
	}
	
	@Override
	public long getEnergy(InkColor color) {
		return Long.MAX_VALUE;
	}
	
	@Override
	public Map<InkColor, Long> getEnergy() {
		return STORAGE;
	}
	
	@Override
	public void setEnergy(Map<InkColor, Long> colors, long total) {
	}
	
	@Override
	public long getMaxPerColor() {
		return Long.MAX_VALUE;
	}
	
	@Override
	public long getMaxTotal() {
		return Long.MAX_VALUE;
	}
	
	@Override
	public long getCurrentTotal() {
		return Long.MAX_VALUE;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean isFull() {
		return true;
	}
	
	@Override
	public void fillCompletely() {
	}
	
	@Override
	public void clear() {
	}
	
	@Override
	public void addTooltip(List<Text> tooltip) {
		tooltip.add(Text.translatable("item.spectrum.creative_ink_assortment.tooltip"));
	}
	
	@Override
	public long getRoom(InkColor color) {
		return Long.MAX_VALUE;
	}

	// Do nothing since transfers don't mutate this storage's state
	@Override
	public void updateSnapshots(TransactionContext transaction) {}

	@Override
	public long getCapacity(InkColor variant) {
		return Long.MAX_VALUE;
	}

	@Override
	public @NotNull Iterator<StorageView<InkColor>> iterator() {
		return VIEWS.iterator();
	}
}