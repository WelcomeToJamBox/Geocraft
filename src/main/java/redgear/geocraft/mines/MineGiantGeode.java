package redgear.geocraft.mines;

import java.util.Random;

import net.minecraft.world.World;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.Location;
import redgear.geocraft.api.mine.MineSpecialStone;

public class MineGiantGeode extends MineSpecialStone {
	
	
	protected transient int intRarity;
	protected transient int intMineSize;
	protected transient int intVeinSize;
	protected float mineSize;
	protected float mineRarity;
	protected float veinSize;

	public MineGiantGeode() {
		// TODO Auto-generated constructor stub
	}

	public MineGiantGeode(String name, ISimpleItem block, ISimpleItem target,
			ISimpleItem bearer) {
		super(name, block, target, bearer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateMine(World world, Random rand, int chunkX, int chunkZ,
			Boolean debug, int commandY) {
		  //could do a cluster or just chunk based
		  int startX=(debug ? chunkX : chunkX*16);
		  int startZ=(debug ? chunkX : chunkZ*16);
		  int yMin=(debug ? Math.min(commandY, world.getActualHeight()-intMineSize) : Math.max(rand.nextInt(world.getHeightValue(chunkX, chunkZ)-intMineSize), 4));
		  
		  for(int i=intVeinSize; i>=0; i--) {
		     int rad=Math.min(7,(i<=3?3:i));
		     
		     Location centre= new Location(startX+rand.nextInt(16),yMin+rand.nextInt(intMineSize),startZ+rand.nextInt(16));
		     genGeode(world,rand,rad,centre,debug);
		  }
		}
	
	protected void genGeode(World world, Random rand, int rad, Location centre, Boolean debug) {

		   for (int x=-rad; x<=rad; x++) {
		for (int z=-rad; z<=rad; z++) {
		for (int y=-rad; y<=rad; y++) {
		     
		     int d=(int) Math.sqrt((x*x)+(y*y)+(z*z));
		      //is this faster?
		      if(d>rad || d<rad-2) {}
		      int randVal=rand.nextInt(10);
		      if(d==rad) {
		         Location loc = new Location(x,y,z).translate(centre);
		         
		         if(randVal==9) {
		        	 //maybe use mine special stone. don't care for debug since just getting right shape and density
		        	 if(debug) {loc.placeBlock(world, block); }
		            else {loc.placeBlock(world, block, target); }
		          }
		         else {
		           if(debug) {loc.placeBlock(world,bearer); }
		           else {loc.placeBlock(world, bearer, target); }
		         }
		      }
		         
		      else if (d==rad-1) {
		    	  Location loc = new Location(x,y,z).translate(centre);
		           if (randVal<=8) {
		              if (debug) { loc.placeBlock(world, block); }
		              else { loc.placeBlock(world, block, target); }
		              }
		           else {
		        	   loc.setAir(world);
		           }
		              }
		      
		      else if(d<=rad) {
		    	  Location loc = new Location(x,y,z).translate(centre);
		    	  loc.setAir(world);
		      }
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
	
}
