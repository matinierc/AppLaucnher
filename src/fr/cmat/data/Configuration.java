package fr.cmat.data;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
	private Long startTimeInMillis;
	private Long intervalTimeInMillis;
	private List<AppItem> appItems;
	private static Configuration instance;

	public Long getStartTimeInMillis() {
		return startTimeInMillis;
	}

	public void setStartTimeInMillis(Long startTimeInMillis) {
		this.startTimeInMillis = startTimeInMillis;
	}

	public Long getIntervalTimeInMillis() {
		return intervalTimeInMillis;
	}

	public void setIntervalTimeInMillis(Long intervalTimeInMillis) {
		this.intervalTimeInMillis = intervalTimeInMillis;
	}

	public List<AppItem> getAppItems() {
		return appItems;
	}

	public void setAppItems(List<AppItem> appItems) {
		this.appItems = appItems;
	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
			instance.appItems = new ArrayList<>();
		}
		return instance;
	}
}
