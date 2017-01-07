package com.ushine.storeInfo;


import java.io.File;
 
/*import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;*/
 
public class Convertion {
 
    /*private ActiveXComponent objWord;
    private Dispatch document;
 
    private Dispatch wordObject;
 
    public void open(String filename) {
        ComThread.InitSTA();
        // 实例化 objWord
        objWord = new ActiveXComponent("Word.Application");
 
        // 将本地word对象赋到 wordObject上
        wordObject = objWord.getObject();
 
        // 使 word 为"可见"，主要是方便调试。正式应用时，把true改为false
        Dispatch.put(wordObject, "Visible", new Variant(false));
 
        // 获得Documents对象
        Dispatch documents = objWord.getProperty("Documents").toDispatch();
 
        // 调用 Open 打开 Document
        document = Dispatch.call(documents, "Open", filename).toDispatch();
 
        Dispatch.call(document, "SaveAs", "e:\\abc.doc",
                "wdFormatDocumentDefault");
        ComThread.Release();// Thread release
 
    }
 
    public static void main(String[] args) {
        Convertion t1 = new Convertion();
 
        try {
            t1.convertDocx2Doc("e:\\五千万.docx", "e:\\五千万.doc");
        } catch (Exception e) {
            // t1.close();
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
 
    public void close() {
        // 关闭文档
        // 由于是演示程序，这里只简单的把word退出即可
        Dispatch.call(document, "Close");
        Dispatch.call(wordObject, "quit");
    }
 
    *//**
     * 转换doc文件为docx文件
     * 
     * @param docPath
     *            doc源文件路径
     * @param docxPath
     *            docx目标文件路径
     * @return 目标docx文件
     * @throws Exception
     *//*
    private File convertDocx2Doc(String docPath, String docxPath)
            throws Exception {
        ComThread.InitSTA();
        ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word
        try {
            // Set component to hide that is opened
            app.setProperty("Visible", new Variant(false));
            // Instantiate the Documents Property
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // Open a word document
            Dispatch doc = Dispatch.invoke(
                    docs,
                    "Open",
                    Dispatch.Method,
                    new Object[] { docPath, new Variant(true),
                            new Variant(true) }, new int[1]).toDispatch();
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
                    docxPath},
                    new int[1]);
            Dispatch.call(doc, "Close", new Variant(false));
            return new File(docxPath);
        } catch (Exception e) {
            throw e;
        } finally {
            app.invoke("Quit", new Variant[] {});// ActiveXComponent quit
            ComThread.Release();// Thread release
        }
 
    }*/
 
}