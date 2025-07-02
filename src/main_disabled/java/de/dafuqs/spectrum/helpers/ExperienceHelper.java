package de.dafuqs.spectrum.helpers;

public class ExperienceHelper {
	
	public static int getLevelForExperience(int experience) {
		int levels = 0;
		while (experience > 0) {
			int nextLevelExperience = getNextLevelExperience(levels);
			if (experience >= nextLevelExperience) {
				levels++;
			}
			experience -= nextLevelExperience;
		}
		return levels;
	}
	
	public static int getNextLevelExperience(int experienceLevel) {
		if (experienceLevel >= 30) {
			return 112 + (experienceLevel - 30) * 9;
		} else {
			return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
		}
	}
	
	public static int getExperienceOrbSizeForExperience(int experience) {
		if (experience >= 2477) {
			return 10;
		} else if (experience >= 1237) {
			return 9;
		} else if (experience >= 617) {
			return 8;
		} else if (experience >= 307) {
			return 7;
		} else if (experience >= 149) {
			return 6;
		} else if (experience >= 73) {
			return 5;
		} else if (experience >= 37) {
			return 4;
		} else if (experience >= 17) {
			return 3;
		} else if (experience >= 7) {
			return 2;
		} else {
			return experience >= 3 ? 1 : 0;
		}
	}
	
}
