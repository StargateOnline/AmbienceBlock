package com.sekai.ambienceblock.client.gui.widgets.button;

import com.sekai.ambienceblock.client.gui.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractButton extends Widget
{
    public AbstractButton(int x, int y, int width, int height, String text)
    {
        super(x, y, width, height, text);
    }

    public abstract void onPress();

    @Override
    public void onClick(double d, double d2) {
        this.onPress();
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (n == 257 || n == 32 || n == 335) {
            this.playDownSound(Minecraft.getMinecraft().getSoundHandler());
            this.onPress();
            return true;
        }
        return false;
    }
}
