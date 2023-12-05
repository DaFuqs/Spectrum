package de.dafuqs.spectrum.sound.music;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.sound.Sound;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MusicResourceLoader {

    public static final Identifier IDENTIFIER = SpectrumCommon.locate("mutable_music_resources");
    public static final ResourceFinder FINDER = new ResourceFinder("sounds/mutable_music", ".ogg");

    public static final Map<Identifier, Resource> audioIO = new HashMap<>();
    private final ResourceFactory resourceFactory;

    public MusicResourceLoader() {
        resourceFactory = ResourceFactory.fromMap(audioIO);
    }

    public void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return IDENTIFIER;
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        audioIO.clear();
                        var resources = FINDER.findResources(manager);
                        audioIO.putAll(resources);
                    }
                }
        );
    }

    public ResourceFactory getResourceFactory() {
        return resourceFactory;
    }
}
