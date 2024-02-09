package de.dafuqs.spectrum.sound.music;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicAudioLoader {

    public static final Identifier IDENTIFIER = SpectrumCommon.locate("mutable_music_resources");
    public static final ResourceFinder FINDER = new ResourceFinder("sounds/mutable_music", ".ogg");

    public static final Map<Identifier, CachedAudioStream> audioStreams = new HashMap<>();
    private final ResourceFactory resourceFactory;

    public DynamicAudioLoader() {
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

                        try {
                            for (CachedAudioStream oldStream : audioStreams.values()) {
                                oldStream.close();
                            }
                            audioStreams.clear();

                            var resources = FINDER.findResources(manager);
                            for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
                                var stream = new CachedAudioStream(entry.getValue().getInputStream());
                            }

                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }

                    }
                }
        );
    }

    public ResourceFactory getResourceFactory() {
        return resourceFactory;
    }
}
