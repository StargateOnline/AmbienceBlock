package com.sekai.ambienceblock.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public abstract class FocusableGui extends GuiScreen implements INestedGuiEventEventHandler
{
    @Nullable
    private IGuiEventListener focused;
    private boolean isDragging;

    public final boolean isDragging() {
        return this.isDragging;
    }

    public final void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    @Nullable
    public IGuiEventListener getFocused() {
        return this.focused;
    }

    public void setFocused(@Nullable IGuiEventListener focused) {
        this.focused = focused;
    }
}
