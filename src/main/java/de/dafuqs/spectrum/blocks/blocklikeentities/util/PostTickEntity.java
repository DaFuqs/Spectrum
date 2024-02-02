package de.dafuqs.spectrum.blocks.blocklikeentities.util;

/**
 * An interface for entities that need to be ticked after the main tick.
 */
public interface PostTickEntity {
    void spectrum$postTick();
}