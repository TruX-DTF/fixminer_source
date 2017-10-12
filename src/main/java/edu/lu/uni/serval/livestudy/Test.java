package edu.lu.uni.serval.livestudy;

import java.io.File;
import java.util.List;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class Test {
	public static void main(String[] argus) {
		List<File> files = FileHelper.getAllFiles(Configuration.ROOT_PATH + "LiveStudy/projects/camel/", ".java");
		for (File file : files ) {
//			if (file.getPath().endsWith("/ItemList.java")) {
//				System.out.println(file.getPath());		
//			}
			FileHelper.outputToFile("OUTPUT/camel.test", file.getPath() + "\n", true);
		}
	}
}
