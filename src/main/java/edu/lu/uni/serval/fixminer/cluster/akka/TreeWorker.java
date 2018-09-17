package edu.lu.uni.serval.fixminer.cluster.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.fixminer.cluster.Compare;
import edu.lu.uni.serval.fixminer.cluster.RunnableCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.*;

public class TreeWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(TreeWorker.class);


//	private JedisPool innerPool;
//	private JedisPool outerPool;
//
//	public TreeWorker(String innerPort,String outerPort) {
////		this.innerPool = innerPool;
////		this.outerPool = outerPool;
//		 this.outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(outerPort),20000000);
//		 this.innerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(innerPort),20000000);
//
//
//	}
//
//	public static Props props(final String innerPort,final String outerPort) {
//		return Props.create(new Creator<TreeWorker>() {
//
//			private static final long serialVersionUID = -7615153844097275009L;
//
//			@Override
//			public TreeWorker create() throws Exception {
//				return new TreeWorker(innerPort,outerPort);
//			}
//
//		});
//	}

		public TreeWorker() {

	}

	public static Props props() {
		return Props.create(new Creator<TreeWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public TreeWorker create() throws Exception {
				return new TreeWorker();
			}

		});
	}



	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof TreeMessage) {


//		if (message instanceof edu.lu.uni.serval.fixminer.cluster.akka.EDiffMessage) {
			TreeMessage msg = (TreeMessage) message;
			List<String> files = msg.getName();
			JedisPool innerPool = msg.getInnerPool();
			JedisPool outerPool = msg.getOuterPool();

			int id = msg.getId();
//			int counter = new Object() {
				int counter = 0;

				//
			for (String name : files)
				{
//					files.stream().
//							parallel().
//							peek(x -> counter++).
//							forEach(m ->
//									{
//										Compare compare = new Compare();
//										compare.coreCompare(m, innerPool, outerPool);
//									}
//							);
//				}
//			}.counter;

//
				final ExecutorService executor = Executors.newFixedThreadPool(20);
//				// schedule the work
				final Future<?> future = executor.submit(new RunnableCompare(name, innerPool, outerPool, new Compare()));
				try {
//					 wait for task to complete
					future.get(Configuration.SECONDS_TO_WAIT, TimeUnit.SECONDS);
					Compare compare = new Compare();
					compare.coreCompare(name, innerPool, outerPool);
					counter++;
//					nullDiffEntry += parser.nullMatchedDiffEntry;
//					nullMappingGumTreeResults += parser.nullMappingGumTreeResult;
//					pureDeletion += parser.pureDeletions;
//					largeHunk += parser.largeHunk;
//					nullSourceCode += parser.nullSourceCode;
//					testInfos += parser.testInfos;
//					testingInfo.append(parser.testingInfo);
//					builder.append(parser.unfixedViolations);
//
//					String editScript = parser.getAstEditScripts();
//					if ("".equals(editScript)) {
////						if (parser.resultType == 1) {
////							nullGumTreeResults += countAlarms(positionFile, "");
////						} else if (parser.resultType == 2) {
////							noSourceCodeChanges += countAlarms(positionFile, "");
////						} else if (parser.resultType == 3) {
////							noStatementChanges += countAlarms(positionFile, "");
////						}
//					} else {
//						editScripts.append(editScript);
//						patchesSourceCode.append(parser.getPatchesSourceCode());
//						sizes.append(parser.getSizes());
//						tokens.append(parser.getTokensOfSourceCode());
//
//						counter ++;
//						if (counter % 100 == 0) {
//							FileHelper.outputToFile(editScriptsFilePath + "edistScripts_" + id + ".list", editScripts, true);
//							FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
//							FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
//							FileHelper.outputToFile(buggyTokensFilePath + "tokens_" + id + ".list", tokens, true);
//							editScripts.setLength(0);
//							patchesSourceCode.setLength(0);
//							sizes.setLength(0);
//							tokens.setLength(0);
//							log.info("Worker #" + id +" finialized parsing " + counter + " files...");
//							FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
//							testingInfo.setLength(0);
//						}
//					}
				} catch (TimeoutException e) {
					future.cancel(true);
//////					timeouts += countAlarms(positionFile, "#Timeout:");
//					System.err.println("#Timeout: " + name);
				} catch (InterruptedException e) {
//////					timeouts += countAlarms(positionFile, "#TimeInterrupted:");
////					System.err.println("#TimeInterrupted: " + revFile.getName());
					e.printStackTrace();
				} catch (ExecutionException e) {
//////					timeouts += countAlarms(positionFile, "#TimeAborted:");
////					System.err.println("#TimeAborted: " + revFile.getName());
					e.printStackTrace();
				} finally {
					executor.shutdownNow();
				}
			}

			log.info("bitti");
			log.info("Worker #" + id +"finialized parsing " + counter + " files...");
			log.info("Worker #" + id + " finialized the work...");
			this.getSender().tell("STOP", getSelf());
//				String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
//                stopServer = String.format(stopServer,Integer.valueOf(innerPort));
//                loadRedis(stopServer,serverWait);
//
//			if (sizes.length() > 0) {
//				FileHelper.outputToFile(editScriptsFilePath + "editScripts_" + id + ".list", editScripts, true);
//				FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
//				FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
//				FileHelper.outputToFile(buggyTokensFilePath + "tokens_" + id + ".list", tokens, true);
//				editScripts.setLength(0);
//				patchesSourceCode.setLength(0);
//				sizes.setLength(0);
//				tokens.setLength(0);
//
//				FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
//				testingInfo.setLength(0);
//			}
//			String statistic = "\nNullGumTreeResults: " + nullGumTreeResults + "\nNoSourceCodeChanges: " + noSourceCodeChanges +
//					"\nNoStatementChanges: " + noStatementChanges + "\nNullDiffEntry: " + nullDiffEntry + "\nNullMatchedGumTreeResults: " + nullMappingGumTreeResults +
//					"\nPureDeletion: " + pureDeletion + "\nLargeHunk: " + largeHunk + "\nNullSourceCode: " + nullSourceCode +
//					"\nTestingInfo: " + testInfos + "\nTimeout: " + timeouts;
//			FileHelper.outputToFile("OUTPUT/statistic_" + id + ".list", statistic, false);
//			FileHelper.outputToFile("OUTPUT/UnfixedV_" + id + ".list", builder, false);
//
//			log.info("Worker #" + id +"finialized parsing " + counter + " files...");
//			log.info("Worker #" + id + " finialized the work...");
//			this.getSender().tell("STOP", getSelf());
//		} else {
		}else{
			unhandled(message);
		}
	}

//	static final JedisPoolConfig poolConfig = buildPoolConfig();
//
//
//	private static JedisPoolConfig buildPoolConfig() {
//		final JedisPoolConfig poolConfig = new JedisPoolConfig();
//		poolConfig.setMaxTotal(128);
//		poolConfig.setMaxIdle(128);
//		poolConfig.setMinIdle(16);
//		poolConfig.setTestOnBorrow(true);
//		poolConfig.setTestOnReturn(true);
//		poolConfig.setTestWhileIdle(true);
//		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofMinutes(60).toMillis());
//		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofHours(30).toMillis());
//		poolConfig.setNumTestsPerEvictionRun(3);
//		poolConfig.setBlockWhenExhausted(true);
//
//		return poolConfig;
//	}










}
