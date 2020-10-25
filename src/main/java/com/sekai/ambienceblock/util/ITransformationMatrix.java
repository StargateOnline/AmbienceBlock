package com.sekai.ambienceblock.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ITransformationMatrix
{
    default TransformationMatrix getTransformation()
    {
        return (TransformationMatrix)this;
    }

    default boolean isIdentity()
    {
        return getTransformation().equals(TransformationMatrix.identity());
    }

    default void push(MatrixStack stack)
    {
        stack.push();

        Vector3f trans = getTransformation().getTranslation();
        stack.translate(trans.getX(), trans.getY(), trans.getZ());

        stack.rotate(getTransformation().getRotationLeft());

        Vector3f scale = getTransformation().getScale();
        stack.scale(scale.getX(), scale.getY(), scale.getZ());

        stack.rotate(getTransformation().getRightRot());

    }

    default TransformationMatrix compose(TransformationMatrix other)
    {
        if (getTransformation().isIdentity()) return other;
        if (other.isIdentity()) return getTransformation();
        Matrix4f m = getTransformation().getMatrix();
        m.mul(other.getMatrix());
        return new TransformationMatrix(m);
    }

    default TransformationMatrix inverse()
    {
        if (isIdentity()) return getTransformation();
        Matrix4f m = getTransformation().getMatrix().copy();
        m.invert();
        return new TransformationMatrix(m);
    }

    default void transformPosition(Vector4f position)
    {
        position.transform(getTransformation().getMatrix());
    }

    default void transformNormal(Vector3f normal)
    {
        normal.transform(getTransformation().getNormalMatrix());
        normal.normalize();
    }

    default Direction rotateTransform(Direction facing)
    {
        return Direction.rotateFace(getTransformation().getMatrix(), facing);
    }

    /**
     * convert transformation from assuming center-block system to opposing-corner-block system
     */
    default TransformationMatrix blockCenterToCorner()
    {
        return applyOrigin(new Vector3f(.5f, .5f, .5f));
    }

    /**
     * convert transformation from assuming opposing-corner-block system to center-block system
     */
    default TransformationMatrix blockCornerToCenter()
    {
        return applyOrigin(new Vector3f(-.5f, -.5f, -.5f));
    }

    /**
     * Apply this transformation to a different origin.
     * Can be used for switching between coordinate systems.
     * Parameter is relative to the current origin.
     */
    default TransformationMatrix applyOrigin(Vector3f origin) {
        TransformationMatrix transform = getTransformation();
        if (transform.isIdentity()) return TransformationMatrix.identity();

        Matrix4f ret = transform.getMatrix();
        Matrix4f tmp = Matrix4f.makeTranslate(origin.getX(), origin.getY(), origin.getZ());
        ret.multiplyBackward(tmp);
        tmp.setIdentity();
        tmp.setTranslation(-origin.getX(), -origin.getY(), -origin.getZ());
        ret.mul(tmp);
        return new TransformationMatrix(ret);
    }
}
