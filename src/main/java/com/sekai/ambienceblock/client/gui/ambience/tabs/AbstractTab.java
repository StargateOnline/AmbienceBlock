package com.sekai.ambienceblock.client.gui.ambience.tabs;

import com.sekai.ambienceblock.client.gui.widgets.Widget;
import com.sekai.ambienceblock.client.gui.widgets.button.Button;
import com.sekai.ambienceblock.client.gui.widgets.button.CheckboxButton;
import com.sekai.ambienceblock.client.gui.ambience.AmbienceGUI;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class AbstractTab {
    protected FontRenderer font;
    protected AmbienceGUI guiRef;
    protected int x, y, width, height;

    private boolean active;
    private List<Widget> widgets = new ArrayList<>();
    private List<Widget> buttons = new ArrayList<>();

    protected static final int separation = 8;
    protected static final int rowHeight = 20;

    public AbstractTab(AmbienceGUI guiRef) {
        font = Minecraft.getMinecraft().fontRenderer;
        this.guiRef = guiRef;
        updateMetaValues(guiRef);
        //init();
    }

    public void refreshWidgets() {
        for(Widget widget : widgets) {
            if(!guiRef.children().contains(widget))
                guiRef.addWidget(widget);
        }

        for(Widget widget : buttons) {
            if(!guiRef.children().contains(widget))
                guiRef.addButton(widget);
        }
    }

    public void updateMetaValues(AmbienceGUI guiRef)
    {
        this.x = guiRef.xTopLeft;
        this.y = guiRef.yTopLeft;
        this.width = AmbienceGUI.texWidth;
        this.height = AmbienceGUI.texHeight;
    }

    public void addWidget(Widget widget)
    {
        guiRef.addWidget(widget);
        widgets.add(widget);
    }

    public void addButton(Widget widget)
    {
        guiRef.addButton(widget);
        buttons.add(widget);
    }

    public abstract String getName();

    public abstract void initialInit();

    public abstract void updateWidgetPosition();

    //public void secondInit() {}
    /*public void init() {
    }*/

    public abstract void render(int mouseX, int mouseY, float partialTicks);

    public abstract void renderToolTip(int mouseX, int mouseY);

    public abstract void tick();

    /**
     * @param data to pass into the gui from the tile.
     */
    public abstract void setFieldFromData(AmbienceTileEntityData data);
    /**
     * @param data to pass into the tile from the gui.
     */
    public abstract void setDataFromField(AmbienceTileEntityData data);

    public void activate() {
        active = true;
        for(Widget widget : widgets)
        {
            widget.visible = true;
            widget.active = true;
        }
        for(Widget widget : buttons)
        {
            widget.visible = true;
            widget.active = true;
        }
        onActivate();
    }

    public abstract void onActivate();

    public void deactivate() {
        active = false;
        for(Widget widget : widgets) {
            widget.visible = false;
            widget.active = false;
        }
        for(Widget widget : buttons)
        {
            widget.visible = false;
            widget.active = false;
        }
        onDeactivate();
    }

    public abstract void onDeactivate();

    public boolean isActive() {
        return active;
    }

    //util stuff
    protected int getBaseX()
    {
        return x + separation;
    }

    protected int getEndX()
    {
        return x + width - separation;
    }

    protected int getEndY()
    {
        return y + height - separation;
    }

    protected int getNeighbourX(Widget widget)
    {
        return widget.x + widget.getWidth() + separation;
    }

    protected int getRowY(int row)
    {
        return y + rowHeight * row + separation * (row + 1) + AmbienceGUI.tabHeight;
    }

    protected int getOffsetY(int height)
    {
        return (rowHeight - height) / 2;
    }
    //
}
