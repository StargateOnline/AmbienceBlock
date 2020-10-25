package com.sekai.ambienceblock.client.gui.ambience;

import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.client.gui.widgets.Widget;
import com.sekai.ambienceblock.network.packets.PacketUpdateAmbienceTE;
import com.sekai.ambienceblock.client.gui.ambience.tabs.*;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntity;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntityData;
import com.sekai.ambienceblock.network.NetworkHandler;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmbienceGUI extends GuiScreen {
    private final AmbienceTileEntity target;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(AmbienceMod.MOD_ID, "textures/gui/ambience_gui.png");

    public static final int texWidth = 256;
    public static final int texHeight = 184;
    public int xTopLeft, yTopLeft;

    public static final int tabEdgeWidth = 16, tabHeight = 16;

    // Tabs
    /*private MainTab mainTab;
    private BoundsTab boundsTab;
    private PriorityTab priorityTab;
    private DelayTab delayTab;
    private MiscTab miscTab;*/
    private MainTab mainTab = new MainTab(this);
    private BoundsTab boundsTab = new BoundsTab(this);
    private PriorityTab priorityTab = new PriorityTab(this);
    private DelayTab delayTab = new DelayTab(this);
    private MiscTab miscTab = new MiscTab(this);

    private AbstractTab highlightedTab;

    // Buttons
    public static final int CONFIRM_BUTTON_ID = 0;
    public static final int HELP_BUTTON_ID = 1;
    private GuiButton confirmChangesButton;

    private boolean help;
    private GuiButton helpButton;

    private boolean initialized = false;

    public AmbienceGUI(AmbienceTileEntity target) {
        //super(new TextComponentTranslation("narrator.screen.globalambiencegui"));
        this.target = target;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();

        xTopLeft = (this.width - texWidth) / 2;
        yTopLeft = (this.height - texHeight) / 2;

        List<AbstractTab> tabs = getAllTabs();

        if(!initialized)
        {
            /*mainTab = new MainTab(this);
            mainTab.initialInit();
            mainTab.guiOpenedInit();
            setHighlightedTab(mainTab);
            boundsTab = new BoundsTab(this);
            boundsTab.initialInit();
            boundsTab.guiOpenedInit();
            boundsTab.deactivate();
            priorityTab = new PriorityTab(this);
            priorityTab.initialInit();
            priorityTab.guiOpenedInit();
            priorityTab.deactivate();
            delayTab = new DelayTab(this);
            delayTab.initialInit();
            delayTab.guiOpenedInit();
            delayTab.deactivate();
            miscTab = new MiscTab(this);
            miscTab.initialInit();
            miscTab.guiOpenedInit();
            miscTab.deactivate();*/

            //set up in advance to get the correct amount of active tabs
            //mainTab.setFieldFromData(target.data);

            for(AbstractTab tab : tabs) {
                tab.updateMetaValues(this);
                tab.initialInit();
                tab.updateWidgetPosition();
                if(tab instanceof MainTab) setHighlightedTab(tab);
                else tab.deactivate();
            }

            loadDataFromTile();

            initialized = true;
        }
        else
        {
            for(AbstractTab tab : tabs)
            {
                tab.updateMetaValues(this);
                tab.updateWidgetPosition();
                tab.refreshWidgets();
            }
        }

        confirmChangesButton = addButton(new GuiButton(CONFIRM_BUTTON_ID, xTopLeft + 8, yTopLeft + texHeight + 8, 100, 20, I18n.format("gui.ambienceblock.button.confirm")));
        /*confirmChanges = addButton(new GuiButton(xTopLeft + 8, yTopLeft + texHeight + 8, 100, 20, "Confirm Changes", button -> {
            saveDataToTile();
        }));*/

        help = false;
        //bHelp = addButton(new GuiButton(xTopLeft + texWidth - 16 - 8, yTopLeft + texHeight - 16 - 8, 16, 16, "", button -> { clickHelp(); }));
        helpButton = addButton(new GuiButton(HELP_BUTTON_ID, xTopLeft + texWidth - 16 - 8, yTopLeft + texHeight - 16 - 8, 16, 16, ""));

        //loadDataFromTile();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawMainBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawTabs(mouseX, mouseY);

        if(highlightedTab != null)
            highlightedTab.render(mouseX, mouseY, partialTicks);

        if(help && highlightedTab != null)
            highlightedTab.renderToolTip(mouseX, mouseY);

        drawHelp();

        /*if(highlightIndex == 0)
            mainTab.render(mouseX, mouseY, partialTicks);*/
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();

        if(highlightedTab != null)
            highlightedTab.tick();
    }

    private void clickHelp() {
        help = !help;
    }

    private void loadDataFromTile() {
        /*mainTab.setFieldFromData(target.data);

        for(AbstractTab tab : getActiveTabs()) if(!(tab instanceof MainTab)) tab.setFieldFromData(target.data);*/
        setData(target.data);
    }

    private void saveDataToTile() {
        /*AmbienceTileEntityData data = new AmbienceTileEntityData();

        for(AbstractTab tab : getActiveTabs()) tab.setDataFromField(data);

        PacketHandler.NET.sendToServer(new PacketUpdateAmbienceTE(target.getPos(), data));*/
        NetworkHandler.NET.sendToServer(new PacketUpdateAmbienceTE(target.getPos(), getData()));
    }

    public AmbienceTileEntityData getData() {
        AmbienceTileEntityData data = new AmbienceTileEntityData();

        for(AbstractTab tab : getActiveTabs()) tab.setDataFromField(data);

        return data;
    }

    public void setData(AmbienceTileEntityData data) {
        mainTab.setFieldFromData(data);

        for(AbstractTab tab : getActiveTabs()) if(!(tab instanceof MainTab)) tab.setFieldFromData(data);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseType) throws IOException {
        List<AbstractTab> tabs = getActiveTabs();
        for(int i = 0; i < tabs.size(); i++) {
            if(isMouseInTab(mouseX, mouseY, i)) {
                // Play Press sound
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                //setHighlightIndex(i);
                setHighlightedTab(getActiveTabs().get(i));
                super.mouseClicked(mouseX, mouseY, mouseType);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseType);
    }

    private AbstractTab getHighlightedTab()
    {
        return highlightedTab;
    }

    private void setHighlightedTab(AbstractTab tab)
    {
        if(highlightedTab != null)
            highlightedTab.deactivate();

        highlightedTab = tab;
        highlightedTab.activate();
    }

    private List<AbstractTab> getAllTabs()
    {
        List<AbstractTab> list = new ArrayList<>();

        list.add(mainTab);

        list.add(boundsTab);

        list.add(priorityTab);

        list.add(delayTab);

        list.add(miscTab);

        return list;
    }

    private List<AbstractTab> getActiveTabs() {
        List<AbstractTab> list = new ArrayList<>();

        list.add(mainTab);

        list.add(boundsTab);

        if(mainTab.usePriority.isChecked())
            list.add(priorityTab);

        if(mainTab.useDelay.isChecked())
            list.add(delayTab);

        list.add(miscTab);

        return list;
    }

    private int getTabWidth() {
        return texWidth/getActiveTabs().size();
    }

    private TabState getTabState(int mouseX, int mouseY, AbstractTab tab) {
        if(getHighlightedTab() == tab) return TabState.HIGHLIGHTED;
        if(getHoveredTab(mouseX, mouseY) == tab) return TabState.HOVERED;
        return TabState.NEUTRAL;
    }

    private AbstractTab getHoveredTab(int mouseX, int mouseY) {
        List<AbstractTab> list = getActiveTabs();
        for(int i = 0; i < list.size(); i++) {
            if(isMouseInTab(mouseX, mouseY, i))
                return list.get(i);
        }
        return null;
    }

    private boolean isMouseInTab(int mouseX, int mouseY, int index) {
        int size = getTabWidth();
        return mouseX > xTopLeft + index * size && mouseX < xTopLeft + index * size + size && mouseY > yTopLeft && mouseY < yTopLeft + tabHeight;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawMainBackground() {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect(xTopLeft, yTopLeft + tabHeight, 0, tabHeight, texWidth, texHeight - tabHeight);
    }

    public void drawTab(int x, int size, String text, TabState state) {
        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        if(size < 32) size = 32;
        int spaceToFillOut = size - 32;
        int spaceCount = 0;

        this.drawTexturedModalRect(xTopLeft + x, yTopLeft, 0, state.getySpriteOffset(), 16, 16);
        while(spaceToFillOut > spaceCount) {
            if(spaceToFillOut - spaceCount < 16) {
                this.drawTexturedModalRect(xTopLeft + x + tabEdgeWidth + spaceCount, yTopLeft, 16, state.getySpriteOffset(), spaceToFillOut - spaceCount, 16);
                spaceCount = spaceToFillOut;
            }
            else {
                this.drawTexturedModalRect(xTopLeft + x + tabEdgeWidth + spaceCount, yTopLeft, 16, state.getySpriteOffset(), 16, 16);
                spaceCount += 16;
            }
        }
        this.drawTexturedModalRect(xTopLeft + x + size - tabEdgeWidth, yTopLeft, 32, state.getySpriteOffset(), 16, 16);

        drawCenteredString(this.fontRenderer, text, xTopLeft + x + size/2, yTopLeft + (tabHeight - this.fontRenderer.FONT_HEIGHT) / 2 + 2, 0xFFFFFF);
    }

    private void drawTabs(int mouseX, int mouseY) {
        List<AbstractTab> options = getActiveTabs();
        int tabSize = getTabWidth();
        for(int i = 0; i < options.size(); i++) {
            drawTab(tabSize * i, tabSize, options.get(i).getName(), getTabState(mouseX, mouseY, options.get(i)));
        }
    }

    private void drawHelp() {
        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect(xTopLeft + texWidth - 16 - 8, yTopLeft + texHeight - 16 - 8, 48, getHelpState().getySpriteOffset(), 16, 16);
    }

    private TabState getHelpState() {
        if(helpButton.isMouseOver()) return TabState.HOVERED;
        if(help) return TabState.HIGHLIGHTED;
        return TabState.NEUTRAL;
    }

    //meta
    public void addWidget(Gui widget) {
        this.children.add(widget);
    }

    @Nonnull
    public <T extends Widget> T addButton(@Nonnull T widget) {
        return super.addButton(widget);
    }

    enum TabState {
        NEUTRAL(texHeight),
        HIGHLIGHTED(texHeight + 16),
        HOVERED(texHeight + 32);

        public int getySpriteOffset() {
            return ySpriteOffset;
        }

        private final int ySpriteOffset;

        TabState(int ySpriteOffset) {
            this.ySpriteOffset = ySpriteOffset;
        }
    }
}
