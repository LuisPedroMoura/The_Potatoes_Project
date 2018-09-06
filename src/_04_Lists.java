import java.util.*;
import java.io.*;

public class _04_Lists {
	public static void main(String[] args)
	{
		List<Double> var0 = new ArrayList<>();;

		Double var1 = 0.0;
		Double var2 = 1.0;
		Double var3 = var2 * 1.0;
		var1 = var3 * 1.0;

		Double var4 = 0.0;
		Double var5 = 2.0;
		Double var6 = var5 * 1.0;
		var4 = var6 * 1000.0;

		Double var7 = 0.0;

		Double var8 = 3.0;
		var7 = var8 * 1.0;
		List<Double> var9 = var0;
		Double var10 = var1;
		Boolean var11 = var9.add(var10 * 1.0);;
		List<Double> var12 = var0;
		Double var13 = var4;
		Boolean var14 = var12.add(var13 * 0.9144);;
		List<Double> var15 = var0;
		Double var16 = var7;
		Double var17 = var16 * 1.0;
		Boolean var18 = var15.add(var17 * 0.0254);;
		String var19 = "Added:";
		System.out.println(var19);
		List<Double> var20 = var0;

		String var21 = "[";
		Iterator var22 = var20.iterator();
		while (var22.hasNext()) {
			var21+= (var22.next() + " m");
			if(var22.hasNext()){
				var21 += ", ";
			}
		}
		var21 += "]";
		System.out.println(var21);
		List<Double> var23 = var0;
		Double var24 = 2.0;
		Double var25 = var23.remove(var24.intValue());;
		String var26 = "Removed:";
		System.out.println(var26);
		List<Double> var27 = var0;

		String var28 = "\n";

		String var30 = "[";
		Iterator var31 = var27.iterator();
		while (var31.hasNext()) {
			var30+= (var31.next() + " m");
			if(var31.hasNext()){
				var30 += ", ";
			}
		}
		var30 += "]";
		String var29 = var30 + var28;
		System.out.println(var29);

		Double var32 = 0.0;
		Double var33 = 3.0;
		Double var34 = var33 * 1.0;
		var32 = var34 * 1.0;

		Double var35 = 0.0;
		Double var36 = 5.0;
		Double var37 = var36 * 1.0;
		var35 = var37 * 1.0;

		Double var38 = 0.0;

		Double var39 = var1;

		Double var40 = var4;
		Double var41 = var39 + var40 * 0.9144;
		var38 = var41 * 1.0;
	}
}