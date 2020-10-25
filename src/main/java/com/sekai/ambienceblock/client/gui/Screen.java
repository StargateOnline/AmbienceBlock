package com.sekai.ambienceblock.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.client.gui.event.WidgetGuiScreenEvent;
import com.sekai.ambienceblock.client.gui.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@SideOnly(Side.CLIENT)
public abstract class Screen extends FocusableGui implements IRenderable
{
    //private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");
    protected final ITextComponent title;
    protected final List<IGuiEventListener> children = Lists.newArrayList();
    protected ItemRenderer itemRenderer;
    //public int width;
    //public int height;
    protected final List<Widget> buttons = Lists.newArrayList();
    public boolean passEvents;
    //protected FontRenderer font;
    //private URI clickedLink;

    protected Screen(ITextComponent titleIn) {
        this.title = titleIn;
    }

    public ITextComponent getTitle() {
        return this.title;
    }

    @Override
    public void render(int x, int y, float partialTicks)
    {
        for(int i = 0; i < this.buttons.size(); ++i)
        {
            this.buttons.get(i).render(x, y, partialTicks);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int typedChar, int keyCode)
    {
        if(typedChar == 256 && this.shouldCloseOnEsc()){
            this.onClose();
            return true;
        } else if(typedChar == 258) {
            boolean flag = !isShiftKeyDown();
            if (!this.changeFocus(flag)) {
                this.changeFocus(flag);
            }

            return true;
        } else {
            return super.keyPressed(typedChar, keyCode);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        keyPressed(typedChar, keyCode);
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    public void onClose() {
        this.mc.displayGuiScreen(null);
    }

    protected <T extends Widget> T addButton(T button)
    {
        this.buttons.add(button);
        this.children.add((IGuiEventListener) button);
        return button;
    }

    /*protected void renderTooltip(ItemStack itemStack, int p_renderTooltip_2_, int p_renderTooltip_3_)
    {
        FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
        GuiUtils.preItemToolTip(itemStack);
        this.renderTooltip(this.getTooltipFromItem(itemStack), p_renderTooltip_2_, p_renderTooltip_3_);
        GuiUtils.postItemToolTip();
    }*/

    /*public List<String> getTooltipFromItem(ItemStack itemStack)
    {
        return itemStack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
    }*/

    /*public void renderTooltip(String p_renderTooltip_1_, int p_renderTooltip_2_, int p_renderTooltip_3_) {
        this.renderTooltip(Arrays.asList(p_renderTooltip_1_), p_renderTooltip_2_, p_renderTooltip_3_);
    }*/

    /*public void renderTooltip(List<String> tooltip, int p_renderTooltip_2_, int p_renderTooltip_3_) {
        renderTooltip(tooltip, p_renderTooltip_2_, p_renderTooltip_3_, font);
    }*/

    /*public void renderTooltip(List<String> tooltip, int p_renderTooltip_2_, int p_renderTooltip_3_, FontRenderer font)
    {
        GuiUtils.drawHoveringText(tooltip, p_renderTooltip_2_, p_renderTooltip_3_, this.width, this.height, -1, font);
        if(false && !tooltip.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableDepth();
            int i = 0;

            for(String s : tooltip) {
                int j = this.font.getStringWidth(s);
                if (j > i) {
                    i = j;
                }
            }

            int l1 = p_renderTooltip_2_ + 12;
            int i2 = p_renderTooltip_3_ - 12;
            int k = 8;
            if (tooltip.size() > 1) {
                k += 2 + (tooltip.size() - 1) * 10;
            }

            if (l1 + i > this.width) {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > this.height) {
                i2 = this.height - k - 6;
            }

            //this.zLevel = 300.0F;
            //this.itemRenderer.zLevel = 300.0F

            int color1 = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, color1, color1);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, color1, color1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, color1, color1);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, color1, color1);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, color1, color1);

            int color2 = 1347420415;
            int color3 = 1344798847;

            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, color2, color3);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, color2, color3);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, color2, color2);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, color3, color3);

            for(int k1 = 0; k1 < tooltip.size(); ++k1)
            {
                String s1 = tooltip.get(k1);
                if(s1 != null) {
                    this.font.drawString(s1, (float)l1, (float)i2, -1, true);
                }

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            //this.zLevel = 0.0F;
            //this.itemRenderer.zLevel = 0.0F;
            GlStateManager.enableDepth();
            GlStateManager.enableRescaleNormal();
        }
    }*/

    /*protected void renderComponentHoverEffect(ITextComponent p_renderComponentHoverEffect_1_, int p_renderComponentHoverEffect_2_, int p_renderComponentHoverEffect_3_)
    {
        if (p_renderComponentHoverEffect_1_ != null && p_renderComponentHoverEffect_1_.getStyle().getHoverEvent() != null) {
            HoverEvent hoverevent = p_renderComponentHoverEffect_1_.getStyle().getHoverEvent();
            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                ItemStack itemstack = ItemStack.EMPTY;

                try {
                    NBTTagCompound compound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedComponentText());
                    itemstack.deserializeNBT(compound);
                } catch (NBTException exception) {
                    ;
                }

                if (itemstack.isEmpty()) {
                    this.renderTooltip(TextFormatting.RED + "Invalid Item!", p_renderComponentHoverEffect_2_, p_renderComponentHoverEffect_3_);
                } else {
                    this.renderTooltip(itemstack, p_renderComponentHoverEffect_2_, p_renderComponentHoverEffect_3_);
                }
            } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                if (this.minecraft.gameSettings.advancedItemTooltips) {
                    try {
                        NBTTagCompound compoundnbt = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedComponentText());
                        List<String> list = Lists.newArrayList();

                        ITextComponent itextcomponent = ITextComponent.Serializer.fromJsonLenient(compoundnbt.getString("name"));
                        if (itextcomponent != null) {
                            list.add(itextcomponent.getFormattedText());
                        }

                        if (compoundnbt.hasKey("type", 8)) {
                            String s = compoundnbt.getString("type");
                            list.add("Type: " + s);
                        }

                        list.add(compoundnbt.getString("id"));
                        this.renderTooltip(list, p_renderComponentHoverEffect_2_, p_renderComponentHoverEffect_3_);
                    } catch (NBTException | JsonSyntaxException var9) {
                        this.renderTooltip(TextFormatting.RED + "Invalid Entity!", p_renderComponentHoverEffect_2_, p_renderComponentHoverEffect_3_);
                    }
                }
            } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth(hoverevent.getValue().getFormattedText(), Math.max(this.width / 2, 200)), p_renderComponentHoverEffect_2_, p_renderComponentHoverEffect_3_);
            }

        }
    }*/

    protected void insertText(String p_insertText_1_, boolean p_insertText_2_) {
    }

    /*public boolean handleComponentClicked(ITextComponent textComponent)
    {
        if (textComponent == null) {
            return false;
        } else {
            ClickEvent clickevent = textComponent.getStyle().getClickEvent();
            if (isShiftKeyDown()) {
                if (textComponent.getStyle().getInsertion() != null) {
                    this.insertText(textComponent.getStyle().getInsertion(), false);
                }
            } else if (clickevent != null) {
                if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.mc.gameSettings.chatLinks) {
                        return false;
                    }

                    try {
                        URI uri = new URI(clickevent.getValue());
                        String s = uri.getScheme();
                        if (s == null) {
                            throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
                        }

                        if (!ALLOWED_PROTOCOLS.contains(s.toLowerCase(Locale.ROOT))) {
                            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));
                        }

                        if (this.minecraft.gameSettings.chatLinksPrompt) {
                            this.clickedLink = uri;
                            //this.minecraft.displayGuiScreen(new ConfirmOpenLinkScreen(this::confirmLink, clickevent.getValue(), false));
                            this.openLink(uri);
                        } else {
                            this.openLink(uri);
                        }
                    } catch (URISyntaxException urisyntaxexception) {
                        AmbienceMod.logger.error("Can't open url for {}", clickevent, urisyntaxexception);
                    }
                } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI uri1 = (new File(clickevent.getValue())).toURI();
                    this.openLink(uri1);
                } else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.insertText(clickevent.getValue(), true);
                } else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    this.sendMessage(clickevent.getValue(), false);
                } //else if (clickevent.getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD) {
                  //  this.minecraft.keyboardListener.setClipboardString(clickevent.getValue());
                //}
                else {
                    AmbienceMod.logger.error("Don't know how to handle {}", (Object)clickevent);
                }

                return true;
            }

            return false;
        }
    }*/

    /*public void sendMessage(String p_sendMessage_1_) {
        this.sendMessage(p_sendMessage_1_, true);
    }

    public void sendMessage(String p_sendMessage_1_, boolean p_sendMessage_2_) {
        p_sendMessage_1_ = ForgeEventFactory.onClientSendMessage(p_sendMessage_1_);
        if (p_sendMessage_1_.isEmpty()) return;
        if (p_sendMessage_2_) {
            this.minecraft.ingameGUI.getChatGUI().addToSentMessages(p_sendMessage_1_);
        }
        //if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.player, msg) != 0) return; //Forge: TODO Client command re-write

        this.minecraft.player.sendChatMessage(p_sendMessage_1_);
    }*/

    public void init(Minecraft minecraft, int width, int height) {
        this.mc = minecraft;
        this.itemRenderer = minecraft.getItemRenderer();
        this.itemRender = minecraft.getRenderItem();
        this.fontRenderer = minecraft.fontRenderer;
        this.width = width;
        this.height = height;
        java.util.function.Consumer<Widget> remove = (b) -> { buttons.remove(b); children.remove(b); };
        if(!MinecraftForge.EVENT_BUS.post(new WidgetGuiScreenEvent.InitGuiEvent.Pre(this, this.buttons, this::addButton, remove)))
        {
            this.buttons.clear();
            this.children.clear();
            this.setFocused((IGuiEventListener)null);
            //this.init();
        }
        MinecraftForge.EVENT_BUS.post(new WidgetGuiScreenEvent.InitGuiEvent.Post(this, this.buttons, this::addButton, remove));
    }

    /**
     * Called when the GUI is resized in order to update the world and the resolution
     */
    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        this.init(mcIn, width, height);
    }

}
