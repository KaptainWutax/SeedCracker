package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedcracker.util.Log;

public class ProgressListener {

	protected float progress;

	public ProgressListener() {
		this(0.0F);
	}

	public ProgressListener(float progress) {
		this.progress = progress;
	}

	public synchronized void addPercent(float percent, boolean debug) {
		if(debug) {
			Log.debug("Progress: " + this.progress +  "%");
		}

		this.progress += percent;
	}

}
