/**
 * 
 */
package redgear.geocraft.api.mine;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.Location;

/**
 * @author JamBox
 *
 */
public class MineFault extends MineSingleOre {

	protected transient int intRarity;
	protected transient int intMineSize;
	protected transient int intVeinSize; 

	protected float mineSize;
	protected float mineRarity;
	protected float veinSize; 
	
	public MineFault() {
		// TODO Auto-generated constructor stub
	}

	public MineFault(String name, ISimpleItem block, ISimpleItem target, float mineRarity, float mineSize, int veinSize) {
		super(name, block, target);
		this.mineSize = mineSize;
		this.mineRarity = mineRarity;
		this.veinSize = veinSize;
	}

	@Override
	public void generateMine(World world, Random rand, int chunkX, int chunkZ,
			Boolean debug, int commandY) {
		if(debug || rand.nextInt(intRarity)==0) { 
		     
			//round z
		     int zRot=setRotation(rand, 0);
		     //round y
		     int yRot=setRotation(rand, 1);
		     int horRot45=setRot45(rand);
		     int verRot45=setRot45(rand);
		     ForgeDirection orient=setVeinOrientation(rand, zRot, yRot);
		     int[] modifiers=setModifier(rand);
		     int genSpacing=setGenSpacing();
		     int length=intMineSize/2+rand.nextInt(intMineSize/2);
		     int yMin= setYmin(length, zRot, verRot45);
             //will need more control of commandY
		     int startY=(debug ? commandY : rand.nextInt(world.getHeightValue(chunkX, chunkZ)-yMin)+yMin);
		     int startX=(debug ? chunkX : chunkX*16+rand.nextInt(16));
		     int startZ=(debug ? chunkZ : chunkZ*16+rand.nextInt(16));
		     Location mineStart= new Location(startX, startY, startZ);
		     
		      
		     int y=0;
		     int z=0;
		     for(int x=0; x<=length; x++) {
		        //add weighted random here instead here
		        y=y+(rand.nextInt(2)-1);
		        z=z+(rand.nextInt(2)-1);
		        if(x%genSpacing==0) { 
		            Location veinStart=new Location(x,y,z).rotate(ForgeDirection.NORTH, zRot).rotate(ForgeDirection.UP, yRot).translate(mineStart);
		            genVein(world, rand, veinStart, mineStart, length, horRot45, verRot45, orient, modifiers, debug);
		        }
		     }
		}
		        

	}

	@Override
	public void generate(World world, Random rand, int chunkX, int chunkZ) {
		generateMine(world, rand, chunkX, chunkZ, false, 0);

	}
	
	@Override
	public void init() {
		super.init();
		intRarity = (int) (mineRarity * getRegistry().rarityModifier());
		if (intRarity <= 0)
			intRarity = 1;
		intMineSize = (int) (mineSize * getRegistry().volumeModifier() / 2);
		intVeinSize = (int) (veinSize * getRegistry().volumeModifier() / 2);
	}
	
	protected void genVein(World world, Random rand , Location veinStart, Location mineStart, int length, int horRot45, int verRot45, ForgeDirection orient, int[] modifiers, Boolean debug) {
		if(debug) { veinStart.placeBlock(world, block); }
		else {veinStart.placeBlock(world, block, target); }
	}
	
	
	//all the following methods are to allow subclass to set mine by mine specific variables
	
	//for subclass to be able to limit rotation
	protected int setRotation(Random rand, int axis) {
		  
	      switch (axis) {
	      	//z-axis      
	        case 0: return rand.nextInt(4);
	        //y-axis
	        case 1: return rand.nextInt(4);
	        default: return 0;
	      }
	}

	//for subclass to change sub-axis rotation
	protected int setRot45(Random rand) {
	      int amountMod=rand.nextInt(3); 
	        switch(amountMod) {
	          case 0: return -45;
	          case 1: return 45;
	          case 2: return 0;
	          default: return 0;
	        }
	   }

	//For passing direction to subclass, maybe do it a better way
	
	protected ForgeDirection setVeinOrientation(Random rand, int zRot, int yRot) {
	    if(zRot==1 || zRot==3) {
	      return ForgeDirection.NORTH; }
	    else { return ForgeDirection.UP;}

	}
	
	//used by subclass variable amounts of modifiers
	protected int[] setModifier(Random rand) {
		   int[] mod= {1};
		  return mod;
		}

	//for subclass to set the x movements between calls
	protected int setGenSpacing() {
		   return 1;
		 }

	//currently unfinished unused sub-axis rotation
	protected Location rotate45(Location point, Location centre, int ang, Boolean isHorRot) {  
		   //if dirHor z=z*cos(ang)-x*sin(ang)
		   // may need minus angle 
		   //          x=z*sin(ang)+x*cos(ang)
		   //if dirVer switch z with x and x with y
		//do i need centre
		   if (isHorRot) {
			  if(ang==0) { return point; }
			  else {
		      Location newLoc= new Location((int) (point.getZ()*Math.sin(ang)+point.getX()*Math.cos(ang)), point.getY(), (int) (point.getZ()*Math.cos(ang)-point.getZ()*Math.sin(ang)));
		      return newLoc;
			  }
		   }
		   //not correct yet
		   else {
			   if(ang==0) { return point; }
			   else {
			   Location newLoc= new Location((int) (point.getZ()*Math.cos(ang)-point.getZ()*Math.sin(ang)), point.getY(), (int) (point.getZ()*Math.sin(ang)+point.getX()*Math.cos(ang))).translate(centre);
			      return newLoc;
			   }
		   }
	}
	
	protected int setYmin(int length, int zRot, int verRot45) {
		if (zRot==1 || zRot==3 || verRot45==-45) {
			return length;
		}
		return 4;
		}
	}

