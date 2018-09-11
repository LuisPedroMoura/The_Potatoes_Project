import java.util.*;
import java.util.Map.*;
import java.io.*;

public class _04_Lists {
	public static void main(String[] args)
	{
		List<String> var0 = new ArrayList<>();

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

		Double var9 = 0.0;

		Double var10 = 1.0;
		Double var11 = var10 * 1.0;
		var9 = var11 * 1.0;

		Double var12 = 0.0;

		Double var13 = 1.0;
		Double var14 = var13 * 1.0;
		Double var15 = var14 * 1.0936132983377078;
		var12 = var15 * 1.0;

		Double var16 = 0.0;

		Double var17 = 3.0;
		var16 = var17 * 1.0;
		List<String> var18 = var0;

		Double var19 = var1;
		Boolean var20 = var18.add(var19 + " m");
		List<String> var21 = var0;

		Double var22 = var4;
		Boolean var23 = var21.add(var22 + " yd");
		List<String> var24 = var0;

		Double var25 = var7;
		Double var26 = var25 * 1.0;
		Boolean var27 = var24.add(var26 + " in");
		String var28 = "Added:";
		System.out.println(var28);
		List<String> var29 = var0;

		String var30 = "\n";
		String var31 = "[1.0 m, 2.0 yd, 3.0 in]\n";
		System.out.println(var31);

		Double var32 = 0.0;

		List<String> var33 = var0;

		Double var34 = 0.0;
		Double var35 = Double.parseDouble(var33.get(var34.intValue()).split(" ")[0]);
		var32 = var35 * 1.0;

		Double var36 = 0.0;

		List<String> var37 = var0;

		Double var38 = 1.0;
		Double var39 = Double.parseDouble(var37.get(var38.intValue()).split(" ")[0]);
		var36 = var39 * 0.9144;
		String var40 = "got:";
		System.out.println(var40);
		List<String> var41 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		String var42 = "met3 = ";

		Double var43 = var32;
		String var44 = var42 + var43 + " m";

		String var45 = ", met4 = ";
		String var46 = var44 + var45;

		Double var47 = var36;
		String var48 = var46 + var47 + " m";

		String var49 = "\n";
		String var50 = var48 + var49;
		System.out.println(var50);

		Boolean var51 = false;

		List<String> var52 = var0;

		Double var53 = var4;
		Boolean var54 = var52.contains(var53 + " yd");
		var51 = var54;

		Boolean var55 = false;

		List<String> var56 = var0;

		Double var57 = var12;
		Boolean var58 = var56.contains(var57 + " yd");
		var55 = var58;
		String var59 = "contains:";
		System.out.println(var59);
		List<String> var60 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		String var61 = "2 yd = ";

		Boolean var62 = var51;
		String var63 = var61 + var62;

		String var64 = ", 1,.. yd = ";
		String var65 = var63 + var64;

		Boolean var66 = var55;
		String var67 = var65 + var66;

		String var68 = "\n";
		String var69 = var67 + var68;
		System.out.println(var69);

		Double var70 = 0.0;

		List<String> var71 = var0;

		Double var72 = var1;
		Double var73 = (double) var71.indexOf(var72 + " m");
		var70 = var73 * 1.0;

		Double var74 = 0.0;

		List<String> var75 = var0;

		Double var76 = var7;
		Double var77 = var76 * 1.0;
		Double var78 = (double) var75.indexOf(var77 + " in");
		var74 = var78 * 1.0;
		String var79 = "indexOf:";
		System.out.println(var79);
		List<String> var80 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		String var81 = "1 m = ";

		Double var82 = var70;
		String var83 = var81 + var82 + " ";

		String var84 = ", 3 in = ";
		String var85 = var83 + var84;

		Double var86 = var74;
		String var87 = var85 + var86 + " ";

		String var88 = "\n";
		String var89 = var87 + var88;
		System.out.println(var89);

		Boolean var90 = false;

		List<String> var91 = var0;
		Boolean var92 = var91.isEmpty();
		var90 = var92;
		List<String> var93 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		String var94 = "isEmpty? = ";

		Boolean var95 = var90;
		String var96 = var94 + var95;

		String var97 = "\n";
		String var98 = var96 + var97;
		System.out.println(var98);

		Double var99 = 0.0;

		List<String> var100 = var0;
		Double var101 = (double)var100.size();
		var99 = var101 * 1.0;
		List<String> var102 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		String var103 = "size = ";

		Double var104 = var99;
		String var105 = var103 + var104 + " ";

		String var106 = "\n";
		String var107 = var105 + var106;
		System.out.println(var107);

		Double var108 = 0.0;

		List<String> var109 = var0;

		Double var110 = 0.0;
		Double var111 = Double.parseDouble(var109.get(var110.intValue()).split(" ")[0]);
		var108 = var111 * 1.0;

		Double var112 = 0.0;

		List<String> var113 = var0;

		Double var114 = 1.0;
		Double var115 = Double.parseDouble(var113.get(var114.intValue()).split(" ")[0]);
		var112 = var115 * 0.9144;
		String var116 = "array index:";
		System.out.println(var116);
		List<String> var117 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		String var118 = "at0 = ";

		Double var119 = var108;
		String var120 = var118 + var119 + " m";

		String var121 = ", at1 = ";
		String var122 = var120 + var121;

		Double var123 = var112;
		String var124 = var122 + var123 + " m";

		String var125 = "\n";
		String var126 = var124 + var125;
		System.out.println(var126);
		List<String> var127 = var0;Collections.sort(var127);
		String var129 = "sorted:";
		System.out.println(var129);
		List<String> var130 = var0;
		System.out.println("[1.0 m, 2.0 yd, 3.0 in]");
		List<String> var131 = var0;

		String var132 = "\n";
		String var133 = "[1.0 m, 2.0 yd, 3.0 in]\n";
		System.out.println(var133);
	}
}