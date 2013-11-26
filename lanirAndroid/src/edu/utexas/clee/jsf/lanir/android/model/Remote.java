package edu.utexas.clee.jsf.lanir.android.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Remote implements Parcelable {

	private String name;
	private List<String> commands = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeStringList(commands);
	}

	public Remote(Parcel in) {
		readFromParcel(in);
	}

	public Remote() {
		// TODO Auto-generated constructor stub
	}

	private void readFromParcel(Parcel in) {
		this.name = in.readString();
		in.readStringList(commands);
	}

	public static final Parcelable.Creator<Remote> CREATOR = new Parcelable.Creator<Remote>() {

		public Remote[] newArray(int size) {
			return new Remote[size];
		}

		@Override
		public Remote createFromParcel(Parcel source) {
			return new Remote(source);
		}

	};
}
