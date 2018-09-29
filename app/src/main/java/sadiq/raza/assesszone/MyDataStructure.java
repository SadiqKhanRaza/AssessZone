package sadiq.raza.assesszone;

import java.util.ArrayList;

public class MyDataStructure {
    String ques;
    ArrayList<String > option;
    String ans;
    public  MyDataStructure()
    {

    }

    public MyDataStructure(String ques, ArrayList<String> option, String ans) {
        this.ques = ques;
        this.option = option;
        this.ans = ans;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public ArrayList<String> getOption() {
        return option;
    }

    public void setOption(ArrayList<String> option) {
        this.option = option;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
