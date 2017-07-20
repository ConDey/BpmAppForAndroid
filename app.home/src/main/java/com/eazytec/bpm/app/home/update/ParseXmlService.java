package com.eazytec.bpm.app.home.update;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author 16735
 * @version Id: ParseXmlService, v 0.1 2017-7-20 10:29 16735 Exp $$
 */
public class ParseXmlService {

    public HashMap<String, String> parseXml(InputStream inStream)
            throws Exception {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        // 实例化一个文档构建器工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 通过文档构建器工厂获取一个文档构建器
        DocumentBuilder builder = factory.newDocumentBuilder();
        // 通过文档通过文档构建器构建一个文档实例
        Document document = builder.parse(inStream);
        // 获取XML文件根节点
        Element root = document.getDocumentElement();
        // 获得所有子节点
        // NodeList childNodes = root.getChildNodes();
        NodeList childNodes = root.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
            // 遍历子节点
            Node childNode = (Node) childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                hashMap.put(childElement.getNodeName(), ReturnStr(childElement));
            }
        }
        return hashMap;
    }

    private static String ReturnStr(Element childElement) {
        String str = "";
        if (childElement.hasChildNodes()) {
            if (childElement.getFirstChild() != null) {
                if (childElement.getFirstChild().getNodeValue() != null) {
                    str = childElement.getFirstChild().getNodeValue();
                }
            }
        }
        return str;

    }
}
