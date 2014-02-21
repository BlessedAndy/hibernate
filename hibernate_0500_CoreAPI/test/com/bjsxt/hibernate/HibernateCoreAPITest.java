package com.bjsxt.hibernate;

import java.util.Date;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HibernateCoreAPITest {
	private static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void beforeClass() {
			//这种方法已经过时，请参考com.andy.hibernateutil包下的写法
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
	}
	@AfterClass
	public static void afterClass() {
		sessionFactory.close();
	}
	
	//Andy -- 2013.10.22
	@Test
	public void testStudentsave(){
		Student student = new Student();
		student.setName("marry");
		student.setAge(7);
		student.setSex("girl");
		student.setGood(true);
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(student);
		session.getTransaction().commit();
	}
	
	@Test
	public void testTeacherSave() {
	
		Teacher t = new Teacher();
		
		t.setName("t1");
		t.setTitle("middle");
		t.setBirthDate(new Date());
		
		//Session session = sessionFactory.openSession();
		Session session = sessionFactory.getCurrentSession();
		
		session.beginTransaction();
		session.save(t);
		
		Session session2 = sessionFactory.getCurrentSession();
		
		System.out.println(session == session2);
		
		session.getTransaction().commit();
		
		Session session3 = sessionFactory.getCurrentSession();
		
		System.out.println(session == session3);
		
		
	}
	
	@Test
	public void testSaveWith3State() {
	
		Teacher t = new Teacher();
		
		t.setName("t1");
		t.setTitle("middle");
		t.setBirthDate(new Date());
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(t);
		System.out.println(t.getId());
		session.getTransaction().commit();
		
		System.out.println(t.getId());
	}
	
	@Test
	public void testDelete() {
	
		Teacher t = new Teacher();
		t.setName("t1");
		t.setTitle("middle");
		t.setBirthDate(new Date());
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(t);
		System.out.println(t.getId());
		session.getTransaction().commit();
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		session2.delete(t);
		session2.getTransaction().commit();
	}
	
	@Test
	public void testDelete2() {
	
		Teacher t = new Teacher();
		t.setId(2);
		
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		session2.delete(t);
		session2.getTransaction().commit();
	}
	
	@Test
	public void testLoad() {
	
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Teacher t = (Teacher)session.load(Teacher.class, 55);
		
		session.getTransaction().commit();
		System.out.println(t.getClass());
		//System.out.println(t.getName());
	}
	
	@Test
	public void testGet() {
	
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Teacher t = (Teacher)session.get(Teacher.class, 1);
		
		session.getTransaction().commit();
		System.out.println(t.getClass());
		//System.out.println(t.getName());
	}
	
	//update detached object
	@Test
	public void testUpdate1() {
	
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Teacher t = (Teacher)session.get(Teacher.class, 1);
		
		session.getTransaction().commit();
		
		t.setName("zhanglaoshi");
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		session2.update(t);
		
		session2.getTransaction().commit();
	}
	
	//update transient object, object don't have id, so it won't works.
	@Test
	public void testUpdate2() {
		
		
		Teacher t = new Teacher();
		t.setName("zhanglaoshi");
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		//session2.update(t);	//t don't have id, so it won't works.
		
		session2.getTransaction().commit();
	
	}
	
	//if update a transient object and set the id manually 
	//which have record in database, it works.
	@Test
	public void testUpdate3() {
		
		
		Teacher t = new Teacher();
		t.setId(1);
		t.setName("zhanglaoshi");
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		session2.update(t);
		
		session2.getTransaction().commit();
	}
	
	//update
	@Test
	public void testUpdate4() {
		
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Teacher t = (Teacher)session.load(Teacher.class, 1);
		t.setName("zhangsan");
		session.getTransaction().commit();
	}
	
	@Test
	public void testUpdate5() {
		
		//Student.hbm.xml 中 dynamic-update 为 true ，可以只更新修改过的字段
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Student s = (Student)session.get(Student.class, 1);
		s.setName("zhangsan");
		session.getTransaction().commit();
		
		//下面这个是修改的detached对象，所以无法在缓存里检查那个字段做过修改，所以更新时所有字段都更新
		//如果想只更新detached对象被修改的字段，可以参考下面testUpdate6
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		s.setName("z4");
		session2.update(s);
		session2.getTransaction().commit();
	}
	
	//session.merge
	@Test
	public void testUpdate6() {
		
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Student s = (Student)session.get(Student.class, 1);
		s.setName("zhangsan6");
		session.getTransaction().commit();
		
		s.setName("z4");
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		session2.merge(s);
		session2.getTransaction().commit();
	}
	
	@Test
	public void testUpdate7() {
		
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		//Student 不是表名，是对象名
		Query q = session.createQuery("update Student s set s.name='z5' where s.id = 1");
		q.executeUpdate();
		session.getTransaction().commit();
		
	}
	
	@Test
	public void testSaveOrUpdate() {
		
		
		Teacher t = new Teacher();
		t.setName("t1");
		t.setTitle("middle");
		t.setBirthDate(new Date());
		
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.saveOrUpdate(t);
		
		session.getTransaction().commit();
		
		t.setName("t2");
		
		Session session2 = sessionFactory.getCurrentSession();
		session2.beginTransaction();
		session2.saveOrUpdate(t);
		session2.getTransaction().commit();
		
	}
	
	@Test
	public void testClear() {
	
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Teacher t = (Teacher)session.load(Teacher.class, 1);
		System.out.println(t.getName());
		
		//清楚缓存的方法
		session.clear();
		
		Teacher t2 = (Teacher)session.load(Teacher.class, 1);
		System.out.println(t2.getName());
		session.getTransaction().commit();
		
		
	}
	
	@Test
	public void testFlush() {
	
		Session session = sessionFactory.getCurrentSession();
		//session.setFlushMode(FlushMode.AUTO);
		session.beginTransaction();
		Teacher t = (Teacher)session.load(Teacher.class, 1);
		t.setName("tttt");
		
		//session.clear();  //只是清缓存，清完之后由于没有的t对象，所以下面的t.setName()也不会引起更新了
		session.flush(); //强制缓存和数据库同步，在commit（）的时候默认flush（）
		
		
		t.setName("ttttt");
		
	
		session.getTransaction().commit();
		
		
	}
	
	@Test
	public void testSchemaExport() {
		new SchemaExport(new AnnotationConfiguration().configure()).create(true, true);
	}
	
	public static void main(String[] args) {
		beforeClass();
	}
}
