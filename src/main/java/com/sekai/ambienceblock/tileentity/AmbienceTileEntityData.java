package com.sekai.ambienceblock.tileentity;

import com.sekai.ambienceblock.tileentity.ambiencetilebounds.AbstractBounds;
import com.sekai.ambienceblock.tileentity.ambiencetilebounds.SphereBounds;
import com.sekai.ambienceblock.util.BoundsUtil;
import com.sekai.ambienceblock.util.NBTHelper;
import com.sekai.ambienceblock.util.ParsingUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// Data holder class
public class AmbienceTileEntityData
{
    public static final int maxPriorities = 99;
    public static final int maxChannels = 9;

    //main
    private String soundName = "";
    private boolean shouldFuse = false;
    private boolean needRedstone = false;

    //sounds
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private int fadeIn = 0;
    private int fadeOut = 0;

    //priority
    private boolean usePriority = false;
    private int priority = 0;
    private int channel = 0;

    //bounds
    private AbstractBounds bounds = new SphereBounds(16D);
    private boolean isGlobal = false;

    //offset
    private Vec3d offset = new Vec3d(0, 0, 0);

    //delay
    private boolean useDelay = false;
    private int minDelay = 0;
    private int maxDelay = 0;

    private float minRandVolume = 0;
    private float maxRandVolume = 0;
    private float minRandPitch = 0;
    private float maxRandPitch = 0;

    private boolean canPlayOverSelf = false;
    private boolean shouldStopPrevious = false;

    //NBT util
    public NBTTagCompound toNBT(NBTTagCompound compound) {
        compound.setString("musicName", this.soundName);
        compound.setBoolean("shouldFuse", this.shouldFuse);
        compound.setBoolean("needRedstone", this.needRedstone);

        compound.setFloat("volume", this.volume);
        compound.setFloat("pitch", this.pitch);
        compound.setInteger("fadeIn", this.fadeIn);
        compound.setInteger("fadeOut", this.fadeOut);

        compound.setBoolean("usePriority",this.usePriority);
        if(usePriority) {
            compound.setInteger("priority", this.priority);
            compound.setInteger("channel", this.channel);
        }

        compound.setTag("bounds", BoundsUtil.toNBT(this.bounds));
        compound.setTag("offset", NBTHelper.writeVec3d(this.offset));
        compound.setBoolean("isGlobal", this.isGlobal);

        compound.setBoolean("useDelay", this.useDelay);
        if(useDelay) {
            compound.setInteger("minDelay", this.minDelay);
            compound.setInteger("maxDelay", this.maxDelay);
            compound.setBoolean("canPlayOverSelf", this.canPlayOverSelf);
            compound.setBoolean("shouldStopPrevious", this.shouldStopPrevious);

            compound.setFloat("minRandVolume", this.minRandVolume);
            compound.setFloat("maxRandVolume", this.maxRandVolume);

            compound.setFloat("minRandPitch", this.minRandPitch);
            compound.setFloat("maxRandPitch", this.maxRandPitch);
        }

        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        this.soundName = compound.getString("musicName");
        this.shouldFuse = compound.getBoolean("shouldFuse");
        this.needRedstone = compound.getBoolean("needRedstone");

        this.volume = compound.getFloat("volume");
        this.pitch = compound.getFloat("pitch");
        this.fadeIn = compound.getInteger("fadeIn");
        this.fadeOut = compound.getInteger("fadeOut");

        this.usePriority = compound.getBoolean("usePriority");
        if(usePriority) {
            this.priority = compound.getInteger("priority");
            this.channel = compound.getInteger("channel");
        }

        this.bounds = BoundsUtil.fromNBT(compound.getCompoundTag("bounds"));
        this.offset = NBTHelper.readVec3d(compound.getCompoundTag("offset"));
        this.isGlobal = compound.getBoolean("isGlobal");

        this.useDelay = compound.getBoolean("useDelay");
        if(useDelay) {
            this.minDelay = compound.getInteger("minDelay");
            this.maxDelay = compound.getInteger("maxDelay");
            this.canPlayOverSelf = compound.getBoolean("canPlayOverSelf");
            this.shouldStopPrevious = compound.getBoolean("shouldStopPrevious");

            this.minRandVolume = compound.getFloat("minRandVolume");
            this.maxRandVolume = compound.getFloat("maxRandVolume");

            this.minRandPitch = compound.getFloat("minRandPitch");
            this.maxRandPitch = compound.getFloat("maxRandPitch");
        }

    }
    ////

    //Buffer util
    public void toBuff(ByteBuf buf) {
        //Encode the data for the buffer
        buf.writeInt(this.soundName.getBytes(StandardCharsets.UTF_8).length);
        buf.writeBytes(this.soundName.getBytes(StandardCharsets.UTF_8));

        buf.writeBoolean(this.shouldFuse);
        buf.writeBoolean(this.needRedstone);

        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);

        buf.writeInt(this.fadeIn);
        buf.writeInt(this.fadeOut);

        buf.writeBoolean(this.usePriority);
        if(usePriority) {
            buf.writeInt(this.priority);
            buf.writeInt(this.channel);
        }

        //buf.writeDouble(this.offDistance);
        BoundsUtil.toBuff(this.bounds, buf);
        buf.writeDouble(this.offset.x);
        buf.writeDouble(this.offset.y);
        buf.writeDouble(this.offset.z);
        buf.writeBoolean(this.isGlobal);

