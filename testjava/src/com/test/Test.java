package com.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Test {
	/**
	 * Description: ��֤�����ʽ�Ƿ���ȷ
	 * 
	 * @Version1.0 2011-12-9 ����04:13:56 mustang created
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isNumber(String num) {
		Pattern pattern = Pattern.compile("^[0-9]*$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(num);
		return matcher.matches();
	}

	public static Integer ttt = 1;

	public static void ttt(Integer t) {
		t = 100;
	}

	public enum KeyEnum {
		id, title, imgurl, category, ratingBar, ratingScore1, ratingScore2, director, actor, descrip, offering_id, episode, poster, recommend_rank, service_name, total_num
	}

	public static void main(String[] a) {

		/*
		 * try { download(); } catch (ClientProtocolException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		// ttt(ttt);
		
		try {
			testpost();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			//testSessinId();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public static void testSessinId() throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		// 创建一个本地Cookie存储的实例
		BasicCookieStore cookieStore = new BasicCookieStore();
		// 创建一个本地上下文信息
		HttpContext localContext = new BasicHttpContext();
		// 在本地上下问中绑定一个本地存储
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		// 设置请求的路径
		// HttpPost httpget = new HttpPost("http://192.168.7.95:8090/");
		// HttpPost httprequest = new
		// HttpPost("http://192.168.7.145/account/login");

	//	HttpGet httprequest = new HttpGet("http://192.168.7.56/sms/login.action");
		//HttpGet httprequest = new HttpGet("http://192.168.7.95:8090/");
		HttpGet httprequest = new HttpGet("http://192.168.7.56/smslite/login.action?a=b");
		
		//httprequest.setHeader("Cookie", "JSESSIONID=1111");
		// httprequest.addHeader(header)
		// httprequest.setHeader(name, value)
		// http://192.168.7.56/smslite/login.action

		HttpParams params = new BasicHttpParams();
		params.setParameter("j_username", "user");
		params.setParameter("j_password", "user ");
		// params.setParameter("ispsd", true);

		
		httprequest.setParams(params);
		httpclient.getParams().setParameter("j_username", "user");
		System.out.println(httprequest.getRequestLine().getUri());
		System.out.println(httprequest.getParams().getParameter("j_username"));
		// 传递本地的http上下文给服务器
		HttpResponse response = httpclient.execute(httprequest, localContext);
		
		
		
		// 获取本地信息
		HttpEntity entity = response.getEntity();
		//System.out.println(response.getStatusLine());
		if (entity != null) {
			System.out.println("Response content length: " + entity.getContentLength());
		}

		// 获取cookie中的各种信息
		List cookies = cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			Cookie c = (Cookie) cookies.get(i);
			System.out.println(c.getName()+"=========="+c.getValue());
			System.out.println(c);
		}
		// 获取消息头的信息
		/*Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			System.out.println(headers[i]);
		}*/

	}

	public static void download() throws ClientProtocolException, IOException {
		String url1 = "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg";
		URL url = new URL(url1);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		File file = new File("d:/a.jpg");
		FileOutputStream fos = new FileOutputStream(file);
		/*
		 * BufferedInputStream bin = new BufferedInputStream(inStream); int t =
		 * 0; while ((t = bin.read()) != -1) { fos.write(bin.read()); }
		 */
		// 缓存
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		fos.write(outStream.toByteArray());
		/*
		 * HttpGet httprequest = new HttpGet(url); HttpClient client = new
		 * DefaultHttpClient(); HttpResponse response =
		 * client.execute(httprequest); HttpEntity responseEntity =
		 * response.getEntity(); InputStream is = responseEntity.getContent();
		 * File file = new File("d:/a.jpg"); FileOutputStream fos = new
		 * FileOutputStream(file); BufferedInputStream bin = new
		 * BufferedInputStream (is); int t = 0; while ((t = bin.read()) != -1) {
		 * fos.write(bin.read()); }
		 */

		/*
		 * String url =
		 * "http://image1.webscache.com/ipad/201112/fe8b04754e42bf8d3cb20e3990603201.jpg"
		 * ; HttpGet httprequest = new HttpGet(url); HttpClient client = new
		 * DefaultHttpClient(); HttpResponse response =
		 * client.execute(httprequest); HttpEntity responseEntity =
		 * response.getEntity(); InputStream is = responseEntity.getContent();
		 * File file = new File("d:/a.jpg"); FileOutputStream fos = new
		 * FileOutputStream(file); BufferedInputStream bin = new
		 * BufferedInputStream (is); int t = 0; while ((t = bin.read()) != -1) {
		 * fos.write(bin.read()); }
		 */

		/*
		 * File storeFile = new File("d:/a.jpg"); FileOutputStream output = new
		 * FileOutputStream(storeFile);
		 * 
		 * output.write(response.getEntity().getContent().); output.close();
		 */

		/*
		 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); int vb = 0;
		 * try { while ((vb = bin.read()) != -1) { baos.write(vb); } } catch
		 * (IOException e) { }
		 */

		// HttpClient client = new DefaultHttpClient();
		/*
		 * HttpResponse response = client.execute(httprequest); HttpEntity
		 * responseEntity = response.getEntity(); InputStream is =
		 * responseEntity.getContent();
		 */
	}

	
	public static void testpost() throws ClientProtocolException, IOException{
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		HttpPost httprequest = new HttpPost("http://192.168.7.56/smslite/login.action");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("json", "==========json=============="));
		HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
		httprequest.setEntity(httpentity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httprequest, localContext);
		
	}
	
	public static void test() {
		/*
		 * String str1 = "aaa/bbb/ccc"; String[] s = str1.split("/"); for(String
		 * str :s){ System.out.println(s[s.length-1]); }
		 */
		// testlist();
		// System.out.println("==================="+list.get(0).get("1"));
		/*
		 * List list = new ArrayList(); User u = new User();
		 * u.setUserName("1111"); list.add(u); u.setUserName("2222"); User uu =
		 * (User) list.get(0); System.out.print(uu.getUserName());
		 */
		/*
		 * List<User> list = new ArrayList<User>();
		 * 
		 * User m = new User("2222"); User u1 = new User("1111"); list.add(m);
		 * list.add(u1); Map<Object,Object> map = new HashMap<Object,Object>();
		 * map.put(u1, "11111111"); map.put(m, "222222222");
		 */

		/*
		 * Iterator lit = list.iterator(); while(lit.hasNext()){ User u =(User)
		 * lit.next(); System.out.println("========="+u.getUserName()); }
		 */

		/*
		 * Iterator it = map.keySet().iterator(); while(it.hasNext()){
		 * System.out.println(map.keySet().size());
		 * //System.out.println(it.next()); String a = (String)
		 * map.get(it.next()); map.put(m, "3333333"); System.out.println(a);
		 * //System.out.println((map.get(it.next()))); }
		 */

		/*
		 * Object[] obj = map.keySet().toArray(); for(int i=0;i<obj.length;i++){
		 * System.out.println(map.get(obj[i])); }
		 */

		// System.out.println(map.get(u1));
		// System.out.println(map.get(m));

		/*
		 * map.put("1","10"); list.add(map); Map m1 =list.get(0); m1.put("1",
		 * "11");
		 */
		/*
		 * map2.put("1111", 1111+""); while(map2.containsKey("1111")){ try {
		 * Thread.sleep(500); } catch (InterruptedException e) {
		 * e.printStackTrace(); } System.out.println("=============");
		 * System.gc(); //System.out.println("============="+map2.get("1111"));
		 * }
		 */
		/*
		 * int t = 0xFFFFFFF; int t1 = 07777777777; System.out.println(t);
		 * System.out.println(t1);
		 */
		/*
		 * int a= hash(100); int a1= hash(100); System.out.println(a);
		 */
		/*
		 * int aaa = 1 << 30; System.out.println(aaa);
		 */
		/*
		 * Map map = new HashMap(0,0.0002f); map.put("11", "22222"); String aa=
		 * (String) map.put("11", "1111");
		 */

		/*
		 * int MAX_VALUE = 0x7fffffff; System.out.println(MAX_VALUE);
		 */
		// changNum2Chinese(99990000);
		List list = new ArrayList(2);
		for (int i = 0; i < 5; i++) {
			list.add(i);
		}
		Iterator it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

	}

	public static List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	public static void testlist() {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map1 = new LinkedHashMap<String, String>();
		Map<String, String> map2 = new WeakHashMap<String, String>();
		Map<String, String> map3 = new ConcurrentHashMap<String, String>();
		///Collections.synchronizedCollection(c)

		/*
		 * map.put("1","10"); list.add(map); Map m1 =list.get(0); m1.put("1",
		 * "11");
		 */
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("=============" + map2.get("1111"));
		System.gc();
		System.out.println("=============" + map2.get("1111"));
	}

	public static class User {
		public User(String name) {
			userName = name;
		}

		private String userName;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

	static int hash(int h) {
		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/**
	 * Description: 把输入的阿拉巴数字转换成大写中文输出 细节实现： 1、阿拉伯数字到大写中文数字的对应转换
	 * 2、单位转换，9位以上统一单位是亿，5-8位统一单位是万 3、用求商运算判断整数位数据及单位
	 * 4、用求模运算，获得该数去整后的余数，如果余数位数与求余前长度相差大于1，输出描述中按照中文习惯加零 5、把输出描述中后边的零全部干掉
	 * 
	 * @Version1.0 2011-12-19 下午02:16:54 mustang created
	 * @param value
	 */
	public static void changNum2Chinese(long value) {
		Map<String, String> danwei = new HashMap<String, String>();
		danwei.put("1", "");
		danwei.put("2", "拾");
		danwei.put("3", "佰");
		danwei.put("4", "仟");
		danwei.put("5", "万");
		danwei.put("6", "十");
		danwei.put("7", "百");
		danwei.put("8", "千");
		danwei.put("9", "亿");

		Map<String, String> daxie = new HashMap<String, String>();
		daxie.put("0", "");
		daxie.put("1", "壹");
		daxie.put("2", "贰");
		daxie.put("3", "叁");
		daxie.put("4", "肆");
		daxie.put("5", "伍");
		daxie.put("6", "陆");
		daxie.put("7", "柒");
		daxie.put("8", "捌");
		daxie.put("9", "玖");
		String str = "";
		long shang = 0;
		long yushu = value;
		long length = (yushu + "").length();
		while (length > 0) {
			shang = yushu / (long) Math.pow(10, length - 1);
			yushu = yushu % (long) Math.pow(10, length - 1);

			str += daxie.get(shang + "") + danwei.get(length + "");
			long nlength = (yushu + "").length();

			if (length > 9 && nlength < 9) {
				str += "亿";
			}
			if (length > 5 && nlength < 5) {
				str += "万";
			}

			if (length - nlength > 1) {
				str += "零";
			}

			if (length == 1) {
				length = 0;
			} else {
				length = (yushu + "").length();
			}
		}
		while (str.endsWith("零")) {
			str = str.substring(0, str.length() - 1);
		}
		System.out.println(str);
	}

}
