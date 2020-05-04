package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureFeatures;

public class TempleType extends RegionType {

	public TempleType(int salt) {
		super(salt, StructureFeatures.CONFIG.getTempleDistance(), StructureFeatures.CONFIG.getTempleSeparation());
	}

}
