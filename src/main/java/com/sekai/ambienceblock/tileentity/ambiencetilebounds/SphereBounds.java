package com.sekai.ambienceblock.tileentity.ambiencetilebounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SphereBounds extends AbstractBounds {
    public static final int id = 0;

    private double radius;

    public SphereBounds(double radius) {
        this.radius = radius;
    }

    public SphereBounds() {
        radius = 0;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return "Sphere";
    }

    @Override
    public boolean isWithinBounds(EntityPlayer player, Vec3d origin) {
        double dist = distanceFromCenter(player, origin);

        return dist < radius;
    }

    @Override
    public double distanceFromCenter(EntityPlayer player, Vec3d origin) {
        Vec3d vecPlayer = new Vec3d(player.posX, player.posY, player.posZ);
        Vec3d vecTile = new Vec3d(origin.x, origin.y, origin.z).add(blockOffset);

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

        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {
        this.radius = compound.getDouble("radius");
    }

    @Override
    public void toBuff(ByteBuf buf) {
        buf.writeDouble(this.radius);
    }

    @Override
    public void fromBuff(ByteBuf buf) {
        this.radius = buf.readDouble();
    }

    @Override
    public String toString() {
        return getName() + " : " +
                "radius=" + radius +
                '}';
    }
}
