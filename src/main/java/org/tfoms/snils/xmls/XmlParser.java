package org.tfoms.snils.xmls;

import org.tfoms.snils.model.FindSnils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.tfoms.snils.model.Person;
import org.tfoms.snils.model.Personadd;
import org.tfoms.snils.model.TablePerson;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class XmlParser {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public boolean createDocument(FindSnils person){
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

//            Element placeType = document.createElement("pfr:PlaceType");
//            placeType.appendChild(document.createTextNode("СТАНДАРТНОЕ"));

            Element settlement = document.createElement("pfr:Settlement");
            settlement.appendChild(document.createTextNode("НОВОСИБИРСК"));

//            Element disctrict = document.createElement("pfr:District");
//            disctrict.appendChild(document.createTextNode("ЖЕЛЕЗИНСКИЙ"));
//
//            Element region = document.createElement("pfr:Region");
//            region.appendChild(document.createTextNode("АЛТАЙСКИЙ КРАЙ"));
////
//            Element country = document.createElement("pfr:Country");
//            country.appendChild(document.createTextNode("РОССИЙСКАЯ ФЕДЕРАЦИЯ"));

//            birthPlace.appendChild(placeType);
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
            passport.appendChild(issueDate);
            Element issuer = document.createElement("smev:Issuer");
            issuer.appendChild(document.createTextNode(person.getPersonadd().getDok_vi()!=null ? person.getPersonadd().getDok_vi():"-"));
            passport.appendChild(issuer);

            passport.appendChild(series);
            passport.appendChild(number);


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
//            StreamResult streamResult = new StreamResult(new File("\\\\Srv-term03\\542202_3s\\out\\" + person.getGd() + ".xml"));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);
            return true;
        }catch (ParserConfigurationException pce) {
//            pce.printStackTrace();
            return false;
        } catch (TransformerException tfe) {
            return false;
//            tfe.printStackTrace();
        } catch (NullPointerException npe){
            System.out.println("NPE:" + person);
            return false;
        }
    }

    public boolean createDocument(TablePerson person) throws NullPointerException,ParserConfigurationException
            , TransformerException{

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
            clientId.appendChild(document.createTextNode(person.getEnp() + "_" + UUID.randomUUID().toString().substring(0,23)));
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
            familyName.appendChild(document.createTextNode(person.getPersonSurname()));
            root.appendChild(familyName);

            // фамилия
            Element firstName = document.createElement("smev:FirstName");
            firstName.appendChild(document.createTextNode(person.getPersonFirstname()));
            root.appendChild(firstName);

            // фамилия
            Element patronymic = document.createElement("smev:Patronymic");
            patronymic.appendChild(document.createTextNode(person.getPersonLastname()));
            root.appendChild(patronymic);

            Element birthDate = document.createElement("tns:BirthDate");
            birthDate.appendChild(document.createTextNode(dateFormat.format(person.getPersonBirthday())));
            root.appendChild(birthDate);

            Element gender = document.createElement("tns:Gender");
            gender.appendChild(document.createTextNode(person.getSex()));
            root.appendChild(gender);

            if(person.getPersonadd() != null) {
                Element birthPlace = parseBirthPlace(person.getPersonadd(), document);
                if (birthPlace != null) {
                    root.appendChild(birthPlace);
                }
            }

            Element passport = document.createElement("smev:PassportRF");

            Element series = document.createElement("smev:Series");
            series.appendChild(document.createTextNode(person.getPersonSerdoc().replaceAll(" ","")));
            passport.appendChild(series);

            Element number = document.createElement("smev:Number");
            number.appendChild(document.createTextNode(person.getPersonNumdoc()));
            passport.appendChild(number);

            if(person.getPersonadd() != null) {
                if (person.getPersonadd().getDatepassport() != null) {
                    Element issueDate = document.createElement("smev:IssueDate");
                    issueDate.appendChild(document.createTextNode(person.getPersonadd().getDatepassport() != null ? dateFormat.format(person.getPersonadd().getDatepassport()) : "-"));
                    passport.appendChild(issueDate);
                }
                if (person.getPersonadd().getDok_vi() != null) {
                    Element issuer = document.createElement("smev:Issuer");
                    issuer.appendChild(document.createTextNode(person.getPersonadd().getDok_vi() != null ? person.getPersonadd().getDok_vi() : "-"));
                    passport.appendChild(issuer);
                }
            }
            root.appendChild(passport);



            messagePrimaryContent.appendChild(root);
            content.appendChild(messagePrimaryContent);
            requestContent.appendChild(content);
            requestMessage.appendChild(requestContent);

            realRoot.appendChild(requestMessage);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult1 = new StreamResult(new File("documents/" + person.getEnp() + ".xml"));
            StreamResult streamResult = new StreamResult(new File("\\\\Srv-term03\\542202_3s\\out\\" + person.getEnp() + ".xml"));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);
            transformer.transform(domSource, streamResult1);
            System.out.println(person.getEnp() + " - create good!");
            return true;
    }



    private Element parseBirthPlace(Personadd p,Document document){
            System.out.println("parse address:" + p);
            String bornString = p.getBorn();
            if(bornString == null || bornString.length() == 0) return null;
            bornString = bornString.toUpperCase();

            String[] prefTown = {"гор","город","село","с","г.","гор.","г","с.","п.","пос.","р.п.","р-п","пос.г.т.","п.г.т.","д.","дер.","деревня","пгт","пгт.","рп"};
            String[] postReg = {"ОБЛАСТЬ","ОБЛ.","ОБЛАСТИ"};
            String[] postReg1 = {"КРАЙ","КРАЯ","КР."};
            String[] postDist = {"РАЙОН","РАЙОНА","Р-ОН","Р-ОНА","Р-НА","Р-Н"};

            String town = "";
            String region = "";
            int regInd = 0;

            String district = "";

            if(!bornString.trim().contains(" ")){
                town = bornString.trim().toUpperCase();
            }else{
                String[] strs = bornString.trim().split(" ");
                System.out.println(strs.length);
                for(int i = 0; i < strs.length; i++){
                    for(String prefix : prefTown){
                        if(prefix.equalsIgnoreCase(strs[i])){
                            town = strs[i + 1].toUpperCase();
                            if(town.equals("НИЖНИЙ") || town.equals("ВЕРХНИЙ")){
                                town = town + " " + strs[i + 2];
                            }
                        }
                    }

                    for(String postfix : postReg){
                        if(postfix.equals(strs[i].replaceAll(",",""))){
                            region = strs[i - 1];
                        }
                    }

                    for(String postfix : postReg1){
                        if(postfix.equals(strs[i].replaceAll(",",""))){
                            region = strs[i - 1];
                            regInd = 1;
                        }
                    }

                    for(String postfix : postDist){
                        if(postfix.equals(strs[i].replaceAll(",",""))){
                            district = strs[i - 1];
                        }
                    }

                }

                for(String s : strs){
                    for(String prefix : prefTown){
                        if(prefix.contains(".")){
                            if(s.startsWith(prefix.toUpperCase()) && s.length() > prefix.length()){
                                town = (s.substring(prefix.length()));
                            }
                        }
                    }
                }
                town = town.replaceAll(",","");
                region = region.replaceAll(",","");
            }



            if(town.equals("")) return null;

            if(region.endsWith("КОГО")){
                region = region.substring(0,region.length() - 4);
                region = region + "КИЙ" + " " + "КРАЙ";
                //(regInd == 1 ? postReg1[0] : postReg[0])новосибирской
            }else if(region.endsWith("КОЙ")){
                region = region.substring(0,region.length() - 3)
                        + "КАЯ" + " " + "ОБЛАСТЬ";
            }else if(!region.equals("")){
                region = region + " " + (regInd == 1 ? postReg1[0] : postReg[0]);
            }

            if(district.endsWith("КОГО")){
                district = district.substring(0,district.length() - 4) + "КИЙ";
            }else if(district.endsWith("НОГО")){
                district = district.substring(0, district.length() - 4) + "НЫЙ";
            }

            System.out.println("town:" + town);
            System.out.println("district:" + district);
            System.out.println("region:" + region);

            Element birthPlace = document.createElement("tns:BirthPlace");
            Element placeType = document.createElement("pfr:PlaceType");
            placeType.appendChild(document.createTextNode((!district.equals("") && !region.equals("")) ? "СТАНДАРТНОЕ" : "ОСОБОЕ"));
            birthPlace.appendChild(placeType);


            Element settlement = document.createElement("pfr:Settlement");
            settlement.appendChild(document.createTextNode(town));
            birthPlace.appendChild(settlement);

            if(!district.equals("")) {
                Element disctrict = document.createElement("pfr:District");
                disctrict.appendChild(document.createTextNode(district));
                birthPlace.appendChild(disctrict);
            }
            if(!region.equals("")) {
                Element reg = document.createElement("pfr:Region");
                reg.appendChild(document.createTextNode(region));
                birthPlace.appendChild(reg);
            }
            /*
            Element country = document.createElement("pfr:Country");
            country.appendChild(document.createTextNode("РОССИЙСКАЯ ФЕДЕРАЦИЯ"));
            birthPlace.appendChild(country);
            */

            return birthPlace;
    }
}
