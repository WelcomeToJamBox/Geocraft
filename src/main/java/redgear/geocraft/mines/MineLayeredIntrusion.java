/**
 * 
 */
package redgear.geocraft.mines;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.Location;
import redgear.geocraft.api.mine.MineFault;

/**Unfinished
 * @author James
 *
 */
public class MineLayeredIntrusion extends MineFault {

    protected ISimpleItem[] validOres;

	public MineLayeredIntrusion(String name, ISimpleItem block,
			ISimpleItem target, float mineRarity, float mineSize, int veinSize, ISimpleItem[] validOres) {
		super(name, block, target, mineRarity, mineSize, veinSize);
		this.validOres= validOres;
	}
	
	
	@Override
	protected void genVein(World world, Random rand, Location veinStart, Location mineStart, int length, int horRot45, int verRot45, ForgeDirection orient, int[] modifiers, Boolean debug) {

	 int numBlocks=modifiers[6];
	 int veinLength=intVeinSize+(length/2);
	 double scale=((Math.PI*Math.PI)/(2*veinLength));
	 ForgeDirection perp = getPerpendicularAxis(orient);
	 
	 for(int x=0; x<veinLength; x++) {
	    int z=(int) ((x/3)*(Math.sin((x*scale)+modifiers[5])));
	    int y=(int) ((x/3)*(Math.sin((x*scale)+modifiers[4])));
	    //TODO finish rotation handling
	    Location start=new Location(0,y,0).translate(orient, x).translate(perp, z).translate(veinStart);
	    
	    for(int n=0; n<numBlocks; n++) {
	    	Location next =start.translate(ForgeDirection.UP, n);
	    	if(debug) {next.placeBlock(world, validOres[modifiers[n]]); }
	    	else {next.placeBlock(world, validOres[modifiers[n]], target); }
	
	     }
	   }
	  }

	@Override
	protected int setRotation(Random rand, int axis) {
			switch(axis) {
			    case 0: return 0;
			    case 1: return rand.nextInt(4);
			    default: return 0;
			}
	}

	
	@Override
	protected ForgeDirection setVeinOrientation(Random rand, int zRot, int yRot) {
		  if(yRot==1 || yRot==3) {
			    ForgeDirection dir=(rand.nextBoolean() ? ForgeDirection.EAST: ForgeDirection.WEST);
			    return dir;
			   }
			  else {
			     ForgeDirection dir=(rand.nextBoolean() ? ForgeDirection.SOUTH: ForgeDirection.NORTH);
			     return dir;
			   }
	}


	@Override
	protected int[] setModifier(Random rand) {
	    //first 3 getting ores
	    //next 2 getting x phase shift
	    //last numBlocks
	    int[] modifiers= {rand.nextInt(validOres.length),
							 rand.nextInt(validOres.length),
							 rand.nextInt(validOres.length),
							 rand.nextInt(10),
							 rand.nextInt(10),
							 rand.nextInt(3)+1 };
	    return modifiers;
	}

	//maybe move to super class for more control
	protected ForgeDirection getPerpendicularAxis(ForgeDirection orient) {
		
		//{DOWN, UP, NORTH, SOUTH, WEST, EAST, UNKwOWN}
		int[] perps= {2, 3, 4, 5, 2, 3, 6};
			return ForgeDirection.getOrientation(perps[orient.ordinal()]);
	}
	
	@Override
	protected int setYmin(int length, int zRot, int verRot45) {
		if (verRot45==-45) {
			//TODO get correct value
			return length*2;
		}
		if(zRot==0 || zRot==2 || verRot45==0){
			return length;
		}
		return 4;
		}
}
