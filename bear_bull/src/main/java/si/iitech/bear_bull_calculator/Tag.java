package si.iitech.bear_bull_calculator;

import java.io.Serializable;

public class Tag implements Serializable {

	private static final long serialVersionUID = -2827607450897226484L;
	private String tag;
	private String color;

	public Tag(String tag, String color) {
		this.color = color;
		this.tag = tag;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getColor() {
		return color;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return tag + ";" + color;
	}
}
