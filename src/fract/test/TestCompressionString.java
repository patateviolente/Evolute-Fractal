package fract.test;

import java.io.*;
import java.util.zip.*;

public class TestCompressionString {

	public static void main(String[] args) {
		System.out.println(str);
		try {
			byte[] b = Compression.compress(str);
			System.out.println(new String(b).toString());
			System.out.println(Compression.uncompress(b));
		
			String comp = "��s�]%!W�n�T\nX�	J� =�cH�[���<�m�x�v�ytU�j�K��2Ǐg=���oj�";
			System.out.println(Compression.uncompress(comp.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	final static int BUFFER = 2048;
	final static String str = "6;6;6;6;6;6;6;6;6;8;8;8;8;8;8;8;8;8;2;5;5;5;5;8;5;8;8;";
}

class Compression {
	public static byte[] compress(String str) throws IOException {
		int size = 1024;
		BufferedInputStream bis = new BufferedInputStream(
				new ByteArrayInputStream(str.getBytes()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(baos);
		byte[] buffer = new byte[size];
		int len;
		while ((len = bis.read(buffer, 0, size)) != -1) {
			gzip.write(buffer, 0, len);
		}
		gzip.finish();
		bis.close();
		gzip.close();
		return baos.toByteArray();
	}

	public static String uncompress(byte[] data) throws IOException {
		int size = 1024;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(
				data));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[size];
		int len;
		while ((len = gzip.read(buffer, 0, size)) != -1) {
			baos.write(buffer, 0, len);
		}
		gzip.close();
		baos.close();
		return new String(baos.toByteArray());
	}
}