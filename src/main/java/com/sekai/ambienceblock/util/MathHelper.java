package com.sekai.ambienceblock.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MathHelper
{
    @SideOnly(Side.CLIENT)
    public static float fastInvCubeRoot(float number) {
        int i = Float.floatToIntBits(number);
        i = 1419967116 - i / 3;
        float f = Float.intBitsToFloat(i);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * number);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * number);
        return f;
    }
}
