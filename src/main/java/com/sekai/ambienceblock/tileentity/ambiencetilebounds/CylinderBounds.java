package com.sekai.ambienceblock.tileentity.ambiencetilebounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class CylinderBounds extends AbstractBounds {
    public static final int id = 1;

    private double radius;
    private double length;
    private BoundsAxis axis;

    public CylinderBounds(double radius, double length, BoundsAxis axis) {
        this.radius = radius;
        this.length = length;
        this.axis = axis;
    }

    public CylinderBounds() {
        this.radius = 0;
        this.length = 0;
        this.axis = BoundsAxis.Y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public BoundsAxis getAxis() {
        return axis;
    }

    public void setAxis(BoundsAxis axis) {
        this.axis = axis;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return "Cylinder";
    }

    @Override
    public boolean isWithinBounds(EntityPlayer player, Vec3d origin) {
        double dist = distanceFromCenter(player, origin);

        //return dist < radius && player.posY < origin.y - 1 + length && player.posY > origin.y - 1;
        double playerPos = getPlayerPosByAxis(player, axis);
        double originPos = getVec3dPosByAxis(getFixedOrigin(origin), axis);
        return dist < radius && playerPos < originPos + length / 2D && playerPos > originPos - length / 2D;
    }

    //We can ignore y, we're only interested in the other two coordinates
    @Override
    public double distanceFromCenter(EntityPlayer player, Vec3d origin) {
        //Vec3d vecPlayer = new Vec3d(player.posX, 0, player.posZ);
        //Vec3d vecTile = new Vec3d(origin.x, 0, origin.z).add(new Vec3d(blockOffset.x, 0D, blockOffset.z));
        Vec3d axisMask = axis.getIgnoreAxisMask();

        Vec3d vecPlayer = new Vec3d(player.posX * axisMask.x, player.posY * axisMask.y, player.posZ * axisMask.z);
        Vec3d vecTile = getFixedOrigin(new Vec3d(origin.x * axisMask.x, origin.y * axisMask.y - blockOffset.y, origin.z * axisMask.z));

        return vecPlayer.distanceTo(vecTile);
    }

    @Override
    public double getPercentageHowCloseIsPlayer(EntityPlayer player, Vec3d origin) {
        return (radius - distanceFromCenter(player, origin)) / radius;//distanceFromCenter(player, origin) / radius;
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setDouble("radius", radius);
        compound.setDouble("length", length);
        compound.setInteger("axis", axis.getId());

        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {
        this.radius = compound.getDouble("radius");
        this.length = compound.getDouble("length");
        this.axis = BoundsAxis.getAxisFromInt(compound.getInteger("axis"));
    }

    @Override
    public void toBuff(ByteBuf buf) {
        buf.writeDouble(this.radius);
        buf.writeDouble(this.length);
        buf.writeInt(this.axis.getId());
    }

    @Override
    public void fromBuff(ByteBuf buf) {
        this.radius = buf.readDouble();
        this.length = buf.readDouble();
        this.axis = BoundsAxis.getAxisFromInt(buf.readInt());
    }

    @Override
    public String toString() {
        return getName() + " : " +
                "radius=" + radius +
                ", length=" + length +
                '}';
    }
}
