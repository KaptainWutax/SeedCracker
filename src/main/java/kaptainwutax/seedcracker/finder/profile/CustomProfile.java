package kaptainwutax.seedcracker.finder.profile;

public class CustomProfile extends FinderProfile {

	public CustomProfile(String author) {
		super(true);
		this.author = author;
		this.locked = false;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
