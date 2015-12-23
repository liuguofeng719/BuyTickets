package com.ticket.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLUtil {
    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     */
    public static Map doXMLParse(String strxml) {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map hashMap = new HashMap();
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例
            Document rootElement = builder.parse(in);   //解析输入流 得到Document实例
            NodeList items = rootElement.getElementsByTagName("xml");
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                NodeList properties = item.getChildNodes();
                for (int j = 0; j < properties.getLength(); j++) {
                    Node property = properties.item(j);
                    String nodeName = property.getNodeName();
                    hashMap.put(nodeName,property.getFirstChild().getNodeValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hashMap;
    }
    public static void main(String[] args) {
        Map map = doXMLParse("<xml>" +
                "<return_code" +
                "><![CDATA[SUCCESS]]>" +
                "</return_code>" +
                "<return_msg>" +
                "<![CDATA[OK]]>" +
                "</return_msg>" +
                "<appid>" +
                "<![CDATA[wxf8982470f270c06e]]>" +
                "</appid>" +
                "<mch_id><![CDATA[1231617402]]></mch_id>" +
                "<nonce_str><![CDATA[EIFKmHAnfSqERogR]]>" +
                "</nonce_str>" +
                "<sign><![CDATA[EBE96ED080969BF75052B8AB2EC9ACC4]]></sign>" +
                "<result_code><![CDATA[SUCCESS]]></result_code>" +
                "<prepay_id><![CDATA[wx2015092401131637d556a9da0148190008]]></prepay_id>" +
                "<trade_type><![CDATA[JSAPI]]></trade_type>" +
                "</xml>");
        System.out.println(map.toString());
    }
}
