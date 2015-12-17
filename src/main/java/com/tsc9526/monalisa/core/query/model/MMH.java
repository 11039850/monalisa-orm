package com.tsc9526.monalisa.core.query.model;

/**
 * 
 * Do not use this class
 *
 * @author zzg.zhou(11039850@qq.com)
 */
public class MMH {

	@SuppressWarnings("unchecked")
	public static <T extends Model<?>> Model<T> createFrom(Model<T> model) {
		try {
			Model<T> x = model.getClass().newInstance();

			x.TABLE_NAME = model.TABLE_NAME;
			x.PRIMARY_KEYS = model.PRIMARY_KEYS;
			x.db = model.db;

			return x;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
