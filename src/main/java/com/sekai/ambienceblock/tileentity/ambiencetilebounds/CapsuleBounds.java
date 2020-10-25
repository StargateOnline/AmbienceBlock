package com.sekai.ambienceblock.tileentity.ambiencetilebounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import org.omg.CORBA.Bounds;

public class CapsuleBounds extends AbstractBounds {
    public static final int id = 2;

    private double radius;
    private double length;
    private BoundsAxis axis;

    public CapsuleBounds(double radius, double length, BoundsAxis axis) {
        this.radius = radius;
        this.length = length;
        this.axis = axis;
    }

    public CapsuleBounds() {
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
        return "Capsule";
    }

    @Override
    public boolean isWithinBounds(EntityPlayer player, Vec3d origin) {
        double dist = distanceFromCenter(player, origin);

        boolean upperFlag = false, bottomFlag = false;

        double playerPos = getPlayerPosByAxis(player, axis);
        double originPos = getVec3dPosByAxis(getFixedOrigin(origin), axis);

        if(playerPos > originPos + length / 2D)
            upperFlag = true;

        if(playerPos < originPos - length / 2D)
            bottomFlag = true;
        /*switch (axis) {
            case X:
                if(player.getPosX() > origin.x + blockOffset.x + length / 2D)
                    upperFlag = true;
                if(player.getPosX() < origin.x + blockOffset.x - length / 2D)
                    bottomFlag = true;
                break;
            case Y:
                if(player.getPosY() > origin.y + blockOffset.y + length / 2D)
                    upperFlag = true;
                if(player.getPosY() < origin.y + blockOffset.y - length / 2D)
                    bottomFlag = true;
                break;
            case Z:
                if(player.getPosZ() > origin.z + blockOffset.z + length / 2D)
                    upperFlag = true;
                if(player.getPosZ() < origin.z + blockOffset.z - length / 2D)
                    bottomFlag = true;
                break;
        }*/

        Vec3d vecPlayer = new Vec3d(player.posX, player.posY, player.posZ);

        if(upperFlag) {
            Vec3d axisMask = axis.getAxisMask();

            Vec3d shiftedOrigin = new Vec3d(
                    origin.x + length / 2D * axisMask.x,
                    origin.y + length / 2D * axisMask.y,
                    origin.z + length / 2D * axisMask.z
            );

            return vecPlayer.distanceTo(shiftedOrigin) < radius;
        }

        if(bottomFlag) {
            Vec3d axisMask = axis.getAxisMask();

            Vec3d shiftedOrigin = new Vec3d(
                    origin.x - length / 2D * axisMask.x,
                    origin.y - length / 2D * axisMask.y,
                    origin.z - length / 2D * axisMask.z
            );

            return vecPlayer.distanceTo(shiftedOrigin) < radius;
        }

        return dist < radius && playerPos < originPos + length / 2D && playerPos > originPos - length / 2D;

        //return dist < radius && player.posY < origin.y - 1 + length && player.posY > origin.y - 1;
    }

    //We can ignore y, we're only interested in the other two coordinates
    @Override
    public double distanceFromCenter(EntityPlayer player, Vec3d origin) {
        boolean upperFlag = false, bottomFlag = false;

        double playerPos = getPlayerPosByAxis(player, axis);
        double originPos = getVec3dPosByAxis(getFixedOrigin(origin), axis);

        if(playerPos > originPos + length / 2D)
            upperFlag = true;

        if(playerPos < originPos - length / 2D)
            bottomFlag = true;

        /*switch (axis) {
            case X:
                if(player.posX > origin.x + blockOffset.x + length / 2D)
                    upperFlag = true;
                if(player.posX < origin.x + blockOffset.x - length / 2D)
                    bottomFlag = true;
                break;
            case Y:
                if(player.posY > origin.y + blockOffset.y + length / 2D)
                    upperFlag = true;
                if(player.posY < origin.y + blockOffset.y - length / 2D)
                    bottomFlag = true;
                break;
            case Z:
                if(player.posZ > origin.z + blockOffset.z + length / 2D)
                    upperFlag = true;
                if(player.posZ < origin.z + blockOffset.z - length / 2D)
                    bottomFlag = true;
                break;
        }*/

        Vec3d vecPlayer = new Vec3d(player.posX, player.posY, player.posZ);

        if(upperFlag) {
            Vec3d axisMask = axis.getAxisMask();

            Vec3d shiftedOrigin = new Vec3d(
                    origin.x + length / 2D * axisMask.x,
                    origin.y + length / 2D * axisMask.y,
                    origin.z + length / 2D * axisMask.z
            );

            return vecPlayer.distanceTo(shiftedOrigin);
        }

        if(bottomFlag) {
            Vec3d axisMask = axis.getAxisMask();

            Vec3d shiftedOrigin = new Vec3d(
                    origin.x - length / 2D * axisMask.x,
                    origin.y - length / 2D * axisMask.y,
                    origin.z - length / 2D * axisMask.z
            );

            return vecPlayer.distanceTo(shiftedOrigin);
        }

        Vec3d axisMask = axis.getIgnoreAxisMask();
        //Vec3d vecTile = new Vec3d(origin.getX() * axisMask.x, origin.getY() * axisMask.y, origin.getZ() * axisMask.z).add(new Vec3d(blockOffset.x, blockOffset.y, blockOffset.z));
        Vec3d vecMaskedTile = getFixedOrigin(origin).crossProduct(axisMask);
        Vec3d vecMaskedPlayer = vecPlayer.crossProduct(axisMask);

        return vecMaskedPlayer.distanceTo(vecMaskedTile);
    }

    @Override
    public double getPercentageHowCloseIsPlayer(EntityPlayer player, Vec3d origin) {
        return (radius - distanceFromCenter(player, origin)) / radius; //distanceFromCenter(player, origin) / radius;
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
