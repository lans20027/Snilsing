package org.tfoms.snils.xmls;

import org.tfoms.snils.model.FindSnils;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class XmlParser {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public void createDocument(FindSnils person){
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element realRoot = document.createElement("tns1:ClientMessage");
            realRoot.setAttribute("xmlns:tns1","urn://x-artefacts-smev-gov-ru/services/service-adapter/types");
            realRoot.setAttribute("xmlns:ns2","urn://x-artefacts-smev-gov-ru/services/service-adapter/types/faults");
            document.appendChild(realRoot);

            Element system = document.createElement("tns1:itSystem");
            system.appendChild(document.createTextNode("542202_3S"));
            realRoot.appendChild(system);


            Element requestMessage = document.createElement("tns1:RequestMessage");


            Element messageType = document.createElement("tns1:messageType");
            messageType.appendChild(document.createTextNode("RequestMessageType"));

            requestMessage.appendChild(messageType);


            Element requestMetaData = document.createElement("tns1:RequestMetadata");
            Element clientId = document.createElement("tns1:clientId");
            clientId.appendChild(document.createTextNode(person.getGd()));
            Element testMessage = document.createElement("testMessage");
            testMessage.appendChild(document.createTextNode("false"));
            requestMetaData.appendChild(clientId);
            requestMetaData.appendChild(testMessage);

            requestMessage.appendChild(requestMetaData);



            Element requestContent = document.createElement("tns1:RequestContent");
            Element content = document.createElement("tns1:content");
            Element messagePrimaryContent = document.createElement("tns1:MessagePrimaryContent");

            // root element
            Element root = document.createElement("tns:SnilsByAdditionalDataRequest");
            root.setAttribute("xmlns:smev","urn://x-artefacts-smev-gov-ru/supplementary/commons/1.0.1");
            root.setAttribute("xmlns:pfr","http://common.kvs.pfr.com/1.0.0");
            root.setAttribute("xmlns:tns","http://kvs.pfr.com/snils-by-additionalData/1.0.1");


            // фамилия
            Element familyName = document.createElement("smev:FamilyName");
            familyName.appendChild(document.createTextNode(person.getFam()));
            root.appendChild(familyName);

            // фамилия
            Element firstName = document.createElement("smev:FirstName");
            firstName.appendChild(document.createTextNode(person.getIm()));
            root.appendChild(firstName);

            // фамилия
            Element patronymic = document.createElement("smev:Patronymic");
            patronymic.appendChild(document.createTextNode(person.getOt()));
            root.appendChild(patronymic);

            Element birthDate = document.createElement("tns:BirthDate");
            birthDate.appendChild(document.createTextNode(dateFormat.format(person.getDr())));
            root.appendChild(birthDate);

            Element gender = document.createElement("tns:Gender");
            gender.appendChild(document.createTextNode(person.getPerson().getSex()));
            root.appendChild(gender);


            Element birthPlace = document.createElement("tns:BirthPlace");

            Element placeType = document.createElement("pfr:PlaceType");
            placeType.appendChild(document.createTextNode("ОСОБОЕ"));

            Element settlement = document.createElement("pfr:Settlement");
            settlement.appendChild(document.createTextNode("ХАБАРОВСК"));

////            Element disctrict = document.createElement("pfr:District");
////            disctrict.appendChild(document.createTextNode("ЖЕЛЕЗИНСКИЙ"));
////
//            Element region = document.createElement("pfr:Region");
//            region.appendChild(document.createTextNode("ИРКУТСКАЯ ОБЛАСТЬ"));
//
//            Element country = document.createElement("pfr:Country");
//            country.appendChild(document.createTextNode("РОССИЙСКАЯ ФЕДЕРАЦИЯ"));

            birthPlace.appendChild(placeType);
            birthPlace.appendChild(settlement);
//            birthPlace.appendChild(disctrict);
//            birthPlace.appendChild(region);
//            birthPlace.appendChild(country);


            root.appendChild(birthPlace);


            Element passport = document.createElement("smev:PassportRF");
            Element series = document.createElement("smev:Series");
            series.appendChild(document.createTextNode(person.getSerdoc().replaceAll(" ","")));
            Element number = document.createElement("smev:Number");
            number.appendChild(document.createTextNode(person.getNumdoc()));
            Element issueDate = document.createElement("smev:IssueDate");
            issueDate.appendChild(document.createTextNode(dateFormat.format(person.getPersonadd().getDatepassport())));
            Element issuer = document.createElement("smev:Issuer");
            issuer.appendChild(document.createTextNode(person.getPersonadd().getDok_vi()!=null?person.getPersonadd().getDok_vi():"-"));


            passport.appendChild(series);
            passport.appendChild(number);
            passport.appendChild(issueDate);
            passport.appendChild(issuer);
            root.appendChild(passport);



            messagePrimaryContent.appendChild(root);
            content.appendChild(messagePrimaryContent);
            requestContent.appendChild(content);
            requestMessage.appendChild(requestContent);

            realRoot.appendChild(requestMessage);











            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("documents/" + person.getGd() + ".xml"));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");



        }catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
