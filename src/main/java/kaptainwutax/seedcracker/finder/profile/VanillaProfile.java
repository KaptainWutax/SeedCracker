package kaptainwutax.seedcracker.finder.profile;

public class VanillaProfile extends FinderProfile {

	public VanillaProfile() {
		super(true);
	}

	@Override
	public String getAuthor() {
		return "KaptainWutax";
	}

	@Override
	public boolean getLocked() {
		return true;
	}

	@Override
	public void setAuthor(String author) {
	}

	@Override
	public void setLocked(boolean locked) {
	}

}
