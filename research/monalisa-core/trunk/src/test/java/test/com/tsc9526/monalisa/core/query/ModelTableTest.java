package test.com.tsc9526.monalisa.core.query;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.query.model.Model;

@DB(url="jdbc:mysql://127.0.0.1:3306/jy_market", username="jy_market", password="jy_market")
public class ModelTableTest extends Model<ModelTableTest>{ 
	private static final long serialVersionUID = -1974865252589672370L;

	String title;
	
	public ModelTableTest() {
		 super("gift");
	}
	
	
	public static void main(String[] args) {
		Model<?> tx=new Model<Model<?>>("gift");
		tx.use(DBConfig.fromClass(ModelTableTest.class));
		
		tx.set("gift_id", 1);
		tx.load();
		System.out.println("=="+tx);
		
		Map<String, Object> hs=new LinkedHashMap<String, Object>();
		hs.put("x",new ModelTableTest());
		hs.put("y",new ModelTableTest());
		Object c=hs.values();
		System.out.println(c);
		Object c2=hs.values();
		System.out.println(c2);
	 	
		ModelTableTest m=new ModelTableTest();
		m.set("package_name", "package_name");
		
		m.title="xxx";
		
		m.set("icon_path", "icon_path");
		m.set("description", "description");
		m.set("begin_time", new Date());
		m.set("end_time", new Date());
		m.save();
		
		System.out.println("Create gift: "+m.get("gift_id"));
		
		ModelTableTest t=new ModelTableTest();
		t.set("gift_id", m.get("gift_id"));
		t.load();
		System.out.println(t);
		
		
		System.out.println("NEW: "+new ModelTableTest());
		
		int times=10000;
		long l1=System.currentTimeMillis();
		for(int i=0;i<times;i++){
			ModelTableTest x=new ModelTableTest();
			x.changedFields();
		}
		long l2=System.currentTimeMillis();
		System.out.println("Use time: "+(l2-l1)+" ms");
	}

}
