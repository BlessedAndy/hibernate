package com.andy.hibernateutil;


import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public final class HibernateUtil {
	private HibernateUtil(){}
	private static SessionFactory sessionFactory;
	//利用静态代码块来获得数据库的连接
	static{
		Configuration cfg = new Configuration();
		cfg.configure();
		
		ServiceRegistry  sr = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();  
		sessionFactory = cfg.buildSessionFactory(sr);
	}
	//返回创建的SessionFactory对象
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	//返回Session对象
	public static Session getSession(){
		return sessionFactory.openSession();
	}
	//向数据库增加一条记录
	public static void addUser(Object entity){
		Session s = null;
		Transaction tx = null;
		try{
			s = HibernateUtil.getSession();
			tx=s.beginTransaction();
			s.save(entity);
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			throw e;
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
	//更新数据库中的一条记录
	public static void update(Object entity){
		Session s = null;
		Transaction tx = null;
		try{
			s = HibernateUtil.getSession();
			tx=s.beginTransaction();
			s.update(entity);
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			throw e;
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
	//删除数据库中的一条记录
	public static void delete(Object entity){
		Session s = null;
		Transaction tx = null;
		try{
			s = HibernateUtil.getSession();
			tx=s.beginTransaction();
			s.delete(entity);
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			throw e;
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
	//根据记录ID在数据库中查询一条记录
	public static Object getById(Class<?> clazz, Serializable id){
		Session s = null;
		Transaction tx = null;
		try{
			s = HibernateUtil.getSession();
			tx=s.beginTransaction();
			Object obj = s.get(clazz, id);
			tx.commit();
			return obj;
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			throw e;
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
}