package test.com.tsc9526.monalisa.core.sql.java;
 
 
import test.com.tsc9526.monalisa.core.mysql.MysqlDB;

import com.jy.app.instools.oss.util.DateUtils;
import com.tsc9526.monalisa.core.annotation.Select;
import com.tsc9526.monalisa.core.query.DataMap;
import com.tsc9526.monalisa.core.query.Page;
import com.tsc9526.monalisa.core.query.Query; 
import com.tsc9526.monalisa.core.query.datatable.DataTable;

public class Demo {
	final static long $VERSION =1L;
	
	public Page findUserByName(String name,String pwd,int[] status){
		String sql=<<~
		===================================================
		select * 
			from user 
			where name=$name 
		===================================================
		
		Query q=new Query(MysqlDB.DB);
		
		q.add(sql);
		
		q.add(" AND status").in(status);
		
		return q.getPage(0, 10);
	}
	
	public Page findUserByName2(String name,String pwd,int[] status){
		String sql 	=<<~ 
		===================================================
		SELECT la.*,COUNT(1) arrive_count,ic.name channel_name,ip.name partner_name 
			FROM log_arrive la,jy_instools.ins_channel ic,jy_instools.ins_partner ip 
			WHERE la.partner_id=ip.partner_id AND la.channel= ic.channel AND arrive_time  >= ? AND arrive_time <  ? 
		===================================================
				
		query.add(sql,start,DateUtils.addTime(end, 1));
		addChannelAndPartner(chId, partnerId, query,"la.");
		query.add(" GROUP BY DATE_FORMAT(arrive_time, '%Y-%m-%d' ),la.channel  ORDER BY arrive_time DESC,la.partner_id,la.ch_id");
		DataTable<DataMap> list = query.getList();
	}
}