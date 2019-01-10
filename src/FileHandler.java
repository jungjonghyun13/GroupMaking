import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileHandler {
	// 사용자 정보가 담겨 있는 파일과 중복 체크 기준으로 할 태그 선택(여기서는 이름)
	// 이름으로 만난 적 있는지 여부 판단 함
	private HashMap<String, Set<String>> makeList(String file, String mainTag) {
		HashMap<String, Set<String>> nameList = new HashMap<String, Set<String>>();
		int mainTagIndex = 0;
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0); // 해당 엑셀파일의 시트(Sheet) 수
			int rows = sheet.getPhysicalNumberOfRows(); // 해당 시트의 행의 개수
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex); // 각 행을 읽어온다
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells();
					// 첫 행 읽을 때
					if (rowIndex == 0) {
						for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
							String currentTag = row.getCell(columnIndex).toString();
							if (mainTag.equals(currentTag)) {
								mainTagIndex = columnIndex;
							}
						}
					} else {
						Set<String> record = new HashSet<>();
						nameList.put(row.getCell(mainTagIndex).toString(), record);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nameList;
	}

	// 어느 방에 속했었는지 기록을 recordTag 리스트로 받아와서 각자 만난 사람들 기록함
	public HashMap<String, Set<String>> openFile(String file, String mainTag, ArrayList<String> recordTag) {
		HashMap<String, Set<String>> nameList = makeList(file, mainTag);
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0); // 해당 엑셀파일의 시트(Sheet) 수
			int rows = sheet.getPhysicalNumberOfRows(); // 해당 시트의 행의 개수

			int mainTagIndex = 0;
			ArrayList<Integer> recordTagIndex = new ArrayList<Integer>();
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex); // 각 행을 읽어온다
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells();
					// 첫 행 읽을 때

					if (rowIndex == 0) {
						for (int i = 0; i < recordTag.size(); i++) {
							for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
								String currentTag = row.getCell(columnIndex).toString();
								if (mainTag.equals(currentTag)) {
									mainTagIndex = columnIndex;
								} else if (recordTag.get(i).equals(currentTag)) {
									recordTagIndex.add(columnIndex);
								}
							}
						}
					} else {
						for (int j = 0; j < recordTagIndex.size(); j++) {
							String currentRecord = row.getCell(recordTagIndex.get(j)).toString();
							for (int rowIndex2 = 1; rowIndex2 < rows; rowIndex2++) {
								XSSFRow row2 = sheet.getRow(rowIndex2);
								if (row2.getCell(recordTagIndex.get(j)).toString().equals(currentRecord)) {
									if (row.getCell(mainTagIndex).toString()
											.equals(row2.getCell(mainTagIndex).toString()))
										continue;
									nameList.get(row.getCell(mainTagIndex).toString())
											.add(row2.getCell(mainTagIndex).toString());
								}
							}
						}
					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return nameList;

	}

	public void writeFile(String inputfile,String outputfile,HashMap<String, String> teamInfo,String mainTag, String newTag) {

		try {
			FileInputStream fis = new FileInputStream(inputfile);
			XSSFWorkbook readworkbook = new XSSFWorkbook(fis);
			XSSFSheet readsheet = readworkbook.getSheetAt(0); // 해당 엑셀파일의
																// 시트(Sheet) 수
			int rows = readsheet.getPhysicalNumberOfRows(); // 해당 시트의 행의 개수

			XSSFWorkbook writeworkbook = new XSSFWorkbook(); // 새 엑셀 생성
			XSSFSheet writesheet = writeworkbook.createSheet(); // 새 시트(Sheet)
																// 생성
			 

			int mainTagIndex = 0;
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				XSSFRow writerow = writesheet.createRow(rowIndex);
				XSSFRow readrow = readsheet.getRow(rowIndex); // 각 행을 읽어온다
				if (readrow != null) {
					int cells = readrow.getPhysicalNumberOfCells();
					// 첫 행 읽을 때
					if (rowIndex == 0) {
						for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
							XSSFCell cell = writerow.createCell(columnIndex);
							String currentTag = readrow.getCell(columnIndex).toString();
							cell.setCellValue(currentTag);
							if (mainTag.equals(currentTag)) {
								mainTagIndex = columnIndex;
							}
						}
						XSSFCell cell = writerow.createCell(cells);
						cell.setCellValue(newTag);
					} else {
						for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
							XSSFCell cell = writerow.createCell(columnIndex);
							cell.setCellValue(readrow.getCell(columnIndex).toString());	
						}
						
						String teamName = null;
						teamName = teamInfo.get(readrow.getCell(mainTagIndex).toString());
						XSSFCell cell = writerow.createCell(cells);

						cell.setCellValue(Integer.parseInt(teamName));
					}
				}
			}
			FileOutputStream fileoutputstream = new FileOutputStream(outputfile);
			writeworkbook.write(fileoutputstream);
			fileoutputstream.close();
			
			readworkbook.close();
			writeworkbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}
