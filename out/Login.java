import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;


public class Login {
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数,json格式，请求参数应该是{name:value1,name2:value2}的形式。
     * @return 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        URLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            conn.setRequestProperty("Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(120000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            // out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream stream = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String loginStatus = sendPost("http://10.255.255.11:801/eportal/?c=Portal&a=login&callback=dr1003&login_method=1&user_account=8209200504%40unicom&user_password=281272&wlan_user_ip=10.36.152.67&wlan_user_ipv6=&wlan_user_mac=A87EEA76C870&wlan_ac_ip=&wlan_ac_name=&jsVersion=3.3.2&v=3356","c: Portal\n" +
                "a: login\n" +
                "callback: dr1003\n" +
                "login_method: 1\n" +
                "user_account: 8209200504@unicom\n" +
                "user_password: 281272\n" +
                "wlan_user_ip: 10.36.152.67\n" +
                "wlan_user_ipv6: \n" +
                "wlan_user_mac: A87EEA76C870\n" +
                "wlan_ac_ip: \n" +
                "wlan_ac_name: \n" +
                "jsVersion: 3.3.2\n" +
                "v: 8586");

        if (loginStatus.contains("\"result\":\"1\""))
            System.out.println("已经认证成功！");
        else
            System.out.println("认证失败！已经认证过了呢！");
    }
}