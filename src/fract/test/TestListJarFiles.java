package fract.test;

import java.io.*;
import java.util.*;
import java.util.jar.*;

public class TestListJarFiles {
	public static void main(String args[]) throws IOException {

		JarFile jarFile = new JarFile("../JAR/fract.jar");
		jarFile.entries();
		Enumeration<JarEntry> en = jarFile.entries();
		while (en.hasMoreElements()) {
			process(en.nextElement());
		}
	}

	private static void process(Object obj) {
		JarEntry entry = (JarEntry) obj;
		String name = entry.getName();
		long size = entry.getSize();
		long compressedSize = entry.getCompressedSize();
		System.out.println(name + "\t" + size + "\t" + compressedSize);
	}
}