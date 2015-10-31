package redgear.geocraft.mines;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.Location;
import redgear.geocraft.api.mine.MineSingleOre;

public class MineLaccolith extends MineSingleOre {

	protected transient int intRarity;
	protected transient int intMineSize;
	protected transient int intVeinSize;
	protected float mineSize;
	protected float mineRarity;
	protected float veinSize;

	public MineLaccolith(String name, ISimpleItem block, ISimpleItem target,
			float mineRarity, float mineSize, float veinSize) {
		super(name, block, target);
		this.mineSize = mineSize;
		this.mineRarity = mineRarity;
		this.veinSize = veinSize;
	}

	@Override
	public void generateMine(World world, Random rand, int chunkX, int chunkZ,
			Boolean debug, int commandY) {
		
		if (rand.nextInt(intRarity) == 0 || debug) {
			int centreX = (debug ? chunkX : chunkX * 16 + rand.nextInt(16));
			int centreZ = (debug ? chunkZ : chunkZ * 16 + rand.nextInt(16));
			//System.out.println("MineSize is" + mineSize);
			
			/*if (debug) {
				System.out.println("registry modifier is"
						+ getRegistry().rarityModifier());
			} */
			
			//System.out.println("intMineSize is" + intMineSize);
			float yAxis = (float) intMineSize / 2;
			float xzAxis = ((float) intMineSize / 2 + rand
					.nextInt(intMineSize / 2));
			//System.out.println("xzAxis and yAxis; " + xzAxis + "and" + yAxis);
			float xzAxisSqr = xzAxis * xzAxis;
			float yAxisSqr = yAxis * yAxis;
			int positionModifier = (world.getHeightValue(centreX, centreZ) - (int) yAxis * 2);
			
			if (positionModifier <= 0) {
				return;
			}
			int centreY = (debug ? Math.min(commandY,
					(world.getActualHeight() - (int) yAxis)) : (rand
					.nextInt(positionModifier) + (int) yAxis));

			for (int x = (int) -xzAxis; x <= xzAxis; x++) {
				// System.out.println("x loop called for"+x);
				for (int z = (int) -xzAxis; z <= xzAxis; z++) {
					// System.out.println("z loop called for"+z);
					for (int y = (int) -yAxis; y <= yAxis; y++) {
						// System.out.println("y loop called for"+y);

						float c1 = ((x * x) / xzAxisSqr);
						// System.out.println("c1 is"+c1);
						if (Float.isNaN(c1)) {
							c1 = 0;
						}
						float c2 = ((z * z) / xzAxisSqr);
						// System.out.println("c2 is"+c2);
						if (Float.isNaN(c2)) {
							c2 = 0;
						}
						float c3 = ((y * y) / yAxisSqr);
						// System.out.println("c3 is"+c3);
						if (Float.isNaN(c3)) {
							c3 = 0;
						}
						float bounds = c1 + c2 + c3;
						if (bounds <= 1) {
							System.out.println("bounds is " + bounds);
							// density needs fixing
							// not sure but bounds tend to zero only on 0.5
							// may get smaller with size
							if (rand.nextFloat() * 2 >= bounds * 0.8f) {
								Location loc = new Location(x, y, z).translate(
										centreX, centreY, centreZ);
								if (debug) {
									loc.placeBlock(world, block);
								} else {
									loc.placeBlock(world, block, target);
								}

							}
						}
					}
				}
			}
			/*System.out.println("MineSize is" + mineSize);
			if (debug) {
				//System.out.println("registry modifier is"
						+ getRegistry().rarityModifier());
			}*/
			//System.out.println("intMineSize is" + intMineSize);
			genTail(world, new Location(centreX, centreY, centreZ), rand,
					intMineSize, debug);
		}
	}

	private void genTail(World world, Location start, Random rand,
			int remaining, Boolean debug) {
		
		if (debug) {
			start.placeBlock(world, block);
		} else {
			start.placeBlock(world, block, target);
		}
		if (remaining > 0) {
			if (rand.nextInt(5) == 0)
				start = start.translate(2 + rand.nextInt(5), 1);

			genTail(world, start.translate(ForgeDirection.DOWN, 1), rand,
					--remaining, debug);
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
