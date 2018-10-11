package testdomino;

public enum FitPossibilities {

	LEFT_LEFT, LEFT_RIGHT, RIGHT_LEFT, RIGHT_RIGHT;

	public boolean fitsRight() {
		if (this.equals(RIGHT_LEFT) || this.equals(RIGHT_RIGHT))
			return true;
		return false;
	}

	public boolean fitsLeft() {
		if (this.equals(LEFT_LEFT) || this.equals(LEFT_RIGHT))
			return true;
		return false;
	}

	public boolean rightSide() {
		if (this.equals(LEFT_RIGHT) || this.equals(RIGHT_RIGHT))
			return true;
		return false;
	}

	public boolean leftSide() {
		if (this.equals(LEFT_LEFT) || this.equals(RIGHT_LEFT))
			return true;
		return false;
	}
}
