package com.sekai.ambienceblock.util;

import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util
{
    public static <T> T make(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> T make(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

    public static Collection<ResourceLocation> getAvailableSounds() {
        SoundRegistry soundRegistry = ReflectionUtil.getSoundRegistry();
        return soundRegistry.getKeys();
    }
}
