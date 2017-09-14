a <- read.csv("~/Public/git/FPM_Violations/RQ1/TenFolds-Q/Ten-fold-all-fixed-violation-type1-70.csv", header=T)
d<-cor.test( ~ X0 + X0.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = FALSE, sep = "@")

d<-cor.test( ~ X1 + X1.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X2 + X2.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X3 + X3.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X4 + X4.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X5 + X5.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X6 + X6.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X7 + X7.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X8 + X8.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X9 + X9.T, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X0.R + X0.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X1.R + X1.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X2.R + X2.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X3.R + X3.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X4.R + X4.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X5.R + X5.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X6.R + X6.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X7.R + X7.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X8.R + X8.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X9.R + X9.T.R, 
          data=a,          method = "spearman",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X0 + X0.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X1 + X1.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X2 + X2.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X3 + X3.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X4 + X4.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X5 + X5.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X6 + X6.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X7 + X7.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X8 + X8.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X9 + X9.T, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X0.R + X0.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X1.R + X1.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X2.R + X2.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X3.R + X3.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X4.R + X4.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X5.R + X5.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X6.R + X6.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X7.R + X7.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X8.R + X8.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X9.R + X9.T.R, 
          data=a,          method = "pearson",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X0 + X0.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X1 + X1.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X2 + X2.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X3 + X3.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X4 + X4.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X5 + X5.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X6 + X6.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X7 + X7.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X8 + X8.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X9 + X9.T, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X0.R + X0.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X1.R + X1.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X2.R + X2.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X3.R + X3.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X4.R + X4.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X5.R + X5.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X6.R + X6.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X7.R + X7.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X8.R + X8.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

d<-cor.test( ~ X9.R + X9.T.R, 
          data=a,          method = "kendall",
          continuity = FALSE,
          conf.level = 0.95)
write(c(d$estimate, d$p.value), file = "~/Public/git/FPM_Violations/RQ1/SpearmanResults.list",append = TRUE, sep = "@")

a=1
