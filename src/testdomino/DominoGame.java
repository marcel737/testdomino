package testdomino;

import java.util.ArrayList;
import java.util.List;

public class DominoGame {
	private static final int COUNT_OF_DOMINOS_FOR_PLAYER = 5;
	private List<Domino> dominoList = null;
	private Domino fieldDomino = null;
	private Dominoplayer player1;
	private Dominoplayer player2;

	String playField = "";
	private int doDebug = 1;

	/**
	 * 
	 */
	public DominoGame() {
	}

	public void init() {
		player1 = new Dominoplayer();
		player2 = new Dominoplayer();
		dominoList = new DominoPool().provideShuffledDominoHeap();
		boolean firstPlayer = false;
		for (Domino d : dominoList) {
			if (firstPlayer) {
				player1.addDomino(d);
				firstPlayer = false;
			} else {
				player2.addDomino(d);
				firstPlayer = true;
			}
			if (player1.getDominos().size() == COUNT_OF_DOMINOS_FOR_PLAYER
					&& player2.getDominos().size() == COUNT_OF_DOMINOS_FOR_PLAYER) {
				break;
			}
		}
		dominoList.removeAll(player1.getDominos());
		dominoList.removeAll(player2.getDominos());
		fieldDomino = dominoList.remove(0);
		playField = "|" + fieldDomino.getLeft() + ":" + fieldDomino.getRight() + "|";
	}

	public List<FitPossibilities> isDominoFitting(Domino d) {
		List<FitPossibilities> result = new ArrayList<>();
		if (fieldDomino.getLeft() == d.getLeft())
			result.add(FitPossibilities.LEFT_LEFT);
		if (fieldDomino.getLeft() == d.getRight())
			result.add(FitPossibilities.LEFT_RIGHT);
		if (fieldDomino.getRight() == d.getLeft())
			result.add(FitPossibilities.RIGHT_LEFT);
		if (fieldDomino.getRight() == d.getRight())
			result.add(FitPossibilities.RIGHT_RIGHT);
		return result;
	}

	public Domino chooseTurn(String prompt, Dominoplayer p) {
		UserDialog dlg = new UserDialog();
		List<String> strings = new ArrayList<>();
		for (Domino d : p.getDominos()) {
			strings.add(d.toString() + "\n");
		}
		int reply = dlg.getUserInput(prompt, strings.toArray(new String[strings.size()]));

		return p.getDominos().remove(reply);
	}

	public boolean chooseSide() {
		UserDialog dlg = new UserDialog();
		List<String> strings = new ArrayList<>();
		strings.add("left \n");
		strings.add("right \n");
		int reply = dlg.getUserInput("Bitte wählen Sie die Seite aus, an der Sie setzen möchten \n ",
				strings.toArray(new String[strings.size()]));

		return reply == 1;
	}

	public boolean setDomino(Domino d) {
		List<FitPossibilities> dominoFitting = isDominoFitting(d);

		if (dominoFitting.size() < 1) {
			return false;
		}
		if (dominoFitting.size() > 1 && (fieldDomino.getLeft() == d.getLeft() && fieldDomino.getRight() == d.getRight())
				|| (fieldDomino.getRight() == d.getLeft() && fieldDomino.getLeft() == d.getRight())
				|| fieldDomino.getRight() == fieldDomino.getLeft()) {
			handleTurn(d, chooseSide());
			return true;
		}
		handleTurn(d, dominoFitting.get(0).fitsRight());
		return true;
	}

	private void handleTurn(Domino d, boolean placeRight) {
		if (placeRight) {
			if (d.getRight() == fieldDomino.getRight()) {
				fieldDomino.setRight(d.getLeft());
				playField = playField + "|" + d.getRight() + ":" + d.getLeft() + "|";
			} else {
				fieldDomino.setRight(d.getRight());
				playField = playField + "|" + d.getLeft() + ":" + d.getRight() + "|";
			}

		} else {
			if (d.getLeft() == fieldDomino.getLeft()) {
				fieldDomino.setLeft(d.getRight());
				playField = "|" + d.getRight() + ":" + d.getLeft() + "|" + playField;
			} else {
				fieldDomino.setLeft(d.getLeft());
				playField = "|" + d.getLeft() + ":" + d.getRight() + "|" + playField;
			}
		}
	}

	public void play() {
		init();
		Dominoplayer playerToPlay = player1;
		debugLog(1, "Aktueller Spieler: Spieler 1");
		debugLog(0, "hat Dominos: " + playerToPlay.getDominos());
		while (!isFinished()) {
			debugLog(2, "Spielfeld: " + playField);
			debugLog(1, "fieldDomino: " + fieldDomino);
			if (playerCannotTurn(playerToPlay)) {
				playerToPlay.addDomino(dominoList.remove(0));
				System.out.println(
						"Der aktuelle Spieler kann nicht ziehen und bekommt darum einen Domino aus dem Vorrat!");
				if (playerToPlay.equals(player1)) {
					playerToPlay = player2;
					debugLog(1, "Aktueller Spieler: Spieler 2");
					debugLog(0, "hat Dominos: " + playerToPlay.getDominos());
				} else {
					playerToPlay = player1;
					debugLog(1, "Aktueller Spieler: Spieler 1");
					debugLog(0, "hat Dominos: " + playerToPlay.getDominos());
				}
			} else {
				Domino turn = chooseTurn("Bitte wählen Sie den Domino-Stein aus, den Sie setzen möchten: \n ",
						playerToPlay);
				if (setDomino(turn)) {
					if (playerToPlay.equals(player1)) {
						playerToPlay = player2;
						debugLog(1, "Aktueller Spieler: Spieler 2");
						debugLog(0, "hat Dominos: " + playerToPlay.getDominos());
					} else {
						playerToPlay = player1;
						debugLog(1, "Aktueller Spieler: Spieler 1");
						debugLog(0, "hat Dominos: " + playerToPlay.getDominos());
					}
				} else {
					debugLog(2, "Dieser Zug ist nicht möglich. ");
					playerToPlay.addDomino(turn);
				}
			}
		}
		if (dominoList.isEmpty() && playerCannotTurn(player1) && playerCannotTurn(player2)) {
			debugLog(2, "keiner hat gewonnen");
			return;
		}
		if (player1.getDominos().isEmpty()) {
			debugLog(2, "Spieler 1 hat gewonnen");
			return;
		}
		if (player2.getDominos().isEmpty()) {
			debugLog(2, "Spieler 2 hat gewonnen");
			return;
		}
	}

	private void debugLog(int level, String string) {
		if (doDebug <= level)
			System.out.println(string);

	}

	private boolean isFinished() {
		return player1.getDominos().isEmpty() || player2.getDominos().isEmpty()
				|| (dominoList.isEmpty() && playerCannotTurn(player1) && playerCannotTurn(player2));
	}

	private boolean playerCannotTurn(Dominoplayer player) {
		boolean result = true;
		for (Domino d : player.getDominos()) {
			if (!isDominoFitting(d).isEmpty()) {
				result = false;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		DominoGame game = new DominoGame();
		game.play();
	}
}
