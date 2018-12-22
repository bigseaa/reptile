package com.bigsea.utils;


import net.htmlparser.jericho.Source;
import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * @Description 通过httpclient获取url中的信息
 * @author  sea
 * @Date 2018-12-16
 */
public class DataUtil {

    public static Source getSource(String path) throws IOException {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString().trim();
            Result result = HttpUtil.get(path, null, null);
            int statusCode = result.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("访问网络出错.网址：" + path );
            }
            String response = result.getBody();
            InputStream is = new ByteArrayInputStream(response.getBytes());
            Source source = new Source(new InputStreamReader(is, "utf-8"));

            source.getSource().clearCache();
            return source;
    }

}
