/**
 * ANT Task for android "res" directory concatenation 
 */
package ru.lsv.ant.androidresconcat;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * ANT Task for android "res" directory concatenation
 * 
 * @author s.lezhnev
 */
public class AndroidResConcat extends Task {

    /**
     * XML file extension
     */
    // private String[] xmlExt = {"xml"};
    /**
     * Factory для работы с XPath
     */
    private XPath xPath = XPathFactory.newInstance().newXPath();

    /**
     * Return collection of xml files from File (pointing to directory)
     * 
     * @param file
     *            File
     * @return Return collection of xml files in File (pointing to directory)
     */
    private Collection<File> extractFileList(File file) {
        Collection<File> files = null;
        if (file != null) {
            files = FileUtils.listFiles(file, null, true);
        } else {
            throw new BuildException("\"to\" or \"from\" must be specifyed");
        }
        return files;
    }

    /**
     * Выполняет вырезание пути до пути, содержащегося в "res"
     * 
     * @param fileNameIn
     *            Имя файла
     * @return Вырезанное имя файла или null, если в нем нет "res"
     */
    private String extractResPart(String fileNameIn) {
        String separator = "res" + File.separatorChar;
        String fileName = fileNameIn;
        int resPos = fileName.indexOf(separator);
        if (resPos > 0) {
            return fileName.substring(resPos + separator.length(),
                    fileName.length());
        } else {
            return null;
        }
    }

