import java.util.*;
import java.io.*;

public class _04_Lists {
	public static void main(String[] args)
	{
		List<Double> var0 = new ArrayList<>();;
		List<Double> var1 = new ArrayList<>();;

		Double var2 = 0.0;
		Double var3 = 1.0;
		Double var4 = var3 * 1.0;
		var2 = var4 * 1.0;

		Double var5 = 0.0;
		Double var6 = 2.0;
		Double var7 = var6 * 1.0;
		var5 = var7 * 1.0;

		Double var8 = 0.0;

		Double var9 = 3.0;
		var8 = var9 * 1.0;
		List<Double> var10 = var0;
		Double var11 = var2;
		Boolean var12 = var10.add(var11 * 1.0);;
		List<Double> var13 = var0;
		Double var14 = var5;
		Boolean var15 = var13.add(var14 * 0.9144);;
		List<Double> var16 = var0;
		Double var17 = var8;
		Boolean var18 = var16.add(var17 * 1.0);;
		String var19 = "Added:";
		System.out.println(var19);
		List<Double> var20 = var0;

		String var21 = "\n";

		String var23 = "[";
		Iterator var24 = var20.iterator();
		while (var24.hasNext()) {
			var23+= (var24.next() + " m");
			if(var24.hasNext()){
				var23 += ", ";
			}
		}
		var23 += "]";
		String var22 = var23 + var21;
		System.out.println(var22);
		List<Double> var25 = var0;
		Double var26 = 2.0;
		Double var27 = var25.remove(var26.intValue());;
		String var28 = "Removed:";
		System.out.println(var28);
		List<Double> var29 = var0;

		String var30 = "\n";

		String var32 = "[";
		Iterator var33 = var29.iterator();
		while (var33.hasNext()) {
			var32+= (var33.next() + " m");
			if(var33.hasNext()){
				var32 += ", ";
			}
		}
		var32 += "]";
		String var31 = var32 + var30;
		System.out.println(var31);
	}
}