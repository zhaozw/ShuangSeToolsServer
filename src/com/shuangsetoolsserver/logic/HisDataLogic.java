package com.shuangsetoolsserver.logic;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.shuangsetoolsserver.db.DBUtil;
import com.shuangsetoolsserver.meta.CodeItem;

public class HisDataLogic {

    // private final static String TAG = "HisDataLogic";

    public int getLatestHisItemID() throws SQLException {
        String sql = "select max(itemid) from historydata";
        DBUtil db = new DBUtil();

        int latestID = db.readInt(sql);

        db.release();

        return latestID;
    }

    /**
     * 获取从startItemID（包含） 到 endItemID （包含） 在内的历史数据 如果二者相等，获取当期数据
     */
    public Vector<CodeItem> getHisDataList(int startItemID, int endItemID)
            throws SQLException {

        if (endItemID < startItemID) {
            throw new SQLException("Wrong parameter");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select * from historydata where itemid > ")
                .append(startItemID).append(" and itemid <= ")
                .append(endItemID).append(" order by itemid asc");

        Vector<CodeItem> hisDataList = new Vector<CodeItem>(endItemID
                - startItemID + 1);

        DBUtil db = new DBUtil();

        ResultSet rs = db.read(sql.toString());
        while (rs != null && rs.next()) {
            CodeItem item = new CodeItem(rs.getInt("itemid"),
                    rs.getInt("red1"), rs.getInt("red2"), rs.getInt("red3"),
                    rs.getInt("red4"), rs.getInt("red5"), rs.getInt("red6"),
                    rs.getInt("blue"));

            item.setOpenDate(rs.getString("openDate"));
            item.setTotalSale(rs.getInt("totalSale"));
            item.setPoolTotal(rs.getInt("poolTotal"));
            item.setFirstPrizeCnt(rs.getInt("firstPrizeCnt"));
            item.setFirstPrizeValue(rs.getInt("firstPrizeValue"));
            item.setSecondPrizeCnt(rs.getInt("secondPrizeCnt"));
            item.setSecondPrizeValue(rs.getInt("secondPrizeValue"));

            hisDataList.add(item);
        }

        db.release();

        return hisDataList;
    }

    public CodeItem getCodeItem(int itemID) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from historydata where itemid = ").append(itemID);
        
        CodeItem item = null;
        
        try {
            DBUtil db = new DBUtil();
        
            ResultSet rs = db.read(sql.toString());
            if (rs != null && rs.next()) {
                item = new CodeItem(rs.getInt("itemid"), rs.getInt("red1"),
                        rs.getInt("red2"), rs.getInt("red3"),
                        rs.getInt("red4"), rs.getInt("red5"),
                        rs.getInt("red6"), rs.getInt("blue"));

                item.setOpenDate(rs.getString("openDate"));
                item.setTotalSale(rs.getInt("totalSale"));
                item.setPoolTotal(rs.getInt("poolTotal"));
                item.setFirstPrizeCnt(rs.getInt("firstPrizeCnt"));
                item.setFirstPrizeValue(rs.getInt("firstPrizeValue"));
                item.setSecondPrizeCnt(rs.getInt("secondPrizeCnt"));
                item.setSecondPrizeValue(rs.getInt("secondPrizeValue"));
            }

            db.release();

            return item;
        } catch (Exception ex) {
            return null;
        }
    }

    public String getXMLStr(Vector<CodeItem> hisData)
            throws ParserConfigurationException, TransformerException {
        String xmlStr = "";
        int size = hisData.size();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        /****************************************************************
         * //<?xml version="1.0" encoding="UTF-8"?> //<HISDATA> //<ITEM>
         * //<ID>2012012</ID> //<OPENDATE>2012-01-06</OPENDATE> //<RED>1</RED>
         * //<RED>2</RED> //<RED>3</RED> //<RED>4</RED> //<RED>5</RED>
         * //<RED>6</RED> //<BLUE>1</BLUE> //</ITEM> // ... //</HISDATA>
         *****************************************************************/
        // root elements
        Document doc = docBuilder.newDocument();
        Element rootEle = doc.createElement("HISDATA");
        doc.appendChild(rootEle);

        for (int i = 0; i < size; i++) {
            CodeItem tmpItem = hisData.get(i);
            Element itemEle = doc.createElement("ITEM");

            Element idEle = doc.createElement("ID");
            idEle.appendChild(doc.createTextNode(Integer.toString(tmpItem
                    .getId())));
            itemEle.appendChild(idEle);

            Element dateEle = doc.createElement("OPENDATE");
            dateEle.appendChild(doc.createTextNode(tmpItem.getOpenDate()));
            itemEle.appendChild(dateEle);

            for(int index=0;index<6;index++) {
                Element redEle = doc.createElement("RED");
                redEle.appendChild(doc.createTextNode(Integer.toString(tmpItem
                    .getRed(index))));
                itemEle.appendChild(redEle);
            }

            Element blueEle = doc.createElement("BLUE");
            blueEle.appendChild(doc.createTextNode(Integer.toString(tmpItem
                    .getBlue())));
            itemEle.appendChild(blueEle);

            rootEle.appendChild(itemEle);
        }

        // transfer the content into String for output
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(bos));
        xmlStr = bos.toString();

        // Log.i(TAG, "getXMLStr(): " + xmlStr);

        return xmlStr;
    }
}
