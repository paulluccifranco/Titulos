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
         for(Title tit : titles){
             String fileName = "PDFS/"+tit.getName().replace(" ", "").concat(".pdf");
             File outputFile = new File(fileName);
             try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                 outputStream.write(pdfGenerator.generatorPDF(tit));
             }
             break;
         }

        return "Titulos";
    }
}
