package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.item.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class ResonanceDropsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "resonance_drops";
	public static final ResonanceDropsDataLoader INSTANCE = new ResonanceDropsDataLoader();
	
	protected static final HashMap<Item, Item> RESONANCE_DROPS = new HashMap<>();
	
	private ResonanceDropsDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		RESONANCE_DROPS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Item itemIn = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "input")));
			Item itemOut = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "output")));
			
			if (itemIn != Items.AIR && itemOut != Items.AIR) {
				RESONANCE_DROPS.put(itemIn, itemOut);
			}
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static ItemStack applyResonance(ItemStack stack) {
		Item item = stack.getItem();
		if (RESONANCE_DROPS.containsKey(item)) {
			ItemStack convertedStack = RESONANCE_DROPS.get(item).getDefaultStack();
			convertedStack.setCount(stack.getCount());
			return convertedStack;
		} else {
			return stack;
		}
	}
	
}