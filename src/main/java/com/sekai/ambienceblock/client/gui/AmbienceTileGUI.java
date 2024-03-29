package com.sekai.ambienceblock.client.gui;

import com.sekai.ambienceblock.client.gui.widgets.Widget;
import com.sekai.ambienceblock.client.gui.widgets.button.CheckboxButton;
import com.sekai.ambienceblock.client.eventhandler.AmbienceEventHandler;
import com.sekai.ambienceblock.client.gui.widgets.TextInstance;
import com.sekai.ambienceblock.network.packets.PacketUpdateAmbienceTE;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntity;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntityData;
import com.sekai.ambienceblock.tileentity.ambiencetilebounds.*;
import com.sekai.ambienceblock.util.BoundsUtil;
import com.sekai.ambienceblock.network.NetworkHandler;
import com.sekai.ambienceblock.util.ParsingUtil;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.HoverChecker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class AmbienceTileGUI extends GuiScreen {
    private AmbienceTileEntity tile;

    private boolean initiated = false;

    //Parameter
    private int boundType;

    //Coordinate stuff
    static final int offsetX = 16;
    static final int offsetY = 16;
    static final int separation = 8;

    static final int rowHeight = 20;

    static final int checkBoxWidth = 150;
    static final int checkBoxHeight = 20;

    static final int textFieldWidth = 200;
    static final int textFieldHeight = 20;

    //Row choice
    static final int musicRow = 0;
    static final int soundRow = 1;
    static final int boundsRow = 2;
    static final int offsetRow = 3;
    static final int priorityRow = 4;
    static final int delayRow = 5;

    //Widget mapping
    TextInstance textMusic;
    GuiTextField musicName;
    CheckboxButton checkFuse;
    CheckboxButton checkRedstone;

    TextInstance textSoundOption;
    TextInstance textSoundVolume;
    GuiTextField soundVolume;
    TextInstance textSoundPitch;
    GuiTextField soundPitch;

    TextInstance textBounds;
    GuiButton buttonBounds;

    //Sphere
    TextInstance textSphereRadius;
    GuiTextField sphereRadius;

    //Cylinder
    TextInstance textCylinderRadius;
    GuiTextField cylinderRadius;
    TextInstance textCylinderHeight;
    GuiTextField cylinderHeight;

    //Simple Cubic
    TextInstance textCubicX;
    GuiTextField cubicX;
    TextInstance textCubicY;
    GuiTextField cubicY;
    TextInstance textCubicZ;
    GuiTextField cubicZ;

    CheckboxButton checkGlobal;

    TextInstance textOffset;
    TextInstance textOffsetX;
    GuiTextField tileOffsetX;
    TextInstance textOffsetY;
    GuiTextField tileOffsetY;
    TextInstance textOffsetZ;
    GuiTextField tileOffsetZ;

    TextInstance textPriority;
    GuiTextField priorityNumber;
    CheckboxButton checkPriority;

    TextInstance textDelay;
    TextInstance textMinDelay;
    GuiTextField minDelay;
    TextInstance textMaxDelay;
    GuiTextField maxDelay;
    CheckboxButton checkDelay;

    //Widget category
    ArrayList<Widget> boundsSphere = new ArrayList<>();
    ArrayList<Widget> boundsCylinder = new ArrayList<>();
    ArrayList<Widget> boundsCubic = new ArrayList<>();

    //Predicate
    private final Predicate<String> numberFilter = (stringIn) -> {
        if (!net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++) {
                if (!ParsingUtil.isNumber(stringIn.charAt(i)))
                    return false;
            }

        }
        return true;
    };

    private final Predicate<String> negativeNumberFilter = (stringIn) -> {
        if (net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            return true;
        } else {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++)
            {
                if (ParsingUtil.isNumber(stringIn.charAt(i)) || (i == 0 && stringIn.charAt(i) == '-'))
                    continue;

                return false;
            }

            return true;
        }
    };

    private final Predicate<String> decimalNumberFilter = (stringIn) -> {
        if (net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            return true;
        } else {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++)
            {
                if (!ParsingUtil.isNumberOrDot(stringIn.charAt(i)))
                    return false;
            }

            if(ParsingUtil.countChar(stringIn, '.') > 1)
                return false;

            return true;
        }
    };

    public AmbienceTileGUI(AmbienceTileEntity tile) {
        super(new TranslationTextComponent("narrator.screen.globalambiencegui"));
        this.tile = tile;
    }

    @Override
    public void init() {
        textMusic = new TextInstance(offsetX, getRowY(musicRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.music_id") + " :", font);
        musicName = new GuiTextField(font, getNextToWidgetX(textMusic), getRowY(musicRow) + getOffsetY(textFieldHeight), textFieldWidth, textFieldHeight, tile.getMusicName());
        this.children.add(musicName);
        checkFuse = this.addButton(new CheckboxButton(getNextToWidgetX(musicName), getRowY(musicRow) + getOffsetY(checkBoxHeight), 20 + font.getStringWidth("Should fuse"), checkBoxHeight, "Should fuse", tile.data.shouldFuse()));
        checkRedstone = this.addButton(new CheckboxButton(getNextToWidgetX(checkFuse), getRowY(musicRow) + getOffsetY(checkBoxHeight), checkBoxWidth, checkBoxHeight, "Needs Redstone", tile.data.needsRedstone()));

        textSoundOption = new TextInstance(offsetX, getRowY(soundRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.music_par") + " :", font);
        textSoundVolume = new TextInstance(getNextToWidgetX(textSoundOption), getRowY(soundRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.volume"), font);
        soundVolume = new GuiTextField(font, getNextToWidgetX(textSoundVolume), getRowY(soundRow) + getOffsetY(20), 40, 20, "no");
        this.children.add(soundVolume);
        soundVolume.setValidator(decimalNumberFilter);
        soundVolume.setMaxStringLength(5);

        textSoundPitch = new TextInstance(getNextToWidgetX(soundVolume), getRowY(soundRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.pitch"), font);
        soundPitch = new GuiTextField(font, getNextToWidgetX(textSoundPitch), getRowY(soundRow) + getOffsetY(20), 40, 20, "no");
        this.children.add(soundPitch);
        soundPitch.setValidator(decimalNumberFilter);
        soundPitch.setMaxStringLength(5);

        textBounds = new TextInstance(offsetX, getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.bounds") + " :", font);

        buttonBounds = addButton(new Button(textBounds.x + textBounds.getWidth() + separation, getRowY(boundsRow) + getOffsetY(20), 60, 20, "no", button -> {
            moveToNextBoundType();
        }));

        textSphereRadius = new TextInstance(getNextToWidgetX(buttonBounds), getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.radius"), font);
        sphereRadius = new GuiTextField(font, getNextToWidgetX(textSphereRadius), getRowY(boundsRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(sphereRadius);
        //sphereRadius.setValidator(character -> StringUtils.isNumeric(character));
        sphereRadius.setValidator(decimalNumberFilter);
        sphereRadius.setMaxStringLength(5);
        //sphereRadius.setText(String.valueOf((int) ((SphereBounds)tile.data.getBounds()).getRadius()));
        sphereRadius.setText("0");
        boundsSphere.add(textSphereRadius);
        boundsSphere.add(sphereRadius);

        textCylinderRadius = new TextInstance(getNextToWidgetX(buttonBounds), getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.radius"), font);
        cylinderRadius = new GuiTextField(font, getNextToWidgetX(textCylinderRadius), getRowY(boundsRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(cylinderRadius);
        //cylinderRadius.setValidator(character -> StringUtils.isNumeric(character));
        cylinderRadius.setValidator(decimalNumberFilter);
        cylinderRadius.setMaxStringLength(5);
        cylinderRadius.setText("0");
        boundsCylinder.add(textCylinderRadius);
        boundsCylinder.add(cylinderRadius);

        textCylinderHeight = new TextInstance(getNextToWidgetX(cylinderRadius), getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.height"), font);
        cylinderHeight = new GuiTextField(font, getNextToWidgetX(textCylinderHeight), getRowY(boundsRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(cylinderHeight);
        //cylinderHeight.setValidator(character -> StringUtils.isNumeric(character));
        cylinderHeight.setValidator(decimalNumberFilter);
        cylinderHeight.setMaxStringLength(5);
        cylinderHeight.setText("0");
        boundsCylinder.add(textCylinderHeight);
        boundsCylinder.add(cylinderHeight);

        textCubicX = new TextInstance(getNextToWidgetX(buttonBounds), getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, "X", font);
        cubicX = new GuiTextField(font, getNextToWidgetX(textCubicX), getRowY(boundsRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(cubicX);
        cubicX.setValidator(decimalNumberFilter);
        cubicX.setMaxStringLength(5);
        cubicX.setText("0");
        boundsCubic.add(textCubicX);
        boundsCubic.add(cubicX);

        textCubicY = new TextInstance(getNextToWidgetX(cubicX), getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, "Y", font);
        cubicY = new GuiTextField(font, getNextToWidgetX(textCubicY), getRowY(boundsRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(cubicY);
        cubicY.setValidator(decimalNumberFilter);
        cubicY.setMaxStringLength(5);
        cubicY.setText("0");
        boundsCubic.add(textCubicY);
        boundsCubic.add(cubicY);

        textCubicZ = new TextInstance(getNextToWidgetX(cubicY), getRowY(boundsRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, "Z", font);
        cubicZ = new GuiTextField(font, getNextToWidgetX(textCubicZ), getRowY(boundsRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(cubicZ);
        cubicZ.setValidator(decimalNumberFilter);
        cubicZ.setMaxStringLength(5);
        cubicZ.setText("0");
        boundsCubic.add(textCubicZ);
        boundsCubic.add(cubicZ);

        checkGlobal = this.addButton(new CheckboxButton(this.width - checkBoxWidth / 2 + separation, getRowY(boundsRow) + getOffsetY(checkBoxHeight), checkBoxWidth, checkBoxHeight, "Global", tile.isGlobal()));

        textOffset = new TextInstance(offsetX, getRowY(offsetRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.offset") + " :", font);

        textOffsetX = new TextInstance(getNextToWidgetX(textOffset), getRowY(offsetRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, "X", font);
        tileOffsetX = new GuiTextField(font, getNextToWidgetX(textOffsetX), getRowY(offsetRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(tileOffsetX);
        tileOffsetX.setValidator(negativeNumberFilter);
        tileOffsetX.setMaxStringLength(8);

        textOffsetY = new TextInstance(getNextToWidgetX(tileOffsetX), getRowY(offsetRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, "Y", font);
        tileOffsetY = new GuiTextField(font, getNextToWidgetX(textOffsetY), getRowY(offsetRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(tileOffsetY);
        tileOffsetY.setValidator(negativeNumberFilter);
        tileOffsetY.setMaxStringLength(8);

        textOffsetZ = new TextInstance(getNextToWidgetX(tileOffsetY), getRowY(offsetRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, "Z", font);
        tileOffsetZ = new GuiTextField(font, getNextToWidgetX(textOffsetZ), getRowY(offsetRow) + getOffsetY(20), 30, 20, "no");
        this.children.add(tileOffsetZ);
        tileOffsetZ.setValidator(negativeNumberFilter);
        tileOffsetZ.setMaxStringLength(8);

        //setDefaultBoundType(tile.data.getBounds().getID());

        textPriority = new TextInstance(offsetX, getRowY(priorityRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.priority") + " :", font);
        priorityNumber = new GuiTextField(font, getNextToWidgetX(textPriority), getRowY(priorityRow) + getOffsetY(20), 20, 20, String.valueOf(tile.getPriority()));
        this.children.add(priorityNumber);
        priorityNumber.setValidator(numberFilter);
        priorityNumber.setMaxStringLength(2);
        priorityNumber.setText(String.valueOf(tile.getPriority()));
        checkPriority = this.addButton(new CheckboxButton(getNextToWidgetX(priorityNumber), getRowY(priorityRow) + getOffsetY(checkBoxHeight), checkBoxWidth, checkBoxHeight, "Using priority", tile.isUsingPriority()));

        textDelay = new TextInstance(offsetX, getRowY(delayRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.delay") + " :", font);
        textMinDelay = new TextInstance(getNextToWidgetX(textDelay), getRowY(delayRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.min"), font);
        minDelay = new GuiTextField(font, getNextToWidgetX(textMinDelay), getRowY(delayRow) + getOffsetY(20), 50, 20, String.valueOf(tile.data.getMinDelay()));
        this.children.add(minDelay);
        minDelay.setValidator(numberFilter);
        minDelay.setMaxStringLength(4);
        textMaxDelay = new TextInstance(getNextToWidgetX(minDelay), getRowY(delayRow) + getOffsetY(font.FONT_HEIGHT), 0xFFFFFF, I18n.format("ui.ambienceblocks.max"), font);
        maxDelay = new GuiTextField(font, getNextToWidgetX(textMaxDelay), getRowY(delayRow) + getOffsetY(20), 50, 20, String.valueOf(tile.data.getMaxDelay()));
        this.children.add(maxDelay);
        maxDelay.setValidator(numberFilter);
        maxDelay.setMaxStringLength(4);
        checkDelay = this.addButton(new CheckboxButton(getNextToWidgetX(maxDelay), getRowY(delayRow) + getOffsetY(checkBoxHeight), checkBoxWidth, checkBoxHeight, "Using delay", tile.data.isUsingDelay()));

        this.addButton(new Button(offsetX, this.height - offsetY - 20, 98, 20, "Confirm changes", button -> confirmChanges()));

        this.addButton(new Button(width - offsetX - separation - 40 - 40, this.height - offsetY - 20, 40, 20, I18n.format("ui.ambienceblocks.copy"), button -> saveDataToClipboard()));

        this.addButton(new Button(width - offsetX - 40, this.height - offsetY - 20, 40, 20, I18n.format("ui.ambienceblocks.paste"), button -> loadDataFromClipboard()));

        getFieldsFromData(tile.data);

        initiated = true;
    }

    public void getFieldsFromData(AmbienceTileEntityData data) {
        musicName.setText(data.getSoundName());
        setCheckBoxChecked(checkFuse, data.shouldFuse());
        soundVolume.setText(String.valueOf(data.getVolume()));
        soundPitch.setText(String.valueOf(data.getPitch()));
        setCheckBoxChecked(checkGlobal, data.isGlobal());
        setCheckBoxChecked(checkRedstone, data.needsRedstone());

        setBoundTypeFromData(data);

        tileOffsetX.setText(String.valueOf(data.getOffset().getX()));
        tileOffsetY.setText(String.valueOf(data.getOffset().getY()));
        tileOffsetZ.setText(String.valueOf(data.getOffset().getZ()));

        priorityNumber.setText(String.valueOf(data.getPriority()));
        setCheckBoxChecked(checkPriority, data.isUsingPriority());
        minDelay.setText(String.valueOf(data.getMinDelay()));
        maxDelay.setText(String.valueOf(data.getMaxDelay()));
        setCheckBoxChecked(checkDelay, data.isUsingDelay());
    }

    public AmbienceTileEntityData getDataFromFields() {
        AmbienceTileEntityData data = new AmbienceTileEntityData();

        data.setSoundName(musicName.getText());
        data.setShouldFuse(checkFuse.isChecked());
        data.setNeedRedstone(checkRedstone.isChecked());

        data.setVolume(ParsingUtil.tryParseFloat(soundVolume.getText()));
        data.setPitch(ParsingUtil.tryParseFloat(soundPitch.getText()));

        if(boundType == SphereBounds.id) {
            SphereBounds bounds = new SphereBounds();

            bounds.setRadius(ParsingUtil.tryParseDouble(sphereRadius.getText()));

            data.setBounds(bounds);
        }

        if(boundType == CylinderBounds.id) {
            CylinderBounds bounds = new CylinderBounds();

            bounds.setRadius(ParsingUtil.tryParseDouble(cylinderRadius.getText()));
            bounds.setLength(ParsingUtil.tryParseDouble(cylinderHeight.getText()));

            data.setBounds(bounds);
        }

        if(boundType == CubicBounds.id) {
            CubicBounds bounds = new CubicBounds();

            bounds.setxSize(ParsingUtil.tryParseDouble(cubicX.getText()));
            bounds.setySize(ParsingUtil.tryParseDouble(cubicY.getText()));
            bounds.setzSize(ParsingUtil.tryParseDouble(cubicZ.getText()));

            data.setBounds(bounds);
        }

        if(boundType == NoneBounds.id) {
            data.setBounds(new NoneBounds());
        }

        /*data.setOffset(new BlockPos(
                ParsingUtil.tryParseInt(tileOffsetX.getText()),
                ParsingUtil.tryParseInt(tileOffsetY.getText()),
                ParsingUtil.tryParseInt(tileOffsetZ.getText())
        ));*/

        //((SphereBounds)data.getBounds()).setRadius(ParsingUtil.tryParseDouble(sphereRadius.getText()));
        data.setGlobal(checkGlobal.isChecked());

        data.setPriority(ParsingUtil.tryParseInt(priorityNumber.getText()));
        data.setUsePriority(checkPriority.isChecked());

        data.setMinDelay(ParsingUtil.tryParseInt(minDelay.getText()));
        data.setMaxDelay(ParsingUtil.tryParseInt(maxDelay.getText()));
        data.setUseDelay(checkDelay.isChecked());

        return data;
    }

    public void setBoundTypeFromData(AmbienceTileEntityData data) {
        setBoundType(data.getBounds().getID());

        AbstractBounds tempBounds = data.getBounds();

        if(boundType == SphereBounds.id) {
            SphereBounds bounds = (SphereBounds) tempBounds;

            resetBoundFields();

            sphereRadius.setText(String.valueOf((int) (bounds.getRadius())));
        }

        if(boundType == CylinderBounds.id) {
            CylinderBounds bounds = (CylinderBounds) tempBounds;

            resetBoundFields();

            cylinderRadius.setText(String.valueOf((int) (bounds.getRadius())));
            cylinderHeight.setText(String.valueOf((int) (bounds.getLength())));
        }

        if(boundType == CubicBounds.id) {
            CubicBounds bounds = (CubicBounds) tempBounds;

            resetBoundFields();

            cubicX.setText(String.valueOf((int) (bounds.getxSize())));
            cubicY.setText(String.valueOf((int) (bounds.getySize())));
            cubicZ.setText(String.valueOf((int) (bounds.getzSize())));
        }

        if(boundType == NoneBounds.id) {
            //NoneBounds bounds = (NoneBounds) tempBounds;

            resetBoundFields();
        }
    }

    public void setCheckBoxChecked(CheckboxButton check, boolean b) {
        if(b && !check.isChecked())
            check.onPress();
        if(!b && check.isChecked())
            check.onPress();
    }

    public void resetBoundFields() {
        sphereRadius.setText(String.valueOf(0));

        cylinderRadius.setText(String.valueOf(0));
        cylinderHeight.setText(String.valueOf(0));

        cubicX.setText(String.valueOf(0));
        cubicY.setText(String.valueOf(0));
        cubicZ.setText(String.valueOf(0));
    }

    public void moveToNextBoundType() {
        if(boundType == BoundsUtil.lastBoundType)
        {
            setBoundType(0);
        }
        else
        {
            setBoundType(boundType + 1);
        }
    }

    public void setBoundType(int boundType) {
        this.boundType = boundType;
        deactivateOtherBoundWidget();

        buttonBounds.setMessage(BoundsUtil.getBoundsFromType(boundType).getName());

        if(boundType == SphereBounds.id) {
            for(Widget widget : boundsSphere) {
                widget.active = true;
                widget.visible = true;
            }

            checkGlobal.x = getNextToWidgetX(sphereRadius);//sphereRadius.x + sphereRadius.getWidth() + separation;
        }

        if(boundType == CylinderBounds.id) {
            for(Widget widget : boundsCylinder) {
                widget.active = true;
                widget.visible = true;
            }

            checkGlobal.x = getNextToWidgetX(cylinderHeight);//cylinderHeight.x + cylinderHeight.getWidth() + separation;
        }

        if(boundType == CubicBounds.id) {
            for(Widget widget : boundsCubic) {
                widget.active = true;
                widget.visible = true;
            }

            checkGlobal.x = getNextToWidgetX(cubicZ);//cubicZ.x + cubicZ.getWidth() + separation;
        }

        if(boundType == NoneBounds.id) {
            checkGlobal.x = getNextToWidgetX(buttonBounds);//cubicZ.x + cubicZ.getWidth() + separation;
        }
    }

    public void deactivateOtherBoundWidget() {
        for(Widget widget : boundsSphere) {
            widget.active = false;
            widget.visible = false;
        }
        for(Widget widget : boundsCylinder) {
            widget.active = false;
            widget.visible = false;
        }
        for(Widget widget : boundsCubic) {
            widget.active = false;
            widget.visible = false;
        }
    }

    public void saveDataToClipboard() {
        AmbienceEventHandler.instance.setClipboard(getDataFromFields());
    }

    public void loadDataFromClipboard() {
        AmbienceTileEntityData data = AmbienceEventHandler.instance.getClipboard();

        if(data == null)
            return;

        getFieldsFromData(data);
    }

    public void confirmChanges() {
        AmbienceTileEntityData data = getDataFromFields();

        NetworkHandler.NET.sendToServer(new PacketUpdateAmbienceTE(tile.getPos(), data));
    }

    public int getNextToWidgetX(Widget n) {
        return n.x + n.getWidth() + separation;
    }

    public int getRowY(int row) {
        return offsetY + rowHeight * row + separation * row;
    }

    public int getOffsetY(int height) {
        return (rowHeight - height) / 2;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();

        super.render(mouseX, mouseY, partialTicks);

        if(!initiated)
            return;

        //drawString(font, "Music ID :", offsetX, offsetY + (musicName.getHeight() - font.FONT_HEIGHT) / 2, 0xFFFFFF);
        textMusic.render(mouseX, mouseY);
        musicName.render(mouseX, mouseY, partialTicks);

        textSoundOption.render(mouseX, mouseY);
        textSoundVolume.render(mouseX, mouseY);
        soundVolume.render(mouseX, mouseY, partialTicks);
        textSoundPitch.render(mouseX, mouseY);
        soundPitch.render(mouseX, mouseY, partialTicks);

        //drawString(font, "Max distance :", offsetX, sphereRadius.y + (sphereRadius.getHeight() - font.FONT_HEIGHT) / 2, 0xFFFFFF);
        textBounds.render(mouseX, mouseY);
        textSphereRadius.render(mouseX, mouseY);
        sphereRadius.render(mouseX, mouseY, partialTicks);
        textCylinderRadius.render(mouseX, mouseY);
        cylinderRadius.render(mouseX, mouseY, partialTicks);
        textCylinderHeight.render(mouseX, mouseY);
        cylinderHeight.render(mouseX, mouseY, partialTicks);
        textCubicX.render(mouseX, mouseY);
        cubicX.render(mouseX, mouseY, partialTicks);
        textCubicY.render(mouseX, mouseY);
        cubicY.render(mouseX, mouseY, partialTicks);
        textCubicZ.render(mouseX, mouseY);
        cubicZ.render(mouseX, mouseY, partialTicks);

        textOffset.render(mouseX, mouseY);
        textOffsetX.render(mouseX, mouseY);
        tileOffsetX.render(mouseX, mouseY, partialTicks);
        textOffsetY.render(mouseX, mouseY);
        tileOffsetY.render(mouseX, mouseY, partialTicks);
        textOffsetZ.render(mouseX, mouseY);
        tileOffsetZ.render(mouseX, mouseY, partialTicks);

        //drawString(font, "Priority :", offsetX, priorityNumber.y + (priorityNumber.getHeight() - font.FONT_HEIGHT) / 2, 0xFFFFFF);
        textPriority.render(mouseX, mouseY);
        priorityNumber.render(mouseX, mouseY, partialTicks);

        textDelay.render(mouseX, mouseY);
        textMinDelay.render(mouseX, mouseY);
        minDelay.render(mouseX, mouseY, partialTicks);
        textMaxDelay.render(mouseX, mouseY);
        maxDelay.render(mouseX, mouseY, partialTicks);

        //drawString(font, "Position : " + tile.getPos(), width/4, height/2, 0xFFFFFF);
        //drawString(font, "Music : " + tile.getMusicName(), width/4, height/2 + font.FONT_HEIGHT, 0xFFFFFF);
        //drawString(font, "Off-distance : " + ((SphereBounds)tile.data.getBounds()).getRadius(), width/4, height/2 + font.FONT_HEIGHT * 2, 0xFFFFFF);

        drawToolTip(mouseX, mouseY);
    }

    public void drawToolTip(int mouseX, int mouseY) {
        HoverChecker musicHover = new HoverChecker(textMusic, 0);
        HoverChecker boundsHover = new HoverChecker(textBounds, 0);
        HoverChecker priorityHover = new HoverChecker(textPriority, 0);
        HoverChecker delayHover = new HoverChecker(textDelay, 0);

        HoverChecker boundsButtonHover = new HoverChecker(buttonBounds, 0);

        HoverChecker offsetHover = new HoverChecker(textOffset, 0);

        HoverChecker checkFuseHover = new HoverChecker(checkFuse, 0);
        HoverChecker checkGlobalHover = new HoverChecker(checkGlobal, 0);
        HoverChecker checkPriorityHover = new HoverChecker(checkPriority, 0);

        List<String> list = new ArrayList<String>();

        if (musicHover.checkHover(mouseX, mouseY))
        {
            list.add(TextFormatting.RED + "Music ID");
            list.add("Choose the music to play.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (boundsHover.checkHover(mouseX, mouseY))
        {
            list.add(TextFormatting.RED + "Bounds Detection");
            list.add("Border that delimits when you can hear the ambience.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (priorityHover.checkHover(mouseX, mouseY))
        {
            list.add(TextFormatting.RED + "Priority");
            list.add(TextFormatting.WHITE + "If active, will prioritize ambience tiles with a higher value.");
            list.add(TextFormatting.GRAY + "(Useful for playing music within an area that already has music without overlapping)");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (delayHover.checkHover(mouseX, mouseY))
        {
            list.add(TextFormatting.RED + "Delay");
            list.add(TextFormatting.WHITE + "The sound will wait between min and max ticks times 4 before playing again.");
            list.add(TextFormatting.GRAY + "(You can set min and max to the same value if you don't want the value to be random)");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (boundsButtonHover.checkHover(mouseX, mouseY))
        {
            if(boundType == SphereBounds.id) {
                list.add(TextFormatting.RED + "Sphere Bounds");
                list.add(TextFormatting.WHITE + "Sphere bounds that cover a radius all around it.");
                list.add(TextFormatting.GRAY + "(If the sound isn't global, the volume will be scaled by the distance of the player from the source)");
                GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
            }

            if(boundType == CylinderBounds.id) {
                list.add(TextFormatting.RED + "Cylinder Bounds");
                list.add(TextFormatting.WHITE + "Cylinder bounds that cover a radius all around it except the height which can be changed.");
                list.add(TextFormatting.GRAY + "(If the sound isn't global, the volume will be unaffected by height and only scaled by horizontal distance)");
                GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
            }

            if(boundType == CubicBounds.id) {
                list.add(TextFormatting.RED + "Cubic Bounds");
                list.add(TextFormatting.WHITE + "Cubic bounds that cover a cubic area of origin by the value of x, y and z.");
                list.add(TextFormatting.GRAY + "(If the sound isn't global, the volume will be scaled as if this was a Sphere Bounds which won't cover the whole area)");
                GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
            }

            if(boundType == NoneBounds.id) {
                list.add(TextFormatting.RED + "None Bounds");
                list.add(TextFormatting.WHITE + "Will always play as long as the tile is loaded on the client.");
                list.add(TextFormatting.GRAY + "(The volume will not change with distance regardless of if the tile is set to be global)");
                GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
            }
        }

        if (offsetHover.checkHover(mouseX, mouseY))
        {
            list.add(TextFormatting.RED + "Offset");
            list.add(TextFormatting.WHITE + "Allows you to shift the bounds detection somewhere else.");
            list.add(TextFormatting.GRAY + "(Useful for hiding the block from sight while still playing it)");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (checkFuseHover.checkHover(mouseX, mouseY))
        {
            list.add("If this tile has the same sound as another one in range and this is checked, it will play from whichever is closer without stopping the sound.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (checkGlobalHover.checkHover(mouseX, mouseY))
        {
            list.add("If unchecked, the volume of the sound will lower with distance, otherwise it stays the same as long as it's in range.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }

        if (checkPriorityHover.checkHover(mouseX, mouseY))
        {
            list.add("If unchecked, this tile will ignore the priority system and always play when in range, otherwise it will stop when a sound with a higher priority is playing.");
            GuiUtils.drawHoveringText(list, mouseX + 3, mouseY + 3, width, height, width / 2, font);
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
