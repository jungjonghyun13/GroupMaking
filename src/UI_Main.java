
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

	// 팀 기준 설정 여기서는 7인방 10개, 4인방 2개로 함
	ArrayList<Team> teams = new ArrayList<>();
	
	//파일 선택 구성
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
	    
		// 프레임 생성 객체
		this.setTitle("그룹 만들기");
		// 프레임 닫기 이벤트
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPane.setLayout(null);
		mainPane.setBackground(Color.WHITE); // 프레임 바탕색
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 384, 21);
		getContentPane().add(menuBar);
		
		JMenu FIleMenu = new JMenu("파일");
		menuBar.add(FIleMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("파일 열기");

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

		// Tag Test Case 구성
//		if (!inputFile.equals("")) {
//			getUserInfoTags();
//		}

		printAccInfoTags();

		JLabel label = new JLabel("조별 구성");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("굴림", Font.BOLD, 15));
		label.setBackground(new Color(180, 199, 231));
		label.setBounds(10, 47, 94, 23);
		label.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		label.setOpaque(true);
		getContentPane().add(label);
		

		JLabel label_2 = new JLabel("누적 데이터 입력 TAG");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBackground(new Color(180, 199, 231));
		label_2.setFont(new Font("굴림", Font.BOLD, 15));
		label_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		label_2.setBounds(10, 300, 188, 23);
		label_2.setOpaque(true);
		getContentPane().add(label_2);
		for (int i = 0; i < 8; i++) {
			CombiBox.addItem(Integer.toString(i + 1));
		}

		// TODO
		JButton writeBtn = new JButton("파일 출력하기");
		writeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("조합의 수는 "+CombiBox.getSelectedItem()+" 개");
				System.out.println("파일 출력하기");

				//어떤것을 참고로 팀을 정할 것 인지 설정
				
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
		label_1.setFont(new Font("굴림", Font.BOLD, 15));
		label_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		label_1.setBackground(new Color(180, 199, 231));
		label_1.setBounds(10, 402, 107, 23);
		getContentPane().add(label_1);

		this.setSize(400, 650); // 프레임 사이즈
		this.setLocation(100, 100); // 프레위치
		this.setVisible(true); // 프레임 표시유무
	}
	void printAccInfoTags() {
		AccInfoPanel.removeAll();
		AccInfoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		AccInfoPanel.setBackground(Color.WHITE);
		AccInfoPanel.setBounds(10, 321, 350, 71);
		
		for (int i = 0; i < AccInfoTags.size(); i++) {
			AccInfoTags.get(i).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					System.out.println("\n선택된 Tag 출력 ");
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
			XSSFSheet sheet = workbook.getSheetAt(0); // 해당 엑셀파일의 시트(Sheet) 수
			int rows = sheet.getPhysicalNumberOfRows(); // 해당 시트의 행의 개수
			XSSFRow row = sheet.getRow(0); // 각 행을 읽어온다
			if (row != null) {
				int cells = row.getPhysicalNumberOfCells();
				// 첫 행 읽을 때
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
		OpPanel.add(new JLabel("조 별 "));
		for (int i = 0; i < 20; i++) {
			group.personNum.addItem(Integer.toString(i + 1));
		}
		OpPanel.add(group.personNum);
		OpPanel.add(new JLabel("명      "));

		OpPanel.add(new JLabel("총 "));
		for (int i = 0; i < 20; i++) {
			group.groupNum.addItem(Integer.toString(i + 1));
		}
		OpPanel.add(group.groupNum);
		OpPanel.add(new JLabel("조     "));
		Groups.add(group);
	}
	void init() {

		//chooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
	    chooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용 
	    chooser.setDialogTitle("파일 불러오기"); // 창의 제목
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드

	    //FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary File", "cd11"); // filter 확장자 추가
	    //chooser.setFileFilter(filter); // 파일 필터를 추가
	    
	}
	void process() {
		/*
		 * TODO
		 * 1. 연결 
		 * 
		 */
		
		// 원본파일 읽어오고, 어떤 기준으로 팀읆 묶을 것 인지 설정, 여기서는 이름으로 설정
		nameList = fh.openFile(inputFile,"이름",recordList);
		
		// 팀은 생성자로 인원수와 팀 이름을 넣어주면 됨
		
		//7, 10
		//4, 2
		for (int i = 0; i < Groups.size(); i++) {
			setCombiConfiguration(
					Integer.parseInt((String)Groups.get(i).personNum.getSelectedItem()),
					Integer.parseInt((String)Groups.get(i).groupNum.getSelectedItem()));

		}
		
		//읽어온 엑셀 파일에 명단을 참고로 각 팀에 인원수 배정 
		tm.setTeam(teams, nameList);
		
		
		//각 인원들에 팀을 매칭시킴
		HashMap<String, String> teamInfo = tm.teamForHuman(teams);
		

		
		getSaveFile();

		//원본파일, 출력파일, 각 팀정보, 매칭시킬 태그이름, 새로 추가할 태그 이름으로 새 팡리 만듦
		fh.writeFile(inputFile, outputFile, teamInfo,"이름",	TagtextField.getText());
	}
	
	void getSaveFile() {

		int returnVal = chooser.showSaveDialog(null); // 열기용 창 오픈
		if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
			outputFile = chooser.getSelectedFile().toString();
		} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
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
			int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈

			if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
				inputFile = chooser.getSelectedFile().toString();
			} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
				System.out.println("cancel");
				inputFile = "";
			}
			System.out.println(inputFile);
			getUserInfoTags();
			printAccInfoTags();
		}
	}
}
