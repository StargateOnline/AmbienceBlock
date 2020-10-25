package com.sekai.ambienceblock.tileentity.ambiencetilebounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CubicBounds extends AbstractBounds {
    public static final int id = 3;

    private double xSize;
    private double ySize;
    private double zSize;

    public CubicBounds(double xSize, double ySize, double zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public CubicBounds() {
        this.xSize = 0;
        this.ySize = 0;
        this.zSize = 0;
    }

    private double getAverage() {
        return xSize + ySize + zSize;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return "Cubic";
    }

    @Override
    public boolean isWithinBounds(EntityPlayer player, Vec3d origin) {
        return player.posX >= origin.x - xSize / 2 && player.posY >= origin.y - ySize / 2 && player.posZ >= origin.z - zSize / 2
                && player.posX <= origin.x + 1 + xSize / 2 && player.posY <= origin.y + 1 + ySize / 2 && player.posZ <= origin.z + 1 + zSize / 2;
    }

    @Override
    public double distanceFromCenter(EntityPlayer player, Vec3d origin) {
        /*Vec3d vecPlayer = new Vec3d(player.posX, player.posY, player.posZ);
        Vec3d vecTile = new Vec3d(origin.x, origin.y, origin.z);

        return vecPlayer.distanceTo(vecTile);*/

        double x = player.posX - origin.x, y = player.posY - origin.y, z = player.posZ - origin.z;
        double sum = Math.abs(x) + Math.abs(y) + Math.abs(z);

        return sum;
    }

    public double maxDistanceFromCenter(Vec3d origin) {
        //double x = getxSize() - origin.x, y = player.posY - origin.y, z = player.posZ - origin.z;
        double sum = getxSize() + getySize() + getzSize();

        return sum;
    }

    @Override
    public double getPercentageHowCloseIsPlayer(EntityPlayer player, Vec3d origin) {
        //return (getAverage() - distanceFromCenter(player, origin)) / getAverage();//distanceFromCenter(player, origin) / getAverage();
        //return 1 - (distanceFromCenter(player, origin) / maxDistanceFromCenter(origin));
        //double x = Math.abs(player.posX - origin.x), y = Math.abs(player.posY - origin.y), z = Math.abs(player.posZ - origin.z);
        double x = Math.abs(player.posX - getFixedOrigin(origin).x), y = Math.abs(player.posY - getFixedOrigin(origin).y), z = Math.abs(player.posZ - getFixedOrigin(origin).z);
        return ((1 - (x / xSize / 2)) * (1 - (y / ySize / 2)) * (1 - (z / zSize / 2)));
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setDouble("xSize", xSize);
        compound.setDouble("ySize", ySize);
        compound.setDouble("zSize", zSize);

        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {
        this.xSize = compound.getDouble("xSize");
        this.ySize = compound.getDouble("ySize");
        this.zSize = compound.getDouble("zSize");
    }

    @Override
    public void toBuff(ByteBuf buf) {
        buf.writeDouble(xSize);
        buf.writeDouble(ySize);
        buf.writeDouble(zSize);
    }

    @Override
    public void fromBuff(ByteBuf buf) {
        xSize = buf.readDouble();
        ySize = buf.readDouble();
        zSize = buf.readDouble();
    }

    @Override
    public String toString() {
        return getName() + " : " +
                "x=" + xSize +
                ", y=" + ySize +
                ", z=" + zSize +
                '}';
    }

    //Getter setter
    public double getxSize() {
        return xSize;
    }

    public void setxSize(double xSize) {
        this.xSize = xSize;
    }

    public double getySize() {
        return ySize;
    }

    public void setySize(double ySize) {
        this.ySize = ySize;
    }

    public double getzSize() {
        return zSize;
    }

    public void setzSize(double zSize) {
        this.zSize = zSize;
    }
}
