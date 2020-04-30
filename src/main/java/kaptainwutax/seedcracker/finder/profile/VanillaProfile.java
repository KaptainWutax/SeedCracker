package kaptainwutax.seedcracker.finder.profile;

import kaptainwutax.seedcracker.finder.Finder;

public class VanillaProfile extends FinderProfile {

	public VanillaProfile() {
		super(true);
		this.author = "KaptainWutax";
		this.locked = false;

		this.setTypeState(Finder.Type.DUNGEON, false);
	}

}
