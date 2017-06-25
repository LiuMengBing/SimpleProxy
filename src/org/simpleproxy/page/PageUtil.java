//package org.simpleproxy.page;
//
//import java.util.List;
//import org.hibernate.Query;
//import org.hibernate.Session;
//
///**
// *本工具类主要用来实现分页
// */
//public class PageUtil {
///**
//     * @param session :一个会话
//     * @param hql:是需要执行的hql语句，
//     * @param offset 设置开始位置
//     * @param length:读取记录条数
//     * return 返回结果集List<?>表示一个泛型的List
//     */
//    public static List<?> getList(Session session, String hql,
//			List<Object> paraList, int offset, int length) {
//		Query q = session.createQuery(hql);
//		for (int i = 0; i < paraList.size(); i++) {
//			Object obj = paraList.get(i);
//			System.out.println((String) obj);
//			q.setString(i, (String) obj);
//		}
//		q.setFirstResult(offset);
//		q.setMaxResults(length);
//		List<?> list = q.list();
//		return list;
//	}
//    
//    
//}