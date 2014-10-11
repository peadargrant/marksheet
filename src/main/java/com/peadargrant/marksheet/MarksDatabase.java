/*
 *  Copyright Peadar Grant.
 *  All rights reserved.
 */
package com.peadargrant.marksheet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Peadar Grant
 */
public class MarksDatabase {
    
    private Document doc;
    private XPath xp;
    
    public MarksDatabase(String filename) throws Exception {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder db = dbf.newDocumentBuilder();
        File inputFile = new File(filename);
        doc = db.parse(inputFile);
        doc.normalize();

        XPathFactory xpf = XPathFactory.newInstance();
        xp = xpf.newXPath();
        
    }
    
    public int marksPerQuestion() throws Exception {
        XPathExpression xpe = xp.compile("/marks/marksPerQuestion");
        Double marksPerQuestion = (Double) xpe.evaluate(doc, XPathConstants.NUMBER);
        return marksPerQuestion.intValue();
    }
    
    public int bestOf() throws Exception {
        XPathExpression xpe = xp.compile("/marks/bestOf");
        Double marksPerQuestion = (Double) xpe.evaluate(doc, XPathConstants.NUMBER);
        return marksPerQuestion.intValue();
    }
    
    public List<List<Integer>> questions() throws Exception {
        
        XPathExpression xpe = xp.compile("/marks/question");
        NodeList questionList = (NodeList) xpe.evaluate(doc, XPathConstants.NODESET);
        
        List<List<Integer>> marksList = new ArrayList<>(); 
        
        XPathExpression partExpression = xp.compile("part");
        
        for ( int k = 0 ; k < questionList.getLength(); k++ ) {
            
            List<Integer> partMarksList = new ArrayList<>();
            
            Node question = questionList.item(k);
            NodeList partsList = (NodeList) partExpression.evaluate(question, XPathConstants.NODESET);
            for ( int m = 0 ; m < partsList.getLength() ; m++ ) {
                Node part = partsList.item(m);
                partMarksList.add( new Integer(part.getTextContent()) );
            }
                
            marksList.add(partMarksList);
        }
        return marksList;
        
    }
    
}
