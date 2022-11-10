package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    private static final String mireaScheduleUrl = "https://www.mirea.ru/schedule/";

    public static void main(String[] args) {
        // Получение кода страницы для парсинга
        try {
            Document doc = Jsoup.connect(mireaScheduleUrl).get();
            // Получение всех элементов с тегом <a> c с классом uk-link-toggle
            Elements links = doc.select("a.uk-link-toggle");

            // Перебор всех элементов
            for (Element link : links) {

                // Получение значения атрибута href
                String linkHref = link.attr("href");

                // Получение текста внутри тега
                String linkText = link.text();

                Pattern pattern = Pattern.compile("\\d курс");
                Matcher matcher = pattern.matcher(linkText);

                if (matcher.find()) {
                    // Скачиваем и сохраняем файл
                    URL url = new URL(linkHref);
                    InputStream stream = url.openStream();

                    Pattern pattern2 = Pattern.compile("/([^/]*.(xlsx|xls))");
                    Matcher matcher2 = pattern2.matcher(linkHref);

                    String fileName = "";
                    if (matcher2.find()) {
                        fileName = matcher2.group(1);
                    } else {
                        fileName = randomString(10) + ".xlsx";
                    }

                    // Создаём папку schedule
                    Files.createDirectories(Paths.get("schedule"));

                    Files.copy(stream, Paths.get("schedule/" + fileName), StandardCopyOption.REPLACE_EXISTING);
                }
//                System.out.println(linkText + ' ' + linkHref);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String randomString(int i) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < i) { // length of the random string.
            int index = (int) (Math.random() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


}
