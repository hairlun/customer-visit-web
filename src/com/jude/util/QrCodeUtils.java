package com.jude.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class QrCodeUtils {
	private static final int BLACK = -16777216;
	private static final int WHITE = -1;
	private static MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, 1);

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				image.setRGB(x, y, (matrix.get(x, y)) ? -16777216 : -1);
			}
		}
		return image;
	}

	private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file))
			throw new IOException("Could not write an image of format " + format + " to " + file);
	}

	private static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream))
			throw new IOException("Could not write an image of format " + format);
	}

	public static void toFile(String content, String format, String path) throws Exception {
		File file = new File(path, "qrcode.jpg");
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		BitMatrix matrix = multiFormatWriter
				.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);

		writeToFile(matrix, format, file);
	}

	public static void toStream(String content, String format, OutputStream out) throws Exception {
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = multiFormatWriter
				.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);

		writeToStream(matrix, format, out);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(123);
		toFile("www.suning.com", "jpg", "E:/");
	}
}