    /**
     * Parse xml document to DOM
     * 
     * @param file
     *            File to parse
     * @return DOM document
     * @throws SAXException
     *             see DocumentBuilder.parse
     * @throws IOException
     *             see DocumentBuilder.parse
     * @throws ParserConfigurationException
     *             see DocumentBuilder.parse
     */
    private Document parseXML(File file) throws SAXException, IOException,
            ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(file);
    }

    /**
     * Execute task
     * 
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        // Формируем файлы для обработки в "to"
        Collection<File> filesTo = extractFileList(toFile);
        Collection<File> filesFrom = extractFileList(fromFile);
        // Формируем из "to" HashMap<String, File> - чтобы было быстрее искать
        HashMap<String, File> filesToH = new HashMap<String, File>();
        for (Iterator<File> files = filesTo.iterator(); files.hasNext();) {
            File currFile = files.next();
            String tmpF = extractResPart(currFile.getAbsolutePath());
            // Добавляем только файлы ресурсов
            if (tmpF != null) {
                filesToH.put(tmpF, currFile);
            }
        }
        // Поехали по from...
        for (Iterator<File> files = filesFrom.iterator(); files.hasNext();) {
            File currFile = files.next();
            // Достаем "урезанное" имя файла
            String fileName = extractResPart(currFile.getAbsolutePath());
            // Проверяем - если это файл ресурса - то обрабатываем
            // Дополнительно проверяем что файл не начинается с prefixTo (в этом
            // случае с ним НИЧЕГО делать не надо!
            if ((fileName != null) &&
                    (!FilenameUtils.getName(fileName).toLowerCase()
                            .startsWith(prefixTo.toLowerCase()))) {
                // Проверяем на то, что такой файл есть в to
                // Дополнительно проверяем, что это xml. Иначе - мы его просто
                // копируем (см.ниже)
                // Дополнительно - если файл начинается с prefixFrom - то мы его
                // должны просто скопировать (см.ниже)
                if ((filesToH.containsKey(fileName)) &&
                        (!FilenameUtils.getName(fileName).toLowerCase()
                                .startsWith(prefixFrom.toLowerCase())) &&
                        ("xml".equals(FilenameUtils.getExtension(fileName)
                                .toLowerCase()))) {
                    // Значит мы нашли файл, который есть и там, и там
                    // И он не начинается с prefixFrom
                    // Значит его надо мержить....
                    Document fromDoc;
                    Document toDoc;
                    try {
                        toDoc = parseXML(filesToH.get(fileName));
                    } catch (Exception e) {
                        throw new BuildException("Cannot parse \"" +
                                filesToH.get(fileName).getAbsolutePath() + "\"");
                    }
                    try {
                        fromDoc = parseXML(currFile);
                    } catch (Exception e) {
                        throw new BuildException("Cannot parse   \"" +
                                currFile.getAbsolutePath() + "\"");
                    }
                    System.out.println("Merging \"" +
                            filesToH.get(fileName).getAbsolutePath() +
                            "\" and \"" + currFile.getAbsolutePath() + "\"");
                    // Пройдемся по toDoc - удалим ВСЕ вершины, у которых есть
                    // name,
                    // начинающийся с prefixFrom
                    for (int i = 0; i < toDoc.getChildNodes().getLength(); i++) {
                        deletePrefixed(toDoc, toDoc.getChildNodes().item(i),
                                prefixFrom);
                    }
                    // Поедем по fromDoc - и будем переносить в toDoc
                    for (int i = 0; i < fromDoc.getChildNodes().getLength(); i++) {
                        mergeNode(toDoc, fromDoc.getChildNodes().item(i),
                                prefixTo, prefixFrom);
                    }
                    // Сохраняем
                    Transformer transformer;
                    try {
                        transformer = TransformerFactory.newInstance()
                                .newTransformer();
                    } catch (Exception e) {
                        throw new BuildException(
                                "Cannot save changed xml to \"" +
                                        filesToH.get(fileName)
                                                .getAbsolutePath() +
                                        "\" - creating of transforming factory failed");
                    }
                    Result output = new StreamResult(filesToH.get(fileName));
                    Source input = new DOMSource(toDoc);
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty(
                            "{http://xml.apache.org/xslt}indent-amount", "4");
                    try {
                        transformer.transform(input, output);
                    } catch (TransformerException e) {
                        throw new BuildException(
                                "Cannot save changed xml to \"" +
                                        filesToH.get(fileName)
                                                .getAbsolutePath() + "\"");
                    }
                    System.out.println();
                } else {
                    // Просто копируем файл
                    // Копируются только файлы, которые начинаются с prefixFrom
                    File fileTo = new File(toFile.getAbsolutePath() +
                            File.separatorChar + "res" + File.separatorChar +
                            fileName);
                    // Копировать мы будем в 2-х случаях:
                    // 1. Если файла нет
                    // 2. Если файл начинается с prefixFrom
                    if ((!toFile.exists()) ||
                            (FilenameUtils.getName(fileName).toLowerCase()
                                    .startsWith(prefixFrom.toLowerCase()))) {
                        System.out.println("Copying \"" +
                                currFile.getAbsolutePath() + "\" to \"" +
                                toFile.getAbsolutePath() + File.separatorChar +
                                "res" + File.separatorChar + fileName + "\"");
                        try {
                            FileUtils.copyFile(currFile, fileTo);
                        } catch (IOException e) {
                            throw new BuildException("Cannot copy \"" +
                                    currFile.getAbsolutePath() + "\" to \"" +
                                    toFile.getAbsolutePath() +
                                    File.separatorChar + fileName + "\"");
                        }
                    }
                }
            }
        }
    }

    /**
     * Выполняет слияние вершины item с toDoc <br/>
     * Используются следующие правила: <br/>
     * 1. Если item не имеет атрибута name: <br/>
     * 1.1 Проверяется наличие в toDoc такой же вершины (по nodeName) <br/>
     * 1.2 Если такой вершины нет - она добавляется в toDoc. Если есть - то
     * игнорируется<br/>
     * 2. Если item имеет атрибут name: <br/>
     * 2.1 Если name начинается с prefixTo2 - оно игнорируется <br/>
     * 2.2 Если name начинается с prefixFrom2 - оно добавляется в toDoc <br/>
     * 2.3 Если name не начинается ни с prefixTo2, ни с prefixFrom2 - то
     * проверяется наличие такой вершины с таким name в toDoc. Если такой
     * вершины нет - она добавляется. Если есть - игнорируется
     * 
     * @param toDoc
     *            Куда проводить merge
     * @param item
     *            Вершина, которая должна быть смержена
     * @param prefixTo2
     *            Префикс toDoc
     * @param prefixFrom2
     *            Префикс fromDoc (откуда берется item)
     */
    private void mergeNode(Document toDoc, Node item, String prefixTo2,
            String prefixFrom2) {
        if (item.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        Node name = null;
        if (item.getAttributes() != null) {
            name = item.getAttributes().getNamedItem("name");
        }
        if (name != null) {
            if (name.getNodeValue().toLowerCase()
                    .startsWith(prefixFrom2.toLowerCase())) {
                // Добавляем в toDoc
                addToDoc(toDoc, item);
            } else if (!name.getNodeValue().toLowerCase()
                    .startsWith(prefixTo2.toLowerCase())) {
                // Надо проверить на то - есть ли такая вершина в toDoc
                if (!isNodePresent(toDoc, item, name.getNodeValue())) {
                    addToDoc(toDoc, item);
                    return;
                } else {
                    System.out.println("Found duplicated node - \"" +
                            getFullXPath(item, null) + "\"");
                }
            }
        } else {
            // Надо проверить на то - есть ли такая вершина в toDoc
            if (!isNodePresent(toDoc, item, null)) {
                addToDoc(toDoc, item);
                return;
            }
        }
        // А если мы попали сюда - то там надо еще и по чайлдам пройтись -
        // значит такая вершина ЕСТЬ в toDoc
        for (int i = 0; i < item.getChildNodes().getLength(); i++) {
            mergeNode(toDoc, item.getChildNodes().item(i), prefixTo2,
                    prefixFrom2);
        }
    }

    /**
     * Добавляет вершину item в toDoc
     * 
     * @param toDoc
     *            Документ, куда добавляется вершина
     * @param item
     *            Вершина, которую надо добавить
     */
    private void addToDoc(Document toDoc, Node item) {
        StringBuffer res = getFullXPath(item, null);
        System.out.println("Adding " + res);
        Node imported = toDoc.importNode(item, true);
        if ((item.getParentNode() != null) &&
                (item.getParentNode().getNodeType() != Node.DOCUMENT_NODE)) {
            // Значит тут оно где-то унутре
            // Поищем через xpath
            StringBuffer xpath = getFullXPath(item.getParentNode(), null);
            NodeList nl;
            try {
                nl = (NodeList) xPath.compile(xpath.toString()).evaluate(toDoc,
                        XPathConstants.NODESET);
            } catch (XPathExpressionException e) {
                nl = null;
            }
            if ((nl != null) && (nl.getLength() > 0)) {
                // Добавляем в первое
                nl.item(0).appendChild(imported);
            } else {
                // Ищем по сокращенному xpath
                xpath = getXPath(item.getParentNode(), null);
                try {
                    nl = (NodeList) xPath.compile(xpath.toString()).evaluate(
                            toDoc, XPathConstants.NODESET);
                } catch (XPathExpressionException e) {
                    nl = null;
                }
                if ((nl != null) && (nl.getLength() > 0)) {
                    // Добавляем в первое
                    nl.item(0).appendChild(imported);
                } else {
                    // А тут все плохо - никуда ничего добавить не смогли. БАТ
                    // ВАЙ?!
                    // Падаем. Хотя не должны, по идее
                    throw new BuildException("Cannot find parent node for " +
                            getFullXPath(item, null));
                }
            }
        } else {
            // Добавляем ТУПО к документу!
            toDoc.adoptNode(imported);
        }
    }

    /**
     * Формирует ПОЛНОЕ XPath описание вершины (с атрибутами!)
     * 
     * @param item
     *            Вершина для описания
     * @param in
     *            Предварительно сформированиое описание по child'ам
     * @return Сформированное ПОЛНОЕ XPath описание
     */
    private StringBuffer getFullXPath(Node item, StringBuffer in) {
        StringBuffer res;
        if (in == null) {
            res = new StringBuffer(item.getNodeName()).insert(0, "/");
        } else {
            res = in.insert(0, item.getNodeName()).insert(0, "/");
        }
        NamedNodeMap nl = item.getAttributes();
        for (int i = 0; i < nl.getLength(); i++) {
            res.append("[@").append(nl.item(i).getNodeName()).append("=\'")
                    .append(nl.item(i).getNodeValue()).append("\']");
        }
        if ((item.getParentNode() != null) &&
                (item.getParentNode().getNodeType() != Node.DOCUMENT_NODE)) {
            res = getFullXPath(item.getParentNode(), res);
        }
        return res;
    }

    /**
     * Проверка на наличие ноды item в документе toDoc
     * 
     * @param toDoc
     *            Документ, в котором проверяется наличие
     * @param item
     *            Вершина, наличие которой проверяется
     * @param name
     *            Значение атрибута "name" для вершины item (или null - если
     *            атрибута нет)
     * @return true - если такая вершина существует в документе, false - иначе
     */
    private boolean isNodePresent(Document toDoc, Node item, String name) {
        // Строим xpath для item
        String xpath = getXPath(item, null).toString();
        if (name != null) {
            xpath = xpath + "[@name=\'" + name + "\']";
        }
        NodeList nl;
        try {
            nl = (NodeList) xPath.compile(xpath).evaluate(toDoc,
                    XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            return false;
        }
        return (nl != null) && (nl.getLength() > 0);
    }

    /**
     * Удаляет вершины, атрибут "name" которых начинающиется с указанного
     * префикса
     * 
     * @param parentNode
     *            Родительская вершина (чтобы иметь возможность удалять вершины)
     * @param item
     *            Вершина для обработки
     * @param prefix
     *            Префикс
     */
    private void deletePrefixed(Node parentNode, Node item, String prefix) {
        if (item.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        Node name = null;
        if (item.getAttributes() != null) {
            name = item.getAttributes().getNamedItem("name");
        }
        if (name != null) {
            if (name.getNodeValue().toLowerCase()
                    .startsWith(prefix.toLowerCase())) {
                // Удаляем вершину
                System.out.println("Remove " + getXPath(item, null).toString() +
                        "[@name=\'" + name.getNodeValue() + "\']");
                parentNode.removeChild(item);
            }
        } else {
            for (int i = 0; i < item.getChildNodes().getLength(); i++) {
                deletePrefixed(item, item.getChildNodes().item(i), prefix);
            }
        }
    }

    /**
     * Формирует XPath описание вершины (БЕЗ атрибутов!)
     * 
     * @param item
     *            Вершина
     * @param in
     *            Уже сформированный кусок xpath
     * @return xpath
     */
    private StringBuffer getXPath(Node item, StringBuffer in) {
        StringBuffer res;
        if (in == null) {
            res = new StringBuffer(item.getNodeName()).insert(0, "/");
        } else {
            res = in.insert(0, item.getNodeName()).insert(0, "/");
        }
        if ((item.getParentNode() != null) &&
                (item.getParentNode().getNodeType() != Node.DOCUMENT_NODE)) {
            res = getXPath(item.getParentNode(), res);
        }
        return res;
    }

    /**
     * todir
     */
    protected File toFile = null;
    /**
     * fromdir
     */
    protected File fromFile = null;
    /**
     * prefixTo
     */
    protected String prefixTo;
    /**
     * prefixFrom
     */
    protected String prefixFrom;

    /**
     * @param toFileIn
     *            the to to set
     */
    public void setto(String toFileIn) {
        this.toFile = new File(toFileIn);
        if (!this.toFile.isDirectory()) {
            throw new BuildException("\"to\" must be a directory");
        }
    }

    /**
     * @param fromFileIn
     *            the from to set
     */
    public void setfrom(String fromFileIn) {
        this.fromFile = new File(fromFileIn);
        if (!this.fromFile.isDirectory()) {
            throw new BuildException("\"from\" must be a directory");
        }
    }

    /**
     * @param prefixIn
     *            the prefixTo to set
     */
    public void setprefixto(String prefixIn) {
        this.prefixTo = prefixIn;
    }

    /**
     * @param prefixIn
     *            the prefixFrom to set
     */
    public void setprefixfrom(String prefixIn) {
        this.prefixFrom = prefixIn;
    }

}
