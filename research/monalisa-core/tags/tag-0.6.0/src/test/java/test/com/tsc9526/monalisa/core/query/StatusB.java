package test.com.tsc9526.monalisa.core.query;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public enum StatusB {
	B1(1),B2(2),B3(3);
	
	private int value;	
	
	StatusB(int value){
		this.value=value;		
	}
	
	
	public int getValue() {
		return value;
	}
	
}
