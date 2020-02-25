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
				statusLabel.setText("V�lasszon ki 3 k�rty�t!");
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
							statusLabel.setText("V�lasszon ki 3 k�rty�t!");
						} else {
							statusLabel.setText("Vizsg�lat ind�t�sa!");
							if (CardTrioAnalysis(SelectedCards)) {
								statusLabel.setText("Helyes kiv�laszt�s!");
								DeleteCards(SelectedCards);
							} else {
								statusLabel.setText("T�ves kiv�laszt�s!");
								for (SETCard SelectedCard : SelectedCards) {
									SelectedCard.SetSelected(false);
								}
							}
						}
					}
				});

				Button StartButton = new Button("�j j�t�k ind�t�sa");
				StartButton.setFont(new Font("Helvetica", Font.BOLD, 18));
				StartButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						visible_cards_number = DEFAULT_VISIBLE_CARD_NUMBER;
						GenerateAllCards();
						ViewCards();

					}
				});

				Button AddCardButton = new Button("3 �j k�rtya megjelen�t�se");
				AddCardButton.setFont(new Font("Helvetica", Font.BOLD, 18));
				AddCardButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (VisibleCards.size() == AllCards.size()) {
							JOptionPane.showMessageDialog(null,
									"Elfogytak a k�rty�k!", "Figyelmeztet�s!",
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
						leiras += "J�T�KSZAB�LY!" + System.lineSeparator();
						leiras += "A j�t�k c�lja, hogy 3 k�rty�b�l �ll� SET-eket tal�ljunk meg a megjelen�tett k�rty�kb�l."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "Minden k�rty�nak 4 tulajdons�ga van, amik a k�vetkez�k:"
								+ System.lineSeparator();
						leiras += "FORMA: h�romsz�g, n�gysz�g, k�r"
								+ System.lineSeparator();
						leiras += "SZ�N: piros, z�ld, k�k"
								+ System.lineSeparator();
						leiras += "SZ�M: 1, 2 vagy 3 forma"
								+ System.lineSeparator();
						leiras += "TARTALOM: t�m�r, cs�kos, �res"
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "Egy SET 3 k�rty�b�l �ll, amiben minden jellemz�t k�l�n megvizsg�lva,"
								+ System.lineSeparator();
						leiras += "   azoknak vagy azonosnak vagy minden k�rty�n k�l�nb�z�nek kell lennie."
								+ System.lineSeparator();
						leiras += "Ennek a szab�lynak MINDEN tulajdons�g eset�ben meg kell felelnie a kiv�lasztott h�rom lapnak."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "A k�nnyebb meg�rt�s�rt a szab�ly megfogalmazhat� ford�tva is:"
								+ System.lineSeparator();
						leiras += "Ha a lapokon b�rmely tulajdons�got vizsg�lva 2 azonos �s 1 k�l�nb�z� t�pus l�that�,akkor az nem SET."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "J�T�KPROGRAM HASZN�LATA!"
								+ System.lineSeparator();
						leiras += "Kattintson az '�j j�t�k ind�t�sa' gombra."
								+ System.lineSeparator();
						leiras += "Eg�rkattint�ssal jel�lj�n ki 3 k�rty�t."
								+ System.lineSeparator();
						leiras += "A vizsg�lathoz nyomja meg a 'SET' gombot."
								+ System.lineSeparator();
						leiras += "Amennyiben az alkalmaz�s szerint a lehets�ges tal�latok sz�ma nulla,"
								+ System.lineSeparator();
						leiras += "   akkor kattintson a 'h�rom �j k�rtya megjelen�t�se' gombra."
								+ System.lineSeparator();
						leiras += "" + System.lineSeparator();
						leiras += "J� sz�rakoz�st k�v�nok!"
								+ System.lineSeparator();

						/*
						 * Minden alkalommal, amikor valaki
						 * SET-et tal�l, akkor a kiv�lasztott h�rom lapot
						 * ellen�rzi a t�bbi j�t�kos is.
						 * 
						 * Ha val�ban SET, akkor a
						 * j�t�kos megkapja a 3 lapot, amely 1 pontot �r, az
						 * oszt� pedig 3 �jabb lapot oszt a felvettek hely�re.
						 * 
						 * Miel�tt felvenn�nk a k�rty�kat, SET-et kell
						 * bemondanunk.
						 * 
						 * Hogyan is zajlik a j�t�k: Nincsenek
						 * k�r�k, az els� j�t�kos, aki SET-et ki�lt, veszi �t az
						 * ir�ny�t�st. Miut�n SET- et mondott, senki m�s nem
						 * vehet le k�rty�kat, csak �, eg�sz addig am�g a
						 * megtal�lt SET lapjait fel nem veszi. A SET-et a
						 * bemond�st k�vet� p�r m�sodpercen bel�l azonnal fel
						 * kell venni.
						 * 
						 * Ha a bemond�nak m�gsincs SET- je, vagy a
						 * kiv�lasztott SET hib�s, akkor vesz�t 1 pontot �s a
						 * hib�san felvett k�rty�k visszaker�lnek a hely�kre.
						 * 
						 * Amennyiben a kivett lapok val�ban SET-et alkotnak,
						 * akkor a SET lev�tele ut�n �jabb lapokkal eg�sz�tj�k
						 * ki 12-re a felcsapott lapokat.
						 * 
						 * Ezut�n minden j�t�kos
						 * azonnal keresi az �j SET-eket a felcsapott lapok
						 * k�z�tt. Speci�lis eset: Ha mindenki egyet�rt, hogy
						 * nincs m�r levehet� SET a fent l�v� 12 lapban, akkor 3
						 * plusz lap ker�l az asztalra. Amennyiben a 15 lapban
						 * siker�l SET-et tal�lni, akkor nem ker�l p�tl�sra a 3
						 * lap, ez�ltal �jra 12 lap lesz lent az asztalon.
						 * Megjegyz�s: egy 12 lapos leoszt�sban 33 az 1-hez az
						 * es�lye, hogy van benne SET, egy 15 laposban pedig
						 * 2500 az 1-hez. A j�t�k addig tart, am�g elfogy a
						 * pakli, �s az asztalon l�v� lapokb�l m�r nem lehet
						 * SET-et l�trehozni. A j�t�k v�g�n 2 3 el�fordulhat,
						 * hogy maradnak lapok az asztalon, melyekb�l m�r nem
						 * lehet SET-et l�trehozni. Ekkor megsz�moljuk, kinek
						 * h�ny SET-je van. Minden SET 1 pontot �r. A legt�bb
						 * pontot (SET-et) gy�jt� j�t�kos nyer. Ha hosszabb
						 * j�t�kot szeretn�nk, akkor miut�n pontoztunk, az oszt�
						 * szerepet �ramutat� j�r�snak megfelel�en tov�bb adjuk
						 * �s eg�sz addig j�tszunk, m�g mindegyik j�t�kosra
						 * r�ker�l az oszt� szerep. A j�t�kos a legnagyobb
						 * �sszes�tett pontsz�mmal, nyer. Egyszem�lyes j�t�km�d:
						 * Kezdetnek ugyan�gy 12 lapot csapunk fel, mint t�bb
						 * j�t�kos eset�n. Minden megtal�lt SET egy pontot �r.
						 * Ha nem tal�lunk SET-et, akkor 3 �j lapot tesz�nk ki,
						 * de egy SET �rt�k�t levonunk a v�gs� pontsz�mb�l. Hogy
						 * megnyerj�k a j�t�kot, az utols� 12 lapban is kell
						 * SET-et tal�lnunk.
						 */
						JOptionPane.showMessageDialog(null, leiras,
								"Haszn�lati �tmutat�!",
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
		System.out.println("Lehets�ges tal�latok vizsg�lata:");
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
							System.out.println("Lehets�ges tal�latok:"
									+ Integer.toString(i) + ","
									+ Integer.toString(j) + ","
									+ Integer.toString(k));
						}
					}
				}
			}
		}
		System.out.println("Lehets�ges tal�latok darabsz�ma:"
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

		TiosNumberLabel.setText("Lehets�ges tal�latok darabsz�ma:"
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
