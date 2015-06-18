package logic;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Tim on 15.06.2015.
 */
public class MyLogic {

    private static MyLogic instance;
    private MyLogic() {}
    public static MyLogic getInstance() {
        if (MyLogic.instance == null) {
            MyLogic.instance = new MyLogic();
        }
        return MyLogic.instance;
    }

    private File logFile = new File("JSONErrorLog.txt");

    public JSONArray getJSONArrayFromFilePath(String filePath) {

        JSONArray jsonArray = new JSONArray();

        try {
            String jsonString = FileUtils.readFileToString(new File(filePath));
            jsonArray = new JSONArray(jsonString);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error in file path!");
            e.printStackTrace();
        }

        return jsonArray;
    }

    public void checkJSONArrayForMaxLength(JSONArray jsonArray, int maxQuestionLength, int maxAnswerLength, boolean log) {

        StringBuilder errors = new StringBuilder();

        for (int i = 0; i < jsonArray.length(); i++) {

            if (jsonArray.getJSONObject(i) == null) break;

            String id = jsonArray.getJSONObject(i).get("id").toString();
            String question = jsonArray.getJSONObject(i).get("question").toString();
            JSONArray answers = (JSONArray)jsonArray.getJSONObject(i).get("answers");
            String firstAnswer = answers.get(0).toString();
            String secondAnswer = answers.get(1).toString();
            String thirdAnswer = answers.get(2).toString();
            String fourthAnswer = answers.get(3).toString();

            if (question.length() > maxQuestionLength) {
                errors.append("ID: " + id + " -> Question length is bigger than allowed! (" + question.length() + "\n");
            }
            if (firstAnswer.length() > maxAnswerLength) {
                errors.append("ID: " + id + " -> Answer length of answer 1 is bigger than allowed! (" + firstAnswer.length() + ")\n");
            }
            if (secondAnswer.length() > maxAnswerLength) {
                errors.append("ID: " + id + " -> Answer length of answer 2 is bigger than allowed! (" + secondAnswer.length() + ")\n");
            }
            if (thirdAnswer.length() > maxAnswerLength) {
                errors.append("ID: " + id + " -> Answer length of answer 3 is bigger than allowed! (" + thirdAnswer.length() + ")\n");
            }
            if (fourthAnswer.length() > maxAnswerLength) {
                errors.append("ID: " + id + " -> Answer length of answer 4 is bigger than allowed! (" + fourthAnswer.length() + ")\n");
            }
        }

        JTextArea textArea = new JTextArea(errors.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
        JOptionPane.showMessageDialog(null, scrollPane, "Error logging window",
                JOptionPane.YES_NO_OPTION);

        if (log) {
            try {
                FileWriter fw = new FileWriter(logFile);
                fw.append(errors.toString());
                fw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error in file path!");
                e.printStackTrace();
            }
        }
    }

}
