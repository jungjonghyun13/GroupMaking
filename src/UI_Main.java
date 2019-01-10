
import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.border.LineBorder;

public class UI_Main extends JFrame {
	ArrayList<Group> Groups = new ArrayList<Group>();
	ArrayList<Checkbox> AccInfoTags = new ArrayList<Checkbox>();
	JComboBox CombiBox = new JComboBox();

	FileHandler fh = new FileHandler();
	TaskManager tm = new TaskManager();
	HashMap<String, Set<String>> nameList;
	ArrayList<String> recordList = new ArrayList<String>();

	// �� ���� ���� ���⼭�� 7�ι� 10��, 4�ι� 2���� ��
	ArrayList<Team> teams = new ArrayList<>();
	
	//���� ���� ����
	JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	String inputFile = "";
	String outputFile = "";
	Container mainPane = getContentPane();
	JPanel AccInfoPanel = new JPanel();
	JTextField TagtextField = new JTextField();

	int isScrollOpPanel = 0;
	
	int teamnum = 1;
	void Init() {
	
	}
	public UI_Main() {
		
		init();
	    
		// ������ ���� ��ü
		this.setTitle("�׷� �����");
		// ������ �ݱ� �̺�Ʈ
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPane.setLayout(null);
		mainPane.setBackground(Color.WHITE); // ������ ������
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 384, 21);
		getContentPane().add(menuBar);
		
		JMenu FIleMenu = new JMenu("����");
		menuBar.add(FIleMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("���� ����");

		MenuActionListener listener = new MenuActionListener(); 
		mntmNewMenuItem.addActionListener(listener);
		FIleMenu.add(mntmNewMenuItem);
		
		JPanel OpPanel = new JPanel(); // OP = Option
		OpPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		OpPanel.setBackground(new Color(255, 255, 255));
		OpPanel.setSize(350, 203);
		OpPanel.setLocation(10, 68);
		
		
		JPanel groupPanel = new JPanel();
		groupPanel.setBorder(new LineBorder(new Color(192, 192, 192), 2));
		groupPanel.setBackground(new Color(255, 255, 255));
		addGroup(groupPanel);

		JScrollPane scrollPane = new JScrollPane(OpPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		mainPane.add(scrollPane);

		JButton groupPlus = new JButton("+");
		groupPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel groupPanel = new JPanel();
				groupPanel.setBorder(new LineBorder(new Color(192, 192, 192), 2));
				groupPanel.setBackground(new Color(255, 255, 255));
				addGroup(groupPanel);
				groupPanel.remove(groupPlus);
				groupPanel.add(groupPlus);
				OpPanel.add(groupPanel);

	
				
				
				OpPanel.revalidate();
				OpPanel.repaint();
			}
		});
		groupPanel.add(groupPlus);
		OpPanel.add(groupPanel);
		mainPane.add(OpPanel);

		// Tag Test Case ����
//		if (!inputFile.equals("")) {
//			getUserInfoTags();
//		}

		printAccInfoTags();

		JLabel label = new JLabel("���� ����");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("����", Font.BOLD, 15));
		label.setBackground(new Color(180, 199, 231));
		label.setBounds(10, 47, 94, 23);
		label.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		label.setOpaque(true);
		getContentPane().add(label);
		

		JLabel label_2 = new JLabel("���� ������ �Է� TAG");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBackground(new Color(180, 199, 231));
		label_2.setFont(new Font("����", Font.BOLD, 15));
		label_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		label_2.setBounds(10, 300, 188, 23);
		label_2.setOpaque(true);
		getContentPane().add(label_2);
		for (int i = 0; i < 8; i++) {
			CombiBox.addItem(Integer.toString(i + 1));
		}

		// TODO
		JButton writeBtn = new JButton("���� ����ϱ�");
		writeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("������ ���� "+CombiBox.getSelectedItem()+" ��");
				System.out.println("���� ����ϱ�");

				//����� ����� ���� ���� �� ���� ����
				
