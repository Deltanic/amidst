package amidst.map.layers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import MoF.Biome;
import MoF.ChunkManager;
import MoF.ReflectionInfo;
import amidst.Log;
import amidst.Options;
import amidst.foreign.VersionInfo;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectNether;
import amidst.map.MapObjectStronghold;
import amidst.map.MapObjectVillage;
import amidst.minecraft.Minecraft;
import amidst.minecraft.MinecraftObject;
import amidst.minecraft.MinecraftUtil;

public class StrongholdLayer extends IconLayer {
	public static StrongholdLayer instance;
	
	private static Biome[] biomesDefault  = { Biome.d, Biome.f, Biome.e, Biome.h };
	private static Biome[] biomes1_0  = { Biome.d, Biome.f, Biome.e, Biome.h, Biome.g, Biome.n, Biome.o };
	private static Biome[] biomes1_1 = { Biome.d, Biome.f, Biome.e, Biome.h, Biome.g, Biome.n, Biome.o, Biome.s, Biome.t, Biome.v };
	private static Biome[] biomes12w03a = { Biome.d, Biome.f, Biome.e, Biome.h, Biome.g, Biome.n, Biome.o, Biome.s, Biome.t, Biome.v, Biome.w, Biome.x };
	
	private MapObjectStronghold[] strongholds;
	
	public StrongholdLayer() {
		super("strongholds");
		instance = this;
		setVisibilityPref(Options.instance.showIcons);
		
		findStrongholds();
	
	}
	/*		public class MapGenStronghold {

			public MapObjectStronghold[] coords = new MapObjectStronghold[3];
			private Random c = new Random();
			public static boolean reset0 = false;
			protected MapObjectStronghold[] a(long seed, ChunkManager x) {
				
			}
		}*/
	public void generateMapObjects(Fragment frag) {
		int size = Fragment.SIZE >> 4;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int chunkX = x + frag.getChunkX();
				int chunkY = y + frag.getChunkY();
				if (checkChunk(chunkX, chunkY)) {
					frag.addObject(new MapObjectStronghold(x << 4, y << 4).setParent(this));
				}
			}
		}
	}
	 
	public void findStrongholds() {
		Random random = new Random();
		random.setSeed(Options.instance.seed);
		
		Biome[] validBiomes = biomesDefault;
		if (Minecraft.getActiveMinecraft().version == VersionInfo.V1_9pre6 || Minecraft.getActiveMinecraft().version == VersionInfo.V1_0)
			validBiomes = biomes1_0;
		if (Minecraft.getActiveMinecraft().version == VersionInfo.V1_1)
			validBiomes = biomes1_1;
		if (Minecraft.getActiveMinecraft().version.isAtLeast(VersionInfo.V12w03a))
			validBiomes = biomes12w03a;
		
		List<Biome> biomeArrayList = Arrays.asList(validBiomes);
		
		double angle = random.nextDouble() * 3.141592653589793D * 2.0D;
		for (int i = 0; i < 3; i++) {
			double distance = (1.25D + random.nextDouble()) * 32.0D;
			int x = (int)Math.round(Math.cos(angle) * distance);
			int y = (int)Math.round(Math.sin(angle) * distance);


			
			Point strongholdLocation = MinecraftUtil.findValidLocation((x << 4) + 8, (y << 4) + 8, 112, biomeArrayList, random);
			if (strongholdLocation != null) {
				x = strongholdLocation.x >> 4;
				y = strongholdLocation.y >> 4;
			}
			strongholds[i] = new MapObjectStronghold((x << 4), (y << 4));
			
			angle += 6.283185307179586D / 3.0D;
		}
	}

	public boolean checkChunk(int chunkX, int chunkY) {
		for (int i = 0; i < 3; i++) {
			int strongholdChunkX = strongholds[i].x >> 4;
			int strongholdChunkY = strongholds[i].y >> 4;
			if ((strongholdChunkX == chunkX) && (strongholdChunkY == chunkY))
				return true;
			
			
		}
		return false;
	}
	public MapObjectStronghold[] getStrongholds() {
		return strongholds;
	}
}
