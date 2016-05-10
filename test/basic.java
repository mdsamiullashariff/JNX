package org.smartechz.jnx.test;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Date;

import org.json.simple.parser.JSONParser;
import org.smartechz.jnx.JNX;
import org.smartechz.jnx.datatypes.JNXArray;
import org.smartechz.jnx.datatypes.JNXObject;
import org.smartechz.jnx.parser.JNXParser;

public class basic {
	public static void main(String[] args) throws Exception {
		testSimpleJSONParseDifference();
		// testJNX2vsJNX3();
		// compareJNX1vsJNX2Parse();
		// writeBigJson();

	}

	public static void writeBigJson() throws Exception {
		JNXObject obj = new JNXObject();

		buildBigJson(obj);

		JNX.format(obj, new File(
				"D:/Projectapps/WorkSpaces/TestArea/JnxOut/test11.json.big"));
	}

	private static int count = 0;
	private static int depth = 0;
	private static int maxcount = 1200000;
	private static int maxdepth = 50;

	@SuppressWarnings("unchecked")
	private static void buildBigJson(JNXObject obj) {
		if (depth == 0) {
			while (count < maxcount) {
				double rand = Math.random();
				if (rand <= 0.2) {
					if (depth < maxdepth) {
						JNXObject childObj = new JNXObject();
						depth++;
						buildBigJson(childObj);
						obj.put("randomKey" + rand, childObj);
						depth--;
					}
				} else if (rand <= 0.4) {
					if (depth < maxdepth) {
						JNXArray childObj = new JNXArray();
						depth++;
						buildBigJsonArr(childObj);
						obj.put("randomKey" + rand, childObj);
						depth--;
					}
				} else if (rand <= 0.6) {
					obj.put("randomKey" + rand, "randomval" + rand);
					count++;
				} else if (rand <= 0.8) {
					obj.put("randomKey" + rand, rand);
					count++;
				} else if (rand <= 1) {
					continue;
				}
			}

		} else {
			while (count < maxcount) {
				double rand = Math.random();
				if (rand <= 0.2) {
					if (depth < maxdepth) {
						JNXObject childObj = new JNXObject();
						depth++;
						buildBigJson(childObj);
						obj.put("randomKey" + rand, childObj);
						depth--;
					}
				} else if (rand <= 0.4) {
					if (depth < maxdepth) {
						JNXArray childObj = new JNXArray();
						depth++;
						buildBigJsonArr(childObj);
						obj.put("randomKey" + rand, childObj);
						depth--;
					}
				} else if (rand <= 0.6) {
					obj.put("randomKey" + rand, "randomval" + rand);
					count++;
				} else if (rand <= 0.8) {
					obj.put("randomKey" + rand, rand);
					count++;
				} else if (rand <= 1) {
					return;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void buildBigJsonArr(JNXArray obj) {
		while (count < maxcount) {
			double rand = Math.random();
			if (rand <= 0.2) {
				if (depth < maxdepth) {
					JNXObject childObj = new JNXObject();
					depth++;
					buildBigJson(childObj);
					obj.add(childObj);
					depth--;
				}
			} else if (rand <= 0.4) {
				if (depth < maxdepth) {
					JNXArray childObj = new JNXArray();
					depth++;
					buildBigJsonArr(childObj);
					obj.add(childObj);
					depth--;
				}
			} else if (rand <= 0.6) {
				obj.add("randomval" + rand);
				count++;
			} else if (rand <= 0.8) {
				obj.add(rand);
				count++;
			} else if (rand <= 1) {
				return;
			}
		}

	}

	private static void testSimpleJSONParseDifference() throws Exception {
		File dir = new File(
				"D:/Projectapps/WorkSpaces/TestArea/Jnx/DataFilesAll");
		File[] files = dir.listFiles();
		Arrays.sort(files);
		System.out.printf("%15s\t%10s\t%10s\t%10s\t%10s\n", "Name", "Size",
				"JNX", "simple", "Diff");

		for (int i = 0; i < files.length; i++) {
			int jnxDiff, simpleDiff;

			Date start2 = new Date();
			JSONParser parser2 = new JSONParser();
			try {
				parser2.parse(new FileReader(files[i]));
				Date end2 = new Date();
				simpleDiff = (int) (end2.getTime() - start2.getTime());
			} catch (Exception e) {
				simpleDiff = 99999999;
			}

			Date start = new Date();
			JNXParser parser = new JNXParser();
			parser.parse(files[i]);
			Date end = new Date();
			jnxDiff = (int) (end.getTime() - start.getTime());

			System.out.printf("%15s\t%10s\t%10d\t%10d\t%10d\n",
					files[i].getName(), readableSize(files[i].length()),
					jnxDiff, simpleDiff, jnxDiff - simpleDiff);
		}
	}

	private static String readableSize(long length) {
		double size;
		String type;
		if (length >= 1073741824) {
			size = ((double) length / 1073741824);
			type = "GB";
		} else if (length >= 1048576) {
			size = ((double) length / 1048576);
			type = "MB";
		} else if (length >= 1024) {
			size = ((double) length / 1024);
			type = "KB";
		} else {
			return length + " B";
		}
		return ((double) Math.round(size * 100) / 100) + " " + type;
	}
}
