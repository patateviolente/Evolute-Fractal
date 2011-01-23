package fract.test;

import java.io.IOException;

import fract.BiomorphLauncher;

public class TestProcess {

	public static void main(String[] args) {
		try {
			Process p = Runtime.getRuntime().exec("ls");
			
			BiomorphLauncher.runAndDisplayProcess(p, "/home/patoche/exp.sh");
			//BiomorphLauncher.runAndDisplayProcess(p, "ssh patoche@127.0.0.1");
			System.out.println("----");
			
			BiomorphLauncher.runAndDisplayProcess(p, "ls");
		} catch (IOException e) {
		}
	}

}
