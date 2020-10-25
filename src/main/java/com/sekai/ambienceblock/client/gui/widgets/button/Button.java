package com.sekai.ambienceblock.client.gui.widgets.button;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Button extends AbstractButton
{
    protected final IPressable onPress;

    public Button(int x, int y, int width, int height, String text, IPressable iPressable)
    {
        super(x, y, width, height, text);
        this.onPress = iPressable;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @SideOnly(Side.CLIENT)
    public static interface IPressable {
        public void onPress(Button button);
    }
}
