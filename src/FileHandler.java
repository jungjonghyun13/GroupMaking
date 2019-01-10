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
	// ����� ������ ��� �ִ� ���ϰ� �ߺ� üũ �������� �� �±� ����(���⼭�� �̸�)
	// �̸����� ���� �� �ִ��� ���� �Ǵ� ��
	private HashMap<String, Set<String>> makeList(String file, String mainTag) {
		HashMap<String, Set<String>> nameList = new HashMap<String, Set<String>>();
		int mainTagIndex = 0;
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0); // �ش� ���������� ��Ʈ(Sheet) ��
			int rows = sheet.getPhysicalNumberOfRows(); // �ش� ��Ʈ�� ���� ����
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex); // �� ���� �о�´�
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells();
					// ù �� ���� ��
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

	// ��� �濡 ���߾����� ����� recordTag ����Ʈ�� �޾ƿͼ� ���� ���� ����� �����
	public HashMap<String, Set<String>> openFile(String file, String mainTag, ArrayList<String> recordTag) {
		HashMap<String, Set<String>> nameList = makeList(file, mainTag);
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0); // �ش� ���������� ��Ʈ(Sheet) ��
			int rows = sheet.getPhysicalNumberOfRows(); // �ش� ��Ʈ�� ���� ����

			int mainTagIndex = 0;
			ArrayList<Integer> recordTagIndex = new ArrayList<Integer>();
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex); // �� ���� �о�´�
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells();
					// ù �� ���� ��

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
			XSSFSheet readsheet = readworkbook.getSheetAt(0); // �ش� ����������
																// ��Ʈ(Sheet) ��
			int rows = readsheet.getPhysicalNumberOfRows(); // �ش� ��Ʈ�� ���� ����

			XSSFWorkbook writeworkbook = new XSSFWorkbook(); // �� ���� ����
			XSSFSheet writesheet = writeworkbook.createSheet(); // �� ��Ʈ(Sheet)
																// ����
			 

			int mainTagIndex = 0;
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				XSSFRow writerow = writesheet.createRow(rowIndex);
				XSSFRow readrow = readsheet.getRow(rowIndex); // �� ���� �о�´�
				if (readrow != null) {
					int cells = readrow.getPhysicalNumberOfCells();
					// ù �� ���� ��
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
