package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.fabricmc.api.ClientModInitializer;

public class SpectrumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpectrumBlocks.registerClient();
    }
}
