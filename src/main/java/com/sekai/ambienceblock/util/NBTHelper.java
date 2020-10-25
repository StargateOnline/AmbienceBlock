package com.sekai.ambienceblock.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class NBTHelper {
    public static NBTTagCompound writeVec3d(Vec3d pos) {
        NBTTagCompound compoundnbt = new NBTTagCompound();
        compoundnbt.setDouble("X", pos.x);
        compoundnbt.setDouble("Y", pos.y);
        compoundnbt.setDouble("Z", pos.z);
        return compoundnbt;
    }

    public static Vec3d readVec3d(NBTTagCompound tag) {
        return new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
    }
}
