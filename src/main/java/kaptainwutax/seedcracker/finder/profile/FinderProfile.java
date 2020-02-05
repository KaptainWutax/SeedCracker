package kaptainwutax.seedcracker.finder.profile;

import kaptainwutax.seedcracker.finder.FinderConfig;

public abstract class FinderProfile extends FinderConfig {

	public FinderProfile(boolean defaultState) {
		super(defaultState);
	}

	public abstract String getAuthor();
	public abstract boolean getLocked();

	public abstract void setAuthor(String author);
	public abstract void setLocked(boolean locked);

}
