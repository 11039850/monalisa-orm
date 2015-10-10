package com.tsc9526.monalisa.core.tools;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */  
public class JavaBeansHelper { 
    private JavaBeansHelper() {
        super();
    }
   
    
    public static String getGetterMethodName(String property,String javaType) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
         
        if (javaType.equals("boolean") || javaType.equals("Boolean") || javaType.equals("java.lang.Boolean")) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }

        return sb.toString();
    }

    /**
     * JavaBeans rules:<br>
     * 
     * eMail     > setEMail() <br>
     * firstName > setFirstName() <br>
     * URL       > setURL()<br> 
     * XAxis     > setXAxis() <br> 
     * a         > setA() <br> 
     * B         > setB() <br> 
     * @param property
     * @return the setter method name
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
         
        sb.insert(0, "set");

        return sb.toString();
    }

    public static String getJavaName(String name,boolean firstCharacterUppercase){
    	if(name==null || name.trim().length()==0){
    		return "_";
    	}
    	
	    String javaName=JavaBeansHelper.getCamelCaseString(name, firstCharacterUppercase);			
		 
		if(TypeHelper.isJavaKeyword(javaName) || javaName.length()==0){
			javaName=javaName+"_";
		}
		return javaName;
    }
    
    public static String getCamelCaseString(String inputString,boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();
        
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if( (c>='0' && c<='9') || (c>='a' && c<='z') ||  (c>='A' && c<='Z') ){
            	if(c>='0' && c<='9' && sb.length()==0){
            		sb.append("N");
            	}
            	
            	if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                } 
            }else{
            	if (sb.length() > 0) {
                    nextUpperCase = true;
                }            		
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    } 
}
