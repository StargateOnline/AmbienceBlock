package com.sekai.ambienceblock.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReflectionUtil
{
    public static SoundRegistry getSoundRegistry() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            return ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, mc.getSoundHandler(), new String[]{"field_147697_e", "soundRegistry"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
