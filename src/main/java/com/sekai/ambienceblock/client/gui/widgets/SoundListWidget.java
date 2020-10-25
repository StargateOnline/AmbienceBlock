package com.sekai.ambienceblock.client.gui.widgets;

import com.sekai.ambienceblock.AmbienceMod;
import net.minecraft.util.ResourceLocation;

public class SoundListWidget extends Widget
{
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(AmbienceMod.MOD_ID, "textures/gui/ambience_gui.png");
    public static final int texWidth = 256;
    public static final int texHeight = 184;
    public int xTopLeft, yTopLeft;
    protected static final int separation = 8;

    private StringListWidget list;

    public SoundListWidget(int x, int y, int width, int height)
    {
        super(x, y, width, height, "");

        xTopLeft = (this.width - texWidth) / 2;
        yTopLeft = (this.height - texHeight) / 2;

        //list = new StringListWidget(xTopLeft);
    }
}
