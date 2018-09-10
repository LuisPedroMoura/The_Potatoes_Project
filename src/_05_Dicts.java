import java.util.*;
import java.util.Map.*;
import java.io.*;

public class _05_Dicts {
	public static void main(String[] args)
	{
		Map<String, String> var0 = new LinkedHashMap<>();

		Double var1 = 0.0;

		Double var2 = 1.0;
		Double var3 = var2 * 1.0;
		var1 = var3 * 1.0;

		Double var4 = 0.0;

		Double var5 = 2.0;
		Double var6 = var5 * 1.0;
		var4 = var6 * 1.0;

		Double var7 = 0.0;

		Double var8 = 3.0;
		var7 = var8 * 1.0;
		Map<String, String> var9 = var0;

		Double var10 = var1;

		Double var11 = var1;
		Entry<String, String> var12 = new AbstractMap.SimpleEntry<String, String>(var10 + "m", var11 + "m");
		Entry<String, String> var13 = var12;
		Double var14 = var9.put(var13.getKey(), var13.getValue()) == null ? null : Double.parseDouble(var9.put(var13.getKey(), var13.getValue()).split(" ")[0]);
		Map<String, String> var15 = var0;

		Double var16 = var4;

		Double var17 = var4;
		Entry<String, String> var18 = new AbstractMap.SimpleEntry<String, String>(var16 + "yd", var17 + "yd");
		Entry<String, String> var19 = var18;
		Double var20 = var15.put(var19.getKey(), var19.getValue()) == null ? null : Double.parseDouble(var15.put(var19.getKey(), var19.getValue()).split(" ")[0]);
		Map<String, String> var21 = var0;

		Double var22 = var7;
		Double var23 = var22 * 1.0;

		Double var24 = var7;
		Double var25 = var24 * 1.0;
		Entry<String, String> var26 = new AbstractMap.SimpleEntry<String, String>(var23 + "in", var25 + "in");
		Entry<String, String> var27 = var26;
		Double var28 = var21.put(var27.getKey(), var27.getValue()) == null ? null : Double.parseDouble(var21.put(var27.getKey(), var27.getValue()).split(" ")[0]);
		String var29 = "Added:";
		System.out.println(var29);
		Map<String, String> var30 = var0;
		System.out.println("[3.0 in -> 3.0 in, 1.0 m -> 1.0 m, 2.0 yd -> 2.0 yd]");
		Map<String, String> var31 = var0;

		Double var32 = var1;
		Double var33 = (Double) var31.keySet().toArray()[1];
		String var34 = "Removed:";
		System.out.println(var34);
		Map<String, String> var35 = var0;

		String var36 = "\n";
		String var37 = "[3.0 in -> 3.0 in, 2.0 yd -> 2.0 yd]\n";
		System.out.println(var37);
		Map<String, String> var38 = new LinkedHashMap<>();

		Double var39 = 0.0;

		Double var40 = 1.0;
		Double var41 = var40 * 1.0;
		var39 = var41 * 1.0;

		Double var42 = 0.0;

		Double var43 = 0.9144;
		Double var44 = var43 * 1.0;
		Double var45 = var44 * 1.0936132983377078;
		var42 = var45 * 1.0;

		Double var46 = 0.0;

		Double var47 = 3.0;
		var46 = var47 * 1.0;
		Map<String, String> var48 = var38;

		Double var49 = var39;

		Double var50 = var39;
		Entry<String, String> var51 = new AbstractMap.SimpleEntry<String, String>(var49 + "m", var50 + "m");
		Entry<String, String> var52 = var51;
		Double var53 = var48.put(var52.getKey(), var52.getValue()) == null ? null : Double.parseDouble(var48.put(var52.getKey(), var52.getValue()).split(" ")[0]);
		Map<String, String> var54 = var38;

		Double var55 = var42;

		Double var56 = var42;
		Entry<String, String> var57 = new AbstractMap.SimpleEntry<String, String>(var55 + "yd", var56 + "yd");
		Entry<String, String> var58 = var57;
		Double var59 = var54.put(var58.getKey(), var58.getValue()) == null ? null : Double.parseDouble(var54.put(var58.getKey(), var58.getValue()).split(" ")[0]);
		Map<String, String> var60 = var38;

		Double var61 = var46;
		Double var62 = var61 * 1.0;

		Double var63 = var46;
		Double var64 = var63 * 1.0;
		Entry<String, String> var65 = new AbstractMap.SimpleEntry<String, String>(var62 + "in", var64 + "in");
		Entry<String, String> var66 = var65;
		Double var67 = var60.put(var66.getKey(), var66.getValue()) == null ? null : Double.parseDouble(var60.put(var66.getKey(), var66.getValue()).split(" ")[0]);
		String var68 = "Added:";
		System.out.println(var68);
		Map<String, String> var69 = var38;
		System.out.println("[3.0 in -> 3.0 in, 1.0 m -> 1.0 m, 1.0 yd -> 1.0 yd]");
		Map<String, String> var70 = var38;

		Double var71 = var42;
		Double var72 = (Double) var70.keySet().toArray()[2];
		String var73 = "Removed:";
		System.out.println(var73);
		Map<String, String> var74 = var38;

		String var75 = "\n";
		String var76 = "[3.0 in -> 3.0 in, 1.0 m -> 1.0 m]\n";
		System.out.println(var76);

		Boolean var77 = false;

		Map<String, String> var78 = var38;

		Double var79 = var42;
		boolean var80 = var78.containsKey(var79);
		var77 = var80;
		String var81 = "Contains?";
		System.out.println(var81);
		Boolean var82 = var77;

		String var83 = "\n";
		String var84 = var82 + var83;
		System.out.println(var84);
	}
}