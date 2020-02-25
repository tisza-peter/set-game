package set_game;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class GameDesktop {

	int DEFAULT_VISIBLE_CARD_NUMBER = 12;
	int visible_cards_number;

	int grid_panel_row;
	int grid_panel_col;
	JPanel headerPanel;
	JPanel middlePanel;
	JPanel footerPanel;
	Label statusLabel;
	Label TiosNumberLabel;
	JFrame mainFrame = new JFrame("Set Game by Tison (v 1.2)");
	SurfacePane TheSurfacePane = new SurfacePane();
	List<SETCard> TestedCards;
	List<SETCard> SelectedCards;
	List<SETCard> VisibleCards;
	List<SETCard> AllCards;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameDesktop();
	}

	public GameDesktop() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainFrame.setLayout(new BorderLayout());
				mainFrame.setResizable(false);
				AllCards = new ArrayList<SETCard>();
				VisibleCards = new ArrayList<SETCard>();
				SelectedCards = new ArrayList<SETCard>();
				TestedCards = new ArrayList<SETCard>();

				// System.out.println("grid_panel_row : " + grid_panel_row);
				// System.out.println("grid_panel_col : " + grid_panel_col);

				middlePanel = new JPanel();
				headerPanel = new JPanel();
				footerPanel = new JPanel();
				footerPanel.setLayout(new BorderLayout());

				TiosNumberLabel = new Label();
				TiosNumberLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
				TiosNumberLabel.setAlignment(Label.CENTER);
				TiosNumberLabel.setBackground(Color.lightGray);
				TiosNumberLabel.setForeground(Color.DARK_GRAY);
				footerPanel.add(TiosNumberLabel, BorderLayout.NORTH);

				statusLabel = new Label();
				statusLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
				statusLabel.setText("Válasszon ki 3 kártyát!");
				statusLabel.setAlignment(Label.CENTER);
				statusLabel.setBackground(Color.lightGray);
				statusLabel.setForeground(Color.DARK_GRAY);
				footerPanel.add(statusLabel, BorderLayout.SOUTH);

				Button GoButton = new Button("SET");
				GoButton.setFont(new Font("Helvetica", Font.BOLD, 18));
				GoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SelectedCards.clear();
						for (SETCard OneSetCard : AllCards) {
							if (OneSetCard.isSelected())
								SelectedCards.add(OneSetCard);
						}
						if (SelectedCards.size() != 3) {
							statusLabel.setText("Válasszon ki 3 kártyát!");
						} else {
							statusLabel.setText("Vizsgálat indítása!");
							if (CardTrioAnalysis(SelectedCards)) {
								statusLabel.setText("Helyes kiválasztás!");
								DeleteCards(SelectedCards);
							} else {
								statusLabel.setText("Téves kiválasztás!");
								for (SETCard SelectedCard : SelectedCards) {
									SelectedCard.SetSelected(false);
								}
							}
						}
					}
				});

				Button StartButton = new Button("Új játék indítása");
				StartButton.setFont(new Font("Helvetica", Font.BOLD, 18));
				StartButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						visible_cards_number = DEFAULT_VISIBLE_CARD_NUMBER;
						GenerateAllCards();
						ViewCards();

					}
				});

				Button AddCardButton = new Button("3 új kártya megjelenítése");
				AddCardButton.setFont(new Font("Helvetica", Font.BOLD, 18));
				AddCardButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (VisibleCards.size() == AllCards.size()) {
							JOptionPane.showMessageDialog(null,
									"Elfogytak a kártyák!", "Figyelmeztetés!",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							visible_cards_number += 3;
							ViewCards();
						}
					}
				});

				Button HelpButton = new Button("?");
				HelpButton.setFont(new Font("Helvetica", Font.BOLD, 18));
				HelpButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String leiras = "";
						leiras += "JÁTÉKSZABÁLY!" + System.lineSeparator();
						leiras += "A játék célja, hogy 3 kártyából álló SET-eket találjunk meg a megjelenített kártyákból."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "Minden kártyának 4 tulajdonsága van, amik a következõk:"
								+ System.lineSeparator();
						leiras += "FORMA: háromszög, négyszög, kör"
								+ System.lineSeparator();
						leiras += "SZÍN: piros, zöld, kék"
								+ System.lineSeparator();
						leiras += "SZÁM: 1, 2 vagy 3 forma"
								+ System.lineSeparator();
						leiras += "TARTALOM: tömör, csíkos, üres"
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "Egy SET 3 kártyából áll, amiben minden jellemzõt külön megvizsgálva,"
								+ System.lineSeparator();
						leiras += "   azoknak vagy azonosnak vagy minden kártyán különbözõnek kell lennie."
								+ System.lineSeparator();
						leiras += "Ennek a szabálynak MINDEN tulajdonság esetében meg kell felelnie a kiválasztott három lapnak."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "A könnyebb megértésért a szabály megfogalmazható fordítva is:"
								+ System.lineSeparator();
						leiras += "Ha a lapokon bármely tulajdonságot vizsgálva 2 azonos és 1 különbözõ típus látható,akkor az nem SET."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "JÁTÉKPROGRAM HASZNÁLATA!"
								+ System.lineSeparator();
						leiras += "Kattintson az 'új játék indítása' gombra."
								+ System.lineSeparator();
						leiras += "Egérkattintással jelöljön ki 3 kártyát."
								+ System.lineSeparator();
						leiras += "A vizsgálathoz nyomja meg a 'SET' gombot."
								+ System.lineSeparator();
						leiras += "Amennyiben az alkalmazás szerint a lehetséges találatok száma nulla,"
								+ System.lineSeparator();
						leiras += "   akkor kattintson a 'három új kártya megjelenítése' gombra."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "Jó szórakozást kívánok!"
								+ System.lineSeparator();

						/*
						 * Minden alkalommal, amikor valaki
						 * SET-et talál, akkor a kiválasztott három lapot
						 * ellenõrzi a többi játékos is.
						 * 
						 * Ha valóban SET, akkor a
						 * játékos megkapja a 3 lapot, amely 1 pontot ér, az
						 * osztó pedig 3 újabb lapot oszt a felvettek helyére.
						 * 
						 * Mielõtt felvennénk a kártyákat, SET-et kell
						 * bemondanunk.
						 * 
						 * Hogyan is zajlik a játék: Nincsenek
						 * körök, az elsõ játékos, aki SET-et kiált, veszi át az
						 * irányítást. Miután SET- et mondott, senki más nem
						 * vehet le kártyákat, csak õ, egész addig amíg a
						 * megtalált SET lapjait fel nem veszi. A SET-et a
						 * bemondást követõ pár másodpercen belül azonnal fel
						 * kell venni.
						 * 
						 * Ha a bemondónak mégsincs SET- je, vagy a
						 * kiválasztott SET hibás, akkor veszít 1 pontot és a
						 * hibásan felvett kártyák visszakerülnek a helyükre.
						 * 
						 * Amennyiben a kivett lapok valóban SET-et alkotnak,
						 * akkor a SET levétele után újabb lapokkal egészítjük
						 * ki 12-re a felcsapott lapokat.
						 * 
						 * Ezután minden játékos
						 * azonnal keresi az új SET-eket a felcsapott lapok
						 * között. Speciális eset: Ha mindenki egyetért, hogy
						 * nincs már levehetõ SET a fent lévõ 12 lapban, akkor 3
						 * plusz lap kerül az asztalra. Amennyiben a 15 lapban
						 * sikerül SET-et találni, akkor nem kerül pótlásra a 3
						 * lap, ezáltal újra 12 lap lesz lent az asztalon.
						 * Megjegyzés: egy 12 lapos leosztásban 33 az 1-hez az
						 * esélye, hogy van benne SET, egy 15 laposban pedig
						 * 2500 az 1-hez. A játék addig tart, amíg elfogy a
						 * pakli, és az asztalon lévõ lapokból már nem lehet
						 * SET-et létrehozni. A játék végén 2 3 elõfordulhat,
						 * hogy maradnak lapok az asztalon, melyekbõl már nem
						 * lehet SET-et létrehozni. Ekkor megszámoljuk, kinek
						 * hány SET-je van. Minden SET 1 pontot ér. A legtöbb
						 * pontot (SET-et) gyûjtõ játékos nyer. Ha hosszabb
						 * játékot szeretnénk, akkor miután pontoztunk, az osztó
						 * szerepet óramutató járásnak megfelelõen tovább adjuk
						 * és egész addig játszunk, míg mindegyik játékosra
						 * rákerül az osztó szerep. A játékos a legnagyobb
						 * összesített pontszámmal, nyer. Egyszemélyes játékmód:
						 * Kezdetnek ugyanúgy 12 lapot csapunk fel, mint több
						 * játékos esetén. Minden megtalált SET egy pontot ér.
						 * Ha nem találunk SET-et, akkor 3 új lapot teszünk ki,
						 * de egy SET értékét levonunk a végsõ pontszámból. Hogy
						 * megnyerjük a játékot, az utolsó 12 lapban is kell
						 * SET-et találnunk.
						 */
						JOptionPane.showMessageDialog(null, leiras,
								"Használati útmutató!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				});

				headerPanel.add(StartButton);
				headerPanel.add(GoButton);
				headerPanel.add(AddCardButton);
				headerPanel.add(HelpButton);

				TheSurfacePane.add(headerPanel, BorderLayout.NORTH);
				TheSurfacePane.add(middlePanel, BorderLayout.CENTER);
				TheSurfacePane.add(footerPanel, BorderLayout.SOUTH);

				mainFrame.add(TheSurfacePane);
				mainFrame.pack();
				mainFrame.setLocationRelativeTo(null);

				mainFrame.setVisible(true);

			}
		});
	}

	protected int DetectTrios() {
		int NumberOfTrios = 0;
		System.out.println("Lehetséges találatok vizsgálata:");
		for (int i = 0; i < VisibleCards.size(); i++) {
			for (int j = 0; j < VisibleCards.size(); j++) {
				for (int k = 0; k < VisibleCards.size(); k++) {
					if (i < j && j < k) {
						TestedCards.clear();
						TestedCards.add(VisibleCards.get(i));
						TestedCards.add(VisibleCards.get(j));
						TestedCards.add(VisibleCards.get(k));
						if (CardTrioAnalysis(TestedCards)) {
							NumberOfTrios++;
							System.out.println("Lehetséges találatok:"
									+ Integer.toString(i) + ","
									+ Integer.toString(j) + ","
									+ Integer.toString(k));
						}
					}
				}
			}
		}
		System.out.println("Lehetséges találatok darabszáma:"
				+ Integer.toString(NumberOfTrios));
		return NumberOfTrios;

	}

	protected void GenerateAllCards() {
		AllCards.clear();
		for (int i = 0; i < 81; i++) {
			SETCard OneCard = new SETCard();
			OneCard.SetId(i);
			OneCard.SetItemNumber((int) (i / Math.pow(3, 0)) % 3);
			OneCard.SetColorNumber((int) (i / Math.pow(3, 1)) % 3);
			OneCard.SetShapeNumber((int) (i / Math.pow(3, 2)) % 3);
			OneCard.SetFillNumber((int) (i / Math.pow(3, 3)) % 3);
			AllCards.add(OneCard);
		}
		Collections.shuffle(AllCards);
	}

	public class SurfacePane extends JPanel {

		public SurfacePane() {
			setLayout(new BorderLayout());
			// setBackground(Color.YELLOW);

			setBorder(new EmptyBorder(10, 10, 10, 10));

			// add(new InnerPane());

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
				}

			});
		}

	}

	boolean isPrime(int n) {
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	public void ViewCards() {
		GridCalculation();
		middlePanel.setLayout(new GridLayout(grid_panel_row, grid_panel_col));
		middlePanel.removeAll();
		VisibleCards.clear();
		System.out.println("AllCards.size() : " + AllCards.size());
		for (SETCard OneSetCard : AllCards) {
			if (middlePanel.getComponentCount() == grid_panel_row
					* grid_panel_col)
				break;
			middlePanel.add(OneSetCard.GetCardView());
			VisibleCards.add(OneSetCard);
		}
		middlePanel.updateUI();

		mainFrame.pack();

		TiosNumberLabel.setText("Lehetséges találatok darabszáma:"
				+ Integer.toString(DetectTrios()));

	}

	public void GridCalculation() {
		grid_panel_row = (int) Math.sqrt(visible_cards_number);

		while (grid_panel_row > 0) {
			if (visible_cards_number % grid_panel_row == 0) {
				grid_panel_col = visible_cards_number / grid_panel_row;
				break;
			}
			grid_panel_row--;
		}
		System.out.println("grid_panel_row : " + grid_panel_row);
		System.out.println("grid_panel_col : " + grid_panel_col);

	}

	private void DeleteCards(List<SETCard> DelectedCards) {
		for (SETCard OneDeletedCard : DelectedCards) {
			AllCards.remove(OneDeletedCard);
		}
		visible_cards_number -= 3;
		ViewCards();
	}

	private boolean CardTrioAnalysis(List<SETCard> SelectedCards) {
		boolean ColorResult;
		boolean ItemResult;
		boolean ShapeResult;
		boolean FillResult;

		switch (SelectedCards.get(0).GetItemNumber()
				* SelectedCards.get(1).GetItemNumber()
				* SelectedCards.get(2).GetItemNumber()) {
		case 3 * 4 * 5:
		case 3 * 3 * 3:
		case 4 * 4 * 4:
		case 5 * 5 * 5:
			ItemResult = true;
			break;
		default:
			ItemResult = false;
		}

		switch (SelectedCards.get(0).GetShapeNumber()
				* SelectedCards.get(1).GetShapeNumber()
				* SelectedCards.get(2).GetShapeNumber()) {
		case 3 * 4 * 5:
		case 3 * 3 * 3:
		case 4 * 4 * 4:
		case 5 * 5 * 5:
			ShapeResult = true;
			break;
		default:
			ShapeResult = false;
		}

		switch (SelectedCards.get(0).GetFillNumber()
				* SelectedCards.get(1).GetFillNumber()
				* SelectedCards.get(2).GetFillNumber()) {
		case 3 * 4 * 5:
		case 3 * 3 * 3:
		case 4 * 4 * 4:
		case 5 * 5 * 5:
			FillResult = true;
			break;
		default:
			FillResult = false;
		}

		switch (SelectedCards.get(0).GetColorNumber()
				* SelectedCards.get(1).GetColorNumber()
				* SelectedCards.get(2).GetColorNumber()) {
		case 3 * 4 * 5:
		case 3 * 3 * 3:
		case 4 * 4 * 4:
		case 5 * 5 * 5:
			ColorResult = true;
			break;
		default:
			ColorResult = false;
		}

		return ColorResult && ItemResult && ShapeResult && FillResult;

	}

}
