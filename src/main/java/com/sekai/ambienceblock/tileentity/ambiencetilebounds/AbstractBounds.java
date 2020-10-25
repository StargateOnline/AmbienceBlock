package com.sekai.ambienceblock.tileentity.ambiencetilebounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractBounds {
    public static final Vec3d blockOffset = new Vec3d(0.5D, 0.5D, 0.5D);

    public abstract int getID();
    public abstract String getName();

    /*protected BlockPos offset = new BlockPos(0, 0, 0);
    public BlockPos getOffset() {
        return this.offset;
    }
    public void setOffset(BlockPos pos) {
        this.offset = pos;
    }*/

    public abstract boolean isWithinBounds(EntityPlayer player, Vec3d origin);
    public abstract double distanceFromCenter(EntityPlayer player, Vec3d origin);
    public abstract double getPercentageHowCloseIsPlayer(EntityPlayer player, Vec3d origin);

    public abstract NBTTagCompound toNBT();
    public abstract void fromNBT(NBTTagCompound compound);

    public abstract void toBuff(ByteBuf buf);
    public abstract void fromBuff(ByteBuf buf);

    //util
    public Vec3d getFixedOrigin(Vec3d origin) {
        return new Vec3d(origin.x, origin.y, origin.z).add(blockOffset);
    }

    public double getPlayerPosByAxis(EntityPlayer player, BoundsAxis axis) {
        switch(axis) {
            case X:return player.posX;
            case Y:return player.posY;
            case Z:return player.posZ;
        }
        return 0;
    }

    public double getVec3dPosByAxis(Vec3d vec, BoundsAxis axis) {
        switch(axis) {
            case X:return vec.x;
            case Y:return vec.y;
            case Z:return vec.z;
        }
        return 0;
    }
}
