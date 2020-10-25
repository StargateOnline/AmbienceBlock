package com.sekai.ambienceblock.client.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public interface INestedGuiEventEventHandler extends IGuiEventListener
{
    List<? extends IGuiEventListener> children();

    /**
     * Returns the first event listener that intersects with the mouse coordinates.
     */
    default Optional<IGuiEventListener> getEventListenerForPos(double mouseX, double mouseY) {
        for(IGuiEventListener iguieventlistener : this.children()) {
            if (iguieventlistener.isMouseOver(mouseX, mouseY)) {
                return Optional.of(iguieventlistener);
            }
        }

        return Optional.empty();
    }

    default boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for(IGuiEventListener iguieventlistener : this.children()) {
            if (iguieventlistener.mouseClicked(mouseX, mouseY, mouseButton)) {
                this.setFocused(iguieventlistener);
                if (mouseButton == 0) {
                    this.setDragging(true);
                }

                return true;
            }
        }

        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.setDragging(false);
        return this.getEventListenerForPos(mouseX, mouseY).filter((eventListener) -> {
            return eventListener.mouseReleased(mouseX, mouseY, mouseButton);
        }).isPresent();
    }

    default boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double x2, double y2) {
        return this.getFocused() != null && this.isDragging() && mouseButton == 0 && this.getFocused().mouseDragged(mouseX, mouseY, mouseButton, x2, y2);
    }

    boolean isDragging();

    void setDragging(boolean p_setDragging_1_);

    default boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return this.getEventListenerForPos(mouseX, mouseY).filter((eventListener) -> {
            return eventListener.mouseScrolled(mouseX, mouseY, scroll);
        }).isPresent();
    }

    default boolean keyPressed(int typedChar, int keyCode) {
        return this.getFocused() != null && this.getFocused().keyPressed(typedChar, keyCode);
    }

    default boolean keyReleased(int keyCode, int scanCode) {
        return this.getFocused() != null && this.getFocused().keyReleased(keyCode, scanCode);
    }

    default boolean charTyped(char typedChar, int keyCode) {
        return this.getFocused() != null && this.getFocused().charTyped(typedChar, keyCode);
    }

    @Nullable
    IGuiEventListener getFocused();

    void setFocused(@Nullable IGuiEventListener p_setFocused_1_);

    default void setFocusedDefault(@Nullable IGuiEventListener eventListener) {
        this.setFocused(eventListener);
    }

    default void func_212932_b(@Nullable IGuiEventListener eventListener) {
        this.setFocused(eventListener);
    }

    default boolean changeFocus(boolean bl) {
        IGuiEventListener iguieventlistener = this.getFocused();
        boolean flag = iguieventlistener != null;
        if (flag && iguieventlistener.changeFocus(bl)) {
            return true;
        } else {
            List<? extends IGuiEventListener> list = this.children();
            int j = list.indexOf(iguieventlistener);
            int i;
            if (flag && j >= 0) {
                i = j + (bl ? 1 : 0);
            } else if (bl) {
                i = 0;
            } else {
                i = list.size();
            }

            ListIterator<? extends IGuiEventListener> listiterator = list.listIterator(i);
            BooleanSupplier booleansupplier = bl ? listiterator::hasNext : listiterator::hasPrevious;
            Supplier<? extends IGuiEventListener> supplier = bl ? listiterator::next : listiterator::previous;

            while(booleansupplier.getAsBoolean()) {
                IGuiEventListener iguieventlistener1 = supplier.get();
                if (iguieventlistener1.changeFocus(bl)) {
                    this.setFocused(iguieventlistener1);
                    return true;
                }
            }

            this.setFocused((IGuiEventListener)null);
            return false;
        }
    }
}
