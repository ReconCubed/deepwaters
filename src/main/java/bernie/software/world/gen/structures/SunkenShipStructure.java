package bernie.software.world.gen.structures;

import bernie.software.DeepWatersMod;
import bernie.software.utils.GeneralUtils;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class SunkenShipStructure extends Structure<NoFeatureConfig>
{
	public SunkenShipStructure()
	{
		super(NoFeatureConfig::deserialize);
	}

	@Override
	public String getStructureName()
	{
		return GeneralUtils.Location("sunkenship").toString();
	}


	/*
	 * This seems to be unused but cannot be removed. Just return 0 is all you need to do.
	 */
	@Override
	public int getSize()
	{
		return 0;
	}


	/*
	 * This is how the worldgen code will start the generation of our structure when it passes the checks.
	 */
	@Override
	public Structure.IStartFactory getStartFactory()
	{
		return SunkenShipStructure.Start::new;
	}


	/*
	 * I believe this is used so that no two structure's spawning will be in perfect sync as long as they have different
	 * seed modifier.
	 */
	private int getSeedModifier()
	{
		return 543749857;
	}

	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z,
	                                               int spacingOffsetsX, int spacingOffsetsZ)
	{
		//this means shipwrecks cannot be closer than 7 chunks or more than 12 chunks
		int maxDistance = 20;
		int minDistance = 7;

		int xTemp = x + maxDistance * spacingOffsetsX;
		int ztemp = z + maxDistance * spacingOffsetsZ;
		int xTemp2 = xTemp < 0 ? xTemp - maxDistance + 1 : xTemp;
		int zTemp2 = ztemp < 0 ? ztemp - maxDistance + 1 : ztemp;
		int validChunkX = xTemp2 / maxDistance;
		int validChunkZ = zTemp2 / maxDistance;

		((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), validChunkX, validChunkZ,
				getSeedModifier());
		validChunkX = validChunkX * maxDistance;
		validChunkZ = validChunkZ * maxDistance;
		validChunkX = validChunkX + random.nextInt(maxDistance - minDistance);
		validChunkZ = validChunkZ + random.nextInt(maxDistance - minDistance);

		return new ChunkPos(validChunkX, validChunkZ);
	}



	@Override
	public boolean func_225558_a_(BiomeManager biomeManager, ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ, Biome biome)
	{
		ChunkPos chunkpos = getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
		if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z)
		{
			if (chunkGen.hasStructure(biome, this))
			{
				return true;
			}
		}
		return false;
	}

	private static PlacementSettings placementSettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(
			Rotation.NONE).setIgnoreEntities(false).setChunk(
			null);


	public static class Start extends StructureStart
	{

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn)
		{
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ,
		                 Biome biomeIn)
		{
			//Check out vanilla's WoodlandMansionStructure for how they offset the x and z
			//so that they get the y value of the land at the mansion's entrance, no matter
			//which direction the mansion is rotated.
			//
			//However, for most purposes, getting the y value of land with the default x and z is good enough.
			Rotation rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];

			//Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
			int x = (chunkX << 4) + 7;
			int z = (chunkZ << 4) + 7;
			Random random = new Random();
			//Finds the y value of the terrain at location.
			int yPos = random.nextInt(generator.getSeaLevel() - 20) + 20;
			BlockPos blockpos = new BlockPos(x, yPos, z);

			//Now adds the structure pieces to this.components with all details such as where each part goes
			//so that the structure can be added to the world by worldgen.
			SunkenShipPiece.start(templateManagerIn, blockpos, rotation, components, rand);

			//Sets the bounds of the structure.
			recalculateStructureSize();

			//I use to debug and quickly find out if the structure is spawning or not and where it is.
			DeepWatersMod.logger.log(Level.DEBUG,
					"Shipwreck spawned at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
		}

	}
}
