package com.sekai.ambienceblock.client.gui.ambience.tabs;

import com.sekai.ambienceblock.client.eventhandler.AmbienceEventHandler;
import com.sekai.ambienceblock.client.gui.ambience.AmbienceGUI;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntityData;
import com.sekai.ambienceblock.client.gui.widgets.button.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class MiscTab extends AbstractTab {
    Button copy = new Button(getBaseX(), getRowY(0) + getOffsetY(20), 60, 20, "Copy", button -> {
        AmbienceEventHandler.instance.setClipboard(guiRef.getData());
    });
    Button paste = new Button(getNeighbourX(copy), getRowY(0) + getOffsetY(20), 60, 20, "Paste", button -> {
        guiRef.setData(AmbienceEventHandler.instance.getClipboard());
    });
    //StringListWidget musicList = new StringListWidget(getBaseX(), getRowY(1), getEndX() - getBaseX(), getEndY() - getRowY(1), 4, 16, font, null);

    /*
     TODO: Add a preset folder that you can view trough this list
    StringListWidget musicList = new StringListWidget(getBaseX(), getRowY(1), getEndX() - getBaseX(), getEndY() - getRowY(1), 4, 16, font, null);
    TextInstance presets;
    */

    public MiscTab(AmbienceGUI guiRef) {
        super(guiRef);
    }

    @Override
    public String getName() {
        return "Misc";
    }

    @Override
    public void initialInit() {
        /*for (ResourceLocation element : Util.getAvailableSounds()) {
            if(element.getResourcePath().contains("ambients")) musicList.addElement(element.getResourcePath());
        }*/

        addButton(copy);
        addButton(paste);
        //addWidget(musicList);
    }

    @Override
    public void updateWidgetPosition() {
        copy.x = getBaseX(); copy.y = getRowY(0);
        paste.x = getNeighbourX(copy); paste.y = getRowY(0);
        //musicList.x = getBaseX(); musicList.y = getRowY(1);
    }

    public void doubleClicked() {
        System.out.println("double clicked");
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        copy.render(mouseX, mouseY, partialTicks);
        paste.render(mouseX, mouseY, partialTicks);

        //musicList.render(mouseX, mouseY);
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        List<String> list = new ArrayList<String>();

        if(copy.isHovered()) {
            list.add(TextFormatting.RED + "Copy");
            list.add("Copy the current GUIs parameters.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
        if(paste.isHovered()) {
            list.add(TextFormatting.RED + "Paste");
            list.add("Paste the copied parameters onto this block.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
    }

    @Override
    public void tick() {
        /*if(musicList.scrollIndex < 1f)
            musicList.scrollIndex += 0.01f;
        else
            musicList.scrollIndex = 0f;*/
    }

    @Override
    public void setFieldFromData(AmbienceTileEntityData data) {

    }

    @Override
    public void setDataFromField(AmbienceTileEntityData data) {

    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {

    }
}
