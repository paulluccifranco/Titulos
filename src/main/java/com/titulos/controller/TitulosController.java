package com.titulos.controller;

import com.titulos.controller.util.ExcelReader;
import com.titulos.controller.util.PdfGenerator;
import com.titulos.model.Title;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class TitulosController {

    @Autowired
    ExcelReader excelReader;

    @Autowired
    PdfGenerator pdfGenerator;

    @GetMapping("/")
    public String expediente(Model model) {
        return "Titulos";
    }

    @PostMapping("/guardaradjunto")
    public String titlesFile(@RequestParam MultipartFile file, HttpServletResponse response) throws IOException, JRException {
        InputStream inputStream =  new BufferedInputStream(file.getInputStream());
        List<Title> titles = excelReader.generateTitles(inputStream);
        Title title = new Title();
        response.setContentType("application/pdf");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        List<String> srcFiles = Arrays.asList("test1.txt", "test2.txt");
        FileOutputStream fos = new FileOutputStream("Titulos.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        zipOut.close();
        fos.close();
         for(Title tit : titles){
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
             String fileName = tit.getName().replace(" ", "");
             response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\".pdf");
             byte[] byteArray = pdfGenerator.generatorPDF(tit);

             ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
             OutputStream out = response.getOutputStream();

             byte[] buf = new byte[1024];
             int len;
             while ((len = bais.read(buf)) > 0) {
                 out.write(buf, 0, len);
             }
             bais.close();
             out.flush();
             out.close();

         }

        return "Titulos";
    }
}
