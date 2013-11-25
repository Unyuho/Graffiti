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

public class ShapelessGraffitiRecipes implements IRecipe
{
    private final ItemStack recipeOutput;
    public final List recipeItems = new ArrayList();

    public ShapelessGraffitiRecipes(ItemStack par1ItemStack)
    {
        this.recipeOutput = par1ItemStack;
        this.recipeItems.add(par1ItemStack);
    }

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world)
	{
		int size = 0;
		int damage = -1;
		boolean dye = false;

		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 3; ++j)
			{
				ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(j, i);
				if(itemstack != null)
				{
					if (itemstack.itemID == recipeOutput.itemID)
					{
						if(damage == -1)
						{
							damage = itemstack.getItemDamage();
						}
						else
						{
							if(itemstack.getItemDamage() != damage)
							{
								return false;
							}
						}
						size++;
					}
					else if(itemstack.itemID == Item.dyePowder.itemID)
					{
						dye = true;
					}
					else
					{
						return false;
					}
                }
            }
        }

        return (size > 1) || (size == 1 && dye);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
	{
		ItemStack resultitemstack = recipeOutput.copy();
		ItemGraffiti item = (ItemGraffiti)resultitemstack.getItem();
		item.setLineSize(resultitemstack, 0);

		boolean dye = false;

		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 3; ++j)
			{
				ItemStack itemstack = inventorycrafting.getStackInRowAndColumn(j, i);
				if(itemstack != null)
				{
					if (itemstack.itemID == recipeOutput.itemID)
					{
						item.addLineSize(resultitemstack, itemstack);

						if(!dye){
							resultitemstack.setItemDamage(itemstack.getItemDamage());
						}
					}
					else if(itemstack.itemID == Item.dyePowder.itemID)
					{
						resultitemstack.setItemDamage(15 - itemstack.getItemDamage());
						dye = true;
					}
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
