package edu.lu.uni.serval.fixminer.ediff;

import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.utils.EDiffHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
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
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile, String project, JedisPool innerPool,String srcMLPath,String hunkLimit,boolean isJava) {

		String datasetName = project;
		String[] split1 = diffentryFile.getParent().split(datasetName);
		String root = split1[0];
		String pj = split1[1].split("/")[1];


		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,isJava);

		if (actionSets != null && actionSets.size() != 0) {

			boolean processActionSet = true;

			if (actionSets.size() > Integer.valueOf(hunkLimit)) {
				processActionSet = false;
				logger.debug("Skipping {} set size {}", diffentryFile.getName(), hunkLimit);
			}

			int hunkSet = 0;
			if (processActionSet) {

				for (HierarchicalActionSet actionSet : actionSets) {
//					FileOutputStream f = null;

					try {

						String astNodeType = actionSet.getAstNodeType();
//						if (astNodeType.equals(rootType)){
//
//						}
						actionSet.toString();
						int size = actionSet.getActionSize();


						String key = astNodeType + "/" + String.valueOf(size) + "/" + pj + "_" + diffentryFile.getName() + "_" + String.valueOf(hunkSet);

						ITree targetTree = EDiffHelper.getTargets(actionSet,isJava);
						ITree actionTree = EDiffHelper.getActionTrees(actionSet);
						ITree shapeTree = EDiffHelper.getShapeTree(actionSet,isJava);
						try (Jedis inner = innerPool.getResource()) {

							inner.hset("dump", key, actionSet.toString());
							inner.hset(key, "actionTree", actionTree.toStaticHashString());
							inner.hset(key, "targetTree", targetTree.toStaticHashString());
							inner.hset(key, "shapeTree", shapeTree.toStaticHashString());
						}
//						File f = new File(root+"dumps/"+astNodeType+"/"+String.valueOf(size)+"/");
//						f.mkdirs();
//						f = new File(root+"dumps/"+key);
//
//						FileUtils.writeByteArrayToFile(f,EDiffHelper.kryoSerialize(actionSet));
//						FileUtils.writeByteArrayToFile(f,EDiffHelper.commonsSerialize(actionSet));
//						FileUtils.writeByteArrayToFile(f,actionSet.toString().getBytes());
//						FileOutputStream fos = new FileOutputStream(f);
//						ObjectOutputStream oos = new ObjectOutputStream(fos);
//						oos.writeObject(EDiffHelper.kryoSerialize(actionSet));
//						oos.flush();
//						oos.close();

					} catch (Exception e) {
						logger.error("error", e);
//						e.printStackTrace();
					}
					hunkSet++;
				}
				try (Jedis inner = innerPool.getResource()) {
					inner.hset("diffEntry", pj + "_" + diffentryFile.getName(), "1");
				}

			}
		}


	}



}
