package de.dafuqs.spectrum;

public class ExperienceHelper {
	
	public static int getLevelForExperience(int experience) {
		int levels = 0;
		while (experience > 0) {
			int nextLevelExperience = getNextLevelExperience(levels);
			if(experience >= nextLevelExperience) {
				experience -= nextLevelExperience;
				levels++;
			}
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
	
}
