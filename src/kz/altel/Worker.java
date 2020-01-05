/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.altel;

import java.io.*;
import java.sql.ResultSet;

/**
 * @author vazhenin
 */
public class Worker {

    DButilities utils = new DButilities();
    Tools tools = new Tools();
    String user, pwd, get_traces_fullpath, host, phoneNumbersFile, date_from, outputEncoding, date_to, parameters, textParams, directoriesDir, decodersDir, outputFile, logFile, feedToZabbixFile, mainXMLnode = "settings", execCMD, outputfileEncoding, gap = " ";
    int timeInterval, linesCount = 0, debug;
    private String connectionString;
    private String oracleClass;
    private String sqlFile;
    private String msisdn = new String("");

    void run(String[] params) {
        Tools tools = new Tools();
        ParseXMLUtilities xml = new ParseXMLUtilities(params[0]);
        xml.initiate();

        this.user = xml.getNodeValue(xml.getChildNodes("databaseConnection"), "username");
        this.pwd = xml.getNodeValue(xml.getChildNodes("databaseConnection"), "password");
        this.connectionString = xml.getNodeValue(xml.getChildNodes("databaseConnection"), "connectionString");
        this.oracleClass = xml.getNodeValue(xml.getChildNodes("databaseConnection"), "class");
        this.sqlFile = xml.getNodeValue(xml.getChildNodes("inputParameters"), "sqlFile");
        this.outputFile = xml.getNodeValue(xml.getChildNodes("inputParameters"), "outputFile");
        this.logFile = xml.getNodeValue(xml.getChildNodes("inputParameters"), "logFile");
        this.outputEncoding = xml.getNodeValue(xml.getChildNodes("inputParameters"), "outputEncoding");
        this.phoneNumbersFile = xml.getNodeValue(xml.getChildNodes("inputParameters"), "phoneNumbersFile");

        Logging.setLogFileName(this.logFile);
        Logging.put_log("Started " + tools.getCurrentDate("dd.MM.yyyy HH.mm.ss"));

        // set database connection parameters
        utils.setUser(this.user);
        utils.setPassword(this.pwd);
        utils.setDatabase_url(this.connectionString);
        utils.load_class(this.oracleClass);

        // read phone numbers into one line with separators
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.phoneNumbersFile)));
            String line = new String("");
            line = br.readLine();
            while (line != null) {
                msisdn += "'" + line + "'";
                line = br.readLine();
                if (line != null) {
                    msisdn += ",";
                }
            }
            msisdn = "(" + msisdn + ")";
        } catch (Exception e) {
            Logging.put_log(e);
        }

        //open connection and execute query
        utils.open_conn();
        try {
            File file = new File(this.outputFile);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            String SqlFromFile = utils.read_sql(this.sqlFile) + " and p.msisdn in " + msisdn + "\r\n";
            // execute query and loop through result set
            ResultSet rs = utils.executeQuery(SqlFromFile);
            while (rs.next()) {
                bos.write(("ФИО : " + rs.getString(1) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("Дата рождения : " + rs.getString(2) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("Адрес регистрации : " + rs.getString(3) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("Адрес доставки : " + rs.getString(4) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("Документы : " + rs.getString(5) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("ИНН/БИН : " + rs.getString(6) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("Контактные номера : " + rs.getString(7) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("Номер алтел : " + rs.getString(8) + "\r\n").getBytes(this.outputEncoding));
                bos.write(("\r\n").getBytes(this.outputEncoding));
            }
            bos.flush();
            bos.close();
        } catch (Exception e) {
            Logging.put_log(e);
        }
        utils.close_conn();
        Logging.put_log("Finished " + tools.getCurrentDate("dd.MM.yyyy HH.mm.ss"));
    }

}
