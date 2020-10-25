package com.sekai.ambienceblock.client.gui.ambience;

import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.client.gui.IGuiEventListener;
import com.sekai.ambienceblock.client.gui.Screen;
import com.sekai.ambienceblock.client.gui.widgets.StringListWidget;
import com.sekai.ambienceblock.client.gui.widgets.TextFieldWidget;
import com.sekai.ambienceblock.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChooseSoundGUI extends Screen implements StringListWidget.IPressable {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(AmbienceMod.MOD_ID, "textures/gui/ambience_gui.png");

    public static final int texWidth = 256;
    public static final int texHeight = 184;
    public int xTopLeft, yTopLeft;

    private Screen prevScreen;
    private TextFieldWidget targetField;
    public FontRenderer font;

    protected static final int yOffset = 8;
    protected static final int separation = 16;

    Minecraft mc = Minecraft.getMinecraft();

    StringListWidget list;

    private String selectedDomain = "";
    private String selected = "";
    private List<String> activeList = new ArrayList<>();
    private String biasSelectionName = "";

    public ChooseSoundGUI(Screen prevScreen, TextFieldWidget targetField) {
        super(new TranslationTextComponent("narrator.screen.choosesound"));

        this.font = mc.fontRenderer;
        this.prevScreen = prevScreen;
        this.targetField = targetField;

        String previousString = targetField.getText();

        if(previousString.split(":").length <= 2) {
            if(previousString.split(":").length == 1) {
                selectedDomain = "minecraft";

                selected = previousString;
                boolean validate = false;

                for (ResourceLocation element : Util.getAvailableSounds()) {
                    if(element.getResourceDomain().equals(selectedDomain) && element.getResourcePath().equals(selected)) validate = true;
                }

                if(validate) {
                    int firstIndex = selected.lastIndexOf(".") + 1;

                    biasSelectionName = selected.substring(firstIndex);

                    selected = selected.replaceAll(selected.substring(firstIndex), "");
                } else {
                    selectedDomain = ""; selected = "";
                }
            } else {
                selectedDomain = previousString.split(":")[0];
                if(!selectedDomain.equals("")) {
                    selected = previousString.split(":")[1];

                    boolean validate = false;
                    for (ResourceLocation element : Util.getAvailableSounds()) {
                        if(element.getResourceDomain().equals(selectedDomain) && element.getResourcePath().equals(selected)) validate = true;
                    }

                    if(validate) {
                        int firstIndex = selected.lastIndexOf(".") + 1;

                        biasSelectionName = selected.substring(firstIndex);

                        selected = selected.replaceAll(selected.substring(firstIndex), "");
                    } else {
                        selectedDomain = ""; selected = "";
                    }
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        xTopLeft = (this.width - texWidth) / 2;
        yTopLeft = (this.height - texHeight) / 2;

        this.children.add(list = new StringListWidget(xTopLeft + separation, yTopLeft + separation + yOffset, texWidth - separation * 2, texHeight - separation * 2 - yOffset, 4, 16, font, new StringListWidget.IPressable() {
            @Override
            public void onClick(StringListWidget list, int index, String name) {

            }

            @Override
            public void onDoubleClick(StringListWidget list, int index, String name) {
                if(selectedDomain.equals("")) {
                    selectedDomain = name;
                    updateList();
                } else {
                    if(name.equals("<...>")) {
                        if(selected.equals("")) {
                            selectedDomain = "";
                            updateList();
                        } else {
                            int firstIndex = selected.lastIndexOf(".", selected.lastIndexOf(".") - 1) + 1;
                            int lastIndex = selected.lastIndexOf(".") + 1;

                            selected = selected.replaceAll(selected.substring(firstIndex, lastIndex), "");
                            updateList();
                        }
                    } else {
                        if(name.contains("<")) {
                            name = name.replace("<", "").replace(">", "");
                            selected += name + ".";
                            updateList();
                        } else {
                            selected += name;
                            onConfirm();
                        }
                    }
                }
            }
        }));

        updateList();
    }

    public void updateList() {
        activeList.clear();
        list.clearList();
        list.setSelectionIndex(0);
        list.resetScroll();
        if(selectedDomain.equals("")) {
            for (ResourceLocation element : Util.getAvailableSounds()) {
                if(!activeList.contains(element.getResourceDomain())) activeList.add(element.getResourceDomain());
            }
        } else {
            for (ResourceLocation element : Util.getAvailableSounds()) {
                if(selectedDomain.equals(element.getResourceDomain())) {
                    //String name = element.getPath().replace(selected,"").split("\\.")[0];
                    String name = element.getResourcePath().replaceAll("\\b" + selected + "\\b", "").split("\\.")[0];
                    if(element.getResourcePath().replace(selected,"").chars().filter(ch -> ch == '.').count() > 0) {
                        //is a folder
                        name = "<" + name + ">";
                    }  //is not a folder

                    if(element.getResourcePath().contains(selected) && !activeList.contains(name)) activeList.add(name);
                }
            }
            Collections.sort(activeList);
            activeList.add(0, "<...>");
        }

        list.addElements(activeList);

        if(!biasSelectionName.equals("")) {
            list.setSelectionByString(biasSelectionName);
            biasSelectionName = "";
        }
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);

        drawMainBackground();

        drawCenteredString(font, selectedDomain + ":" + selected, xTopLeft + texWidth/2, yTopLeft + separation / 2, 0xFFFFFF);

        list.render(p_render_1_, p_render_2_);
    }

    @Override
    public void onClose() {
        mc.displayGuiScreen(prevScreen);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onClick(StringListWidget list, int index, String name) {
    }

    @Override
    public void onDoubleClick(StringListWidget list, int index, String name) {
        targetField.setText(name);
        mc.displayGuiScreen(prevScreen);
    }

    public void onConfirm() {
        mc.displayGuiScreen(prevScreen);
        //targetField.setText(list.getElementString());
        targetField.setText(selectedDomain + ":" + selected);
    }

    public void drawMainBackground() {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.blit(xTopLeft, yTopLeft, 0, 0, texWidth, texHeight);
    }

    @Override
    public List<? extends IGuiEventListener> children() {
        return null;
    }

    public class SoundSound extends SoundElement implements ChildSoundElement {
        private String name;
        private final SoundHolderElement parent;

        public SoundSound(SoundHolderElement parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public String getAsString() {
            return name;
        }

        @Override
        public String getTotalPath() {
            String finalPath = "";
            List<String> path = new ArrayList<>();
            path.add(name);
            SoundHolderElement e = getParent();
            while(!(e instanceof SoundDomain)) {
                path.add(e.getAsString());
                e = ((ChildSoundElement)e).getParent();
            }
            path.add(e.getAsString());

            Collections.reverse(path);

            if(path.size() > 0) {
                finalPath += path.get(0) + ":";
                for(int i = 0; i < path.size() - 1; i++) {
                    int j = i + 1;
                    finalPath += path.get(j) + ".";
                }
                finalPath += path.get(path.size() - 1);
            }

            return finalPath;
        }

        @Override
        public SoundHolderElement getParent() {
            return parent;
        }
    }
}

