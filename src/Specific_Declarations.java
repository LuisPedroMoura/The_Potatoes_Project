import java.util.*;
import java.io.*;

public class Specific_Declarations {
	static Double var0 = 0.0;
	static Double var1 = 1.0;
	static Double var2 = var1 * 1.0;
	static Double var3 = var2 * 1.0;;
	static Double var4 = 0.0;
	static Double var5 = var3;
	static Double var6 = var5 * 1.0;;
	public static void main(String[] args)
	{
		Double var7 = 2.0;
		Double var8 = var7 * 1.0;
		var3 = var8 * 1.0;
		String var9 = "Inside main";
		System.out.println(var9);
		Double var10 = var3;
		System.out.println(var10 + " m");
	}
	public static Double function1()
	{
		Double var11 = 3.0;
		Double var12 = var11 * 1.0;
		var3 = var12 * 1.0;
		String var13 = "Inside function";
		System.out.println(var13);
		Double var14 = var3;
		System.out.println(var14 + " m");
		Double var15 = var3;
		return var15;
	}
}