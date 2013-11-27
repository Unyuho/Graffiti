package net.minecraft.graffiti;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LinePointManager
{
	private List<LinePoint> linePoint;

    public LinePointManager()
    {
    	this.linePoint = new ArrayList<LinePoint>();
    }

    public void addPoint(LinePoint point)
    {
    	this.linePoint.add(point);
    }

    public void addPoint(float posXstart, float posYstart, float posZstart, float posXend, float posYend, float posZend,int side,int color, int size)
    {
    	LinePoint point = new LinePoint(posXstart,posYstart,posZstart,posXend,posYend,posZend,side,color,size);
    	this.linePoint.add(point);
    }

    public boolean removePoint()
    {
    	if(linePoint.size() > 1){
        	linePoint.remove(linePoint.size()-1);
        	return true;
    	}else{
    		return false;
    	}
    }

    /**
     * NBTへ保存
     * @param par1NBTTagCompound
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Lines");
    	if(nbttaglist == null)
    	{
    		nbttaglist = new NBTTagList();
    	}

    	Iterator<LinePoint> iterator = linePoint.iterator();
    	while(iterator.hasNext())
    	{
    		LinePoint point = iterator.next();
    		point.writeToNBT(nbttaglist);
    	}

    	par1NBTTagCompound.setTag("Lines", nbttaglist);
    }

    /**
     * NBTより読み込み
     * @param par1NBTTagCompound
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	linePoint.clear();

    	NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Lines");
    	if(nbttaglist == null)
    	{
    		return;
    	}

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
	    	LinePoint point = new LinePoint(nbttagcompound1);
	    	this.linePoint.add(point);
		}
    }


    public void writeToPacket(DataOutputStream dos)
    {
    	try{
			dos.writeInt(linePoint.size());
		}catch (IOException e){
			e.printStackTrace();
		}

    	Iterator<LinePoint> iterator = linePoint.iterator();
    	while(iterator.hasNext())
    	{
    		LinePoint point = iterator.next();
    		point.writeToPacket(dos);
    	}
    }

    public void readToPacket(ByteArrayDataInput data)
    {
    	linePoint.clear();

    	int size = data.readInt();
    	for(int cnt = 0 ; cnt < size; cnt++)
    	{
	    	LinePoint point = new LinePoint(data);
	    	this.linePoint.add(point);
    	}
    }

    public LinePoint[] getArray()
    {
    	LinePoint[] points = this.linePoint.toArray(new LinePoint[0]);
    	return points;
    }


    public boolean[] getSideArray()
    {
    	boolean[] sides = new boolean[6];

    	Iterator<LinePoint> iterator = linePoint.iterator();
    	while(iterator.hasNext())
    	{
    		LinePoint point = iterator.next();
    		sides[point.getSide()] = true;
    	}

    	return sides;
    }
}