//				recordList.add("1/14");
//				recordList.add("1/21");

				for (int i = 0; i < AccInfoTags.size(); i++) {
					if(AccInfoTags.get(i).getState()) {
						System.out.println(AccInfoTags.get(i).getLabel());
						recordList.add(AccInfoTags.get(i).getLabel());
					}
				}
				process();


			}
		});
		writeBtn.setBounds(242, 402, 118, 34);
		getContentPane().add(writeBtn);
		
		AccInfoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		AccInfoPanel.setBackground(Color.WHITE);
		AccInfoPanel.setBounds(10, 321, 350, 71);
		getContentPane().add(AccInfoPanel);
		
		TagtextField.setBounds(123, 402, 107, 27);
		getContentPane().add(TagtextField);
		TagtextField.setColumns(10);
		
		JLabel label_1 = new JLabel("\uCD94\uAC00 \uD0DC\uADF8 \uBA85");
		label_1.setOpaque(true);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("����", Font.BOLD, 15));
		label_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		label_1.setBackground(new Color(180, 199, 231));
		label_1.setBounds(10, 402, 107, 23);
		getContentPane().add(label_1);

		this.setSize(400, 650); // ������ ������
		this.setLocation(100, 100); // ������ġ
		this.setVisible(true); // ������ ǥ������
	}
	void printAccInfoTags() {
		AccInfoPanel.removeAll();
		AccInfoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		AccInfoPanel.setBackground(Color.WHITE);
		AccInfoPanel.setBounds(10, 321, 350, 71);
		
		for (int i = 0; i < AccInfoTags.size(); i++) {
			AccInfoTags.get(i).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					System.out.println("\n���õ� Tag ��� ");
					for (int i = 0; i < AccInfoTags.size(); i++) {
						System.out.println(AccInfoTags.get(i).getState());
						//AccInfoTags.get(i).setEnabled(false);
					}
				}
			});
			AccInfoPanel.add(AccInfoTags.get(i));
		}
		mainPane.add(AccInfoPanel);
		AccInfoPanel.updateUI();
	}
	void getUserInfoTags() {

		
		try {
			int mainTagIndex = 0;
			FileInputStream fis = new FileInputStream(inputFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0); // �ش� ���������� ��Ʈ(Sheet) ��
			int rows = sheet.getPhysicalNumberOfRows(); // �ش� ��Ʈ�� ���� ����
			XSSFRow row = sheet.getRow(0); // �� ���� �о�´�
			if (row != null) {
				int cells = row.getPhysicalNumberOfCells();
				// ù �� ���� ��
				for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
					String currentTag = row.getCell(columnIndex).toString();
					System.out.println(currentTag);
					AccInfoTags.add(new Checkbox(currentTag));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void addGroup(JPanel OpPanel) {
		Group group = new Group();
		OpPanel.add(new JLabel("�� �� "));
		for (int i = 0; i < 20; i++) {
			group.personNum.addItem(Integer.toString(i + 1));
		}
		OpPanel.add(group.personNum);
		OpPanel.add(new JLabel("��      "));

		OpPanel.add(new JLabel("�� "));
		for (int i = 0; i < 20; i++) {
			group.groupNum.addItem(Integer.toString(i + 1));
		}
		OpPanel.add(group.groupNum);
		OpPanel.add(new JLabel("��     "));
		Groups.add(group);
	}
	void init() {

		//chooser.setCurrentDirectory(new File("/")); // ���� ��� ���丮�� ����
	    chooser.setAcceptAllFileFilterUsed(true);   // Fileter ��� ���� ���� 
	    chooser.setDialogTitle("���� �ҷ�����"); // â�� ����
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // ���� ���� ���

	    //FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary File", "cd11"); // filter Ȯ���� �߰�
	    //chooser.setFileFilter(filter); // ���� ���͸� �߰�
	    
	}
	void process() {
		/*
		 * TODO
		 * 1. ���� 
		 * 
		 */
		
		// �������� �о����, � �������� ���� ���� �� ���� ����, ���⼭�� �̸����� ����
		nameList = fh.openFile(inputFile,"�̸�",recordList);
		
		// ���� �����ڷ� �ο����� �� �̸��� �־��ָ� ��
		
		//7, 10
		//4, 2
		for (int i = 0; i < Groups.size(); i++) {
			setCombiConfiguration(
					Integer.parseInt((String)Groups.get(i).personNum.getSelectedItem()),
					Integer.parseInt((String)Groups.get(i).groupNum.getSelectedItem()));

		}
		
		//�о�� ���� ���Ͽ� ����� ����� �� ���� �ο��� ���� 
		tm.setTeam(teams, nameList);
		
		
		//�� �ο��鿡 ���� ��Ī��Ŵ
		HashMap<String, String> teamInfo = tm.teamForHuman(teams);
		

		
		getSaveFile();

		//��������, �������, �� ������, ��Ī��ų �±��̸�, ���� �߰��� �±� �̸����� �� �θ� ����
		fh.writeFile(inputFile, outputFile, teamInfo,"�̸�",	TagtextField.getText());
	}
	
	void getSaveFile() {

		int returnVal = chooser.showSaveDialog(null); // ����� â ����
		if (returnVal == JFileChooser.APPROVE_OPTION) { // ���⸦ Ŭ��
			outputFile = chooser.getSelectedFile().toString();
		} else if (returnVal == JFileChooser.CANCEL_OPTION) { // ��Ҹ� Ŭ��
			System.out.println("cancel");
			outputFile = "";
		}
		System.out.println(outputFile);
	}
	void setCombiConfiguration(int personNum, int groupNum) {
		for(int i = 0; i < groupNum; i++){
			String teammname = Integer.toString(teamnum); 
			teamnum += 1;
			Team team = new Team(personNum,teammname);
			teams.add(team);
		}
	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnVal = chooser.showOpenDialog(null); // ����� â ����

			if (returnVal == JFileChooser.APPROVE_OPTION) { // ���⸦ Ŭ��
				inputFile = chooser.getSelectedFile().toString();
			} else if (returnVal == JFileChooser.CANCEL_OPTION) { // ��Ҹ� Ŭ��
				System.out.println("cancel");
				inputFile = "";
			}
			System.out.println(inputFile);
			getUserInfoTags();
			printAccInfoTags();
		}
	}
}
