package edu.lu.uni.serval.fixminer.akka.ediff;

import edu.lu.uni.serval.utils.EDiffHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;



/**
 * Parse fix violations with GumTree in terms of multiple statements.
 * 
 * @author kui.liu
 *
 */
public class EDiffHunkParser extends EDiffParser {

	private static Logger logger = LoggerFactory.getLogger(EDiffHunkParser.class);
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile, String project, JedisPool innerPool) {
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile);
		if (actionSets.size() != 0) {

			boolean processActionSet = true;

			int hunkSet = 0;
			if(processActionSet){
				for (HierarchicalActionSet actionSet : actionSets) {
					FileOutputStream f = null;

					try {

						String astNodeType = actionSet.getAstNodeType();
						actionSet.toString();
						int size = actionSet.getActionSize();

						String datasetName = project;
						String[] split1 = diffentryFile.getParent().split(datasetName);
						String root = split1[0];
						String pj = split1[1].split("/")[1];


						String key = astNodeType+"/"+String.valueOf(size)+"/" + pj +"_" + diffentryFile.getName() + "_" + String.valueOf(hunkSet);

						try (Jedis inner = innerPool.getResource()) {

							inner.hset("dump".getBytes(),key.getBytes(),EDiffHelper.kryoSerialize(actionSet));
						}

					} catch (Exception e) {
						logger.error("error",e);
						e.printStackTrace();
					}
					hunkSet++;
				}

			}
		}

	}



}
