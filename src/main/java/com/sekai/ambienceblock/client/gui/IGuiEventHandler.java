package com.sekai.ambienceblock.client.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IGuiEventHandler
{
    default public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    default public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    default public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double x2, double y2) {
        return false;
    }

    default public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return false;
    }

    default public boolean keyPressed(int typedChar, int keyCode, int modifiers) {
        return false;
    }

    default public boolean charTyped(char typedChar, int keyCode) {
        return false;
    }

    default public boolean changeFocus(boolean bl) {
        return false;
    }

    default public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }
}
