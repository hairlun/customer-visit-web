package com.jude.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ExcelExporter {
	private OutputStream outputStream;
	private List<ExcelColumn> cols;
	private List<Object[]> rows;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private String name;
	private List<String> filePaths;
	private FileOutputStream o = null;
	private File file;
	public static final int MAX_ROW_SIZE = 60000;

	public ExcelExporter(OutputStream outputStream, String name, List<ExcelColumn> cols,
			List<Object[]> rows) throws IOException {
		this.cols = cols;
		this.rows = rows;
		this.outputStream = outputStream;
		this.name = name;
	}

	public void write() throws IOException {
		this.filePaths = new ArrayList();
		int excelCount = this.rows.size() / 60000 + 1;
		for (int j = 1; j <= excelCount; ++j) {
			int begin = (j - 1) * 60000;
			int end = Math.min(this.rows.size() - 1, j * 60000 - 1);
			buildTitle(j);
			writeRows(begin, end);
			flush();
		}

		File[] srcfile = new File[this.filePaths.size()];
		int i = 0;
		for (int n = this.filePaths.size(); i < n; ++i) {
			srcfile[i] = new File((String) this.filePaths.get(i));
		}

		pack();
	}

	private void buildTitle(int excelIndex) throws IOException {
		this.wb = new HSSFWorkbook();
		this.sheet = this.wb.createSheet(this.name);
		HSSFCellStyle headStyle = createHeaderStyle(this.wb);
		HSSFRow row = this.sheet.createRow(0);
		row.setHeightInPoints(21.0F);
		this.file = File.createTempFile("export", ".xls");

		this.o = new FileOutputStream(this.file);
		this.filePaths.add(this.file.getAbsolutePath());
		for (int i = 0; i < this.cols.size(); ++i) {
			ExcelColumn c = (ExcelColumn) this.cols.get(i);
			this.sheet.setColumnWidth(i, c.getWidth() * 256 / 7);
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(c.getTitle());
		}
	}

	private void writeRows(int begin, int end) throws IOException {
		HSSFCellStyle cellStyle = createRowStyle(this.wb);
		int i = 1;
		for (; begin <= end; ++begin) {
			writeRow(i, (Object[]) this.rows.get(begin), cellStyle);
			++i;
		}
	}

	private void writeRow(int rowIdx, Object[] rowData, HSSFCellStyle cellStyle) throws IOException {
		HSSFRow row = this.sheet.createRow(rowIdx);
		row.setHeightInPoints(18.0F);
		for (int i = 0; i < rowData.length; ++i) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
			if (rowData[i] == null) {
				cell.setCellValue("");
			} else if (rowData[i] instanceof Number) {
				cell.setCellType(0);
				cell.setCellValue(((Number) rowData[i]).doubleValue());
			} else {
				cell.setCellValue(rowData[i].toString());
			}
		}
	}

	private void flush() throws IOException {
		try {
			this.wb.write(this.o);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.o.flush();
			this.o.close();
		}
	}

	private HSSFCellStyle createHeaderStyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight((short) 700);
		font.setColor((short) 18);

		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment((short) 2);
		style.setVerticalAlignment((short) 1);
		style.setBorderTop((short) 1);
		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setWrapText(true);
		style.setFont(font);

		return style;
	}

	private HSSFCellStyle createRowStyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);

		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment((short) 0);
		style.setVerticalAlignment((short) 1);
		style.setBorderTop((short) 1);
		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setWrapText(true);
		style.setFont(font);
		return style;
	}

	private void pack() {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(this.outputStream);
			out.setEncoding("GBK");
			for (int i = 0; i < this.filePaths.size(); ++i) {
				FileInputStream in = new FileInputStream((String) this.filePaths.get(i));
				out.putNextEntry(new ZipEntry(this.name + (i + 1) + ".xls"));

				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public OutputStream getOutputStream() {
		return this.outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public List<ExcelColumn> getCols() {
		return this.cols;
	}

	public void setCols(List<ExcelColumn> cols) {
		this.cols = cols;
	}

	public List<Object[]> getRows() {
		return this.rows;
	}

	public void setRows(List<Object[]> rows) {
		this.rows = rows;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}