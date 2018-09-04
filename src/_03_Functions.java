import java.util.*;
import java.io.*;

public class _03_Functions {
	static Double var0 = 0.0;;
	static Double var2 = 3.0;
	static Double var3 = var2 * 1.0;;
	public static Boolean booleanFunction()
	{
		String var5 = "Inside booleanFunction, return is true";
		System.out.println(var5);
		Boolean var6 = true;
		return var6;
	}
	public static Double numberFunction()
	{
		Double var11 = 0.0;

		Double var12 = 1.0;
		var11 = var12 * 1.0;
		String var13 = "Inside numberFunction, return is number";
		System.out.println(var13);
		Double var14 = var11;
		return var14;
	}
	public static Double meterFunction()
	{
		Double var21 = 0.0;
		Double var22 = 1.0;
		Double var23 = var22 * 1.0;
		var21 = var23 * 1.0;
		String var24 = "Inside meterFunction, return is meter";
		System.out.println(var24);
		Double var25 = var21;
		return var25;
	}
	public static String stringFunction()
	{
		String var32 = "Inside sringFunction, just returned this string";
		return var32;
	}
	public static void voidFunction()
	{
		String var8 = "Inside voidFunction, return is void";
		System.out.println(var8);
		String var9 = "Calling numberFunction";
		System.out.println(var9);

		Double var10 = 0.0;

		Double var15 = numberFunction();
		var10 = var15 * 1.0;
		String var16 = "\t";
		Double var17 = var10;
		String var18 = var16 + var17 + " ";
		System.out.println(var18);
		String var19 = "Calling meterFunction";
		System.out.println(var19);

		Double var20 = 0.0;

		Double var26 = meterFunction();
		var20 = var26 * 1.0;
		String var27 = "\t";
		Double var28 = var20;
		String var29 = var27 + var28 + " m";
		System.out.println(var29);
		String var30 = "Calling stringFunction";
		System.out.println(var30);

		String var31 = "";

		String var33 = stringFunction();
		var31 = var33;
		String var34 = "\t";
		String var35 = var31;
		String var36 = var34 + var35;
		System.out.println(var36);
	}
	public static void main(String[] args)
	{
		String var4 = "Inside main";
		System.out.println(var4);
		Boolean var7 = booleanFunction();
		if(var7) {
			voidFunction();
		}
		String var37 = "The End! Nice!";
		System.out.println(var37);
	}
}