import java.awt.*;
import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.List;
import java.util.Map;


public class Login {
    public static void main(String[] args) throws AWTException {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        String returnString = HttpUtil.sendGet("http://10.255.255.11:801/eportal/?c=Portal&a=login&callback=dr1003&login_method=1&user_account=8209200504%40unicom&user_password=281272&wlan_user_ip=10.36.152.67&wlan_user_ipv6=&wlan_user_mac=A87EEA76C870&wlan_ac_ip=&wlan_ac_name=&jsVersion=3.3.2&v=3356",
                null);

        System.out.println("\nReturned with:" + returnString);
        try {
            if (returnString.contains("\"result\":\"1\""))
                WindowsUtil.displayTray("铁道幼儿园-Authentication", "现已成功认证校园网！\n通过10.36.152.67验证\n于A8-7E-EA-76-C8-70之上\n*拥有Internet访问*");
            else
                WindowsUtil.displayTray("铁道幼儿园-Authentication", "此前已认证过校园网！\n通过10.36.152.67验证\n于A8-7E-EA-76-C8-70之上\n*拥有Internet访问*");
        } catch (AWTException awtException) {
            System.out.println("System Tray is not support!");
            awtException.printStackTrace();
        }
        System.exit(0);
    }
}

class WindowsUtil {
    public static void displayTray(String caption, String text) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage("success.png");

        TrayIcon trayIcon = new TrayIcon(image, "Auto-Authentication");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("校园网自动认证");
        tray.add(trayIcon);

        trayIcon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
    }
}

class HttpUtil {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) throws AWTException {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "/" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + " ---> " + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            WindowsUtil.displayTray("铁道幼儿园-Authentication", "没有连接至校内网络！\n于A8-7E-EA-76-C8-70之上\n*尚未拥有Internet访问*");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.exit(-1);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
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
        return result;
    }
}