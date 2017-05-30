package testcase;

public class Test {
	
	public static void main(String[] args) {
		
		int i = 289;System.out.println(toFullBinaryString(i));
		
		i = i|(i>>1);  System.out.println(toFullBinaryString(i));
		i = i|(i>>2);  System.out.println(toFullBinaryString(i));
		i = i|(i>>4);  System.out.println(toFullBinaryString(i));
		i = i|(i>>8);  System.out.println(toFullBinaryString(i));
		i = i|(i>>16); System.out.println(toFullBinaryString(i));
		i = i&(~i>>1);
		System.out.println(toFullBinaryString(i));
	}
	
	
	private static String toFullBinaryString(int x) {
        int[] buffer = new int[Integer.SIZE];
        for (int i = (Integer.SIZE - 1); i >= 0; i--) {
            buffer[i] = x >> i & 1;
        }
        String s = "";
        for (int j = (Integer.SIZE - 1); j >= 0; j--) {
            s = s + buffer[j];
        }
        return s;
    }
	
}
