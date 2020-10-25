package com.sekai.ambienceblock.util;

import com.sekai.ambienceblock.tileentity.ambiencetilebounds.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class BoundsUtil {
    public static int lastBoundType = 4;

    //nbt stuff
    public static NBTTagCompound toNBT(AbstractBounds bounds) {
        NBTTagCompound compound = bounds.toNBT(); //get NBTTagCompound from bounds
        compound.setInteger("type", bounds.getID()); //add type information
        return compound;
    }

    public static AbstractBounds fromNBT(NBTTagCompound nbt) {
        int id = nbt.getInteger("type");
        AbstractBounds bounds = getBoundsFromType(id);

        bounds.fromNBT(nbt);
        return bounds;
    }

    //buffer stuff
    public static void toBuff(AbstractBounds bounds, ByteBuf buf) {
        buf.writeInt(bounds.getID());
        bounds.toBuff(buf);
    }

    public static AbstractBounds fromBuff(ByteBuf buf) {
        int id = buf.readInt();
        AbstractBounds bounds = getBoundsFromType(id);

        bounds.fromBuff(buf);
        return bounds;
    }

    public static AbstractBounds getBoundsFromType(int id) {
        if(SphereBounds.id == id) return new SphereBounds();
        if(CylinderBounds.id == id) return new CylinderBounds();
        if(CapsuleBounds.id == id) return new CapsuleBounds();
        if(CubicBounds.id == id) return new CubicBounds();
        if(NoneBounds.id == id) return new NoneBounds();

        return new SphereBounds();
    }
}
