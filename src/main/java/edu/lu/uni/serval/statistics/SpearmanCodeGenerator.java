package edu.lu.uni.serval.statistics;


public class SpearmanCodeGenerator {

	public static void main(String[] args) {
		generateSpearmanCode();
	}

	public static void generateSpearmanCode() {
		for (int i = 0; i < 10; i ++) {
			String s = "d<-cor.test( ~ X" + i + " + X" + i + ".T, \n";
			s += "          data=a,";
			s += "          method = \"spearman\",\n";
			s += "          continuity = FALSE,\n";
			s += "          conf.level = 0.95)\n";
			if (i == 0) {
				s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
						"append = FALSE, sep = \"@\")\n";
			} else {
				s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
						"append = TRUE, sep = \"@\")\n";
			}
			
			System.out.println(s);
		}
		for (int i = 0; i < 10; i ++) {
			String s = "d<-cor.test( ~ X" + i + ".R + X" + i + ".T.R, \n";
			s += "          data=a,";
			s += "          method = \"spearman\",\n";
			s += "          continuity = FALSE,\n";
			s += "          conf.level = 0.95)\n";
			s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
					"append = TRUE, sep = \"@\")\n";
			System.out.println(s);
		}
		
		for (int i = 0; i < 10; i ++) {
			String s = "d<-cor.test( ~ X" + i + " + X" + i + ".T, \n";
			s += "          data=a,";
			s += "          method = \"pearson\",\n";
			s += "          continuity = FALSE,\n";
			s += "          conf.level = 0.95)\n";
			s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
							"append = TRUE, sep = \"@\")\n";
			System.out.println(s);
		}
		for (int i = 0; i < 10; i ++) {
			String s = "d<-cor.test( ~ X" + i + ".R + X" + i + ".T.R, \n";
			s += "          data=a,";
			s += "          method = \"pearson\",\n";
			s += "          continuity = FALSE,\n";
			s += "          conf.level = 0.95)\n";
			s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
					"append = TRUE, sep = \"@\")\n";
			System.out.println(s);
		}
		
		for (int i = 0; i < 10; i ++) {
			String s = "d<-cor.test( ~ X" + i + " + X" + i + ".T, \n";
			s += "          data=a,";
			s += "          method = \"kendall\",\n";
			s += "          continuity = FALSE,\n";
			s += "          conf.level = 0.95)\n";
			s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
							"append = TRUE, sep = \"@\")\n";
			System.out.println(s);
		}
		for (int i = 0; i < 10; i ++) {
			String s = "d<-cor.test( ~ X" + i + ".R + X" + i + ".T.R, \n";
			s += "          data=a,";
			s += "          method = \"kendall\",\n";
			s += "          continuity = FALSE,\n";
			s += "          conf.level = 0.95)\n";
			s += "write(c(d$estimate, d$p.value), file = \"~/Public/git/FPM_Violations/RQ1/SpearmanResults.list\"," +
					"append = TRUE, sep = \"@\")\n";
			System.out.println(s);
		}
	}

}
