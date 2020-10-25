package com.sekai.ambienceblock.tileentity.ambiencetilebounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class NoneBounds extends AbstractBounds {
    public static final int id = 4;

    public NoneBounds() { }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public boolean isWithinBounds(EntityPlayer player, Vec3d origin) {
        return true;
    }

    @Override
    public double distanceFromCenter(EntityPlayer player, Vec3d origin) {
        return 0;
    }

    @Override
    public double getPercentageHowCloseIsPlayer(EntityPlayer player, Vec3d origin) {
        return 1;
    }

    @Override
    public NBTTagCompound toNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {

    }

    @Override
    public void toBuff(ByteBuf buf) {

    }

    @Override
    public void fromBuff(ByteBuf buf) {

    }

    @Override
    public String toString() {
        return getName();
    }
}
