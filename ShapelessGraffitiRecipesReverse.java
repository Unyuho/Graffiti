package net.minecraft.graffiti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShapelessGraffitiRecipesReverse implements IRecipe
{
    private final ItemStack recipeOutput;
    public final List recipeItems = new ArrayList();

    public ShapelessGraffitiRecipesReverse(ItemStack par1ItemStack)
    {
        this.recipeOutput = par1ItemStack;
        this.recipeItems.add(par1ItemStack);
    }

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world)
	{
		int size = 0;
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 3; ++j)
			{
				ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(j, i);
				if(itemstack != null)
				{
					if (itemstack.itemID == recipeOutput.itemID)
					{
						size++;
					}
					else
					{
						return false;
					}
                }
            }
        }

        return (size == 1);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
	{
		ItemStack resultitemstack = recipeOutput.copy();
		ItemGraffiti item = (ItemGraffiti)resultitemstack.getItem();
		item.setLineSize(resultitemstack, 1);

		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 3; ++j)
			{
				ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(j, i);
				if(itemstack != null)
				{
					resultitemstack.stackSize = item.getLineSize(itemstack);
					resultitemstack.setItemDamage(itemstack.getItemDamage());
				}
			}
		}
		return resultitemstack;
	}

	@Override
	public int getRecipeSize()
	{
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return this.recipeOutput;
	}
}
