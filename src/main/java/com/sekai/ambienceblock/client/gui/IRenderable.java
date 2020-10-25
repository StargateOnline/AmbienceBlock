package com.sekai.ambienceblock.client.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderable
{
    public void render(int x, int y, float partialTicks);
}
