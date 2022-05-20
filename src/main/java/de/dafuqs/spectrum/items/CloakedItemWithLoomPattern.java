package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.items.conditional.CloakedItem;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class CloakedItemWithLoomPattern extends CloakedItem implements LoomPatternProvider {
	
	protected final LoomPattern loomPattern;
	
	public CloakedItemWithLoomPattern(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem, LoomPattern loomPattern) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
		this.loomPattern = loomPattern;
	}
	
	@Override
	public LoomPattern getPattern() {
		return loomPattern;
	}
	
}
