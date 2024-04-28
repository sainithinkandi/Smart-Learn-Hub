interface QuizPanelListener {
    void onAnswerSubmitted(int questionIndex, String answer);
    int getScore();
    int getNumberOfParticipants();
}
