package com.example.quiz;

public class QuestionList {
    private String Question, OptionA, OptionB, OptionC, OptionD, Ans, UserAns;

    public QuestionList(String question, String optionA, String optionB, String optionC, String optionD, String ans, String userAns) {
        Question = question;
        OptionA = optionA;
        OptionB = optionB;
        OptionC = optionC;
        OptionD = optionD;
        Ans = ans;
        UserAns = userAns;
    }

    public String getQuestion() {
        return Question;
    }

    public String getOptionA() {
        return OptionA;
    }

    public String getOptionB() {
        return OptionB;
    }

    public String getOptionC() {
        return OptionC;
    }

    public String getOptionD() {
        return OptionD;
    }

    public String getAns() {
        return Ans;
    }

    public String getUserAns() {
        return UserAns;
    }

    public void setUserAns(String userAns) {
        UserAns = userAns;
    }
}
