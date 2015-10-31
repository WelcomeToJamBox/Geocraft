package redgear.geocraft.mines;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.Location;
import redgear.core.world.WorldLocation;
import redgear.geocraft.api.mine.MineSingleOre;
import redgear.geocraft.generation.VeinHelper;

/**
 * Generates a dense cluster of nodules surrounded by a sparse outer ring resembling an impact crater.
 *
 * @author Jam_Box
 *
 */

public class MineMetioricDeposit extends MineSingleOre {
	
	protected transient int intRarity;
	protected transient int intMineSize;
	protected transient int intVeinSize; 
	protected float mineSize;
	protected float mineRarity;
	protected float veinSize;
   
	public MineMetioricDeposit (String name, ISimpleItem block, ISimpleItem target, float mineRarity, float mineSize, int veinSize) {
      super(name, block, target);
		this.mineSize = mineSize;
		this.mineRarity = mineRarity;
		this.veinSize = veinSize;
 		}

	@Override
	public void generate(World world,Random rand, int chunkX, int chunkZ) {
		  generateMine(world, rand, chunkX, chunkZ, false, 0); 
		  }
	
	@Override
	public void generateMine(World world, Random rand, int chunkX, int chunkZ, Boolean debug, int commandY) {
		if (rand.nextInt(intRarity) == 0 || debug) {
	        //for the time being
			
			int centreX=(debug ? chunkX : chunkX*16+rand.nextInt(16));
			int centreZ=(debug ? chunkZ : chunkX*16+rand.nextInt(16));
			//gradient, and radius to be adjusted
			double m=(double) rand.nextDouble()*(rand.nextInt(2)+1);
			//changed for debugging
			int oRad=intMineSize;
			int iRad=intMineSize/2;
			int maxY=(int) (m*(oRad-iRad));
			int centreY=(debug ? commandY : rand.nextInt(Math.abs(world.getHeightValue(centreX,centreZ)-(maxY+iRad)))+iRad);
			
			//Debug testing
			System.out.println("spawning meteor mine co-ords X:"+centreX+",Y:"+centreY+",Z:"+centreZ);
			
			//generateCrater(world, rand, centreX, centreZ, m, iRad, maxY, centreY);
			//generateCrater(world, rand, centreX, centreZ, m, iRad, oRad,  maxY, centreY);
			generateCrater(world, rand, centreX, centreZ, m, iRad, oRad, centreY, debug);
			generateImpact(world, rand, centreX, centreZ, iRad, centreY, debug);
			}
		}

	/*depricated
	 * protected void generateCrater(World world, Random rand, int centreX, int centreZ, float m, int iRad, int maxY, int centreY) {
		int r;
		for(int y=maxY; (int) (y/m)>=iRad;) {
			r= (int) (y/m);
			for(int x=0; x<=r; x++) {
				int z=(int) Math.sqrt((r*r)-(x*x));					
				for(int rot=0; rot<4; rot++) {						
					if (rand.nextInt(10)==0) {
							new Location(x,y,z).rotate(ForgeDirection.UP, rot).translate(centreX,centreY,centreZ).placeBlock(world, block, target); 
						}
					}					
				r--;
				y= (int) (r*m);
			}
		}
	}	*/	
	
	//will leave gaps on high gradient
	/*protected void generateCrater(World world, Random rand, int centreX, int centreZ, float m, int iRad, int oRad, int maxY, int centreY) {
		for(int r=oRad; r<=iRad; r++) {
			int y=(int) m*(r-iRad);
			for(int x=0; x<=r; x++) {
				int z=(int) Math.sqrt((r*r)-(x*x));
				for(int rot=0; rot<4; rot++) {						
					if (rand.nextInt(10)==0) {
							new Location(x,y,z).rotate(ForgeDirection.UP, rot).translate(centreX,centreY,centreZ).placeBlock(world, block, target); 
						}
				}
			}
		}
	} */
	
	protected void generateCrater(World world, Random rand, int centreX, int centreZ, double m, int iRad, int oRad, int centreY, Boolean debug) {
		
		double ang= Math.atan(m);
		//debug
		//System.out.println("angle value calculated; "+ang);
		int cratRad=oRad-iRad;
		int maxY= (int) (m*(cratRad));
		double sinAng=Math.sin(ang);
		double cosAng=Math.cos(ang);
		int rSStart=(int) Math.sqrt((maxY*maxY)+(cratRad*cratRad));
		
		  for(int rS=rSStart; rS>=0; rS--) {
		     int y=(int) (rS*sinAng);
		     int rC=(int) (rS*cosAng+iRad);
		     
		    for(int x=0; x<=rC; x++) {
		      int z=(int) Math.sqrt((rC*rC)-(x*x));
		      
		      for(int rot=0; rot<4; rot++) {						
					if (rand.nextInt(10)==0) {
							Location location= new Location(x,y,z).rotate(ForgeDirection.UP, rot).translate(centreX,centreY,centreZ);
							
							if(debug) {
								location.placeBlock(world, block);}
							
							else {location.placeBlock(world, block, target);}
							}
					}
		      }
		    }
		  }
	
	
	
	protected void generateImpact(World world, Random rand, int centreX, int centreZ, int iRad, int centreY, Boolean debug) {
		
		for(int y0=-iRad; y0<=iRad; y0++) {
			for(int x=-iRad; x<=iRad; x++) {
					for(int z=-iRad; z<=iRad; z++) {
						
					if(Math.sqrt((x*x)+(y0*y0)+(z*z))<=iRad) {
						
						//ratio & size to be adjusted
						if(rand.nextInt(6)==0) {
							
							int size=rand.nextInt(3);
							
							WorldLocation loc= new WorldLocation(x,y0,z, world).translate(centreX,centreY,centreZ); 						
							VeinHelper.generateSphere(loc,block,target,rand,size);
						}
					}
				}
			}
		}
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
	


