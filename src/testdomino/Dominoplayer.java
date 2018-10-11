package testdomino;

import java.util.ArrayList;
import java.util.List;

public class Dominoplayer {

	private List<Domino> dominos = new ArrayList<>();

	/**
	 * 
	 */
	public Dominoplayer() {

	}

	public void addDomino(Domino d) {
		dominos.add(d);

	}

	public List<Domino> getDominos() {

		return dominos;
	}

}
