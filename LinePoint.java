package net.minecraft.graffiti;

import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.item.ItemDye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LinePoint
{
	private float lineWidth = 0.1F;
	private float cubeSize = 1.0F;
	private float posXstart;
	private float posYstart;
	private float posZstart;
	private float posXend;
	private float posYend;
	private float posZend;
	private int side;
	private int color;
	private int size;

    public LinePoint()
    {
        this(0F, 0F, 0F, 0F, 0F, 0F, 0, 0, 1);
    }

    public LinePoint(NBTTagCompound nbttagcompound1)
    {
        this(0F, 0F, 0F, 0F, 0F, 0F, 0, 0, 1);
        readFromNBT(nbttagcompound1);
    }

    public LinePoint(ByteArrayDataInput data)
    {
        this(0F, 0F, 0F, 0F, 0F, 0F, 0, 0, 1);
        readToPacket(data);
    }

    public LinePoint(float posXstart, float posYstart, float posZstart, float posXend, float posYend, float posZend,int side, int color, int size)
    {
       	if(posXstart == posXend)
       	{
           	if(posXstart == lineWidth)
           	{
           		posXstart = 0.0F;
           		posXend = 0.0F;
        	}
           	else if(posXstart == (cubeSize - lineWidth) )
           	{
           		posXstart = cubeSize;
           		posXend = cubeSize;
        	}
    	}

       	if(posYstart == posYend)
       	{
           	if(posYstart == lineWidth)
           	{
           		posYstart = 0.0F;
           		posYend = 0.0F;
        	}
           	else if(posXstart == (cubeSize - lineWidth) )
           	{
           		posYstart = cubeSize;
           		posYend = cubeSize;
        	}
    	}

       	if(posZstart == posZend)
       	{
           	if(posZstart == lineWidth)
           	{
           		posZstart = 0.0F;
           		posZend = 0.0F;
        	}
           	else if(posZstart == (cubeSize - lineWidth) )
           	{
           		posZstart = cubeSize;
           		posZend = cubeSize;
        	}
    	}

        this.posXstart = posXstart;
        this.posYstart = posYstart;
        this.posZstart = posZstart;
        this.posXend = posXend;
        this.posYend = posYend;
        this.posZend = posZend;
        this.side = side;
        this.color = color;
        this.size = size;
    }

    public int getSide()
    {
    	return side;
    }


    /**
     * 線の整合性チェック
     * @return
     */
    public boolean checkLine()
    {
    	//同一面ではない場合
    	if(posXstart != posXend && posYstart != posYend && posZstart != posZend)
    	{
    		return false;
    	}

    	return true;
    }

    /**
     * NBTへ保存
     * @return
     */
    public void writeToNBT(NBTTagList nbttaglist)
    {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setFloat("posXstart", posXstart);
		nbttagcompound1.setFloat("posYstart", posYstart);
		nbttagcompound1.setFloat("posZstart", posZstart);
		nbttagcompound1.setFloat("posXend", posXend);
		nbttagcompound1.setFloat("posYend", posYend);
		nbttagcompound1.setFloat("posZend", posZend);
		nbttagcompound1.setInteger("side", side);
		nbttagcompound1.setInteger("color", color);
		nbttagcompound1.setInteger("size", size);
		if(size == 0)
			size = 1;

		nbttaglist.appendTag(nbttagcompound1);
    }

    /**
     * NBTから読み込み
     * @return
     */
    public void readFromNBT(NBTTagCompound nbttagcompound1)
    {
    	posXstart = nbttagcompound1.getFloat("posXstart");
    	posYstart = nbttagcompound1.getFloat("posYstart");
    	posZstart = nbttagcompound1.getFloat("posZstart");
    	posXend = nbttagcompound1.getFloat("posXend");
    	posYend = nbttagcompound1.getFloat("posYend");
    	posZend = nbttagcompound1.getFloat("posZend");
    	side = nbttagcompound1.getInteger("side");
    	color = nbttagcompound1.getInteger("color");
    	size = nbttagcompound1.getInteger("size");
    }

    public void writeToPacket(DataOutputStream dos)
    {
		try{
			dos.writeFloat(posXstart);
			dos.writeFloat(posYstart);
			dos.writeFloat(posZstart);
			dos.writeFloat(posXend);
			dos.writeFloat(posYend);
			dos.writeFloat(posZend);
			dos.writeInt(side);
			dos.writeInt(color);
			dos.writeInt(size);

		}catch (IOException e){
			e.printStackTrace();
		}
    }


    public void readToPacket(ByteArrayDataInput data)
    {
    	posXstart = data.readFloat();
    	posYstart = data.readFloat();
    	posZstart = data.readFloat();
    	posXend = data.readFloat();
    	posYend = data.readFloat();
    	posZend = data.readFloat();
    	side = data.readInt();
    	color = data.readInt();
    	size = data.readInt();
    }

    public float getAngleY()
    {
    	return (float)(Math.atan2(posXend - posXstart,posZend - posZstart) * 180.0D / Math.PI) - 90.0F;
    }

    public double getAngleYlength()
    {
    	double exRadians = Math.toRadians(getAngleY());

    	double len = getLengthX();
    	if(len == 0)
    		return getLengthZ();

        return Math.abs(len / Math.cos(exRadians));
    }

    public float getAngleX()
    {
    	return (float)(Math.atan2(posZend - posZstart,posYend - posYstart) * 180.0D / Math.PI) - 90.0F;
    }

    public double getAngleXlength()
    {
    	double exRadians = Math.toRadians(getAngleX());
        double len = getLengthZ();
        if(len == 0)
        	return getLengthY();

        return Math.abs(len / Math.cos(exRadians));
    }

    public float getAngleZ()
    {
    	return (float)(Math.atan2(posYend - posYstart,posXend - posXstart) * 180.0D / Math.PI) - 90.0F;
    }

    public double getAngleZlength()
    {
    	double exRadians = Math.toRadians(getAngleZ());
        double len = getLengthY();
        if(len == 0)
        	return getLengthX();

        return Math.abs(len / Math.cos(exRadians));
    }

    public float getMinX()
    {
    	return posXstart;
    }

    public float getMinY()
    {
    	return posYstart;
    }

    public float getMinZ()
    {
    	return posZstart;
    }

    public float getMaxX()
    {
    	return posXend;
    }

    public float getMaxY()
    {
    	return posYend;
    }

    public float getMaxZ()
    {
    	return posZend;
    }

    public float getLengthX()
    {
    	return Math.abs(posXend - posXstart);
    }

    public float getLengthY()
    {
    	return Math.abs(posYend - posYstart);
    }

    public float getLengthZ()
    {
    	return Math.abs(posZend - posZstart);
    }

    public int getColor()
    {
    	return ItemDye.dyeColors[15 - color];
    }

    public float getLineSize()
    {
    	return mod_Graffiti.getLineWidth() * size;
    }
}