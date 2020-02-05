package kaptainwutax.seedcracker.finder.profile;

public class CustomProfile extends FinderProfile {

	public CustomProfile() {
		super(false);
		this.author = "";
		this.locked = false;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