        buf.writeBoolean(this.useDelay);
        if(useDelay) {
            buf.writeInt(this.minDelay);
            buf.writeInt(this.maxDelay);
            buf.writeBoolean(this.canPlayOverSelf);
            buf.writeBoolean(this.shouldStopPrevious);

            buf.writeFloat(this.minRandVolume);
            buf.writeFloat(this.maxRandVolume);

            buf.writeFloat(this.minRandPitch);
            buf.writeFloat(this.maxRandPitch);
        }
    }

    public void fromBuff(ByteBuf buf) {
        //Decode the data from the buffer
        int soundNameLength = buf.readInt();
        this.soundName = buf.readBytes(soundNameLength).toString(StandardCharsets.UTF_8);

        this.shouldFuse = buf.readBoolean();
        this.needRedstone = buf.readBoolean();

        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();

        this.fadeIn = buf.readInt();
        this.fadeOut = buf.readInt();

        this.usePriority = buf.readBoolean();
        if(usePriority) {
            this.priority = buf.readInt();
            this.channel = buf.readInt();
        }

        this.bounds = BoundsUtil.fromBuff(buf);
        this.offset = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.isGlobal = buf.readBoolean();

        this.useDelay = buf.readBoolean();
        if(useDelay) {
            this.minDelay = buf.readInt();
            this.maxDelay = buf.readInt();
            this.canPlayOverSelf = buf.readBoolean();
            this.shouldStopPrevious = buf.readBoolean();

            this.minRandVolume = buf.readFloat();
            this.maxRandVolume = buf.readFloat();

            this.minRandPitch = buf.readFloat();
            this.maxRandPitch = buf.readFloat();
        }
    }
    ////

    //Bounds
    public boolean isWithinBounds(EntityPlayer player, BlockPos origin) {
        return bounds.isWithinBounds(player, ParsingUtil.blockPosToVec3d(origin).add(getOffset()));
    }

    public double distanceFromCenter(EntityPlayer player, BlockPos origin) {
        return bounds.distanceFromCenter(player, ParsingUtil.blockPosToVec3d(origin).add(getOffset()));
    }

    public double getPercentageHowCloseIsPlayer(EntityPlayer player, BlockPos origin) {
        return bounds.getPercentageHowCloseIsPlayer(player, ParsingUtil.blockPosToVec3d(origin).add(getOffset()));
    }

    //Getter and setter
    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public boolean shouldFuse() {
        return shouldFuse;
    }

    public void setShouldFuse(boolean shouldFuse) {
        this.shouldFuse = shouldFuse;
    }

    public boolean needsRedstone() {
        return needRedstone;
    }

    public void setNeedRedstone(boolean needRedstone) {
        this.needRedstone = needRedstone;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getFadeIn()
    {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn)
    {
        this.fadeIn = fadeIn;
    }

    public int getFadeOut()
    {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut)
    {
        this.fadeOut = fadeOut;
    }

    public int getPriority() { return priority; }

    public void setPriority(int priority)
    {
        if(priority < 0)
            this.priority = 0;

        if(priority >= maxPriorities)
            this.priority = maxPriorities - 1;

        this.priority = priority;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        /*this.channel = channel;*/
        if(channel < 0)
            this.channel = 0;

        if(channel >= maxChannels)
            this.channel = maxChannels - 1;

        this.channel = channel;
    }

    public boolean isUsingPriority() {
        return usePriority;
    }

    public void setUsePriority(boolean usePriority) {
        this.usePriority = usePriority;
    }

    public AbstractBounds getBounds() {
        return bounds;
    }

    public void setBounds(AbstractBounds bounds) { this.bounds = bounds; }

    public Vec3d getOffset() {
        return offset;
    }

    public void setOffset(Vec3d offset) {
        this.offset = offset;
    }

    public int getMinDelay() {
        return minDelay;
    }

    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public boolean isUsingDelay() {
        return useDelay;
    }

    public void setUseDelay(boolean useDelay) {
        this.useDelay = useDelay;
    }

    public float getMinRandVolume()
    {
        return minRandVolume;
    }

    public void setMinRandVolume(float minRandVolume)
    {
        this.minRandVolume = minRandVolume;
    }

    public float getMaxRandVolume()
    {
        return maxRandVolume;
    }

    public void setMaxRandVolume(float maxRandVolume)
    {
        this.maxRandVolume = maxRandVolume;
    }

    public float getMinRandPitch()
    {
        return minRandPitch;
    }

    public void setMinRandPitch(float minRandPitch)
    {
        this.minRandPitch = minRandPitch;
    }

    public float getMaxRandPitch()
    {
        return maxRandPitch;
    }

    public void setMaxRandPitch(float maxRandPitch)
    {
        this.maxRandPitch = maxRandPitch;
    }

    public boolean isUsingRandomVolume()
    {
        return minRandVolume != 0f || maxRandVolume != 0f;
    }

    public float getMinRandomVolume()
    {
        return getVolume() - minRandVolume;
    }

    public float getMaxRandomVolume()
    {
        return getVolume() + maxRandVolume;
    }

    public boolean isUsingRandomPitch()
    {
        return minRandPitch != 0f || maxRandPitch != 0f;
    }

    public float getMinRandomPitch()
    {
        return getPitch() - minRandPitch;
    }

    public float getMaxRandomPitch()
    {
        return getPitch() + maxRandPitch;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public boolean canPlayOverSelf() {
        return canPlayOverSelf;
    }

    public void setCanPlayOverSelf(boolean canPlayOverSelf) {
        this.canPlayOverSelf = canPlayOverSelf;
    }

    public boolean shouldStopPrevious() {
        return shouldStopPrevious;
    }

    public void setShouldStopPrevious(boolean shouldStopPrevious) {
        this.shouldStopPrevious = shouldStopPrevious;
    }
}
