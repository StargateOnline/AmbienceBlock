package com.sekai.ambienceblock.tileentity;

import com.sekai.ambienceblock.AmbienceMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class AmbienceTileEntity extends TileEntity {
    public static final String KEY = AmbienceMod.MOD_ID + ":ambience_block_tileentity";

    public AmbienceTileEntityData data = new AmbienceTileEntityData();

    public AmbienceTileEntity()
    {
        super();
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        data.fromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        data.toNBT(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        data.fromNBT(tag);
    }

    @Override
    public NBTTagCompound getTileData() {
        NBTTagCompound tag = new NBTTagCompound();
        data.toNBT(tag);

        super.writeToNBT(tag);
        return tag;
    }

    //fancy
    public boolean isWithinBounds(EntityPlayer player)
    {
        return data.isWithinBounds(player, pos);
    }

    public double distanceTo(EntityPlayer player)
    {
        /*Vec3d vecPlayer = new Vec3d(player.getPosX(), player.getPosY(), player.getPosZ());
        Vec3d vecTile = new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());

        return vecPlayer.distanceTo(vecTile);*/
        return data.distanceFromCenter(player, pos);
    }

    //Getter and setters
    public String getMusicName() { return data.getSoundName(); }

    public int getPriority() { return data.getPriority(); }

    public boolean isUsingPriority() { return data.isUsingPriority(); }

    /*public double getOffDistance() {
        return data.getOffDistance();
    }*/

    public boolean isGlobal() {
        return data.isGlobal();
    }

    public void setMusicName(String musicName) {
        data.setSoundName(musicName);
    }

    public void setPriority(int priority) {
        data.setPriority(priority);
    }

    public void setUsePriority(boolean usePriority) { data.setUsePriority(usePriority); }

    /*public void setOffDistance(double offDistance) {
        data.setOffDistance(offDistance);
    }*/

    public void setGlobal(boolean global) {
        data.setGlobal(global);
    }

    public int getDelay()
    {
        int min = data.getMinDelay(), max = data.getMaxDelay();
        if(min > max || min == max) return max;
        return (int) ((max - min) * Math.random() + min);
    }
}
